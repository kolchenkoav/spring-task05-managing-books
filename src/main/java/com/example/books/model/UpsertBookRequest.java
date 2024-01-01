package com.example.books.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpsertBookRequest implements Serializable {
    private Long id;
    private String name;
    private String author;
    private String nameCategory;
}
