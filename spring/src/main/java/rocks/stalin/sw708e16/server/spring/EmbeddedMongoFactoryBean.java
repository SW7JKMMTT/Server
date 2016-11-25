package rocks.stalin.sw708e16.server.spring;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.IFeatureAwareVersion;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.distribution.Versions;
import de.flapdoodle.embed.process.distribution.GenericVersion;
import de.flapdoodle.embed.process.runtime.Network;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;

import java.util.logging.Logger;

import static de.flapdoodle.embed.mongo.distribution.Version.Main.PRODUCTION;

public class EmbeddedMongoFactoryBean implements FactoryBean<MongodProcess>, DisposableBean {
    private Logger logger = Logger
        .getLogger("rocks.stalic.sw708e16.server.persistence.rocks.stalin.sw708e16.server.spring");

    private String version;

    private MongodExecutable executable;

    @Override
    public MongodProcess getObject() throws Exception {
        MongodStarter starter = MongodStarter.getDefaultInstance();
        IMongodConfig mongoConfig = new MongodConfigBuilder()
            .version(this.version == null ? PRODUCTION : parseVersion(this.version))
            .build();

        executable = starter.prepare(mongoConfig);
        return executable.start();
    }

    @Override
    public Class<?> getObjectType() {
        return MongodExecutable.class;
    }

    @Override
    public void destroy() throws Exception {
        executable.stop();
    }


    @Override
    public boolean isSingleton() {
        return true;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    private IFeatureAwareVersion parseVersion(String version) {
        String versionEnumName = version.toUpperCase().replaceAll("\\.", "_");
        if (!versionEnumName.startsWith("V")) {
            versionEnumName = "V" + versionEnumName;
        }
        try {
            return Version.valueOf(versionEnumName);
        } catch (IllegalArgumentException ex) {
            logger.warning(String.format("Unrecognised MongoDB version '{}', this might be a new version" +
                    "that we don't yet know about. Attempting download anyway...", version));
            return Versions.withFeatures(new GenericVersion(version));
        }
    }
}
