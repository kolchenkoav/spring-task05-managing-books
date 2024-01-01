package com.example.books.service;

import com.example.books.model.UpsertBookRequest;

import java.util.List;

public interface BookService {
    UpsertBookRequest findByNameAndAuthor(String name, String author);
    List<UpsertBookRequest> findByCategory(String category);
    UpsertBookRequest create(UpsertBookRequest request);
    UpsertBookRequest update(Long id, UpsertBookRequest request);
    void deleteById(Long id);
}
