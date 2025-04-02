package com.intelliacademy.orizonroute.librarymanagmentsystem.mapper;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.BookDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Author;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Book;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Category;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.enums.BookAvailability;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.impl.AuthorServiceImpl;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    private final AuthorServiceImpl authorService;

    public BookMapper(AuthorServiceImpl authorService) {
        this.authorService = authorService;
    }

    public BookDTO toBookDTO(Book book) {
        if (book == null) {
            return null;
        }

        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setIsbn(book.getIsbn());
        bookDTO.setAvailability(book.getAvailability().name());
        bookDTO.setPublicationYear(book.getPublicationYear());
        bookDTO.setDescription(book.getDescription());
        bookDTO.setImage(book.getImage());
        bookDTO.setStock(book.getStock());

        if (book.getAuthors() != null) {
            bookDTO.setAuthorIds(book.getAuthors().stream()
                    .map(Author::getId)
                    .collect(Collectors.toSet()));

            bookDTO.setAuthorNames(book.getAuthors().stream()
                    .map(Author::getFullName)
                    .collect(Collectors.toSet()));
        }

        if (book.getCategory() != null) {
            bookDTO.setCategoryId(book.getCategory().getId());
            bookDTO.setCategoryName(book.getCategory().getName());
        }

        return bookDTO;
    }


    public Book toBook(BookDTO bookDTO, Category category) {
        if (bookDTO == null) {
            return null;
        }

        Book book = new Book();
        book.setId(bookDTO.getId());
        book.setTitle(bookDTO.getTitle());
        book.setIsbn(bookDTO.getIsbn());
        book.setAvailability(BookAvailability.AVAILABLE);
        book.setPublicationYear(bookDTO.getPublicationYear());
        book.setDescription(bookDTO.getDescription());
        book.setImage(bookDTO.getImage());
        book.setStock(bookDTO.getStock());
        book.setCategory(category);

        if (bookDTO.getAuthorIds() != null) {
            Set<Author> authors = bookDTO.getAuthorIds().stream()
                    .map(authorService::getAuthorEntityById)
                    .collect(Collectors.toSet());
            book.setAuthors(authors);
        }

        return book;
    }
}
