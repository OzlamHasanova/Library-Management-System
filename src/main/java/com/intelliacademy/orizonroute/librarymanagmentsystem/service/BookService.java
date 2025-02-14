package com.intelliacademy.orizonroute.librarymanagmentsystem.service;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.BookDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.exception.BookNotFoundException;
import com.intelliacademy.orizonroute.librarymanagmentsystem.exception.CategoryNotFoundException;
import com.intelliacademy.orizonroute.librarymanagmentsystem.mapper.BookMapper;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Book;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Category;
import com.intelliacademy.orizonroute.librarymanagmentsystem.repository.BookRepository;
import com.intelliacademy.orizonroute.librarymanagmentsystem.common.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    public Page<BookDTO> getAllBooks(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return bookRepository.findAll(pageable).map(bookMapper::toBookDTO);
    }

    public List<BookDTO> getAllBookList() {
        List<Book> bookList = bookRepository.findByIsDeletedFalse();
        return bookList.stream().map(bookMapper::toBookDTO).toList();
    }

    @Transactional
    public BookDTO createBook(BookDTO bookDTO) {
        Category category = categoryService.getCategoryEntityById(bookDTO.getCategoryId());
        if (category == null) {
            throw new CategoryNotFoundException(ErrorMessages.CATEGORY_NOT_FOUND);
        }
        Book book = bookMapper.toBook(bookDTO, category);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toBookDTO(savedBook);
    }

    public List<BookDTO> getBooksByCategory(Long categoryId) {
        List<Book> books = bookRepository.findAllByCategory_Id(categoryId);
        if (books.isEmpty()) {
            throw new BookNotFoundException(ErrorMessages.BOOKS_NOT_FOUND_FOR_CATEGORY);
        }
        return books.stream()
                .map(bookMapper::toBookDTO)
                .toList();
    }

    @Transactional
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book book = bookRepository.findById(id)
                .filter(b -> !b.isDeleted())
                .orElseThrow(() -> new BookNotFoundException(ErrorMessages.BOOK_NOT_FOUND));
        bookRepository.save(book);
        return bookMapper.toBookDTO(book);
    }

    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .filter(b -> !b.isDeleted())
                .orElseThrow(() -> new BookNotFoundException(ErrorMessages.BOOK_NOT_FOUND_WITH_ID + id));
        return bookMapper.toBookDTO(book);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(ErrorMessages.BOOK_NOT_FOUND_WITH_ID + id));
        book.setDeleted(true);
        bookRepository.save(book);
    }
}
