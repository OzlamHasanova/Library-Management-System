package com.intelliacademy.orizonroute.librarymanagmentsystem.service;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.BookDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.exception.BookNotFoundException;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Book;
import com.intelliacademy.orizonroute.librarymanagmentsystem.repository.BookRepository;
import com.intelliacademy.orizonroute.librarymanagmentsystem.mapper.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public BookDTO createBook(BookDTO bookDTO) {
        Book book = bookMapper.toBook(bookDTO);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toBookDTO(savedBook);
    }

    public List<BookDTO> getBooksByCategory(Long categoryId) {
        List<Book> books = bookRepository.findByCategories_Id(categoryId);
        return books.stream()
                .map(bookMapper::toBookDTO)
                .toList();
    }

    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));
        bookMapper.updateBookFromDTO(bookDTO, book);
        Book updatedBook = bookRepository.save(book);
        return bookMapper.toBookDTO(updatedBook);
    }

    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));
        return bookMapper.toBookDTO(book);
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));
        bookRepository.delete(book);
    }
}
