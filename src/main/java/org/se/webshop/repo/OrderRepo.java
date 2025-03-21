package org.se.webshop.repo;

import org.se.webshop.entity.Order;
import org.se.webshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
