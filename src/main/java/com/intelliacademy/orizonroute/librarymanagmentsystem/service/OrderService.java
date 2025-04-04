package com.intelliacademy.orizonroute.librarymanagmentsystem.service;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.OrderDTO;
import org.springframework.data.domain.Page;

public interface OrderService {
    Page<OrderDTO> getAllOrders(int page, int size);
    Long getBorrowedOrdersCount();
    OrderDTO getOrderById(Long orderId);
    OrderDTO createOrder(String sif, String bookIsbn, String dueDate, String notes);
    void returnOrder(String studentSif, String bookIsbn);
}
