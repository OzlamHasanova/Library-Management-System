package com.intelliacademy.orizonroute.librarymanagmentsystem;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.AuthorDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.exception.AuthorNotFoundException;
import com.intelliacademy.orizonroute.librarymanagmentsystem.mapper.AuthorMapper;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Author;
import com.intelliacademy.orizonroute.librarymanagmentsystem.repository.AuthorRepository;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.impl.AuthorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private Author author;
    private AuthorDTO authorDTO;

    @BeforeEach
    void setUp() {
        author = new Author();
        author.setId(1L);
        author.setFullName("John Doe");

        authorDTO = new AuthorDTO();
        authorDTO.setId(1L);
        authorDTO.setFullName("John Doe");
    }

    @Test
    void getAllAuthors_shouldReturnAuthorDTOList() {
        List<Author> authors = List.of(author);
        List<AuthorDTO> authorDTOs = List.of(authorDTO);

        when(authorRepository.findAll()).thenReturn(authors);
        when(authorMapper.toAuthorDTOList(authors)).thenReturn(authorDTOs);

        List<AuthorDTO> result = authorService.getAllAuthors();

        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.get(0).getFullName()).isEqualTo("John Doe");

        verify(authorRepository).findAll();
        verify(authorMapper).toAuthorDTOList(authors);
    }

    @Test
    void getAllAuthors_whenNoAuthorsExist_shouldThrowException() {
        when(authorRepository.findAll()).thenReturn(List.of());

        assertThatThrownBy(() -> authorService.getAllAuthors())
                .isInstanceOf(AuthorNotFoundException.class);

        verify(authorRepository).findAll();
    }

    @Test
    void getAuthors_shouldReturnPagedAuthorDTOs() {
        PageRequest pageable = PageRequest.of(0, 5);
        Page<Author> authorPage = new PageImpl<>(List.of(author));

        when(authorRepository.findAll(pageable)).thenReturn(authorPage);
        when(authorMapper.toAuthorDTO(author)).thenReturn(authorDTO);

        Page<AuthorDTO> result = authorService.getAuthors(pageable);

        assertThat(result).isNotEmpty();
        verify(authorRepository).findAll(pageable);
    }

    @Test
    void getAuthors_whenNoAuthorsExist_shouldThrowException() {
        PageRequest pageable = PageRequest.of(0, 5);
        when(authorRepository.findAll(pageable)).thenReturn(Page.empty());

        assertThatThrownBy(() -> authorService.getAuthors(pageable))
                .isInstanceOf(AuthorNotFoundException.class);

        verify(authorRepository).findAll(pageable);
    }

    @Test
    void getAuthorById_shouldReturnAuthorDTO() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorMapper.toAuthorDTO(author)).thenReturn(authorDTO);

        Optional<AuthorDTO> result = authorService.getAuthorById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getFullName()).isEqualTo("John Doe");

        verify(authorRepository).findById(1L);
        verify(authorMapper).toAuthorDTO(author);
    }

    @Test
    void getAuthorById_whenAuthorDoesNotExist_shouldThrowException() {
        when(authorRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.getAuthorById(2L))
                .isInstanceOf(AuthorNotFoundException.class);

        verify(authorRepository).findById(2L);
    }

    @Test
    void getAuthorEntityById_shouldReturnAuthor() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        Author result = authorService.getAuthorEntityById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getFullName()).isEqualTo("John Doe");

        verify(authorRepository).findById(1L);
    }

    @Test
    void getAuthorEntityById_whenAuthorDoesNotExist_shouldThrowException() {
        when(authorRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.getAuthorEntityById(2L))
                .isInstanceOf(AuthorNotFoundException.class);

        verify(authorRepository).findById(2L);
    }

    @Test
    void getAuthorCount_shouldReturnCorrectCount() {
        when(authorRepository.count()).thenReturn(10L);

        long result = authorService.getAuthorCount();

        assertThat(result).isEqualTo(10L);

        verify(authorRepository).count();
    }

    @Test
    void saveAuthor_shouldReturnSavedAuthorDTO() {
        when(authorMapper.toAuthor(authorDTO)).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(author);
        when(authorMapper.toAuthorDTO(author)).thenReturn(authorDTO);

        AuthorDTO result = authorService.saveAuthor(authorDTO);

        assertThat(result).isNotNull();
        assertThat(result.getFullName()).isEqualTo("John Doe");

        verify(authorMapper).toAuthor(authorDTO);
        verify(authorRepository).save(author);
        verify(authorMapper).toAuthorDTO(author);
    }

    @Test
    void deleteAuthor_shouldDeleteAuthor() {
        when(authorRepository.existsById(1L)).thenReturn(true);
        doNothing().when(authorRepository).deleteById(1L);

        authorService.deleteAuthor(1L);

        verify(authorRepository).deleteById(1L);
    }

    @Test
    void deleteAuthor_whenAuthorDoesNotExist_shouldThrowException() {
        when(authorRepository.existsById(2L)).thenReturn(false);

        assertThatThrownBy(() -> authorService.deleteAuthor(2L))
                .isInstanceOf(AuthorNotFoundException.class);

        verify(authorRepository).existsById(2L);
    }
}
