package rocks.stalin.sw708e16.server.spring.connection;

public interface ConnectionInformationProvider {
    String getUrl();

    int getPort();

    String getUsername();

    String getPassword();
}
