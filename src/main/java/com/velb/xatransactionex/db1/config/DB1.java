package com.velb.xatransactionex.db1.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;

import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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
        transactionManagerRef = "customerTransactionManager",
        basePackages = "com.velb.xatransactionex.db1.*")        //ПАКЕТ C Repository
//@ComponentScan(basePackages = {"com.velb.xatransactionex.db1.*"})
//@EntityScan("com.velb.xatransactionex.db1.*")
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class DB1 {


    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.first")
    public DataSourceProperties firstDbDatasourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "firstDbDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.first.configuration")
    public DataSource firstDbDataSource() {
        return firstDbDatasourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)   //TODO ЗДЕСЬ ВОЗМОЖНО НУЖНО ПОСТАВИТЬ AtomikosDataSourceBean
                .build();
    }


    @Bean
    @Primary
    public JpaVendorAdapter postgresJpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(true);
        hibernateJpaVendorAdapter.setGenerateDdl(true);
        hibernateJpaVendorAdapter.setDatabase(Database.POSTGRESQL);
        return hibernateJpaVendorAdapter;
    }


    @Primary
    @Bean(name = "firstDbEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                       @Qualifier("firstDbDataSource") DataSource dataSource) {

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

        var entityManager = builder.dataSource(dataSource)
                .packages("com.velb.xatransactionex.db1.model")
                .properties(properties)
                .persistenceUnit("db1").build();
        entityManager.setJpaVendorAdapter(postgresJpaVendorAdapter());
        return entityManager;
    }

    @Primary
    @Bean(name = "customerTransactionManager")
    public PlatformTransactionManager customerTransactionManager(
            @Qualifier("firstDbEntityManagerFactory") EntityManagerFactory customerEntityManagerFactory) {
        return new JpaTransactionManager(customerEntityManagerFactory);
    }
}
