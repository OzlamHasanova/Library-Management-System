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
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
    void updateStudent_givenInvalidId_shouldThrowException() {
        when(studentRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.updateStudent(2L, studentDTO))
                .isInstanceOf(StudentNotFoundException.class);

        verify(studentRepository).findById(2L);
        verify(studentRepository, never()).save(any());
    }

    @Test
    void deleteStudent_shouldSetDeletedTrue() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        studentService.deleteStudent(1L);

        assertThat(student.isDeleted()).isTrue();
        verify(studentRepository).save(student);
    }

    @Test
    void deleteStudent_givenInvalidId_shouldThrowException() {
        when(studentRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.deleteStudent(2L))
                .isInstanceOf(StudentNotFoundException.class);

        verify(studentRepository).findById(2L);
    }



    @Test
    void findStudentById_givenInvalidId_shouldThrowException() {
        when(studentRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.findStudentById(2L))
                .isInstanceOf(StudentNotFoundException.class);

        verify(studentRepository).findById(2L);
    }

    @Test
    void getStudentList_shouldReturnListOfStudentDTOs() {
        when(studentRepository.findAll()).thenReturn(List.of(student));
        when(studentMapper.toDTO(student)).thenReturn(studentDTO);

        List<StudentDTO> result = studentService.getStudentList();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        verify(studentRepository).findAll();
        verify(studentMapper).toDTO(student);
    }
}
