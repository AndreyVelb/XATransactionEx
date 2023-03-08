package com.velb.xatransactionex.db2.config;

import com.atomikos.spring.AtomikosDataSourceBean;
import com.mysql.cj.jdbc.MysqlXADataSource;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.engine.transaction.jta.platform.internal.AtomikosJtaPlatform;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "secondDbEntityManagerFactory",
        transactionManagerRef = "secondTransactionManager",
        basePackages = "com.velb.xatransactionex.db2.*")
public class DB2 {

    @Value("${spring.jta.enabled}")
    private boolean IS_JTA_ENABLED;

    @Value("${spring.datasource.second.persistence-unit}")
    private String PERSISTENCE_UNIT;

    @Value("${spring.datasource.second.xa-resource-unique-name}")
    private String XA_UNIQUE_RESOURCE_NAME;

    @Value("${spring.datasource.second.hibernate.dialect}")
    private String MYSQL_HIBERNATE_DIALECT;

    @Value("${javax.persistence.transactionType}")
    private String TRANSACTION_TYPE;

    @Value("${spring.jpa.hibernate.show-sql}")
    private boolean IS_SHOW_SQL;

    @Value("${spring.jpa.hibernate.generate-ddl}")
    private boolean IS_GENERATE_DDL;

    @Value("${spring.datasource.second.min-pool-size}")
    private int MIN_POOL_SIZE;

    @Value("${spring.datasource.second.max-pool-size}")
    private int MAX_POOL_SIZE;


    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.second")
    public DataSourceProperties secondDbDatasourceProperties() {
        return new DataSourceProperties();
    }


    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.second.configuration")
    public DataSource secondDbDataSource() throws SQLException {
        MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
        mysqlXaDataSource.setUrl(secondDbDatasourceProperties().getUrl());
        mysqlXaDataSource.setDatabaseName(secondDbDatasourceProperties().getName());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
        mysqlXaDataSource.setPassword(secondDbDatasourceProperties().getPassword());
        mysqlXaDataSource.setUser(secondDbDatasourceProperties().getUsername());

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mysqlXaDataSource);
        xaDataSource.setUniqueResourceName(XA_UNIQUE_RESOURCE_NAME);
        xaDataSource.setMinPoolSize(MIN_POOL_SIZE);
        xaDataSource.setMaxPoolSize(MAX_POOL_SIZE);
        return xaDataSource;
    }


    @Bean
    public JpaVendorAdapter mySqlJpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(IS_SHOW_SQL);
        hibernateJpaVendorAdapter.setGenerateDdl(IS_GENERATE_DDL);
        hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
        return hibernateJpaVendorAdapter;
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean secondDbEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                       @Qualifier("secondDbDataSource") DataSource dataSource) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", MYSQL_HIBERNATE_DIALECT);
        properties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
        properties.put("javax.persistence.transactionType", TRANSACTION_TYPE);

        var entityManager = builder.dataSource(dataSource)
                .packages("com.velb.xatransactionex.db2.model")
                .properties(properties)
                .jta(IS_JTA_ENABLED)
                .persistenceUnit(PERSISTENCE_UNIT).build();
        entityManager.setJpaVendorAdapter(mySqlJpaVendorAdapter());
        return entityManager;
    }


    @Bean
    public PlatformTransactionManager secondTransactionManager(
            @Qualifier("secondDbEntityManagerFactory") EntityManagerFactory customerEntityManagerFactory) {
        return new JpaTransactionManager(customerEntityManagerFactory);
    }

}
