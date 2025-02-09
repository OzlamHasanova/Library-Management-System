package com.intelliacademy.orizonroute.librarymanagmentsystem;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.CategoryDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.exception.CategoryNotFoundException;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

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

        categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Science");

        book = new Book();
        book.setId(1L);
        book.setTitle("Physics 101");
        book.setCategory(category);
    }

    @Test
    void givenCategories_whenGetAllCategories_thenReturnCategoryDTOList() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category));
        when(categoryMapper.toCategoryDTO(category)).thenReturn(categoryDTO);

        List<CategoryDTO> result = categoryService.getAllCategories();

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getName()).isEqualTo("Science");

        verify(categoryRepository, times(1)).findAll();
        verify(categoryMapper, times(1)).toCategoryDTO(category);
    }

    @Test
    void givenCategoryDTO_whenCreateCategory_thenReturnSavedCategoryDTO() {
        when(categoryMapper.toCategory(categoryDTO)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toCategoryDTO(category)).thenReturn(categoryDTO);

        CategoryDTO savedCategory = categoryService.createCategory(categoryDTO);

        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getName()).isEqualTo("Science");

        verify(categoryRepository, times(1)).save(category);
        verify(categoryMapper, times(1)).toCategoryDTO(category);
    }

    @Test
    void givenValidId_whenUpdateCategory_thenReturnUpdatedCategoryDTO() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toCategoryDTO(category)).thenReturn(categoryDTO);

        CategoryDTO updatedCategory = categoryService.updateCategory(1L, categoryDTO);

        assertThat(updatedCategory).isNotNull();
        assertThat(updatedCategory.getName()).isEqualTo("Science");

        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(category);
        verify(categoryMapper, times(1)).toCategoryDTO(category);
    }

    @Test
    void givenInvalidId_whenUpdateCategory_thenThrowException() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.updateCategory(2L, categoryDTO))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("Category not found with id: 2");

        verify(categoryRepository, times(1)).findById(2L);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void givenValidId_whenGetCategoryById_thenReturnCategoryDTO() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toCategoryDTO(category)).thenReturn(categoryDTO);

        CategoryDTO result = categoryService.getCategoryById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Science");

        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryMapper, times(1)).toCategoryDTO(category);
    }

    @Test
    void givenInvalidId_whenGetCategoryById_thenThrowException() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getCategoryById(2L))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("Category not found with id: 2");

        verify(categoryRepository, times(1)).findById(2L);
        verify(categoryMapper, never()).toCategoryDTO(any());
    }

    @Test
    void givenValidId_whenGetCategoryEntityById_thenReturnCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category result = categoryService.getCategoryEntityById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Science");

        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void givenInvalidId_whenGetCategoryEntityById_thenThrowException() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getCategoryEntityById(2L))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("Category not found with id: 2");

        verify(categoryRepository, times(1)).findById(2L);
    }

    @Test
    void givenValidId_whenDeleteCategory_thenDeleteSuccessfully() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    void givenInvalidId_whenDeleteCategory_thenThrowException() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.deleteCategory(2L))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("Category not found with id: 2");

        verify(categoryRepository, times(1)).findById(2L);
        verify(categoryRepository, never()).delete(any());
    }

//    @Test
//    void givenValidId_whenGetCategoryBooks_thenReturnBookSet() {
//        category.setBooks(Set.of(book));
//
//        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
//
//        Set<Book> books = categoryService.getCategoryBooks(1L);
//
//        assertThat(books).isNotEmpty();
//        assertThat(books.size()).isEqualTo(1);
//        assertThat(books.iterator().next().getTitle()).isEqualTo("Physics 101");
//
//        verify(categoryRepository, times(1)).findById(1L);
//    }

    @Test
    void givenInvalidId_whenGetCategoryBooks_thenThrowException() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getCategoryBooks(2L))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("Category not found with id: 2");

        verify(categoryRepository, times(1)).findById(2L);
    }
}
