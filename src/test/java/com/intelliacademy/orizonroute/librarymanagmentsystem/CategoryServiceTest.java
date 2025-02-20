package com.intelliacademy.orizonroute.librarymanagmentsystem;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.BookDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.CategoryDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.exception.CategoryNotFoundException;
import com.intelliacademy.orizonroute.librarymanagmentsystem.mapper.BookMapper;
import com.intelliacademy.orizonroute.librarymanagmentsystem.mapper.CategoryMapper;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Book;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Category;
import com.intelliacademy.orizonroute.librarymanagmentsystem.repository.CategoryRepository;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;
    private CategoryDTO categoryDTO;
    private Book book;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Science");
        category.setBooks(new HashSet<>());

        categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Science");

        book = new Book();
        book.setId(1L);
        book.setTitle("Physics 101");
        book.setCategory(category);
    }

    @Test
    void getAllCategories_shouldReturnCategoryDTOList() {
        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(category));
        when(categoryMapper.toCategoryDTO(category)).thenReturn(categoryDTO);

        List<CategoryDTO> result = categoryService.getAllCategories();

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getName()).isEqualTo("Science");

        verify(categoryRepository).findAll();
        verify(categoryMapper).toCategoryDTO(category);
    }

    @Test
    void getCategoryCount_shouldReturnCorrectCount() {
        when(categoryRepository.count()).thenReturn(5L);

        Long count = categoryService.getCategoryCount();

        assertThat(count).isEqualTo(5);
        verify(categoryRepository).count();
    }

    @Test
    void getCategories_shouldReturnPagedCategories() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Category> categoryPage = new PageImpl<>(Collections.singletonList(category));
        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toCategoryDTO(category)).thenReturn(categoryDTO);

        Page<CategoryDTO> result = categoryService.getCategories(pageable);

        assertThat(result).isNotEmpty();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Science");

        verify(categoryRepository).findAll(pageable);
        verify(categoryMapper).toCategoryDTO(category);
    }

    @Test
    void createCategory_shouldReturnSavedCategoryDTO() {
        when(categoryMapper.toCategory(categoryDTO)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toCategoryDTO(category)).thenReturn(categoryDTO);

        CategoryDTO savedCategory = categoryService.createCategory(categoryDTO);

        assertThat(savedCategory.getName()).isEqualTo("Science");

        verify(categoryRepository).save(category);
        verify(categoryMapper).toCategoryDTO(category);
    }

    @Test
    void updateCategory_givenValidId_shouldReturnUpdatedCategoryDTO() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toCategoryDTO(category)).thenReturn(categoryDTO);

        CategoryDTO updatedCategory = categoryService.updateCategory(1L, categoryDTO);

        assertThat(updatedCategory.getName()).isEqualTo("Science");

        verify(categoryRepository).findById(1L);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toCategoryDTO(category);
    }

    @Test
    void updateCategory_givenInvalidId_shouldThrowException() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.updateCategory(2L, categoryDTO))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("Category not found");

        verify(categoryRepository).findById(2L);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void getCategoryById_givenValidId_shouldReturnCategoryDTO() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toCategoryDTO(category)).thenReturn(categoryDTO);

        CategoryDTO result = categoryService.getCategoryById(1L);

        assertThat(result.getName()).isEqualTo("Science");

        verify(categoryRepository).findById(1L);
        verify(categoryMapper).toCategoryDTO(category);
    }

    @Test
    void getCategoryById_givenInvalidId_shouldThrowException() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getCategoryById(2L))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("Category not found");

        verify(categoryRepository).findById(2L);
        verify(categoryMapper, never()).toCategoryDTO(any());
    }

    @Test
    void getCategoryEntityById_givenValidId_shouldReturnCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category result = categoryService.getCategoryEntityById(1L);

        assertThat(result.getName()).isEqualTo("Science");

        verify(categoryRepository).findById(1L);
    }

    @Test
    void getCategoryEntityById_givenInvalidId_shouldThrowException() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getCategoryEntityById(2L))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("Category not found");

        verify(categoryRepository).findById(2L);
    }

    @Test
    void deleteCategory_givenValidId_shouldDeleteSuccessfully() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(1L);

        verify(categoryRepository).delete(category);
    }

    @Test
    void deleteCategory_givenInvalidId_shouldThrowException() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.deleteCategory(2L))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("Category not found");

        verify(categoryRepository).findById(2L);
        verify(categoryRepository, never()).delete(any());
    }

    @Test
    void getCategoryBooks_givenValidId_shouldReturnBookSet() {
        category.setBooks(Set.of(book));
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setTitle("Physics 101");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(bookMapper.toBookDTO(book)).thenReturn(bookDTO);

        Set<BookDTO> books = categoryService.getCategoryBooks(1L);

        assertThat(books).isNotEmpty();
        assertThat(books.size()).isEqualTo(1);

        verify(categoryRepository).findById(1L);
        verify(bookMapper).toBookDTO(book);
    }

    @Test
    void getCategoryBooks_givenInvalidId_shouldThrowException() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getCategoryBooks(2L))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("Category not found");

        verify(categoryRepository).findById(2L);
    }
}
