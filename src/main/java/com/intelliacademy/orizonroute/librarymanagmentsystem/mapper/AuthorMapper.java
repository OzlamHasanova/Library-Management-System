package com.intelliacademy.orizonroute.librarymanagmentsystem.mapper;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.AuthorDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Author;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorDTO toAuthorDTO(Author author);

    Author toAuthor(AuthorDTO authorDTO);

    List<AuthorDTO> toAuthorDTOList(List<Author> authors);
}
