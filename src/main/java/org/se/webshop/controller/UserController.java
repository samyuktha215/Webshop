package org.se.webshop.controller;

import org.se.webshop.entity.Order;
import org.se.webshop.entity.User;
import org.se.webshop.service.OrderService;
import org.se.webshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "userRegister";
    }
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "userRegister";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        boolean isRegistered = userService.registerUser(user);

        if (isRegistered) {
            return "redirect:/products";
        }

        model.addAttribute("error", "Username already exists or invalid details!");
        return "userRegister";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("User") User user, Model model) {
        boolean isLogin = userService.loginUser(user);
        if (isLogin) {
            User loggedInUser = userService.getLoggedInUser();
            System.out.println("Logged in user: " + loggedInUser.getUserName());
            String role = String.valueOf(userService.getUserRole(user.getUserName()));
            if (role.equals("ADMIN")) {
                return "add-product";
            }
            return "products";
        }
        model.addAttribute("error", "Invalid username or password!");
        return "userRegister";

    }
    @GetMapping("/check-user")
    @ResponseBody
    public String checkUser() {
        User user = userService.getLoggedInUser();
        if (user == null) {
            return "No user is logged in.";
        }
        return "Logged in as: " + user.getUserName() + " with role: " + user.getRole();
    }


}
