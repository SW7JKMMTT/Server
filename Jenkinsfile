#!groovy

node {
    stage('Checkout') {
        checkout scm
    }

    stage('Build') {
        gradle "clean classes"

        stash name: "sources"
    }
}

stage('Test') {
    def splits = splitTests count(2)
    def branches = [:]
    for (int i = 0; i < splits.size(); i++) {
        def index = i
        branches["splits${i}"] = {
            node {
                deleteDir()
                unstash "sources"
                def exclusions = splits.get(index)
                writeFile file: 'exclusions.txt', text: exclusions.join("\n")

                gradle "report"

                junit '**/build/reports/junit/*.xml'
                /* step([$class: 'JUnitResultArchiver', testResults: '**\/build\/reports\/junit\/*.xml']) */
            }
        }
    }
	parallel branches
}

stage('Enunciate') {
    node {
        deleteDir()
        unstash "sources"

        gradle "enunciate"

        publishHTML([
        allowMissing: false,
        alwaysLinkToLastBuild: false,
        keepAll: false,
        reportDir: 'services/build/enunciate/docs',
        reportFiles: 'index.html',
        reportName: 'Enunciate'
        ])
    }
}

//Run gradle
void gradle(def args) {
    if (isUnix()) {
        sh "./gradlew ${args}"
    } else {
        bat ".\\gradlew ${args}"
    }
}
