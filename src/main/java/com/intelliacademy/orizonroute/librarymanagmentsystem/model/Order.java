package com.intelliacademy.orizonroute.librarymanagmentsystem.model;

import com.intelliacademy.orizonroute.librarymanagmentsystem.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.BORROWED;

    private LocalDateTime orderTimestamp;
    private LocalDateTime returnTimestamp;
    private LocalDateTime dueDate;

    @Column(precision = 10, scale = 2)
    private BigDecimal fineAmount = BigDecimal.ZERO;

    private boolean returned = false;

    @Column(length = 500)
    private String notes;
}
