package com.intelliacademy.orizonroute.librarymanagmentsystem.mapper;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.OrderDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Order;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.enums.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderDTO toDTO(Order order) {
        if (order == null) {
            return null;
        }

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setStudentSif(order.getStudent().getSif());
        orderDTO.setBookId(order.getBook().getId());
        orderDTO.setStatus(order.getStatus().name());
        orderDTO.setOrderTimestamp(order.getOrderTimestamp());
        orderDTO.setReturnTimestamp(order.getReturnTimestamp());
        orderDTO.setDueDate(order.getDueDate());
        orderDTO.setFineAmount(order.getFineAmount());
        orderDTO.setReturned(order.isReturned());
        orderDTO.setNotes(order.getNotes());

        return orderDTO;
    }

    public Order toEntity(OrderDTO orderDTO) {
        if (orderDTO == null) {
            return null;
        }

        Order order = new Order();
        order.setId(orderDTO.getId());
        order.setStatus(OrderStatus.valueOf(orderDTO.getStatus()));
        order.setOrderTimestamp(orderDTO.getOrderTimestamp());
        order.setReturnTimestamp(orderDTO.getReturnTimestamp());
        order.setDueDate(orderDTO.getDueDate());
        order.setFineAmount(orderDTO.getFineAmount());
        order.setReturned(orderDTO.isReturned());
        order.setNotes(orderDTO.getNotes());

        return order;
    }
}
