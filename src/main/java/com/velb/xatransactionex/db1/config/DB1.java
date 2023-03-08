package com.velb.xatransactionex.db1.config;

import com.atomikos.spring.AtomikosDataSourceBean;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.engine.transaction.jta.platform.internal.AtomikosJtaPlatform;
import org.postgresql.xa.PGXADataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

@Primary
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "firstDbEntityManagerFactory",
        transactionManagerRef = "firstTransactionManager",
        basePackages = "com.velb.xatransactionex.db1.*")
public class DB1 {

    @Value("${spring.jta.enabled}")
    private boolean IS_JTA_ENABLED;

    @Value("${spring.datasource.first.persistence-unit}")
    private String PERSISTENCE_UNIT;

    @Value("${spring.datasource.first.xa-resource-unique-name}")
    private String XA_UNIQUE_RESOURCE_NAME;

    @Value("${spring.datasource.first.hibernate.dialect}")
    private String POSTGRESQL_HIBERNATE_DIALECT;

    @Value("${javax.persistence.transactionType}")
    private String TRANSACTION_TYPE;

    @Value("${spring.jpa.hibernate.show-sql}")
    private boolean IS_SHOW_SQL;

    @Value("${spring.jpa.hibernate.generate-ddl}")
    private boolean IS_GENERATE_DDL;

    @Value("${spring.datasource.first.min-pool-size}")
    private int MIN_POOL_SIZE;

    @Value("${spring.datasource.first.max-pool-size}")
    private int MAX_POOL_SIZE;


    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.first")
    public DataSourceProperties firstDbDatasourceProperties() {
        return new DataSourceProperties();
    }


    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.first.configuration")
    public DataSource firstDbDataSource(@Qualifier("firstDbDatasourceProperties") DataSourceProperties firstDbDatasourceProperties) {
        PGXADataSource ds = new PGXADataSource();
        ds.setUrl(firstDbDatasourceProperties.getUrl());
        ds.setUser(firstDbDatasourceProperties.getUsername());
        ds.setPassword(firstDbDatasourceProperties.getPassword());

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(ds);
        xaDataSource.setUniqueResourceName(XA_UNIQUE_RESOURCE_NAME);
        xaDataSource.setMinPoolSize(MIN_POOL_SIZE);
        xaDataSource.setMaxPoolSize(MAX_POOL_SIZE);
        return xaDataSource;
    }


    @Bean
    @Primary
    public JpaVendorAdapter postgresJpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(IS_SHOW_SQL);
        hibernateJpaVendorAdapter.setGenerateDdl(IS_GENERATE_DDL);
        hibernateJpaVendorAdapter.setDatabase(Database.POSTGRESQL);
        return hibernateJpaVendorAdapter;
    }


    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean firstDbEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("firstDbDataSource") DataSource dataSource) {

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", POSTGRESQL_HIBERNATE_DIALECT);
        properties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
        properties.put("javax.persistence.transactionType", TRANSACTION_TYPE);

        var entityManager = builder.dataSource(dataSource)
                .packages("com.velb.xatransactionex.db1.model")
                .properties(properties)
                .jta(IS_JTA_ENABLED)
                .persistenceUnit(PERSISTENCE_UNIT).build();
        entityManager.setJpaVendorAdapter(postgresJpaVendorAdapter());
        return entityManager;
    }


    @Bean
    @Primary
    public PlatformTransactionManager firstTransactionManager(
            @Qualifier("firstDbEntityManagerFactory") EntityManagerFactory customerEntityManagerFactory) {
        return new JpaTransactionManager(customerEntityManagerFactory);
    }

}
