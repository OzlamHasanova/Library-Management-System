package com.intelliacademy.orizonroute.librarymanagmentsystem.repository;


import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Order;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStudentSif(String sif);

    List<Order> findAllByStatusAndDueDateBefore(OrderStatus status, LocalDateTime dueDate);
}
