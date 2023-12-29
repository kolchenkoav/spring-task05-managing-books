package com.example.books.service;

import com.example.books.configuration.properties.AppCacheProperties;
import com.example.books.entity.Book;
import com.example.books.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
//@CacheConfig(cacheManager = "redisCacheManager")
public class BookService {
    private final BookRepository repository;

    //@Cacheable(AppCacheProperties.CacheNames.DATABASE_ENTITIES)
    public List<Book> findAll() {
        log.info("findAll");
        return repository.findAll();
    }

    @Cacheable(cacheNames = AppCacheProperties.CacheNames.DATABASE_ENTITIES_BY_ID, key = "#id")
    public Book findById(Long id) {
        log.info("findById {}", id);
        return repository.findById(id).orElseThrow();
    }

    @Cacheable(AppCacheProperties.CacheNames.DATABASE_ENTITIES_BY_NAME)
    public Book findByName(String name) {
        Book probe = new Book();
        probe.setNameBook(name);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths("id", "date");
        Example<Book> example = Example.of(probe, matcher);
        log.info("findByName {}", name);
        return repository.findOne(example).orElseThrow();
    }

    @CacheEvict(value = "databaseEntities", allEntries = true)
    public Book create(Book entity) {
        Book forSave = new Book();
        forSave.setNameBook(entity.getNameBook());

        return repository.save(forSave);
    }

    @CacheEvict(cacheNames = AppCacheProperties.CacheNames.DATABASE_ENTITIES_BY_ID, key = "#id", beforeInvocation = true)
    public Book update(Long id, Book entity) {
        Book entityForUpdate = findById(id);

        entityForUpdate.setNameBook(entity.getNameBook());

        return repository.save(entityForUpdate);
    }

    @CacheEvict(cacheNames = AppCacheProperties.CacheNames.DATABASE_ENTITIES_BY_ID, key = "#id", beforeInvocation = true)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
