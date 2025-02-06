package com.intelliacademy.orizonroute.librarymanagmentsystem.controller;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.OrderDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.BookService;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.OrderService;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final StudentService studentService;
    private final BookService bookService;

    @GetMapping
    public String listOrders(Model model) {
        List<OrderDTO> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "order/list";
    }

    @GetMapping("/create")
    public String showCreateOrderForm(Model model) {
        model.addAttribute("order", new OrderDTO());
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("books", bookService.getAllBooks());
        return "order/create";
    }

    @GetMapping("/return")
    public String showReturnOrderForm(Model model) {
        model.addAttribute("order", new OrderDTO());
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("books", bookService.getAllBooks());
        return "order/return";
    }


    @PostMapping("/create")
    public String createOrder(@ModelAttribute("order") OrderDTO orderDTO, Model model) {
        try {
            System.out.println("1111");
            orderService.createOrder(orderDTO.getStudentSif(), orderDTO.getBookId());
            System.out.println("222222222222222");
            return "redirect:/orders";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("students", studentService.getAllStudents());
            model.addAttribute("books", bookService.getAllBooks());
            return "order/create";
        }
    }


    @PostMapping("/return")
    public String returnOrder(@ModelAttribute("order") OrderDTO orderDTO, Model model) {
        try {
            orderService.returnOrder(orderDTO.getStudentSif(), orderDTO.getBookId());
            return "redirect:/orders";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("students", studentService.getAllStudents());
            model.addAttribute("books", bookService.getAllBooks());
            return "order/return";
        }
    }
}
