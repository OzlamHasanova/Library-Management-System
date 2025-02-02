package com.intelliacademy.orizonroute.librarymanagmentsystem.controller;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.OrderDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public String listOrders(Model model) {
        List<OrderDTO> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "order/list";
    }

    @GetMapping("/create")
    public String showCreateOrderForm(Model model) {
        model.addAttribute("order", new OrderDTO());
        return "order/create";
    }

    @PostMapping("/create")
    public String createOrder(@ModelAttribute("order") OrderDTO orderDTO, Model model) {
        try {
            orderService.createOrder(orderDTO.getStudentSif(), orderDTO.getBookId());
            return "redirect:/orders";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "order/create";
        }
    }

    @GetMapping("/return")
    public String showReturnOrderForm(Model model) {
        model.addAttribute("order", new OrderDTO());
        return "order/return";
    }

    @PostMapping("/return")
    public String returnOrder(@ModelAttribute("order") OrderDTO orderDTO, Model model) {
        try {
            orderService.returnOrder(orderDTO.getStudentSif(), orderDTO.getBookId());
            return "redirect:/orders";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "order/return";
        }
    }
}
