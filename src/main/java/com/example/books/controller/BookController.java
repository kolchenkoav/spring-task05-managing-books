package com.example.books.controller;

import com.example.books.entity.Book;
import com.example.books.service.BookService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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

//    @PostMapping
//    public ResponseEntity<Book> createEntity(@RequestBody UpsertEntityRequest request) {
//        //var newEntity = client.createEntity(request); // TODO createEntity
//        var savedEntity = service.create(new Book());
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedEntity);
//    }

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
