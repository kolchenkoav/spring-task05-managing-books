package com.example.books.service;

import com.example.books.entity.Book;
import com.example.books.entity.Category;
import com.example.books.model.UpsertBookRequest;
import com.example.books.repository.BookRepository;
import com.example.books.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
//@CacheConfig(cacheManager = "redisCacheManager")
public class BookServiceImpl implements BookService {
    private final BookRepository repository;
    private final CategoryRepository categoryRepository;

    @Override
    //@Cacheable(AppCacheProperties.CacheNames.DATABASE_ENTITIES)
    public List<UpsertBookRequest> findAll() {
        log.info("findAll");

        List<Book> bookList = repository.findAll();

        List<UpsertBookRequest> bookRequests = new ArrayList<>();
        if (!bookList.isEmpty()) {
            for (Book book : bookList) {
                UpsertBookRequest bookRequest = upsertBookRequestFromBook(book);
                bookRequests.add(bookRequest);
            }
        } else {
            log.warn("bookRequests is empty");
        }
        return bookRequests;
    }

    @Override
    //@Cacheable(cacheNames = AppCacheProperties.CacheNames.DATABASE_ENTITIES_BY_ID, key = "#id")
    public UpsertBookRequest findById(Long id) {
        log.info("findById {}", id);
        Book book = repository.findById(id).orElseThrow();
        return upsertBookRequestFromBook(book);
    }

    private UpsertBookRequest upsertBookRequestFromBook(Book book) {
        UpsertBookRequest bookRequest = new UpsertBookRequest();
        bookRequest.setId(book.getId());
        bookRequest.setName(book.getName());
        bookRequest.setAuthor(book.getAuthor());
        bookRequest.setNameCategory(book.getCategory().getNameCategory());
        return bookRequest;
    }

    @Override
    //@Cacheable(AppCacheProperties.CacheNames.DATABASE_ENTITIES_BY_NAME)
    public List<UpsertBookRequest> findByName(String name) {
        log.info("findByName {}", name);
        List<UpsertBookRequest> bookRequests = new ArrayList<>();

        List<Book> bookList = repository.findByName(name).stream().toList();

        if (!bookList.isEmpty()) {
            for (Book book : bookList) {
                UpsertBookRequest bookRequest = upsertBookRequestFromBook(book);
                bookRequests.add(bookRequest);
            }
        } else {
            log.warn("bookRequests is empty");
        }

        return bookRequests;
    }

    @Override
    //@CacheEvict(value = "databaseEntities", allEntries = true)
    public Book create(UpsertBookRequest request) {
        log.info("create Book - Category");

        Category category = new Category();
        category.setNameCategory(request.getNameCategory());

        Book savedEntity = new Book();
        savedEntity.setName(request.getName());
        savedEntity.setAuthor(request.getAuthor());
        savedEntity.setCategory(category);

        Book u = repository.save(savedEntity);

        category.setNameCategory(request.getNameCategory());
        return u;
    }


    @Override
    //@CacheEvict(cacheNames = AppCacheProperties.CacheNames.DATABASE_ENTITIES_BY_ID, key = "#id", beforeInvocation = true)
    public Book update(Long id, UpsertBookRequest request) {
        Book entityForUpdate = repository.findById(id).orElseThrow();
        log.info("update Book - Category");

        Category category = entityForUpdate.getCategory();
        category.setNameCategory(request.getNameCategory());

        entityForUpdate.setName(request.getName());
        entityForUpdate.setAuthor(request.getAuthor());
        entityForUpdate.setCategory(category);

        return repository.save(entityForUpdate);
    }

    @Override
    //@CacheEvict(cacheNames = AppCacheProperties.CacheNames.DATABASE_ENTITIES_BY_ID, key = "#id", beforeInvocation = true)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Book findByAuthor(String author) {
        return repository.findByAuthor(author);
    }


    public List<UpsertBookRequest> findByCategory(String category) {
        List<UpsertBookRequest> bookRequests = new ArrayList<>();
        List<Book> bookList = categoryRepository.findByNameCategory(category).stream().map(Category::getBook).toList();

        if (!bookList.isEmpty()) {
            for (Book book : bookList) {
                UpsertBookRequest bookRequest = upsertBookRequestFromBook(book);
                bookRequests.add(bookRequest);
            }
        } else {
            log.warn("bookRequests is empty");
        }

        return bookRequests;
    }

}
