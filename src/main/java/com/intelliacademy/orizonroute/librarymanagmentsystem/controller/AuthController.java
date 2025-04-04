package com.intelliacademy.orizonroute.librarymanagmentsystem.controller;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.UserDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.AuthorService;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.BookService;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.CategoryService;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final BookService bookService;
    private final OrderService orderServiceImpl;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    @GetMapping("/login")
    public ModelAndView loginPage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            return new ModelAndView("redirect:/admin");
        }

        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("userDto", new UserDTO());
        return modelAndView;
    }

    @GetMapping("/admin")
    public String adminDashboard(Model model) {
        Long availableBookCount = bookService.getAvailableBookCount();
        Long borrowedOrdersCount = orderServiceImpl.getBorrowedOrdersCount();
        Long authorCount = authorService.getAuthorCount();
        Long categoryCount = categoryService.getCategoryCount();

        model.addAttribute("availableBooks", availableBookCount);
        model.addAttribute("borrowedOrders", borrowedOrdersCount);
        model.addAttribute("authorCount", authorCount);
        model.addAttribute("categoryCount", categoryCount);
        return "admin";
    }
}
