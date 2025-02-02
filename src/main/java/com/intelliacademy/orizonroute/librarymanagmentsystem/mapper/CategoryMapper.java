package com.intelliacademy.orizonroute.librarymanagmentsystem.mapper;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.CategoryDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO toCategoryDTO(Category category);

    Category toCategory(CategoryDTO categoryDTO);
}
