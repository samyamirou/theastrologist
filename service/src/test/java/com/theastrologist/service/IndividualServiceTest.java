package com.theastrologist.service;

import com.theastrologist.ServiceTestConfiguration;
import com.theastrologist.data.repository.IndividualRepository;
import com.theastrologist.data.repository.UserRepository;
import com.theastrologist.domain.individual.Individual;
import com.theastrologist.domain.user.User;
import com.theastrologist.exception.TooManyResultsException;
import com.theastrologist.exception.UserAlreadyExistsException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ServiceTestConfiguration.class})
public class IndividualServiceTest {
	@MockBean
	private IndividualRepository individualRepository;

	@Autowired
	private IndividualService service;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Before
	public void setUp() {
		userRepository.deleteAll();
		individualRepository.deleteAll();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void findByName() throws TooManyResultsException, UserAlreadyExistsException {
		String username = "Alex";

		User user = userService.createUser(username);

		String name = "Robert";
		Individual individual = new Individual(name);

		List<Individual> individuals = newArrayList();
		individuals.add(individual);

		Mockito.when(individualRepository.findDistinctIndividualByUserOrName(user, name)).thenReturn(individuals);

		Individual found = service.findIndividualByName(user, name);
		assertThat(found.getName()).isEqualTo(name);
	}

	@Test
	public void findByNameNoResult() throws TooManyResultsException, UserAlreadyExistsException {
		String username = "Alex";

		User user = userService.createUser(username);

		String name = "Robert";
		Individual individual = new Individual(name);

		List<Individual> individuals = newArrayList();

		Mockito.when(individualRepository.findDistinctIndividualByUserOrName(user, name)).thenReturn(individuals);

		Individual found = service.findIndividualByName(user, name);
		assertThat(found).isNull();
	}
}