package com.intelliacademy.orizonroute.librarymanagmentsystem.repository;

import com.intelliacademy.orizonroute.librarymanagmentsystem.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByCategory_Id(Long categoryId);

    Page<Book> findByIsDeletedFalse(Pageable pageable);

    List<Book> findByIsDeletedFalse();

    Optional<Book> findBookByIsbn(String isbn);

    @Query("SELECT COUNT(b) FROM Book b WHERE b.stock > 0 AND b.isDeleted = false")
    Long countAvailableBooks();


    @Query("SELECT b FROM Book b LEFT JOIN b.authors a " +
            "WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(a.fullName) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Book> searchBooks(@Param("query") String query, Pageable pageable);
}

