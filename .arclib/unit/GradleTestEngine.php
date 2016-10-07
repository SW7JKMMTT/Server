<?php

/**
 * Basic test engine for running tests using Maven.
 */
final class GradleTestEngine extends ArcanistUnitTestEngine {

	private $project_root;

	public function supportsRunAllTests() {
		return true;
	}

    private function getGradlePath()
    {
        $gradle_bin = "gradle";

        list($err, $stdout) = exec_manual('which %s', $gradle_bin);
        if ($err) {
            throw new ArcanistUsageException("Gradle does not appear to be " . 'available on the path.');
        }

        return trim($stdout);
    }

	public function run() {
		$working_copy = $this->getWorkingCopy();
		$this->project_root = $working_copy->getProjectRoot();

		//Is there a gradle wrapper in the project root
		$gradle_bin = join('/', array(
			rtrim($this->project_root, '/'),
			"gradlew"
		));

		//Lets use the global one
        if (!file_exists($gradle_bin)) {
            $gradle_bin = $this->getGradlePath();
        }

		// We only want to report results for tests that actually ran, so
		// we'll compare the test result files' timestamps to the start time
		// of the test run. This will probably break if multiple test runs
		// are happening in parallel, but if that's happening then we can't
		// count on the results files being intact anyway.
		$start_time = time();

		$gradle_top_dirs = $this->findTopLevelGradleDirectories();

		// We'll figure out if any of the modified files we're testing are in
		// gradle directories. We won't want to run a bunch of Java tests for
		// changes to CSS files or whatever.
		if($this->getRunAllTests())
			$modified_paths = array( $this->getWorkingCopy()->getProjectRoot() . "/" );
		else
			$modified_paths = $this->getModifiedPaths();

		$modified_paths = array( $this->getWorkingCopy()->getProjectRoot() . "/" );

		$gradle_failed = false;

		foreach ($gradle_top_dirs as $dir) {
			$dir_with_trailing_slash = $dir . '/';
			foreach ($modified_paths as $path) {
				if ($dir_with_trailing_slash ===
					substr($path, 0, strlen($dir_with_trailing_slash))) {
					$run_str = $gradle_bin . ' clean report';
					echo "Executing $run_str\n";
					$future = new ExecFuture($run_str);
					$future->setCWD($dir);
					list($status, $stdout, $stderr) = $future->resolve();
					if ($status) {
						// gradle exits with a nonzero status if there were test failures
						// or if there was a compilation error.
						$gradle_failed = true;
						break 2;
					}
					break;
				}
			}
		}

		$testResults = $this->parseTestResultsSince($start_time);
		if ($gradle_failed) {
			// If there wasn't a test failure, then synthesize one to represent
			// the failure of the test run as a whole, since it probably means the
			// code failed to compile.
			$found_failure = false;
			foreach ($testResults as $testResult) {
				if ($testResult->getResult() === ArcanistUnitTestResult::RESULT_FAIL ||
					$testResult->getResult() === ArcanistUnitTestResult::RESULT_BROKEN) {
					$found_failure = true;
					break;
				}
			}

			if (!$found_failure) {
				$testResult = new ArcanistUnitTestResult();
				$testResult->setResult(ArcanistUnitTestResult::RESULT_BROKEN);
				$testResult->setName('./gradlew report');
				$testResults[] = $testResult;
			}
		}

		return $testResults;
	}

	/**
	 * Returns an array of the full canonical paths to all the Maven directories
	 * (directories containing pom.xml files) in the project.
	 */
	private function findGradleDirectories() {
		if (file_exists($this->project_root . "/.git")) {
			// The fastest way to find all the build.gradle files is to let git scan
			// its index.
			$future = new ExecFuture('git ls-files \*build.gradle');
		} else {
			// Not a git repo. Do it the old-fashioned way.
			$future = new ExecFuture('find . -name \*build.gradle -print');
		}

		// TODO: This will find *all* the build.gradle files in the working copy.
		// Need to obey the optional paths argument to "arc unit" to let users
		// run just a subset of tests.
		$future->setCWD($this->project_root);
		list($stdout) = $future->resolvex();

		$confs = explode("\n", trim($stdout));
		if (!$confs) {
			throw new Exception("No build.gradle files found");
		}

		$gradle_dirs = array_map(function($conf) {
			$gradle_dir = dirname($conf);
			return realpath($this->project_root . '/' . $gradle_dir);
		}, $confs);

		return $gradle_dirs;
	}

	/**
	 * Returns an array of the full canonical paths to all the top-level Gradle
	 * directories in the project. A directory is not considered top-level if
	 * one of its parent directories has a build.gradle.
	 */
	private function findTopLevelGradleDirectories() {
		$gradle_dirs = $this->findGradleDirectories();
		sort($gradle_dirs);

		$previous_top_dir = '-';
		$top_dirs = array();
		foreach ($gradle_dirs as $gradle_dir) {
			if ($previous_top_dir !==
				substr($gradle_dir . '/', 0, strlen($previous_top_dir))) {
				$previous_top_dir = $gradle_dir . '/';
				$top_dirs[] = $gradle_dir;
			}
		}

		return $top_dirs;
	}

	/**
	 * Returns an array of paths to the JUnit test result XML files in the
	 * project.
	 */
	private function findTestResultFiles() {
		$gradle_dirs = $this->findGradleDirectories();
		$result_dirs = array();
		foreach ($gradle_dirs as $gradle_dir) {
			$fullpath = $gradle_dir .  '/build/reports/junit';
			if (file_exists($fullpath)) {
				$result_dirs[] = $fullpath;
			}
		}

		$result_files = array();
		foreach ($result_dirs as $result_dir) {
			$xmlfiles = glob($result_dir . "/*.xml");
			$result_files = array_merge($result_files, $xmlfiles);
		}

		return $result_files;
	}

	/**
	 * Returns the full paths to all the files modified in the workspace.
	 */
	private function getModifiedPaths() {
		$paths = $this->getPaths();
		return array_map(function($path) {
			return realpath($this->project_root . '/' . $path);
		}, $paths);
	}

	/**
	 * Parses all the test results that have been written since a particular
	 * starting time.
	 */
	private function parseTestResultsSince($start_time) {
		$parser = new ArcanistXUnitTestResultParser();
		$results = array();

		$result_files = $this->findTestResultFiles();

		foreach ($result_files as $file) {
			$stat = stat($file);
			if ($stat && $stat['mtime'] >= $start_time) {
				$new_results = $parser->parseTestResults(Filesystem::readFile($file));
				if ($new_results) {
					$results = array_merge($results, $new_results);
				}
			}
		}

		return $results;
	}
}
