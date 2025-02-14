package com.intelliacademy.orizonroute.librarymanagmentsystem.service;

import com.intelliacademy.orizonroute.librarymanagmentsystem.common.ErrorMessages;
import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.StudentDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.exception.StudentNotFoundException;
import com.intelliacademy.orizonroute.librarymanagmentsystem.mapper.StudentMapper;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Student;
import com.intelliacademy.orizonroute.librarymanagmentsystem.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public Page<StudentDTO> getAllStudents(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return studentRepository.findAll(pageable)
                .map(student -> new StudentMapper().toDTO(student));
    }

    public StudentDTO createStudent(StudentDTO studentDTO) {
        Student student = studentMapper.toEntity(studentDTO);
        Student savedStudent = studentRepository.save(student);
        return studentMapper.toDTO(savedStudent);
    }

    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(ErrorMessages.STUDENT_NOT_FOUND));
        student.setName(studentDTO.getName());
        student.setSurname(studentDTO.getSurname());
        student.setSif(studentDTO.getSif());
        Student updatedStudent = studentRepository.save(student);
        return studentMapper.toDTO(updatedStudent);
    }

    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(ErrorMessages.STUDENT_NOT_FOUND));
        student.setDeleted(true);
        studentRepository.save(student);
    }

    public StudentDTO findStudentById(Long id) {
        return studentRepository.findById(id).map(studentMapper::toDTO)
                .orElseThrow(() -> new StudentNotFoundException(ErrorMessages.STUDENT_NOT_FOUND));
    }

    public List<StudentDTO> getStudentList() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toDTO).toList();
    }
}
