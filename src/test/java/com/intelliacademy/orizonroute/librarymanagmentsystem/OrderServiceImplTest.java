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
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.impl.OrderServiceImpl;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

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
    void givenPageAndSize_whenGetAllOrders_thenReturnPagedOrders() {
        PageRequest pageable = PageRequest.of(0, 5);
        Page<Order> orderPage = new PageImpl<>(List.of(order));
        when(orderRepository.findAll(pageable)).thenReturn(orderPage);
        when(orderMapper.toDTO(order)).thenReturn(orderDTO);

        Page<OrderDTO> result = orderServiceImpl.getAllOrders(0, 5);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(orderRepository).findAll(pageable);
        verify(orderMapper).toDTO(order);
    }

    @Test
    void givenOrdersBorrowed_whenGetBorrowedOrdersCount_thenReturnCorrectCount() {
        when(orderRepository.countByStatus(OrderStatus.BORROWED)).thenReturn(5L);

        Long count = orderServiceImpl.getBorrowedOrdersCount();

        assertThat(count).isEqualTo(5);
        verify(orderRepository).countByStatus(OrderStatus.BORROWED);
    }

    @Test
    void givenValidInputs_whenCreateOrder_thenOrderShouldBeSaved() {
        when(studentRepository.findBySif("S12345")).thenReturn(Optional.of(student));
        when(bookRepository.findBookByIsbn("ISBN12345")).thenReturn(Optional.of(book));
        when(orderRepository.save(any())).thenReturn(order);
        when(orderMapper.toDTO(order)).thenReturn(orderDTO);

        OrderDTO savedOrder = orderServiceImpl.createOrder("S12345", "ISBN12345", "2025-02-28", "Note");

        assertThat(savedOrder).isNotNull();
        verify(orderRepository).save(any());
        verify(bookRepository).save(book);
    }

    @Test
    void givenInvalidStudent_whenCreateOrder_thenThrowStudentNotFoundException() {
        when(studentRepository.findBySif("S00000")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderServiceImpl.createOrder("S00000", "ISBN12345", "2025-02-28", "Note"))
                .isInstanceOf(StudentNotFoundException.class);

        verify(orderRepository, never()).save(any());
    }

    @Test
    void givenOutOfStockBook_whenCreateOrder_thenThrowRuntimeException() {
        book.setStock(0L);
        when(studentRepository.findBySif("S12345")).thenReturn(Optional.of(student));
        when(bookRepository.findBookByIsbn("ISBN12345")).thenReturn(Optional.of(book));

        assertThatThrownBy(() -> orderServiceImpl.createOrder("S12345", "ISBN12345", "2025-02-28", "Note"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Book is out of stock");

        verify(orderRepository, never()).save(any());
    }

    @Test
    void givenValidInputs_whenReturnOrder_thenUpdateOrderAndBookStock() {
        when(studentRepository.findBySif("S12345")).thenReturn(Optional.of(student));
        when(bookRepository.findBookByIsbn("ISBN12345")).thenReturn(Optional.of(book));
        when(orderRepository.findByStudentSif("S12345")).thenReturn(List.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        orderServiceImpl.returnOrder("S12345", "ISBN12345");

        assertThat(order.isReturned()).isTrue();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.RETURNED);
        verify(bookRepository).save(book);
        verify(orderRepository).save(order);
    }

    @Test
    void givenOverdueOrder_whenReturnOrder_thenCalculateFine() {
        order.setDueDate(LocalDateTime.now().minusDays(5));
        when(studentRepository.findBySif("S12345")).thenReturn(Optional.of(student));
        when(bookRepository.findBookByIsbn("ISBN12345")).thenReturn(Optional.of(book));
        when(orderRepository.findByStudentSif("S12345")).thenReturn(List.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        orderServiceImpl.returnOrder("S12345", "ISBN12345");

        assertThat(order.getStatus()).isEqualTo(OrderStatus.OVERDUE);
        assertThat(order.getFineAmount()).isEqualTo(BigDecimal.valueOf(25));
        verify(bookRepository).save(book);
        verify(orderRepository).save(order);
    }

    @Test
    void givenInvalidOrder_whenReturnOrder_thenThrowOrderNotFoundException() {
        when(studentRepository.findBySif("S12345")).thenReturn(Optional.of(student));
        when(bookRepository.findBookByIsbn("ISBN12345")).thenReturn(Optional.of(book));
        when(orderRepository.findByStudentSif("S12345")).thenReturn(List.of());

        assertThatThrownBy(() -> orderServiceImpl.returnOrder("S12345", "ISBN12345"))
                .isInstanceOf(OrderNotFoundException.class);

        verify(bookRepository, never()).save(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void givenExistingOrderId_whenGetOrderById_thenReturnOrderDTO() {
        // Mock order object
        Order order = new Order();
        order.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toDTO(order)).thenReturn(new OrderDTO());

        OrderDTO orderDTO = orderServiceImpl.getOrderById(1L);

        assertNotNull(orderDTO);
        verify(orderRepository).findById(1L);
        verify(orderMapper).toDTO(order);
    }

    @Test
    void givenNonExistingOrderId_whenGetOrderById_thenThrowOrderNotFoundException() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderServiceImpl.getOrderById(1L));

        verify(orderRepository).findById(1L);
    }
    @Test
    void givenNonExistingStudent_whenReturnOrder_thenThrowStudentNotFoundException() {
        when(studentRepository.findBySif(anyString())).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> orderServiceImpl.returnOrder("studentSif", "12345"));
    }

    @Test
    void givenNonExistingBook_whenReturnOrder_thenThrowBookNotFoundException() {
        when(studentRepository.findBySif(anyString())).thenReturn(Optional.of(new Student()));
        when(bookRepository.findBookByIsbn(anyString())).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> orderServiceImpl.returnOrder("studentSif", "12345"));
    }

    @Test
    void givenValidStudentAndBook_whenReturnOrder_thenProcessReturnOrderSuccessfully() {
        Student student = new Student();
        Book book = new Book();
        book.setIsbn("12345");
        book.setStock(5L);

        Order order = new Order();
        order.setBook(book);
        order.setReturned(false);
        order.setOrderTimestamp(LocalDateTime.now().minusDays(5));
        order.setDueDate(LocalDateTime.now().plusDays(5));

        when(studentRepository.findBySif(anyString())).thenReturn(Optional.of(student));
        when(bookRepository.findBookByIsbn(anyString())).thenReturn(Optional.of(book));
        when(orderRepository.findByStudentSif(anyString())).thenReturn(Collections.singletonList(order));

        orderServiceImpl.returnOrder("studentSif", "12345");

        verify(orderRepository).findByStudentSif("studentSif");
        verify(bookRepository).save(book);
    }

//    @Test
//    void givenValidStudentAndBook_whenReturnOrder_thenProcessReturnOrderSuccessfully2() {
//        Student student = new Student();
//        Book book = new Book();
//        book.setIsbn("12345");
//        book.setStock(5L);
//
//        Book book2 = new Book();
//        book.setIsbn("123453");
//        book.setStock(5L);
//
//        Order order = new Order();
//        order.setBook(book2);
//        order.setReturned(false);
//        order.setOrderTimestamp(LocalDateTime.now().minusDays(5));
//        order.setDueDate(LocalDateTime.now().plusDays(5));
//
//        when(studentRepository.findBySif(anyString())).thenReturn(Optional.of(student));
//        when(bookRepository.findBookByIsbn(anyString())).thenReturn(Optional.of(book));
//        when(orderRepository.findByStudentSif(anyString())).thenReturn(Collections.singletonList(order));
//
//        orderService.returnOrder("studentSif", "12345");
//
//        verify(orderRepository).findByStudentSif("studentSif");
//        verify(bookRepository).save(book);
//    }


    @Test
    void givenInvalidBookIsbn_whenCreateOrder_thenThrowBookNotFoundException() {
        Student student = new Student();
        when(studentRepository.findBySif(anyString())).thenReturn(Optional.of(student));

        when(bookRepository.findBookByIsbn(anyString())).thenReturn(Optional.empty());

        String invalidBookIsbn = "invalid-isbn";
        assertThrows(BookNotFoundException.class, () -> {
            orderServiceImpl.createOrder("studentSif", invalidBookIsbn, "2025-12-31", "Notes");
        });

        verify(bookRepository).findBookByIsbn(invalidBookIsbn);
    }







}
