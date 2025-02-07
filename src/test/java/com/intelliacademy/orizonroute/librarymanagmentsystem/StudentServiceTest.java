package com.intelliacademy.orizonroute.librarymanagmentsystem;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.StudentDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.exception.StudentNotFoundException;
import com.intelliacademy.orizonroute.librarymanagmentsystem.mapper.StudentMapper;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Student;
import com.intelliacademy.orizonroute.librarymanagmentsystem.repository.StudentRepository;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private StudentDTO studentDTO;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setName("John");
        student.setSurname("Doe");
        student.setSif("S12345");

        studentDTO = new StudentDTO();
        studentDTO.setId(1L);
        studentDTO.setFullName("John Doe");
        studentDTO.setSif("S12345");
    }

    @Test
    void givenStudents_whenGetAllStudents_thenReturnStudentDTOPage() {
        Page<Student> studentPage = new PageImpl<>(List.of(student));
        when(studentRepository.findAll(PageRequest.of(0, 5))).thenReturn(studentPage);
        when(studentMapper.toDTO(student)).thenReturn(studentDTO);

        Page<StudentDTO> result = studentService.getAllStudents(0, 5);

        assertThat(result).isNotEmpty();
        assertThat(result.getTotalElements()).isEqualTo(1);

        verify(studentRepository, times(1)).findAll(PageRequest.of(0, 5));
        verify(studentMapper, times(1)).toDTO(student);
    }

    @Test
    void givenStudentDTO_whenCreateStudent_thenReturnSavedStudentDTO() {
        when(studentMapper.toEntity(studentDTO)).thenReturn(student);
        when(studentRepository.save(student)).thenReturn(student);
        when(studentMapper.toDTO(student)).thenReturn(studentDTO);

        StudentDTO result = studentService.createStudent(studentDTO);

        assertThat(result).isNotNull();
        assertThat(result.getFullName()).isEqualTo("John Doe");

        verify(studentRepository, times(1)).save(student);
        verify(studentMapper, times(1)).toDTO(student);
    }

    @Test
    void givenValidId_whenFindStudentById_thenReturnStudentDTO() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentMapper.toDTO(student)).thenReturn(studentDTO);

        StudentDTO result = studentService.findStudentById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getFullName()).isEqualTo("John Doe");

        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void givenInvalidId_whenFindStudentById_thenThrowException() {
        when(studentRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.findStudentById(2L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Student not found");

        verify(studentRepository, times(1)).findById(2L);
    }

    @Test
    void givenValidId_whenDeleteStudent_thenSetDeletedTrue() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        studentService.deleteStudent(1L);

        assertThat(student.isDeleted()).isTrue();
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void givenInvalidId_whenDeleteStudent_thenThrowException() {
        when(studentRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.deleteStudent(2L))
                .isInstanceOf(StudentNotFoundException.class);

        verify(studentRepository, times(1)).findById(2L);
    }
}

