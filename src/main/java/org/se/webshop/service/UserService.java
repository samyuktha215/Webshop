package org.se.webshop.service;

import lombok.Getter;
import org.se.webshop.entity.User;
import org.se.webshop.entity.UserRole;
import org.se.webshop.repo.OrderRepo;
import org.se.webshop.repo.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.List;

@Service
@SessionScope
public class UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private OrderRepo orderRepo;

    @Getter
    private ShoppingBasket shoppingBasket=new ShoppingBasket();

    @Getter
    private User loggedInUser;

    public UserRole getUserRole(String userName) {
        User user=userRepo.getUserByUserName(userName);
        if (user == null) {
            System.out.println("User not found: " + userName);
            return null;
        }
        String roleString = String.valueOf(user.getRole());  // The role should be stored as a String in your User entity
        try {
            return UserRole.valueOf(roleString);  // Convert the String to the UserRole enum
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid role for user: " + userName);
            return null;
        }
    }

    public boolean registerUser(User user) {
     if (userRepo.findByUserName(user.getUserName()).size()>0) {
         return false;
     }
     user.setRole(UserRole.USER);
         user= userRepo.save(user);
     return true;
    }

    public boolean loginUser(User user) {
        List<User> usersList=userRepo.findByUserName(user.getUserName());

        if (!usersList.isEmpty()) {
            User tempUser = usersList.get(0);
            if (tempUser.getPassword().equals(user.getPassword())) {
                this.loggedInUser = tempUser;
                System.out.println("Logged in user: " + this.loggedInUser.getUserName());
                return true;
            }
        }
        return false;
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }


    public boolean isAdmin() {
        User loggedInUser=getLoggedInUser();
        return loggedInUser!=null && loggedInUser.getRole().equals("ADMIN");
    }


}
