package org.se.webshop.controller;

import org.se.webshop.entity.User;
import org.se.webshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

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
    public String login(@ModelAttribute User user, Model model) {
        boolean isLogin = userService.loginUser(user);
        if (isLogin) {
            return "redirect:/products";
        }
        model.addAttribute("error", "Invalid username or password!");
        return "userRegister";

    }



}
