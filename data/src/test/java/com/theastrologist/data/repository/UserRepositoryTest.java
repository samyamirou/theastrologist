package com.theastrologist.data.repository;

import com.theastrologist.data.DataConfiguration;
import com.theastrologist.data.DataTestConfiguration;
import com.theastrologist.domain.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = {DataTestConfiguration.class})
//@AutoConfigurationPackage
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userDataRepository;

    @Before
    public void setup() {
    }


    @Test
    public void findByName() {
        // given
        User alex = new User("Choupi");
        entityManager.persist(alex);
        entityManager.flush();

        User found = userDataRepository.findByUserName(alex.getUserName());
        // then
        assertThat(found.getUserName()).isEqualTo(alex.getUserName());
    }
}