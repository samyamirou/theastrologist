package com.theastrologist.service;

import com.theastrologist.data.service.UserDataService;
import com.theastrologist.domain.user.User;
import com.theastrologist.exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDataService userDataService;

    public User getUser(String username){
        return userDataService.getUserByName(username);
    }

    public User createUser(String username) throws UserAlreadyExistsException {
        User user = new User(username);
        userDataService.createUser(user);
        return user;
    }
}
