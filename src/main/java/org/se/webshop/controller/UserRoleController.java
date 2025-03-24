package org.se.webshop.controller;

import org.se.webshop.entity.User;
import org.se.webshop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserRoleController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserService userService;

    @GetMapping("/role")
    public String getUserRole(@RequestParam String userName,Model model) {
        String role = userService.getUserRole(userName);

        if (role == null) {
            model.addAttribute("message", "User " + userName + " not found.");
        } else {
            model.addAttribute("userName", userName);
            model.addAttribute("role", role);
        }

        return "users";
    }


    @GetMapping("/all-users")
    public String getAllUsers(Model model) {
        List<User> usersList = userService.getAllUsers();
        model.addAttribute("usersList", usersList);
        return "users";
    }

    @GetMapping("/users-by-role")
    public String getUsersByRole(@RequestParam String role, Model model) {
        List<User> usersList = userService.getAllUsers();

        if (usersList.isEmpty()) {
            System.out.println("No users found with role: " + role);
        } else {
            System.out.println("Retrieved Users for role " + role + ": " + usersList);
        }

        model.addAttribute("usersList", usersList);
        return "users";
    }
}
