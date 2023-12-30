package com.example.books.service;

import com.example.books.entity.Book;
import com.example.books.model.UpsertBookRequest;

import java.util.List;

public interface BookService {
    List<UpsertBookRequest> findAll();
    Book findById(Long id);
    Book findByName(String name);
    Book create(UpsertBookRequest request);
    Book update(Long id, Book entity);
    void deleteById(Long id);
}
