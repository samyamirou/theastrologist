package com.theastrologist.data.repository;

import com.theastrologist.data.DataTestConfiguration;
import com.theastrologist.domain.individual.Individual;
import com.theastrologist.domain.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = {DataTestConfiguration.class})
public class IndividualRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private IndividualRepository individualRepository;

	@Before
	public void setup() {
	}

	@Test
	public void findByName() {
		// given
		User alex = new User("Choupi");
		Individual robert = new Individual("Robert");
		entityManager.persist(alex);
		entityManager.persist(robert);
		entityManager.flush();

		List<Individual> found = individualRepository.findDistinctIndividualByUserOrName(alex, robert.getName());
		// then
		assertThat(found).hasSize(1);
		assertThat(found.get(0).getName()).isEqualTo(robert.getName());
	}
}