package org.se.webshop.service;

import org.se.webshop.entity.ShoppingBasket;
import org.se.webshop.entity.User;
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
    private User user;
    private ShoppingBasket shoppingBasket=new ShoppingBasket();

    public boolean registerUser(User user) {
     if (userRepo.findByUserName(user.getUserName()).size()>0) {
         return false;
     }
     user.setRole("USER");
         user= userRepo.save(user);
     return true;
    }

    public User getUserByUsername(String username) {
        List<User> users=userRepo.findByUserName(username);
        if(users.size()>0){
            return users.get(0);
        }
        return null;

    }
    public boolean loginUser(User user) {

        List<User> usersList=userRepo.findByUserName(user.getUserName());

        if(usersList.size()>0){
            User tempuser=usersList.get(0);
            if(tempuser.getPassword().equals(user.getPassword())){
                this.user=tempuser;
                return true;
            }

        }

        return false;
    }


    public User getUserById(Long userId) {
        return userRepo.getById(userId);
    }
}
