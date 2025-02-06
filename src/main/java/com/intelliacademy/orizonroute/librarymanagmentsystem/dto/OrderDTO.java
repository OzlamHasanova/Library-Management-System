package com.intelliacademy.orizonroute.librarymanagmentsystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderDTO {
    private Long id;
    private String studentSif;
    private Long bookId;
    private String status;
    private LocalDateTime orderTimestamp;
    private LocalDateTime returnTimestamp;
    private LocalDateTime dueDate;
    private BigDecimal fineAmount;
    private boolean returned;
    private String notes;
}
