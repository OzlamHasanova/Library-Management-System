package com.intelliacademy.orizonroute.librarymanagmentsystem;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.BookDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.exception.BookNotFoundException;
import com.intelliacademy.orizonroute.librarymanagmentsystem.exception.CategoryNotFoundException;
import com.intelliacademy.orizonroute.librarymanagmentsystem.mapper.BookMapper;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Book;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Category;
import com.intelliacademy.orizonroute.librarymanagmentsystem.repository.BookRepository;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.BookService;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        MockitoAnnotations.openMocks(this);

        category = new Category();
        category.setId(1L);
        category.setName("Fiction");

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
    void givenValidPageAndSize_whenGetAllBooks_thenReturnBookDTOPage() {
        // Given
        int page = 0;
        int size = 10;
        String sortBy = "title";
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Book> bookPage = mock(Page.class);
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        BookDTO bookDTO1 = new BookDTO();
        BookDTO bookDTO2 = new BookDTO();
        when(bookPage.map(any())).thenAnswer(invocation -> {
            List<BookDTO> bookDTOList = List.of(bookDTO1, bookDTO2);
            Page<BookDTO> bookDTOPage = mock(Page.class);
            when(bookDTOPage.getContent()).thenReturn(bookDTOList);
            return bookDTOPage;
        });

        Page<BookDTO> result = bookService.getAllBooks(page, size, sortBy);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(bookRepository).findAll(pageable);
        verify(bookPage).map(any());
    }

    @Test
    void givenBooksExist_whenGetAllBookList_thenReturnListOfBookDTOs() {
        when(bookRepository.findByIsDeletedFalse()).thenReturn(Arrays.asList(book));
        when(bookMapper.toBookDTO(book)).thenReturn(bookDTO);

        List<BookDTO> result = bookService.getAllBookList();

        assertEquals(1, result.size());
        verify(bookRepository).findByIsDeletedFalse();
    }

    @Test
    void givenBooksExist_whenGetAvailableBookCount_thenReturnAvailableBookCount() {
        when(bookRepository.countAvailableBooks()).thenReturn(5L);
        Long result = bookService.getAvailableBookCount();

        assertEquals(5L, result);
        verify(bookRepository).countAvailableBooks();
    }

    @Test
    void givenValidCategory_whenGetBooksByCategory_thenReturnListOfBookDTOs() {
        Long categoryId = 1L;
        when(bookRepository.findAllByCategory_Id(categoryId)).thenReturn(Arrays.asList(book));
        when(bookMapper.toBookDTO(book)).thenReturn(bookDTO);

        List<BookDTO> result = bookService.getBooksByCategory(categoryId);

        assertEquals(1, result.size());
        verify(bookRepository).findAllByCategory_Id(categoryId);
    }

    @Test
    void givenEmptyCategory_whenGetBooksByCategory_thenThrowBookNotFoundException() {
        Long categoryId = 1L;
        when(bookRepository.findAllByCategory_Id(categoryId)).thenReturn(Arrays.asList());

        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> {
            bookService.getBooksByCategory(categoryId);
        });
        assertEquals("No books found for the given category.", exception.getMessage());
    }

    @Test
    void givenValidBookId_whenGetBookById_thenReturnBookDTO() {
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toBookDTO(book)).thenReturn(bookDTO);

        BookDTO result = bookService.getBookById(bookId);

        assertEquals(bookDTO, result);
        verify(bookRepository).findById(bookId);
    }

    @Test
    void givenInvalidBookId_whenGetBookById_thenThrowBookNotFoundException() {
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> {
            bookService.getBookById(bookId);
        });
        assertEquals("Book not found with ID: 1", exception.getMessage());
    }

    @Test
    void givenValidBookDTO_whenCreateBook_thenReturnCreatedBookDTO() {
        when(categoryService.getCategoryEntityById(bookDTO.getCategoryId())).thenReturn(category);
        when(bookMapper.toBook(bookDTO, category)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toBookDTO(book)).thenReturn(bookDTO);

        BookDTO result = bookService.createBook(bookDTO);

        assertEquals(bookDTO, result);
        verify(bookRepository).save(book);
    }

    @Test
    void givenInvalidCategory_whenCreateBook_thenThrowCategoryNotFoundException() {
        when(categoryService.getCategoryEntityById(bookDTO.getCategoryId())).thenReturn(null);

        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> {
            bookService.createBook(bookDTO);
        });
        assertEquals("Category not found.", exception.getMessage());
    }

    @Test
    void givenValidBookIdAndDTO_whenUpdateBook_thenReturnUpdatedBookDTO() {
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toBookDTO(book)).thenReturn(bookDTO);

        BookDTO result = bookService.updateBook(bookId, bookDTO);

        assertEquals(bookDTO, result);
        verify(bookRepository).save(book);
    }

    @Test
    void givenInvalidBookId_whenUpdateBook_thenThrowBookNotFoundException() {
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> {
            bookService.updateBook(bookId, bookDTO);
        });
        assertEquals("Book not found.", exception.getMessage());
    }

    @Test
    void givenValidBookId_whenDeleteBook_thenDeleteBookSuccessfully() {
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        bookService.deleteBook(bookId);

        verify(bookRepository).save(book);
        assertTrue(book.isDeleted());
    }

    @Test
    void givenInvalidBookId_whenDeleteBook_thenThrowBookNotFoundException() {
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> {
            bookService.deleteBook(bookId);
        });
        assertEquals("Book not found with ID: 1", exception.getMessage());
    }
}
