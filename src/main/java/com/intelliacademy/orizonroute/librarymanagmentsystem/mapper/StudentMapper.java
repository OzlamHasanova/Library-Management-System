package com.intelliacademy.orizonroute.librarymanagmentsystem.mapper;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.StudentDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public StudentDTO toDTO(Student student) {
        if (student == null) {
            return null;
        }
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(student.getId());
        studentDTO.setFullName(student.getName() + " " + student.getSurname());
        studentDTO.setSif(student.getSif());
        return studentDTO;
    }

    public Student toEntity(StudentDTO studentDTO) {
        if (studentDTO == null) {
            return null;
        }
        Student student = new Student();
        student.setId(studentDTO.getId());
        student.setName(studentDTO.getFullName().split(" ")[0]);
        student.setSurname(studentDTO.getFullName().split(" ")[1]);
        student.setSif(studentDTO.getSif());
        return student;
    }
}
