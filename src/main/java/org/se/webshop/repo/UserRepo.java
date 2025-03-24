package org.se.webshop.repo;

import org.se.webshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    List<User> findByUserName(String userName);
    User getUserByUserName(String userName);
}
