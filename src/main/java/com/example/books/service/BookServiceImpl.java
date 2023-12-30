package com.example.books.service;

import com.example.books.entity.Book;
import com.example.books.entity.Category;
import com.example.books.entity.Employee;
import com.example.books.model.EmployeeModel;
import com.example.books.model.UpsertBookRequest;
import com.example.books.repository.BookRepository;
import com.example.books.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


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
                UpsertBookRequest empModel = new UpsertBookRequest();
                empModel.setId(book.getId());
                empModel.setName(book.getName());
                empModel.setAuthor(book.getAuthor());
                empModel.setNameCategory(book.getCategory().getNameCategory());
                bookRequests.add(empModel);
            }
        } else {
            log.warn("bookRequests is empty");
        }
        return bookRequests;
    }

    @Override
    //@Cacheable(cacheNames = AppCacheProperties.CacheNames.DATABASE_ENTITIES_BY_ID, key = "#id")
    public Book findById(Long id) {
        log.info("findById {}", id);
        return repository.findById(id).orElseThrow();
    }

    @Override
    //@Cacheable(AppCacheProperties.CacheNames.DATABASE_ENTITIES_BY_NAME)
    public Book findByName(String name) {
        Book probe = new Book();
        probe.setName(name);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths("id", "date");
        Example<Book> example = Example.of(probe, matcher);
        log.info("findByName {}", name);
        return repository.findOne(example).orElseThrow();
    }

    @Override
    //@CacheEvict(value = "databaseEntities", allEntries = true)
    public Book create(UpsertBookRequest request) {
        log.info("create Book - Category");

        Category category = new Category();
        category.setNameCategory(request.getNameCategory());
        log.info(">>>> "+category.getNameCategory());

        Book savedEntity = new Book();
        savedEntity.setName(request.getName());
        savedEntity.setAuthor(request.getAuthor());
        savedEntity.setCategory(category);

        //category.setBook(savedEntity);

        Book u = repository.save(savedEntity);

        category.setNameCategory(request.getNameCategory());
        //
        return u;
    }

    @Override
    //@CacheEvict(cacheNames = AppCacheProperties.CacheNames.DATABASE_ENTITIES_BY_ID, key = "#id", beforeInvocation = true)
    public Book update(Long id, Book entity) {
        Book entityForUpdate = findById(id);

        entityForUpdate.setName(entity.getName());

        return repository.save(entityForUpdate);
    }

    @Override
    //@CacheEvict(cacheNames = AppCacheProperties.CacheNames.DATABASE_ENTITIES_BY_ID, key = "#id", beforeInvocation = true)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
