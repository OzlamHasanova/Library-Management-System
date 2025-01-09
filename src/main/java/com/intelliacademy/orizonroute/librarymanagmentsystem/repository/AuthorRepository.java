package com.intelliacademy.orizonroute.librarymanagmentsystem.repository;

import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
