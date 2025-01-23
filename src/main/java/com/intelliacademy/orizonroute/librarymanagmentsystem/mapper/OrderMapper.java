package com.intelliacademy.orizonroute.librarymanagmentsystem.mapper;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.OrderDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Book;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Order;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Student;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderDTO toDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setStudentSif(order.getStudent().getSif());
        dto.setBookId(order.getBook().getId());
        dto.setOrderTimestamp(order.getOrderTimestamp());
        dto.setReturnTimestamp(order.getReturnTimestamp());
        return dto;
    }

    public Order toEntity(OrderDTO dto, Student student, Book book) {
        Order order = new Order();
        order.setId(dto.getId());
        order.setStudent(student);
        order.setBook(book);
        order.setOrderTimestamp(dto.getOrderTimestamp());
        order.setReturnTimestamp(dto.getReturnTimestamp());
        return order;
    }
}

