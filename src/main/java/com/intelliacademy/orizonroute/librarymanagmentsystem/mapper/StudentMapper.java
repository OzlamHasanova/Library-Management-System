package com.intelliacademy.orizonroute.librarymanagmentsystem.mapper;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.StudentDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public StudentDTO toDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setSurname(student.getSurname());
        dto.setSif(student.getSif());
        return dto;
    }

    public Student toEntity(StudentDTO dto) {
        Student student = new Student();
        student.setId(dto.getId());
        student.setName(dto.getName());
        student.setSurname(dto.getSurname());
        student.setSif(dto.getSif());
        return student;
    }
}

