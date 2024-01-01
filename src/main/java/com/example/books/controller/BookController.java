package com.example.books.controller;

import com.example.books.entity.Book;
import com.example.books.model.UpsertBookRequest;
import com.example.books.service.BookServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
public class BookController {
    private final BookServiceImpl service;

    @GetMapping("{name}/{author}")
    public ResponseEntity<UpsertBookRequest> bookByNameAndAuthor(@PathVariable String name,
                                                                 @PathVariable String author) {
        return ResponseEntity.ok(service.findByNameAndAuthor(name, author));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<UpsertBookRequest>> bookByCategory(@PathVariable String category) {
        return ResponseEntity.ok(service.findByCategory(category));
    }

    @PostMapping
    public ResponseEntity<UpsertBookRequest> createBook(@RequestBody UpsertBookRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpsertBookRequest> updateBook(@PathVariable Long id, @RequestBody UpsertBookRequest request) {
        var updatedDbEntity = service.update(id, request);
        return ResponseEntity.ok(updatedDbEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Book> deleteBookById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/init")
    public ResponseEntity<List<UpsertBookRequest>> initBookList() {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.initBookList());
    }
}
