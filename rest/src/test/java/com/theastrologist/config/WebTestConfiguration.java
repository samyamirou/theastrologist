package com.theastrologist.config;

import com.theastrologist.RestApplication;
import com.theastrologist.service.ThemeService;
import com.theastrologist.util.TimeService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.*;

@Configuration
//@Profile("test")
public class WebTestConfiguration {

	// Nécessaire pour mocker dans les tests l'appel à Google

	@Bean
	@Primary
	public TimeService timeService() {
		return Mockito.spy(TimeService.class);
	}
}
