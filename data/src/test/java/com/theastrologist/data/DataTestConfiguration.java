package com.theastrologist.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

@SpringBootApplication
@ComponentScan
// Pour empêcher les conflits de HikariDataSource
//@EnableMBeanExport(registration= RegistrationPolicy.IGNORE_EXISTING)
public class DataTestConfiguration {
    public static void main(String[] args) {
        SpringApplication.run(DataTestConfiguration.class, args);
    }
}
