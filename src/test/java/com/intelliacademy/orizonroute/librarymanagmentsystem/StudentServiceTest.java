package com.intelliacademy.orizonroute.librarymanagmentsystem;

import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.StudentDTO;
import com.intelliacademy.orizonroute.librarymanagmentsystem.exception.StudentNotFoundException;
import com.intelliacademy.orizonroute.librarymanagmentsystem.mapper.StudentMapper;
import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Student;
import com.intelliacademy.orizonroute.librarymanagmentsystem.repository.StudentRepository;
import com.intelliacademy.orizonroute.librarymanagmentsystem.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentService studentService;

    private Student student1;
    private Student student2;
    private StudentDTO studentDTO1;
    private StudentDTO studentDTO2;
    private Pageable pageable;
    private Long studentId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        studentId = 1L;

        student1 = new Student(1L, "John", "Doe", "12345");
        student2 = new Student(2L, "Jane", "Doe", "12346");

        studentDTO1 = new StudentDTO();
        studentDTO1.setId(1L);
        studentDTO1.setName("John");
        studentDTO1.setSurname("Doe");
        studentDTO1.setSif("12345");

        studentDTO2 = new StudentDTO();
        studentDTO2.setId(2L);
        studentDTO2.setName("Jane");
        studentDTO2.setSurname("Doe");
        studentDTO2.setSif("12346");

    }

    @Test
    void givenValidPageAndSize_whenGetAllStudents_thenReturnStudentDTOPage() {
        pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        Page<Student> studentPage = new PageImpl<>(Arrays.asList(student1, student2), pageable, 2);

        when(studentRepository.findAll(pageable)).thenReturn(studentPage);
        when(studentMapper.toDTO(student1)).thenReturn(studentDTO1);
        when(studentMapper.toDTO(student2)).thenReturn(studentDTO2);

        Page<StudentDTO> result = studentService.getAllStudents(0, 10, "name", "asc");

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("John", result.getContent().get(0).getName());
        assertEquals("Jane", result.getContent().get(1).getName());
        verify(studentRepository).findAll(pageable);
    }

    @Test
    void givenStudentsInRepository_whenGetStudentList_thenReturnStudentDTOList() {
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student1, student2));
        when(studentMapper.toDTO(student1)).thenReturn(studentDTO1);
        when(studentMapper.toDTO(student2)).thenReturn(studentDTO2);

        List<StudentDTO> result = studentService.getStudentList();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getName());
        assertEquals("Jane", result.get(1).getName());
        verify(studentRepository).findAll();
        verify(studentMapper).toDTO(student1);
        verify(studentMapper).toDTO(student2);
    }

    @Test
    void givenDescDirection_whenGetAllStudents_thenReturnSortedByDescending() {
        pageable = PageRequest.of(0, 10, Sort.by("name").descending());
        Page<Student> studentPage = new PageImpl<>(Collections.singletonList(student1), pageable, 1);

        when(studentRepository.findAll(pageable)).thenReturn(studentPage);
        when(studentMapper.toDTO(student1)).thenReturn(studentDTO1);

        Page<StudentDTO> result = studentService.getAllStudents(0, 10, "name", "desc");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("John", result.getContent().get(0).getName());
        verify(studentRepository).findAll(pageable);
    }

    @Test
    void givenAscDirection_whenGetAllStudents_thenReturnSortedByAscending() {
        pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        Page<Student> studentPage = new PageImpl<>(Collections.singletonList(student1), pageable, 1);

        when(studentRepository.findAll(pageable)).thenReturn(studentPage);
        when(studentMapper.toDTO(student1)).thenReturn(studentDTO1);

        Page<StudentDTO> result = studentService.getAllStudents(0, 10, "name", "asc");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("John", result.getContent().get(0).getName());
        verify(studentRepository).findAll(pageable);
    }

    @Test
    void givenInvalidDirection_whenGetAllStudents_thenReturnSortedByDefaultAscending() {
        pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        Page<Student> studentPage = new PageImpl<>(Collections.singletonList(student1), pageable, 1);

        when(studentRepository.findAll(pageable)).thenReturn(studentPage);
        when(studentMapper.toDTO(student1)).thenReturn(studentDTO1);

        Page<StudentDTO> result = studentService.getAllStudents(0, 10, "name", "invalid");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("John", result.getContent().get(0).getName());
        verify(studentRepository).findAll(pageable);
    }

    @Test
    void givenValidId_whenFindStudentById_thenReturnStudentDTO() {
        Long id = 1L;
        when(studentRepository.findById(id)).thenReturn(Optional.of(student1));
        when(studentMapper.toDTO(student1)).thenReturn(studentDTO1);

        StudentDTO result = studentService.findStudentById(id);

        assertNotNull(result);
        assertEquals("John", result.getName());
        assertEquals("Doe", result.getSurname());
        verify(studentRepository).findById(id);
    }

    @Test
    void givenInvalidId_whenFindStudentById_thenThrowStudentNotFoundException() {
        Long id = 1L;
        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentService.findStudentById(id));
        verify(studentRepository).findById(id);
    }

    @Test
    void givenValidStudentDTO_whenCreateStudent_thenStudentIsCreated() {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setName("John");
        studentDTO.setSurname("Doe");
        studentDTO.setSif("12345");

        when(studentMapper.toEntity(studentDTO)).thenReturn(student1);
        when(studentRepository.save(student1)).thenReturn(student1);

        studentService.createStudent(studentDTO);

        verify(studentRepository).save(student1);
    }

    @Test
    void givenValidId_whenUpdateStudent_thenStudentIsUpdated() {
        Student updatedStudent = new Student();
        updatedStudent.setId(studentId);
        updatedStudent.setName("Old Name");
        updatedStudent.setSurname("Old Surname");
        updatedStudent.setSif("12345");

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(updatedStudent));

        studentService.updateStudent(studentId, studentDTO1);

        assertEquals("John", updatedStudent.getName());
        assertEquals("Doe", updatedStudent.getSurname());
        verify(studentRepository).save(updatedStudent);
    }

    @Test
    void givenInvalidId_whenUpdateStudent_thenThrowStudentNotFoundException() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentService.updateStudent(studentId, studentDTO1));
        verify(studentRepository).findById(studentId);
    }

    @Test
    void givenValidId_whenDeleteStudent_thenStudentIsDeleted() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student1));

        studentService.deleteStudent(studentId);

        assertTrue(student1.isDeleted());
        verify(studentRepository).save(student1);
    }

    @Test
    void givenInvalidId_whenDeleteStudent_thenThrowStudentNotFoundException() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentService.deleteStudent(studentId));
        verify(studentRepository).findById(studentId);
    }
}

