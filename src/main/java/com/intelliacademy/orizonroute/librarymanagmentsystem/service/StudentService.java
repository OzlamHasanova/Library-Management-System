package com.intelliacademy.orizonroute.librarymanagmentsystem.service;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.StudentDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StudentService {
    Page<StudentDTO> getAllStudents(int page, int size, String sortBy, String direction);

    StudentDTO findStudentById(Long id);

    List<StudentDTO> getStudentList();

    void createStudent(StudentDTO studentDTO);

    void updateStudent(Long id, StudentDTO studentDTO);

    void deleteStudent(Long id);
}
