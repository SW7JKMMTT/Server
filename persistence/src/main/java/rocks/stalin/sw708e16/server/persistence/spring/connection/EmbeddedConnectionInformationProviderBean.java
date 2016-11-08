package rocks.stalin.sw708e16.server.persistence.spring.connection;

import de.flapdoodle.embed.mongo.MongodProcess;

public class EmbeddedConnectionInformationProviderBean implements ConnectionInformationProvider {
    private MongodProcess process;

    @Override
    public String getUrl() {
        return process.getConfig().net().getBindIp();
    }

    @Override
    public int getPort() {
        return process.getConfig().net().getPort();
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    public void setProcess(MongodProcess executable) {
        this.process = executable;
    }
}
