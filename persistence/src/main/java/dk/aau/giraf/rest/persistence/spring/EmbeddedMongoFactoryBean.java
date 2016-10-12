package dk.aau.giraf.rest.persistence.spring;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongoConfig;
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

import static de.flapdoodle.embed.mongo.distribution.Version.Main.PRODUCTION;

import java.util.logging.Logger;

public class EmbeddedMongoFactoryBean implements FactoryBean<MongodExecutable>, DisposableBean {
    private Logger logger = Logger.getLogger("dk.aau.giraf.rest.persistence.spring");

    private String version;
    private String host;
    private int port;

    private MongodExecutable executable;

    @Override
    public MongodExecutable getObject() throws Exception {
        MongodStarter starter = MongodStarter.getDefaultInstance();
        IMongodConfig mongoConfig = new MongodConfigBuilder()
            .version(this.version == null ? PRODUCTION : parseVersion(this.version))
            .net(new Net(port, Network.localhostIsIPv6()))
            .build();

        executable = starter.prepare(mongoConfig);
        executable.start();
        return executable;
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
        return false;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    private IFeatureAwareVersion parseVersion(String version) {
        String versionEnumName = version.toUpperCase().replaceAll("\\.", "_");
        if (!versionEnumName.startsWith("V")) {
            versionEnumName = "V" + versionEnumName;
        }
        try {
            return Version.valueOf(versionEnumName);
        } catch (IllegalArgumentException ex) {
            logger.warning(String.format("Unrecognised MongoDB version '{}', this might be a new version that we don't yet know about. " +
                "Attempting download anyway...", version));
            return Versions.withFeatures(new GenericVersion(version));
        }
    }
}
