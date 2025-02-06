//package com.intelliacademy.orizonroute.librarymanagmentsystem.controller;
//
//import com.intelliacademy.orizonroute.librarymanagmentsystem.dto.StudentDTO;
//import com.intelliacademy.orizonroute.librarymanagmentsystem.service.StudentService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/students")
//@RequiredArgsConstructor
//public class StudentController {
//
//    private final StudentService studentService;
//
//    @GetMapping
//    public String listStudents(Model model) {
//        List<StudentDTO> students = studentService.getAllStudents();
//        model.addAttribute("students", students);
//        return "student/list";
//    }
//
//    @GetMapping("/create")
//    public String showCreateStudentForm(Model model) {
//        model.addAttribute("student", new StudentDTO());
//        return "student/create";
//    }
//
//    @PostMapping("/create")
//    public String createStudent(@ModelAttribute("student") StudentDTO studentDTO) {
//        studentService.createStudent(studentDTO);
//        return "redirect:/students";
//    }
//
//    @GetMapping("/edit/{id}")
//    public String showEditStudentForm(@PathVariable("id") Long id, Model model) {
//        StudentDTO studentDTO = studentService.updateStudent(id, studentService.findStudentById(id));
//        model.addAttribute("student", studentDTO);
//        return "student/edit";
//    }
//
//    @PostMapping("/edit/{id}")
//    public String updateStudent(@PathVariable("id") Long id, @ModelAttribute("student") StudentDTO studentDTO) {
//        studentService.updateStudent(id, studentDTO);
//        return "redirect:/students";
//    }
//
//    @GetMapping("/delete/{id}")
//    public String deleteStudent(@PathVariable("id") Long id) {
//        studentService.deleteStudent(id);
//        return "redirect:/students";
//    }
//}
