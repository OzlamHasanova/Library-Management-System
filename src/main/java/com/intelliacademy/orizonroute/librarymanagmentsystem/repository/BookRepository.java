package com.intelliacademy.orizonroute.librarymanagmentsystem.repository;

import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByCategory_Id(Long categoryId);

    Page<Book> findByIsDeletedFalse(Pageable pageable);

    List<Book> findByIsDeletedFalse();

    Optional<Book> findBookByIsbn(String isbn);

}
