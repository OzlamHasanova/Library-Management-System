package com.intelliacademy.orizonroute.librarymanagmentsystem;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.AuthorDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.mapper.AuthorMapper;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Author;
import com.intelliacademy.orizonroute.librarymanagmentsystem.repository.AuthorRepository;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.AuthorService;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private AuthorService authorService;

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
    void givenAuthorsExist_whenGetAllAuthors_thenReturnAuthorDTOList() {
        List<Author> authors = Arrays.asList(author);
        List<AuthorDTO> authorDTOs = Arrays.asList(authorDTO);

        when(authorRepository.findAll()).thenReturn(authors);
        when(authorMapper.toAuthorDTOList(authors)).thenReturn(authorDTOs);

        List<AuthorDTO> result = authorService.getAllAuthors();

        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.get(0).getFullName()).isEqualTo("John Doe");

        verify(authorRepository, times(1)).findAll();
        verify(authorMapper, times(1)).toAuthorDTOList(authors);
    }

    @Test
    void givenValidId_whenGetAuthorById_thenReturnAuthorDTO() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorMapper.toAuthorDTO(author)).thenReturn(authorDTO);

        Optional<AuthorDTO> result = authorService.getAuthorById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getFullName()).isEqualTo("John Doe");

        verify(authorRepository, times(1)).findById(1L);
        verify(authorMapper, times(1)).toAuthorDTO(author);
    }

    @Test
    void givenInvalidId_whenGetAuthorById_thenReturnEmptyOptional() {
        when(authorRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<AuthorDTO> result = authorService.getAuthorById(2L);

        assertThat(result).isEmpty();

        verify(authorRepository, times(1)).findById(2L);
        verify(authorMapper, times(0)).toAuthorDTO(any());
    }

    @Test
    void givenValidId_whenGetAuthorEntityById_thenReturnAuthor() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        Author result = authorService.getAuthorEntityById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getFullName()).isEqualTo("John Doe");

        verify(authorRepository, times(1)).findById(1L);
    }

    @Test
    void givenInvalidId_whenGetAuthorEntityById_thenThrowException() {
        when(authorRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> authorService.getAuthorEntityById(2L));

        assertThat(exception.getMessage()).isEqualTo("Author not found with ID: 2");

        verify(authorRepository, times(1)).findById(2L);
    }

    @Test
    void givenAuthorsExist_whenGetAuthorCount_thenReturnCorrectCount() {
        when(authorRepository.count()).thenReturn(10L);

        long result = authorService.getAuthorCount();

        assertThat(result).isEqualTo(10L);

        verify(authorRepository, times(1)).count();
    }

    @Test
    void givenValidAuthorDTO_whenSaveAuthor_thenReturnSavedAuthorDTO() {
        when(authorMapper.toAuthor(authorDTO)).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(author);
        when(authorMapper.toAuthorDTO(author)).thenReturn(authorDTO);

        AuthorDTO result = authorService.saveAuthor(authorDTO);

        assertThat(result).isNotNull();
        assertThat(result.getFullName()).isEqualTo("John Doe");

        verify(authorMapper, times(1)).toAuthor(authorDTO);
        verify(authorRepository, times(1)).save(author);
        verify(authorMapper, times(1)).toAuthorDTO(author);
    }

    @Test
    void givenValidId_whenDeleteAuthor_thenRepositoryDeleteCalled() {
        doNothing().when(authorRepository).deleteById(1L);

        authorService.deleteAuthor(1L);

        verify(authorRepository, times(1)).deleteById(1L);
    }
}
