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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
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
    private OrderDTO orderDTO;

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
        order.setId(1L);
        order.setStudent(student);
        order.setBook(book);
        order.setStatus(OrderStatus.BORROWED);
        order.setOrderTimestamp(LocalDateTime.now());
        order.setDueDate(LocalDateTime.now().plusWeeks(2));

        orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setStudentSif(student.getSif());
        orderDTO.setBookIsbn(book.getIsbn());
    }

    @Test
    void getAllOrders_shouldReturnPagedOrders() {
        PageRequest pageable = PageRequest.of(0, 5);
        Page<Order> orderPage = new PageImpl<>(List.of(order));
        when(orderRepository.findAll(pageable)).thenReturn(orderPage);
        when(orderMapper.toDTO(order)).thenReturn(orderDTO);

        Page<OrderDTO> result = orderService.getAllOrders(0, 5);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(orderRepository).findAll(pageable);
        verify(orderMapper).toDTO(order);
    }

    @Test
    void getBorrowedOrdersCount_shouldReturnCorrectCount() {
        when(orderRepository.countByStatus(OrderStatus.BORROWED)).thenReturn(5L);

        Long count = orderService.getBorrowedOrdersCount();

        assertThat(count).isEqualTo(5);
        verify(orderRepository).countByStatus(OrderStatus.BORROWED);
    }

    @Test
    void createOrder_givenValidInputs_shouldSaveOrder() {
        when(studentRepository.findBySif("S12345")).thenReturn(Optional.of(student));
        when(bookRepository.findBookByIsbn("ISBN12345")).thenReturn(Optional.of(book));
        when(orderRepository.save(any())).thenReturn(order);
        when(orderMapper.toDTO(order)).thenReturn(orderDTO);

        OrderDTO savedOrder = orderService.createOrder("S12345", "ISBN12345", "2025-02-28", "Note");

        assertThat(savedOrder).isNotNull();
        verify(orderRepository).save(any());
        verify(bookRepository).save(book);
    }

    @Test
    void createOrder_givenInvalidStudent_shouldThrowException() {
        when(studentRepository.findBySif("S00000")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder("S00000", "ISBN12345", "2025-02-28", "Note"))
                .isInstanceOf(StudentNotFoundException.class);

        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrder_givenOutOfStockBook_shouldThrowException() {
        book.setStock(0L);
        when(studentRepository.findBySif("S12345")).thenReturn(Optional.of(student));
        when(bookRepository.findBookByIsbn("ISBN12345")).thenReturn(Optional.of(book));

        assertThatThrownBy(() -> orderService.createOrder("S12345", "ISBN12345", "2025-02-28", "Note"))
                .isInstanceOf(RuntimeException.class);

        verify(orderRepository, never()).save(any());
    }

//    @Test
//    void returnOrder_givenValidOrder_shouldUpdateStatus() {
//        when(studentRepository.findBySif("S12345")).thenReturn(Optional.of(student));
//        when(bookRepository.findBookByIsbn("ISBN12345")).thenReturn(Optional.of(book));
//        when(orderRepository.findByStudentSif("S12345")).thenReturn(List.of(order));
//        when(orderMapper.toDTO(order)).thenReturn(orderDTO);
//
//        OrderDTO returnedOrder = orderService.returnOrder("S12345", "ISBN12345");
//
//        assertThat(returnedOrder).isNotNull();
//        assertThat(order.getStatus()).isEqualTo(OrderStatus.RETURNED);
//        verify(orderRepository).save(order);
//        verify(bookRepository).save(book);
//    }

//    @Test
//    void returnOrder_givenOverdueOrder_shouldApplyFine() {
//        order.setDueDate(LocalDateTime.now().minusDays(5));
//        when(studentRepository.findBySif("S12345")).thenReturn(Optional.of(student));
//        when(bookRepository.findBookByIsbn("ISBN12345")).thenReturn(Optional.of(book));
//        when(orderRepository.findByStudentSif("S12345")).thenReturn(List.of(order));
//        when(orderMapper.toDTO(order)).thenReturn(orderDTO);
//
//        OrderDTO returnedOrder = orderService.returnOrder("S12345", "ISBN12345");
//
//        assertThat(returnedOrder).isNotNull();
//        assertThat(order.getStatus()).isEqualTo(OrderStatus.OVERDUE);
//        assertThat(order.getFineAmount()).isEqualTo(BigDecimal.valueOf(25));
//        verify(orderRepository).save(order);
//        verify(bookRepository).save(book);
//    }

    @Test
    void returnOrder_givenInvalidStudent_shouldThrowException() {
        when(studentRepository.findBySif("S00000")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.returnOrder("S00000", "ISBN12345"))
                .isInstanceOf(StudentNotFoundException.class);

        verify(orderRepository, never()).save(any());
        verify(bookRepository, never()).save(any());
    }

//    @Test
//    void returnOrder_givenInvalidOrder_shouldThrowException() {
//        when(studentRepository.findBySif("S12345")).thenReturn(Optional.of(student));
//        when(orderRepository.findByStudentSif("S12345")).thenReturn(List.of());
//
//        assertThatThrownBy(() -> orderService.returnOrder("S12345", "ISBN12345"))
//                .isInstanceOf(OrderNotFoundException.class);
//
//        verify(orderRepository, never()).save(any());
//        verify(bookRepository, never()).save(any());
//    }

    @Test
    void getOrderById_givenValidId_shouldReturnOrderDTO() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toDTO(order)).thenReturn(orderDTO);

        OrderDTO result = orderService.getOrderById(1L);

        assertThat(result).isNotNull();
        verify(orderRepository).findById(1L);
        verify(orderMapper).toDTO(order);
    }
}
