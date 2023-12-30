package com.example.books.model;

import lombok.Data;

@Data
public class UpsertBookRequest {
    private Long id;
    private String name;
    private String author;
    private String nameCategory;
}
