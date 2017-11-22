package com.theastrologist.data.service;

import com.theastrologist.data.DataTestConfiguration;
import com.theastrologist.data.repository.UserRepository;
import com.theastrologist.exception.UserAlreadyExistsException;
import com.theastrologist.domain.user.User;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DataTestConfiguration.class})
public class UserDataServiceTest {
    @Autowired
    private UserDataService userDataService;

    @MockBean
    private UserRepository userRepository;

    @Before
    public void setup() {
        User alex = new User("alex");
        Mockito.when(userRepository.findByUserName(alex.getUserName())).thenReturn(alex);
    }

    @Test
    public void findByName() {
        String name = "alex";
        User found = userDataService.getUserByName(name);
        assertThat(found.getUserName()).isEqualTo(name);
    }

    @Test
    public void createUserTest() throws Exception {
        //GIVEN
        User user = new User("toto");
        Mockito.when(userRepository.save(user)).thenReturn(user);

        // WHEN
        userDataService.createUser(user);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void createUserExistingTest() throws Exception {
        // GIVEN
        String userName = "toto";
        User user = new User(userName);
        Mockito.when(userRepository.findByUserName(userName)).thenReturn(user);

        // WHEN
        userDataService.createUser(user);
    }

    @Test
    @Ignore
    public void getAllArticlesTest() {
        // when
        List<User> userList = userDataService.getUsers();
        assertThat(userList).isNotNull();

        User user = userList.get(0);
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUserName()).isEqualTo("Choupi");

        User userTwo = userList.get(1);
        assertThat(userTwo.getId()).isEqualTo(2L);
        assertThat(userTwo.getUserName()).isEqualTo("Maybe for fun");
    }
}