package com.velb.xatransactionex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@Configuration
//@EnableJpaRepositories(basePackages = "com.velb.xatransactionex.repository")
public class XaTransactionExApplication {

    public static void main(String[] args) {
        SpringApplication.run(XaTransactionExApplication.class, args);


    }

}
