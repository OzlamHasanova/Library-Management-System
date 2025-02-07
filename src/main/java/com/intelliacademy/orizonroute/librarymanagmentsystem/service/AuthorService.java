package com.intelliacademy.orizonroute.librarymanagmentsystem.service;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.AuthorDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.mapper.AuthorMapper;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Author;
import com.intelliacademy.orizonroute.librarymanagmentsystem.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public List<AuthorDTO> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        return authorMapper.toAuthorDTOList(authors);
    }


    public Optional<AuthorDTO> getAuthorById(Long id) {
        Optional<Author> author = authorRepository.findById(id);
        return author.map(authorMapper::toAuthorDTO);
    }

    public Author getAuthorEntityById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with ID: " + id));
    }

    public long getAuthorCount() {
        return authorRepository.count();
    }

    public AuthorDTO saveAuthor(AuthorDTO authorDTO) {
        Author author = authorMapper.toAuthor(authorDTO);
        Author savedAuthor = authorRepository.save(author);
        return authorMapper.toAuthorDTO(savedAuthor);
    }

    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }
}
