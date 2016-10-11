package dk.aau.giraf.rest.persistence.spring;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;

import org.hibernate.ogm.cfg.OgmProperties;
import org.hibernate.dialect.DB2Dialect;
import org.hibernate.dialect.DerbyTenSevenDialect;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.dialect.InformixDialect;
import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.Oracle12cDialect;
import org.hibernate.dialect.SQLServer2012Dialect;
import org.hibernate.dialect.SybaseDialect;
import org.hibernate.ogm.datastore.spi.DatastoreProvider;
import org.hibernate.ogm.options.spi.Option;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;

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
public class HibernateOGMJpaVendorAdapter implements JpaVendorAdapter {

    private final HibernateJpaDialect jpaDialect = new HibernateJpaDialect();

    private final PersistenceProvider persistenceProvider;

    private final Class<? extends EntityManagerFactory> entityManagerFactoryInterface;

    private final Class<? extends EntityManager> entityManagerInterface;

    private String host;
    private int port;
    private String database;

    private String username;
    private String password;

    private boolean createDatabase;

    private Optional<DatastoreProvider> providerInstance;
    private Optional<Class<DatastoreProvider>> providerClass;
    private Optional<String> providerStr;


    @SuppressWarnings("deprecation")
    public HibernateOGMJpaVendorAdapter() {
        this.persistenceProvider = new org.hibernate.ogm.jpa.HibernateOgmPersistence();
        this.entityManagerFactoryInterface = org.hibernate.jpa.HibernateEntityManagerFactory.class;
        this.entityManagerInterface = org.hibernate.jpa.HibernateEntityManager.class;
    }


    /**
     * Set whether to prepare the underlying JDBC Connection of a transactional
     * Hibernate Session, that is, whether to apply a transaction-specific
     * isolation level and/or the transaction's read-only flag to the underlying
     * JDBC Connection.
     * <p>See {@link HibernateJpaDialect#setPrepareConnection(boolean)} for details.
     * This is just a convenience flag passed through to {@code HibernateJpaDialect}.
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
    public Map<String, Object> getJpaPropertyMap() {
        Map<String, Object> jpaProperties = new HashMap<>();

        if(getUsername() != null)
            jpaProperties.put(OgmProperties.USERNAME, getUsername());

        if(getPassword() != null)
            jpaProperties.put(OgmProperties.PASSWORD, getPassword());

        if(getHost() != null)
            jpaProperties.put(OgmProperties.HOST, getHost());

        if(getPort() != 0)
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

    public Optional<DatastoreProvider> getProviderInstance() {
        return providerInstance;
    }

    public void setProviderInstance(DatastoreProvider providerInstance) {
        this.providerInstance = Optional.of(providerInstance);
        this.providerClass = Optional.empty();
        this.providerStr = Optional.empty();
    }

    public Optional<Class<DatastoreProvider>> getProviderClass() {
        return providerClass;
    }

    public void setProviderClass(Class<DatastoreProvider> providerClass) {
        this.providerClass = Optional.of(providerClass);
        this.providerInstance = Optional.empty();
        this.providerStr = Optional.empty();
    }

    public Optional<String> getProviderStr() {
        return providerStr;
    }

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
}
