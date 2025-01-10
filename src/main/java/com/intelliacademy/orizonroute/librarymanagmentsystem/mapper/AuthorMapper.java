package com.intelliacademy.orizonroute.librarymanagmentsystem.mapper;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.AuthorDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Author;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthorMapper {
    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    AuthorDTO authorToAuthorDTO(Author author);

    Author authorDTOToAuthor(AuthorDTO authorDTO);
}