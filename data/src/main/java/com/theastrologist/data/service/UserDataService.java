package com.theastrologist.data.service;

import com.theastrologist.data.repository.UserRepository;
import com.theastrologist.exception.UserAlreadyExistsException;
import com.theastrologist.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDataService {
    @Autowired
    private UserRepository userRepository;

    public User getUserByName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public void createUser(User user) throws UserAlreadyExistsException {
        User dataBaseUser = getUserByName(user.getUserName());
        if(dataBaseUser == null) {
            userRepository.save(user);
        } else {
            throw new UserAlreadyExistsException();
        }
    }

    public List<User> getUsers() {

        List<User> userList = new ArrayList<>();

        // lambda expression
        // findAll() retrieve an Iterator

        // method reference ...
        userRepository.findAll().forEach(userList::add);
        // ... it is equivalent to
        // userRepository.findAll().forEach(article -&gt; articleList.add(article));

        return userList;
    }
}
