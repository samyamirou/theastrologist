package com.theastrologist.service;

import com.theastrologist.data.repository.IndividualRepository;
import com.theastrologist.data.service.IndividualDataService;
import com.theastrologist.domain.SkyPosition;
import com.theastrologist.domain.individual.Individual;
import com.theastrologist.domain.user.User;
import com.theastrologist.exception.IndividualAlreadyExistsException;
import com.theastrologist.exception.TooManyResultsException;
import com.theastrologist.exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndividualService {

	@Autowired
	private IndividualDataService individualDataService;

    public IndividualService() {
    }

    public Individual findIndividualByName(User user, String individualName) throws TooManyResultsException {
        List<Individual> individuals = individualDataService.getIndividualsByUserAndName(user, individualName);
        Individual individual = null;

        if (individuals.size() > 1) {
            throw new TooManyResultsException();
        } else if(!individuals.isEmpty()) {
            individual = individuals.get(0);
        }

        return individual;
    }

    public Individual createIndividual(User user, String name, SkyPosition natalTheme) throws IndividualAlreadyExistsException {
		Individual individual = new Individual(name, natalTheme);
		individualDataService.createIndividual(user, individual);
        return individual;
    }
}
