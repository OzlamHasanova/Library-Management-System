package com.intelliacademy.orizonroute.librarymanagmentsystem.service;

import com.intelliacademy.orizonroute.librarymanagmentsystem.common.ErrorMessages;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;
    private final OrderMapper orderMapper;

    public Page<OrderDTO> getAllOrders(int page, int size) {
        Page<Order> orders = orderRepository.findAll(PageRequest.of(page, size));
        return orders.map(orderMapper::toDTO);
    }

    public Long getBorrowedOrdersCount() {
        return orderRepository.countByStatus(OrderStatus.BORROWED);
    }

    @Transactional
    public OrderDTO createOrder(String sif, String bookIsbn, String dueDate, String notes) {
        Student student = studentRepository.findBySif(sif)
                .orElseThrow(() -> new StudentNotFoundException(ErrorMessages.STUDENT_NOT_FOUND));

        Book book = bookRepository.findBookByIsbn(bookIsbn)
                .orElseThrow(() -> new BookNotFoundException(ErrorMessages.BOOK_NOT_FOUND));

        if (book.getStock() <= 0) {
            throw new RuntimeException(ErrorMessages.BOOK_OUT_OF_STOCK);
        }

        book.setStock(book.getStock() - 1);
        bookRepository.save(book);

        Order order = new Order();
        order.setStudent(student);
        order.setBook(book);
        order.setStatus(OrderStatus.BORROWED);
        order.setOrderTimestamp(LocalDateTime.now());
        order.setDueDate(LocalDateTime.parse(dueDate + "T23:59:59"));
        order.setNotes(notes);

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDTO(savedOrder);
    }


    @Transactional
    public OrderDTO returnOrder(String studentSif, String bookIsbn) {
        studentRepository.findBySif(studentSif)
                .orElseThrow(() -> new StudentNotFoundException(ErrorMessages.STUDENT_NOT_FOUND));

        Book book = bookRepository.findBookByIsbn(bookIsbn)
                .orElseThrow(() -> new BookNotFoundException(ErrorMessages.BOOK_NOT_FOUND));

        Order order = orderRepository.findByStudentSif(studentSif).stream()
                .filter(o -> o.getBook().getIsbn().equals(bookIsbn) && !o.isReturned())
                .findFirst()
                .orElseThrow(() -> new OrderNotFoundException(ErrorMessages.ORDER_NOT_FOUND_ACTIVE));

        book.setStock(book.getStock() + 1);
        bookRepository.save(book);

        order.setReturnTimestamp(LocalDateTime.now());
        order.setStatus(OrderStatus.RETURNED);
        order.setReturned(true);

        if (order.getReturnTimestamp().isAfter(order.getDueDate())) {
            order.setStatus(OrderStatus.OVERDUE);
            long daysOverdue = order.getReturnTimestamp().getDayOfYear() - order.getDueDate().getDayOfYear();
            order.setFineAmount(BigDecimal.valueOf(daysOverdue * 5));
        }

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDTO(savedOrder);
    }

    public OrderDTO getOrderById(Long orderId) {
        return orderMapper.toDTO(orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(ErrorMessages.ORDER_NOT_FOUND)));
    }
}
