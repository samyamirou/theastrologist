package com.theastrologist.data.service;

import com.theastrologist.data.repository.IndividualRepository;
import com.theastrologist.domain.SkyPosition;
import com.theastrologist.domain.individual.Individual;
import com.theastrologist.domain.user.User;
import com.theastrologist.exception.IndividualAlreadyExistsException;
import com.theastrologist.exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IndividualDataService {
    @Autowired
    private IndividualRepository individualRepository;

    public List<Individual> getIndividualsByUserAndName(User user, String name) {
        return individualRepository.findDistinctIndividualByUserOrName(user, name);
    }

    public void createIndividual(User user, Individual individual) throws IndividualAlreadyExistsException {
        List<Individual> dataBaseIndividual = getIndividualsByUserAndName(user, individual.getName());
        if(dataBaseIndividual.isEmpty()) {
            individualRepository.save(individual);
        } else {
            throw new IndividualAlreadyExistsException();
        }
    }
}
