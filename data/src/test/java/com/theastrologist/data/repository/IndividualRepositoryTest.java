package com.theastrologist.data.repository;

import com.theastrologist.data.DataTestConfiguration;
import com.theastrologist.data.service.IndividualDataService;
import com.theastrologist.domain.Degree;
import com.theastrologist.domain.SkyPosition;
import com.theastrologist.domain.individual.Individual;
import com.theastrologist.domain.user.User;
import com.theastrologist.exception.IndividualAlreadyExistsException;
import com.theastrologist.util.CalcUtil;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = {DataTestConfiguration.class})
public class IndividualRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;

	@MockBean
	private IndividualRepository mockedIndividualRepository;

	@Autowired
	private IndividualDataService individualDataService;

	private final DateTime TEST_DATE = new DateTime(1985, 1, 4, 11, 20, CalcUtil.DATE_TIME_ZONE);
	private final Degree LATITUDE = new Degree(48, 39);
	private final Degree LONGITUDE = new Degree(2, 25);

	@Before
	public void setup() {
	}

	@Test
	public void findByName() {
		// given
		User alex = new User("Choupi");
		Individual robert = new Individual("Robert");

		List<Individual> list = newArrayList();
		list.add(robert);

		when(mockedIndividualRepository.findDistinctIndividualByUserOrName(alex,"Robert")).thenReturn(list);

		// WHEN
		List<Individual> found = individualDataService.getIndividualsByUserAndName(alex, "Robert");

		// THEN
		verify(mockedIndividualRepository, times(1)).findDistinctIndividualByUserOrName(alex,"Robert");

		assertThat(found, hasSize(1));
		assertThat(robert.getName(), equalTo(found.get(0).getName()));
	}

	@Test
	public void createIndividualTest() throws IndividualAlreadyExistsException {
		User user = new User("Toto");
		SkyPosition natalTheme = new SkyPosition(TEST_DATE, LATITUDE, LONGITUDE);
		Individual robert = new Individual("Robert", natalTheme);
		user.getIndividuals().add(robert);

		Mockito.when(mockedIndividualRepository.save(robert)).thenReturn(robert);

		// WHEN
		individualDataService.createIndividual(user, robert);

		// THEN
		ArgumentCaptor<Individual> individualArgumentCaptor = ArgumentCaptor.forClass(Individual.class);
		verify(mockedIndividualRepository, times(1)).save(individualArgumentCaptor.capture());

		assertThat(robert.getName(), equalTo(individualArgumentCaptor.getValue().getName()));
	}
}