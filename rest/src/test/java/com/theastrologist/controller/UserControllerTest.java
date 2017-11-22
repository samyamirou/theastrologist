package com.theastrologist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theastrologist.data.service.UserDataService;
import com.theastrologist.exception.UserAlreadyExistsException;
import com.theastrologist.domain.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserDataService service;

	@Autowired
	private ObjectMapper objectMapper;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getUser() throws Exception {
		String username = "toto";
		User user = new User(username);
		Mockito.when(service.getUserByName(username)).thenReturn(user);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/user/{username}", username)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String body = result.getResponse().getContentAsString();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
		assertThat(body, hasJsonPath("userName", equalTo(username)));
	}

	@Test
	public void createUser() throws Exception {
		String username = "toto";
		doNothing().when(service).createUser(isA(User.class));

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/user/{username}", username)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String body = result.getResponse().getContentAsString();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.CREATED.value()));
		//assertThat(body, hasJsonPath("location", equalTo("truc")));
	}

	@Test
	public void createUserAlreadyExists() throws Exception {
		String username = "toto";
		doThrow(new UserAlreadyExistsException()).when(service).createUser(isA(User.class));

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/user/{username}", username)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String body = result.getResponse().getContentAsString();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.BAD_REQUEST.value()));
	}
}