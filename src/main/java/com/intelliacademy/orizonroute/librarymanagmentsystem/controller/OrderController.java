package com.intelliacademy.orizonroute.librarymanagmentsystem.controller;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.OrderDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.BookService;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.OrderService;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public String listOrders(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             Model model) {
        Page<OrderDTO> orders = orderService.getAllOrders(page, size);

        if (orders.getTotalElements() > 0 && orders.getContent().isEmpty()) {
            return "redirect:/orders?page=0";
        }

        model.addAttribute("orders", orders.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orders.getTotalPages());
        return "order/list";
    }

    @GetMapping("/create")
    public String showCreateOrderForm(Model model) {
        model.addAttribute("order", new OrderDTO());
        model.addAttribute("students", studentService.getStudentList());
        model.addAttribute("books", bookService.getAllBookList());
        return "order/create";
    }

    @GetMapping("/return")
    public String showReturnOrderForm(Model model) {
        model.addAttribute("order", new OrderDTO());
        model.addAttribute("students", studentService.getStudentList());
        model.addAttribute("books", bookService.getAllBookList());
        return "order/return";
    }

    @PostMapping("/create")
    public String createOrder(@ModelAttribute("order") OrderDTO orderDTO) {
        orderService.createOrder(orderDTO.getStudentSif(), orderDTO.getBookIsbn());
        return "redirect:/orders";
    }

    @PostMapping("/return")
    public String returnOrder(@RequestParam("studentSif") String studentSif,
                              @RequestParam("bookIsbn") String bookIsbn,
                              Model model) {
        try {
            orderService.returnOrder(studentSif, bookIsbn);
            return "redirect:/orders";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("students", studentService.getStudentList());
            model.addAttribute("books", bookService.getAllBookList());
            return "order/return";
        }
    }
}
