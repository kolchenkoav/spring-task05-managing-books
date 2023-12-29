package com.example.books.controller;

import com.example.books.entity.Book;
import com.example.books.entity.Category;
import com.example.books.model.UpsertBookRequest;
import com.example.books.service.BookService;

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
    private final BookService service;

    @GetMapping
    public ResponseEntity<List<Book>> entityList() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<Book> entityById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/{name}")
    public ResponseEntity<Book> entityByName(@PathVariable String name) {
        return ResponseEntity.ok(service.findByName(name));
    }

    @PostMapping
    public ResponseEntity<Book> createEntity(@RequestBody UpsertBookRequest request) {
        var savedEntity = new Book();
        savedEntity.setNameBook(request.getName());
        savedEntity.setAuthor(request.getAuthor());
        Category category = new Category();
        category.setNameCategory(request.getCategory());
        savedEntity.setCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(savedEntity));
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Book> updateEntity(@PathVariable Long id, @RequestBody UpsertEntityRequest request) {
//        //var updatedEntity = client.updateEntity(id, request);
//        var updatedDbEntity = service.update(id, new Book());
//        return ResponseEntity.ok(updatedDbEntity);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Book> deleteEntityById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
