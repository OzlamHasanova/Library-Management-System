package com.intelliacademy.orizonroute.librarymanagmentsystem.mapper;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.AuthorDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Author;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthorMapper {

    public AuthorDTO toAuthorDTO(Author author) {
        if (author == null) {
            return null;
        }
        return AuthorDTO.builder()
                .id(author.getId())
                .fullName(author.getFullName())
                .birthDate(author.getBirthDate())
                .deathDate(author.getDeathDate())
                .biography(author.getBiography())
                .build();
    }

    public Author toAuthor(AuthorDTO authorDTO) {
        if (authorDTO == null) {
            return null;
        }
        Author author = new Author();
        author.setId(authorDTO.getId());
        author.setFullName(authorDTO.getFullName());
        author.setBirthDate(authorDTO.getBirthDate());
        author.setDeathDate(authorDTO.getDeathDate());
        author.setBiography(authorDTO.getBiography());
        return author;
    }

    public List<AuthorDTO> toAuthorDTOList(List<Author> authors) {
        if (authors == null || authors.isEmpty()) {
            return List.of(); // Boş siyahı qaytarır
        }
        return authors.stream()
                .map(this::toAuthorDTO)
                .collect(Collectors.toList());
    }

    public List<Author> toAuthorList(List<AuthorDTO> authorDTOs) {
        if (authorDTOs == null || authorDTOs.isEmpty()) {
            return List.of();
        }
        return authorDTOs.stream()
                .map(this::toAuthor)
                .collect(Collectors.toList());
    }
}
