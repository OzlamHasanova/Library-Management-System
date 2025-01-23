package com.intelliacademy.orizonroute.librarymanagmentsystem.service;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.StudentDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.mapper.StudentMapper;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Student;
import com.intelliacademy.orizonroute.librarymanagmentsystem.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public StudentDTO createStudent(StudentDTO studentDTO) {
        Student student = studentMapper.toEntity(studentDTO);
        Student savedStudent = studentRepository.save(student);
        return studentMapper.toDTO(savedStudent);
    }

    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        student.setName(studentDTO.getName());
        student.setSurname(studentDTO.getSurname());
        student.setSif(studentDTO.getSif());
        Student updatedStudent = studentRepository.save(student);
        return studentMapper.toDTO(updatedStudent);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public StudentDTO findStudentBySif(String sif) {
        return studentRepository.findBySif(sif).map(studentMapper::toDTO).get();//todo
    }
}

