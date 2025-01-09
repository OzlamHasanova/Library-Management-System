package com.intelliacademy.orizonroute.librarymanagmentsystem.controller;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {

    @GetMapping("/login")
    public ModelAndView loginPage() {
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("userDto", new UserDTO());
        return modelAndView;
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("userDto") UserDTO userDto) {
        return "redirect:/admin";
    }

    @GetMapping("/admin")
    public String adminDashboard() {
        return "admin";
    }
}
