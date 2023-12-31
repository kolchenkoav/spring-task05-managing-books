package com.example.books.repository;

import com.example.books.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Book findByAuthor(String author);

    List<Book> findByName(String name);

}
