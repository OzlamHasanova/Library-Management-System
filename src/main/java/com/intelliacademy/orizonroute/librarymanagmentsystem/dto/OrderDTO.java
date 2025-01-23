package com.intelliacademy.orizonroute.librarymanagmentsystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderDTO {
    private Long id;
    private String studentSif;
    private Long bookId;
    private LocalDateTime orderTimestamp;
    private LocalDateTime returnTimestamp;
}

