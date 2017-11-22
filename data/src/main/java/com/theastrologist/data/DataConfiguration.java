package com.theastrologist.data;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jmx.support.RegistrationPolicy;

@Configuration
@ComponentScan(basePackages = {"com.theastrologist.data"})
@EntityScan(basePackages = {"com.theastrologist.domain"})
// Pour empêcher les conflits de HikariDataSource
@EnableMBeanExport(registration= RegistrationPolicy.IGNORE_EXISTING)
//@EnableJpaRepositories
//@EnableAutoConfiguration
//@EnableTransactionManagement
public class DataConfiguration {

}
