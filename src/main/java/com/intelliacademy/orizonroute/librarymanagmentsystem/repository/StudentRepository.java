package com.intelliacademy.orizonroute.librarymanagmentsystem.repository;


import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findBySif(String sif);
}

