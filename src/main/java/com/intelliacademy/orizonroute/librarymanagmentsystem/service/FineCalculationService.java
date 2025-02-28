package com.intelliacademy.orizonroute.librarymanagmentsystem.service;

import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Order;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.enums.OrderStatus;
import com.intelliacademy.orizonroute.librarymanagmentsystem.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FineCalculationService {

    private final OrderRepository orderRepository;


    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void applyDailyFines() {
        log.info("Checking for overdue orders...");

        List<Order> overdueOrders = orderRepository.findAllByStatusAndDueDateBefore(
                OrderStatus.BORROWED, LocalDateTime.now()
        );

        if (overdueOrders.isEmpty()) {
            log.info("No overdue orders found.");
            return;
        }

        for (Order order : overdueOrders) {
            if (order.getFineAmount() == null) {
                order.setFineAmount(BigDecimal.ZERO);
            }

            BigDecimal newFine = order.getFineAmount().add(BigDecimal.valueOf(0.5));
            order.setFineAmount(newFine);
            log.info("Fine updated for order ID {}: New Fine Amount = {}", order.getId(), newFine);
        }

        orderRepository.saveAll(overdueOrders);
        log.info("Fine calculation completed successfully.");
    }
}
