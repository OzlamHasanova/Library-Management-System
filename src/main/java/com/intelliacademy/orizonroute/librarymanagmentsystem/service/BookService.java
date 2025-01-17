package com.intelliacademy.orizonroute.librarymanagmentsystem.service;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.BookDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.exception.BookNotFoundException;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Book;
import com.intelliacademy.orizonroute.librarymanagmentsystem.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;


    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public BookDTO createBook(BookDTO bookDTO) {
        Book book = convertToEntity(bookDTO);
        Book savedBook = bookRepository.save(book);
        return convertToDTO(savedBook);
    }

    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));
        book.setTitle(bookDTO.getTitle());
        book.setIsbn(bookDTO.getIsbn());
        book.setPublicationYear(bookDTO.getPublicationYear());
        book.setDescription(bookDTO.getDescription());
        book.setImage(bookDTO.getImage());
        book.setStock(bookDTO.getStock());
        Book updatedBook = bookRepository.save(book);
        return convertToDTO(updatedBook);
    }

    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));
        return convertToDTO(book);
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));
        bookRepository.delete(book);
    }

    // Helper methods
    private BookDTO convertToDTO(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle(book.getTitle());
        bookDTO.setIsbn(book.getIsbn());
        bookDTO.setPublicationYear(book.getPublicationYear());
        bookDTO.setDescription(book.getDescription());
        bookDTO.setImage(book.getImage());
        bookDTO.setStock(book.getStock());
        return bookDTO;
    }

    private Book convertToEntity(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setIsbn(bookDTO.getIsbn());
        book.setPublicationYear(bookDTO.getPublicationYear());
        book.setDescription(bookDTO.getDescription());
        book.setImage(bookDTO.getImage());
        book.setStock(bookDTO.getStock());
        return book;
    }
}

