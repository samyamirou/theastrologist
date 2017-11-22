package com.theastrologist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theastrologist.data.service.UserDataService;
import com.theastrologist.domain.individual.Individual;
import com.theastrologist.domain.user.User;
import com.theastrologist.exception.TooManyResultsException;
import com.theastrologist.service.IndividualService;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IndividualControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserDataService userDataService;

	@MockBean
	private IndividualService individualService;

	@Autowired
	private ObjectMapper objectMapper;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getIndividualOK() throws Exception, TooManyResultsException {
		String username = "toto";
		String individualName = "titi";
		User user = new User(username);
		Individual individual = new Individual(individualName);

		Mockito.when(userDataService.getUserByName(username)).thenReturn(user);
		Mockito.when(individualService.findIndividualByName(user, individualName)).thenReturn(individual);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/user/{username}/individual/{individualName}", username, individualName)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String body = result.getResponse().getContentAsString();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
		assertThat(body, hasJsonPath("name", equalTo(individualName)));
	}

	@Test
	public void getIndividualNoUser() throws Exception {
		String username = "toto";
		String individualName = "titi";

		Mockito.when(userDataService.getUserByName(username)).thenReturn(null);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/user/{username}/individual/{individualName}", username, individualName)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String body = result.getResponse().getContentAsString();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.NOT_FOUND.value()));
	}

	@Test
	public void getIndividualTooMany() throws Exception, TooManyResultsException {
		String username = "toto";
		String individualName = "titi";
		User user = new User(username);

		Mockito.when(userDataService.getUserByName(username)).thenReturn(user);
		Mockito.when(individualService.findIndividualByName(user, individualName)).thenThrow(new TooManyResultsException());

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/user/{username}/individual/{individualName}", username, individualName)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String body = result.getResponse().getContentAsString();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.BAD_REQUEST.value()));
	}

	@Test
	public void getIndividualNoIndividual() throws Exception, TooManyResultsException {
		String username = "toto";
		String individualName = "titi";
		User user = new User(username);

		Mockito.when(userDataService.getUserByName(username)).thenReturn(user);
		Mockito.when(individualService.findIndividualByName(user, individualName)).thenReturn(null);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/user/{username}/individual/{individualName}", username, individualName)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String body = result.getResponse().getContentAsString();
		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), equalTo(HttpStatus.NOT_FOUND.value()));
	}
}