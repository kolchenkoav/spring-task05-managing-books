package com.example.books.model;

import lombok.Data;

@Data
public class UpsertBookRequest {
    private String name;
    private String author;
    private String category;
}
