package com.intelliacademy.orizonroute.librarymanagmentsystem;

import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Order;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.enums.OrderStatus;
import com.intelliacademy.orizonroute.librarymanagmentsystem.repository.OrderRepository;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.FineCalculationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class FineCalculationServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private FineCalculationService fineCalculationService;

    private List<Order> overdueOrders;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        overdueOrders = new ArrayList<>();

        Order order1 = new Order();
        order1.setId(1L);
        order1.setStatus(OrderStatus.BORROWED);
        order1.setDueDate(LocalDateTime.now().minusDays(1));
        order1.setFineAmount(BigDecimal.ZERO);

        Order order2 = new Order();
        order2.setId(2L);
        order2.setStatus(OrderStatus.BORROWED);
        order2.setDueDate(LocalDateTime.now().minusDays(3));
        order2.setFineAmount(BigDecimal.valueOf(5.0));

        overdueOrders.add(order1);
        overdueOrders.add(order2);
    }

    @Test
    void applyDailyFines_whenOverdueOrdersExist_thenFinesAreUpdated() {
        // Arrange
        when(orderRepository.findAllByStatusAndDueDateBefore(eq(OrderStatus.BORROWED), any(LocalDateTime.class)))
                .thenReturn(overdueOrders);

        // Act
        fineCalculationService.applyDailyFines();

        // Assert
        verify(orderRepository).findAllByStatusAndDueDateBefore(eq(OrderStatus.BORROWED), any(LocalDateTime.class));
        verify(orderRepository).saveAll(overdueOrders);

        assert overdueOrders.get(0).getFineAmount().equals(BigDecimal.valueOf(0.5));

        assert overdueOrders.get(1).getFineAmount().equals(BigDecimal.valueOf(5.5));
    }

    @Test
    void applyDailyFines_whenNoOverdueOrders_thenNoFinesUpdated() {
        // Arrange
        when(orderRepository.findAllByStatusAndDueDateBefore(eq(OrderStatus.BORROWED), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());

        // Act
        fineCalculationService.applyDailyFines();

        // Assert
        verify(orderRepository).findAllByStatusAndDueDateBefore(eq(OrderStatus.BORROWED), any(LocalDateTime.class));
        verify(orderRepository, never()).saveAll(anyList());
    }

    @Test
    void applyDailyFines_whenOrderWithNullFineAmount_thenFineInitializedToZero() {
        // Arrange
        overdueOrders.get(0).setFineAmount(null);
        when(orderRepository.findAllByStatusAndDueDateBefore(eq(OrderStatus.BORROWED), any(LocalDateTime.class)))
                .thenReturn(overdueOrders);

        // Act
        fineCalculationService.applyDailyFines();

        // Assert
        verify(orderRepository).findAllByStatusAndDueDateBefore(eq(OrderStatus.BORROWED), any(LocalDateTime.class));
        verify(orderRepository).saveAll(overdueOrders);

        assert overdueOrders.get(0).getFineAmount().equals(BigDecimal.valueOf(0.5));

        assert overdueOrders.get(1).getFineAmount().equals(BigDecimal.valueOf(5.5));
    }
}
