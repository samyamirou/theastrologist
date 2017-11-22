package com.theastrologist.data.repository;

import com.theastrologist.domain.individual.Individual;
import com.theastrologist.domain.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IndividualRepository extends CrudRepository<Individual, Long> {
	List<Individual> findDistinctIndividualByUserOrName(User user, String name);

}
