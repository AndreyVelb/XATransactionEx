package com.velb.xatransactionex.db2.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
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

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "secondDbEntityManagerFactory",
        transactionManagerRef = "secondTransactionManager",
        basePackages = "com.velb.xatransactionex.db2.*")        //ПАКЕТ C Repository
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.velb.xatransactionex.db2.*"})
@EntityScan("com.velb.xatransactionex.db2.*")
public class DB2 {


    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.second")
    public DataSourceProperties secondDbDatasourceProperties() {
        return new DataSourceProperties();
    }


    @Bean(name = "secondDbDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.second.configuration")
    public DataSource secondDbDataSource() {
        return secondDbDatasourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)   //TODO ЗДЕСЬ ВОЗМОЖНО НУЖНО ПОСТАВИТЬ AtomikosDataSourceBean
                .build();
    }


    @Bean
    public JpaVendorAdapter mySqlJpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(true);
        hibernateJpaVendorAdapter.setGenerateDdl(true);
        hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
        return hibernateJpaVendorAdapter;
    }


    @Bean(name = "secondDbEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                       @Qualifier("secondDbDataSource") DataSource dataSource) {

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");

        var entityManager = builder.dataSource(dataSource)
                .packages("com.velb.xatransactionex.db2.model")
                .properties(properties)
                .persistenceUnit("db2").build();
        entityManager.setJpaVendorAdapter(mySqlJpaVendorAdapter());
        return entityManager;
    }


    @Bean(name = "secondTransactionManager")
    public PlatformTransactionManager secondTransactionManager(
            @Qualifier("secondDbEntityManagerFactory") EntityManagerFactory customerEntityManagerFactory) {
        return new JpaTransactionManager(customerEntityManagerFactory);
    }
}
