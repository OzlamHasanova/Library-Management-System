package com.intelliacademy.orizonroute.librarymanagmentsystem.service.impl;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.AuthorDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.exception.AuthorNotFoundException;
import com.intelliacademy.orizonroute.librarymanagmentsystem.mapper.AuthorMapper;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Author;
import com.intelliacademy.orizonroute.librarymanagmentsystem.repository.AuthorRepository;
import com.intelliacademy.orizonroute.librarymanagmentsystem.common.ErrorMessages;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public List<AuthorDTO> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        if (authors.isEmpty()) {
            throw new AuthorNotFoundException(ErrorMessages.AUTHORS_NOT_FOUND);
        }
        return authorMapper.toAuthorDTOList(authors);
    }

    public Page<AuthorDTO> getAuthors(Pageable pageable) {
        Page<Author> authorPage = authorRepository.findAll(pageable);
        if (authorPage.isEmpty()) {
            throw new AuthorNotFoundException(ErrorMessages.AUTHORS_NOT_FOUND);
        }
        return authorPage.map(authorMapper::toAuthorDTO);
    }

    public Optional<AuthorDTO> getAuthorById(Long id) {
        Optional<Author> author = authorRepository.findById(id);
        if (author.isEmpty()) {
            throw new AuthorNotFoundException(ErrorMessages.AUTHOR_NOT_FOUND_WITH_ID + id);
        }
        return author.map(authorMapper::toAuthorDTO);
    }

    public Author getAuthorEntityById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(ErrorMessages.AUTHOR_NOT_FOUND_WITH_ID + id));
    }

    public Long getAuthorCount() {
        return authorRepository.count();
    }

    public AuthorDTO saveAuthor(AuthorDTO authorDTO) {
        Author author = authorMapper.toAuthor(authorDTO);
        Author savedAuthor = authorRepository.save(author);
        return authorMapper.toAuthorDTO(savedAuthor);
    }

    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new AuthorNotFoundException(ErrorMessages.AUTHOR_NOT_FOUND_WITH_ID + id);
        }
        authorRepository.deleteById(id);
    }
}
