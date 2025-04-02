package com.intelliacademy.orizonroute.librarymanagmentsystem.service;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.AuthorDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    List<AuthorDTO> getAllAuthors();

    Page<AuthorDTO> getAuthors(Pageable pageable);

    Optional<AuthorDTO> getAuthorById(Long id);

    Author getAuthorEntityById(Long id);

    Long getAuthorCount();

    AuthorDTO saveAuthor(AuthorDTO authorDTO);

    void deleteAuthor(Long id);
}

