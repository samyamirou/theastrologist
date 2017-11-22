package com.theastrologist;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestApplicationTest {
	@Value("${spring.http.converters.preferred-json-mapper}")
	private String preferredMapper;

	@Test
	public void testPreferredMapperGson() {
		assertThat(preferredMapper, equalTo("gson"));
	}
}