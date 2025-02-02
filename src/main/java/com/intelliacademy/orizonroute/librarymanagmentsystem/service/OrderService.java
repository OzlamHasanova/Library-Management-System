package com.intelliacademy.orizonroute.librarymanagmentsystem.service;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.OrderDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.mapper.OrderMapper;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Book;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Order;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Student;
import com.intelliacademy.orizonroute.librarymanagmentsystem.repository.BookRepository;
import com.intelliacademy.orizonroute.librarymanagmentsystem.repository.OrderRepository;
import com.intelliacademy.orizonroute.librarymanagmentsystem.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;
    private OrderMapper orderMapper;

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO createOrder(String sif, Long bookId) {
        Student student = studentRepository.findBySif(sif)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getStock() <= 0) {
            throw new RuntimeException("Book is out of stock");
        }

        book.setStock(book.getStock() - 1);
        bookRepository.save(book);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setStudentSif(sif);
        orderDTO.setBookId(bookId);
        orderDTO.setOrderTimestamp(LocalDateTime.now());

        Order order = orderMapper.toEntity(orderDTO, student, book);
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toDTO(savedOrder);
    }

    public OrderDTO returnOrder(String sif, Long bookId) {
        Student student = studentRepository.findBySif(sif)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        book.setStock(book.getStock() + 1);
        bookRepository.save(book);

        Order order = orderRepository.findByStudentSif(sif).stream()
                .filter(o -> o.getBook().getId().equals(bookId) && o.getReturnTimestamp() == null)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setReturnTimestamp(LocalDateTime.now());

        return orderMapper.toDTO(orderRepository.save(order));
    }
}

