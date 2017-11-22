package com.theastrologist.data.repository;

import com.theastrologist.domain.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends CrudRepository<User, Long> {
    public User findByUserName(String name);
}
