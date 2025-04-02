package com.intelliacademy.orizonroute.librarymanagmentsystem.service;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.BookDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookService {
    Page<BookDTO> getAllBooks(int page, int size, String sortBy);

    List<BookDTO> getAllBookList();

    Long getAvailableBookCount();

    List<BookDTO> getBooksByCategory(Long categoryId);

    BookDTO getBookById(Long id);

    BookDTO createBook(BookDTO bookDTO);

    BookDTO updateBook(Long id, BookDTO bookDTO);

    void deleteBook(Long id);

}
