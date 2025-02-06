package com.intelliacademy.orizonroute.librarymanagmentsystem.service;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;
    private final OrderMapper orderMapper;

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDTO createOrder(String sif, Long bookId) {
        log.info("Creating order for student: {}, book: {}", sif, bookId);

        Student student = studentRepository.findBySif(sif)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with SIF: " + sif));
        log.info("Student found: {}", student.getSif());

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + bookId));
        log.info("Book found: {}", book.getTitle());

        if (book.getStock() == null || book.getStock() <= 0) {
            log.error("Book out of stock!");
            throw new RuntimeException("Book is out of stock");
        }

        book.setStock(book.getStock() - 1);
        bookRepository.save(book);

        Order order = new Order();
        order.setStudent(student);
        order.setBook(book);
        order.setStatus(OrderStatus.BORROWED);
        order.setOrderTimestamp(LocalDateTime.now());
        order.setDueDate(LocalDateTime.now().plusWeeks(2));

        Order savedOrder = orderRepository.save(order);
        log.info("Order saved with ID: {}", savedOrder.getId());

        return orderMapper.toDTO(savedOrder);
    }

    @Transactional
    public OrderDTO returnOrder(String sif, Long bookId) {
        Student student = studentRepository.findBySif(sif)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        Order order = orderRepository.findByStudentSif(sif).stream()
                .filter(o -> o.getBook().getId().equals(bookId) && !o.isReturned())
                .findFirst()
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        book.setStock(book.getStock() + 1);
        bookRepository.save(book);

        order.setReturnTimestamp(LocalDateTime.now());
        order.setStatus(OrderStatus.RETURNED);
        order.setReturned(true);

        if (order.getReturnTimestamp().isAfter(order.getDueDate())) {
            order.setStatus(OrderStatus.OVERDUE);
            order.setFineAmount(BigDecimal.valueOf(5).multiply(
                    BigDecimal.valueOf(order.getReturnTimestamp().getDayOfYear() - order.getDueDate().getDayOfYear())));
        }

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDTO(savedOrder);
    }
}
