package com.intelliacademy.orizonroute.librarymanagmentsystem;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.OrderDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.exception.BookNotFoundException;
import com.intelliacademy.orizonroute.librarymanagmentsystem.exception.OrderNotFoundException;
import com.intelliacademy.orizonroute.librarymanagmentsystem.exception.StudentNotFoundException;
import com.intelliacademy.orizonroute.librarymanagmentsystem.mapper.OrderMapper;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Book;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Order;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Student;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.enums.OrderStatus;
import com.intelliacademy.orizonroute.librarymanagmentsystem.repository.BookRepository;
import com.intelliacademy.orizonroute.librarymanagmentsystem.repository.OrderRepository;
import com.intelliacademy.orizonroute.librarymanagmentsystem.repository.StudentRepository;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    private Student student;
    private Book book;
    private Order order;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setSif("S12345");

        book = new Book();
        book.setId(1L);
        book.setIsbn("ISBN12345");
        book.setStock(5L);

        order = new Order();
        order.setStudent(student);
        order.setBook(book);
        order.setStatus(OrderStatus.BORROWED);
        order.setOrderTimestamp(LocalDateTime.now());
        order.setDueDate(LocalDateTime.now().plusWeeks(2));
    }

    @Test
    void givenStudentAndBook_whenCreateOrder_thenSaveOrder() {
        when(studentRepository.findBySif("S12345")).thenReturn(Optional.of(student));
        when(bookRepository.findBookByIsbn("ISBN12345")).thenReturn(Optional.of(book));
        when(orderRepository.save(any())).thenReturn(order);

        orderService.createOrder("S12345", "ISBN12345");

        verify(orderRepository, times(1)).save(any());
    }

    @Test
    void givenInvalidStudent_whenCreateOrder_thenThrowException() {
        when(studentRepository.findBySif("S00000")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder("S00000", "ISBN12345"))
                .isInstanceOf(StudentNotFoundException.class);

        verify(orderRepository, never()).save(any());
    }

    @Test
    void givenValidOrder_whenReturnOrder_thenUpdateStatus() {
        when(orderRepository.findByStudentSif("S12345")).thenReturn(List.of(order));
        when(bookRepository.findBookByIsbn("ISBN12345")).thenReturn(Optional.of(book));

        orderService.returnOrder("S12345", "ISBN12345");

        assertThat(order.getStatus()).isEqualTo(OrderStatus.RETURNED);
        assertThat(order.isReturned()).isTrue();

        verify(orderRepository, times(1)).save(order);
        verify(bookRepository, times(1)).save(book);
    }


    @Test
    void givenInvalidOrder_whenReturnOrder_thenThrowException() {
        when(orderRepository.findByStudentSif("S00000")).thenReturn(List.of());

        assertThatThrownBy(() -> orderService.returnOrder("S00000", "ISBN12345"))
                .isInstanceOf(OrderNotFoundException.class);

        verify(orderRepository, never()).save(any());
        verify(bookRepository, never()).save(any());
    }

}
