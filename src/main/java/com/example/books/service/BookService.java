package com.example.books.service;

import com.example.books.entity.Book;
import com.example.books.model.UpsertBookRequest;

import java.util.List;

public interface BookService {
    List<UpsertBookRequest> findAll();
    UpsertBookRequest findById(Long id);
    List<UpsertBookRequest> findByName(String name);
    Book create(UpsertBookRequest request);

    //@CacheEvict(cacheNames = AppCacheProperties.CacheNames.DATABASE_ENTITIES_BY_ID, key = "#id", beforeInvocation = true)
    Book update(Long id, UpsertBookRequest request);

    void deleteById(Long id);

    Book findByAuthor(String author);
}
