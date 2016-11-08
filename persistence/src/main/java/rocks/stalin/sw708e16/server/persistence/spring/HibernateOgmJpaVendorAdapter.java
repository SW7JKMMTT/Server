package rocks.stalin.sw708e16.server.persistence.spring;

import org.hibernate.ogm.cfg.OgmProperties;
import org.hibernate.ogm.datastore.spi.DatastoreProvider;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import rocks.stalin.sw708e16.server.persistence.spring.connection.ConnectionInformationProvider;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * {@link org.springframework.orm.jpa.JpaVendorAdapter} implementation for Hibernate
 * EntityManager. Developed and tested against Hibernate 5.0, 5.1 and 5.2;
 * backwards-compatible with Hibernate 4.3 at runtime on a best-effort basis.
 *
 * <p>Exposes Hibernate's persistence provider and EntityManager extension interface,
 * and adapts {@link AbstractJpaVendorAdapter}'s common configuration settings.
 * Also supports the detection of annotated packages (through
 * {@link org.springframework.orm.jpa.persistenceunit.SmartPersistenceUnitInfo#getManagedPackages()}),
 * e.g. containing Hibernate {@link org.hibernate.annotations.FilterDef} annotations,
 * along with Spring-driven entity scanning which requires no {@code persistence.xml}
 * ({@link org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean#setPackagesToScan}).
 *
 * @author Juergen Hoeller
 * @author Rod Johnson
 * @since 2.0
 * @see HibernateJpaDialect
 */
public class HibernateOgmJpaVendorAdapter implements JpaVendorAdapter {

    private final HibernateJpaDialect jpaDialect = new HibernateJpaDialect();

    private final PersistenceProvider persistenceProvider;

    private final Class<? extends EntityManagerFactory> entityManagerFactoryInterface;

    private final Class<? extends EntityManager> entityManagerInterface;

    private String host;
    private int port;
    private String database;

    private String username;
    private String password;

    private ConnectionInformationProvider informationProvider;

    private boolean createDatabase;

    private Optional<DatastoreProvider> providerInstance;
    private Optional<Class<DatastoreProvider>> providerClass;
    private Optional<String> providerStr;


    /**
     * Create a new Hibernate OGM vendor adapter.
     */
    public HibernateOgmJpaVendorAdapter() {
        this.persistenceProvider = new org.hibernate.ogm.jpa.HibernateOgmPersistence();
        this.entityManagerFactoryInterface = org.hibernate.jpa.HibernateEntityManagerFactory.class;
        this.entityManagerInterface = org.hibernate.jpa.HibernateEntityManager.class;
    }


    /**
     * Set whether to prepare the underlying JDBC Connection of a transactional
     * Hibernate Session, that is, whether to apply a transaction-specific
     * isolation level and/or the transaction's read-only flag to the underlying
     * JDBC Connection.
     *
     * <p>See {@link HibernateJpaDialect#setPrepareConnection(boolean)} for details.
     * This is just a convenience flag passed through to {@code HibernateJpaDialect}.
     *
     * <p>On Hibernate 5.2, this flag remains {@code true} by default like against
     * previous Hibernate versions. The vendor adapter manually enforces Hibernate's
     * new connection handling mode {@code DELAYED_ACQUISITION_AND_HOLD} in that case
     * unless a user-specified connection handling mode property indicates otherwise;
     * switch this flag to {@code false} to avoid that interference.
     * @since 4.3.1
     * @see #getJpaPropertyMap()
     * @see HibernateJpaDialect#beginTransaction
     */
    public void setPrepareConnection(boolean prepareConnection) {
        this.jpaDialect.setPrepareConnection(prepareConnection);
    }


    @Override
    public PersistenceProvider getPersistenceProvider() {
        return this.persistenceProvider;
    }

    @Override
    public String getPersistenceProviderRootPackage() {
        return "org.hibernate";
    }

    @Override
    @SuppressWarnings("deprecation")
    public Map<String, Object> getJpaPropertyMap() {
        Map<String, Object> jpaProperties = new HashMap<>();

        if(getInformationProvider() != null) {
            if (getInformationProvider().getUsername() != null)
                jpaProperties.put(OgmProperties.USERNAME, getInformationProvider().getUsername());
            if (getInformationProvider().getPassword() != null)
                jpaProperties.put(OgmProperties.PASSWORD, getInformationProvider().getPassword());
            if (getInformationProvider().getUrl() != null)
                jpaProperties.put(OgmProperties.HOST, getInformationProvider().getUrl());
            if (getInformationProvider().getPort() != 0)
                jpaProperties.put(OgmProperties.PORT, getInformationProvider().getPort());
        }

        if (getUsername() != null)
            jpaProperties.put(OgmProperties.USERNAME, getUsername());

        if (getPassword() != null)
            jpaProperties.put(OgmProperties.PASSWORD, getPassword());

        if (getHost() != null)
            jpaProperties.put(OgmProperties.HOST, getHost());

        if (getPort() != 0)
            jpaProperties.put(OgmProperties.PORT, getPort());

        jpaProperties.put(OgmProperties.DATABASE, getDatabase());

        jpaProperties.put(OgmProperties.CREATE_DATABASE, isCreateDatabase());

        if (getProviderInstance().isPresent())
            jpaProperties.put(OgmProperties.DATASTORE_PROVIDER, getProviderInstance().get());
        else if (getProviderClass().isPresent())
            jpaProperties.put(OgmProperties.DATASTORE_PROVIDER, getProviderClass().get());
        else if (getProviderStr().isPresent())
            jpaProperties.put(OgmProperties.DATASTORE_PROVIDER, getProviderStr().get());

        return jpaProperties;
    }

    @Override
    public HibernateJpaDialect getJpaDialect() {
        return this.jpaDialect;
    }

    @Override
    public Class<? extends EntityManagerFactory> getEntityManagerFactoryInterface() {
        return this.entityManagerFactoryInterface;
    }

    @Override
    public Class<? extends EntityManager> getEntityManagerInterface() {
        return this.entityManagerInterface;
    }

    @Override
    public void postProcessEntityManagerFactory(EntityManagerFactory emf) {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the {@link DatastoreProvider} instance.
     * @return An instance of a {@link DatastoreProvider}
     */
    public Optional<DatastoreProvider> getProviderInstance() {
        return providerInstance;
    }

    /**
     * Set the {@link DatastoreProvider} from an instance.
     * @param providerInstance the {@link DatastoreProvider} instance
     */
    public void setProviderInstance(DatastoreProvider providerInstance) {
        this.providerInstance = Optional.of(providerInstance);
        this.providerClass = Optional.empty();
        this.providerStr = Optional.empty();
    }

    /**
     * Get the {@link DatastoreProvider} subclass.
     * @return A subclass of {@link DatastoreProvider}
     */
    public Optional<Class<DatastoreProvider>> getProviderClass() {
        return providerClass;
    }

    /**
     * Set the {@link DatastoreProvider} from a class.
     * @param providerClass A subclass of {@link DatastoreProvider}
     */
    public void setProviderClass(Class<DatastoreProvider> providerClass) {
        this.providerClass = Optional.of(providerClass);
        this.providerInstance = Optional.empty();
        this.providerStr = Optional.empty();
    }

    /**
     * Get the string specifying a {@link DatastoreProvider} class.
     * @return The string
     */
    public Optional<String> getProviderStr() {
        return providerStr;
    }

    /**
     * Set the provider by providing a string specifying a {@link DatastoreProvider}.
     * @param providerStr A string specifying the provider
     */
    public void setProviderStr(String providerStr) {
        this.providerStr = Optional.of(providerStr);
        this.providerClass = Optional.empty();
        this.providerInstance = Optional.empty();
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

    public boolean isCreateDatabase() {
        return createDatabase;
    }

    public void setCreateDatabase(boolean createDatabase) {
        this.createDatabase = createDatabase;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public ConnectionInformationProvider getInformationProvider() {
        return informationProvider;
    }

    public void setInformationProvider(ConnectionInformationProvider informationProvider) {
        this.informationProvider = informationProvider;
    }
}
