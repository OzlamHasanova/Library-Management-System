package com.intelliacademy.orizonroute.librarymanagmentsystem;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.BookDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.exception.BookNotFoundException;
import com.intelliacademy.orizonroute.librarymanagmentsystem.mapper.BookMapper;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Book;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Category;
import com.intelliacademy.orizonroute.librarymanagmentsystem.repository.BookRepository;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.BookService;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private BookDTO bookDTO;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Science");

        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setCategory(category);
        book.setDeleted(false);

        bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setTitle("Test Book");
        bookDTO.setCategoryId(1L);
    }

    @Test
    void givenBooks_whenGetAllBooks_thenReturnBookDTOList() {
        //Act
        when(bookRepository.findByIsDeletedFalse()).thenReturn(Arrays.asList(book));
        when(bookMapper.toBookDTO(book)).thenReturn(bookDTO);

        List<BookDTO> result = bookService.getAllBookList();

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Test Book");
        //Verify
        verify(bookRepository, times(1)).findByIsDeletedFalse();
        verify(bookMapper, times(1)).toBookDTO(book);
    }

    @Test
    void givenBookDTO_whenCreateBook_thenReturnSavedBookDTO() {
        // Act
        when(categoryService.getCategoryEntityById(1L)).thenReturn(category);
        when(bookMapper.toBook(bookDTO, category)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toBookDTO(book)).thenReturn(bookDTO);

        BookDTO savedBook = bookService.createBook(bookDTO);

        // Assert
        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo("Test Book");
        //Verify
        verify(categoryService, times(1)).getCategoryEntityById(1L);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toBookDTO(book);
    }

    @Test
    void givenValidCategoryId_whenGetBooksByCategory_thenReturnBookDTOList() {
        // Act
        when(bookRepository.findAllByCategory_Id(1L)).thenReturn(Arrays.asList(book));
        when(bookMapper.toBookDTO(book)).thenReturn(bookDTO);

        List<BookDTO> result = bookService.getBooksByCategory(1L);

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getTitle()).isEqualTo("Test Book");
        //Verify
        verify(bookRepository, times(1)).findAllByCategory_Id(1L);
        verify(bookMapper, times(1)).toBookDTO(book);
    }

    @Test
    void givenValidId_whenUpdateBook_thenReturnUpdatedBookDTO() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toBookDTO(book)).thenReturn(bookDTO);
        when(bookRepository.save(book)).thenReturn(book);

        BookDTO updatedBook = bookService.updateBook(1L, bookDTO);

        assertThat(updatedBook).isNotNull();
        assertThat(updatedBook.getTitle()).isEqualTo("Test Book");

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toBookDTO(book);
    }

    @Test
    void givenInvalidId_whenUpdateBook_thenThrowException() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.updateBook(2L, bookDTO))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("Book not found");

        verify(bookRepository, times(1)).findById(2L);
        verify(bookRepository, never()).save(any());
    }

    @Test
    void givenValidId_whenGetBookById_thenReturnBookDTO() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toBookDTO(book)).thenReturn(bookDTO);

        BookDTO foundBook = bookService.getBookById(1L);

        assertThat(foundBook).isNotNull();
        assertThat(foundBook.getTitle()).isEqualTo("Test Book");

        verify(bookRepository, times(1)).findById(1L);
        verify(bookMapper, times(1)).toBookDTO(book);
    }

    @Test
    void givenInvalidId_whenGetBookById_thenThrowException() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getBookById(2L))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("Book not found with ID: 2");

        verify(bookRepository, times(1)).findById(2L);
        verify(bookMapper, never()).toBookDTO(any());
    }

    @Test
    void givenValidId_whenDeleteBook_thenSetDeletedTrue() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.deleteBook(1L);

        assertThat(book.isDeleted()).isTrue();
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void givenInvalidId_whenDeleteBook_thenThrowException() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.deleteBook(2L))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("Book not found with ID: 2");

        verify(bookRepository, times(1)).findById(2L);
        verify(bookRepository, never()).save(any());
    }
}
