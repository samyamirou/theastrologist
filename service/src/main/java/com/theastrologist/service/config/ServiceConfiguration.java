package com.theastrologist.service.config;


import com.theastrologist.service.Swieph;
import com.theastrologist.service.ThemeService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.theastrologist.service"})
public class ServiceConfiguration {

    /*@Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Swieph swieph() {
        return new Swieph();
    }*/
}
