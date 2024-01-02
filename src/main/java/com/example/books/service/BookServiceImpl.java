package com.example.books.service;

import com.example.books.configuration.properties.AppCacheProperties;
import com.example.books.entity.Book;
import com.example.books.entity.Category;
import com.example.books.model.UpsertBookRequest;
import com.example.books.repository.BookRepository;
import com.example.books.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@CacheConfig(cacheManager = "redisCacheManager")
public class BookServiceImpl implements BookService {
    private final BookRepository repository;
    private final CategoryRepository categoryRepository;

    @Override
    @Cacheable(value = AppCacheProperties.CacheNames.BOOK_BY_NAME_AND_AUTHOR, key = "#name + #author")
    public UpsertBookRequest findByNameAndAuthor(String name, String author) {
        log.info("findByNameAndAuthor name: {}  author: {}", name, author);
        Book book = repository.findByNameAndAuthor(name, author);
        return upsertBookRequestFromBook(book);
    }

    @Override
    @Cacheable(value = AppCacheProperties.CacheNames.BOOKS_BY_CATEGORY, key = "#category")
    public List<UpsertBookRequest> findByCategory(String category) {
        log.info("findByCategory category: {}", category);
        List<UpsertBookRequest> bookRequests = new ArrayList<>();
        List<Book> bookList = categoryRepository.findByNameCategory(category).stream().map(Category::getBook).toList();

        if (!bookList.isEmpty()) {
            for (Book book : bookList) {
                UpsertBookRequest bookRequest = upsertBookRequestFromBook(book);
                bookRequests.add(bookRequest);
            }
        } else {
            log.warn("bookList is empty");
        }
        return bookRequests;
    }

    @Override
    @CacheEvict(value = "databaseEntities", allEntries = true)
    public UpsertBookRequest create(UpsertBookRequest request) {
        log.info("create Book - Category");

        Category category = new Category();
        category.setNameCategory(request.getNameCategory());

        Book savedEntity = new Book();
        savedEntity.setName(request.getName());
        savedEntity.setAuthor(request.getAuthor());
        savedEntity.setCategory(category);

        repository.save(savedEntity);

        return request;
    }

    @Override
    //@CacheEvict(cacheNames = AppCacheProperties.CacheNames.BOOK_BY_ID, key = "#id", beforeInvocation = true)
    @Caching(evict = {@CacheEvict(value = AppCacheProperties.CacheNames.BOOK_BY_NAME_AND_AUTHOR, beforeInvocation = true),
            @CacheEvict(value = AppCacheProperties.CacheNames.BOOKS_BY_CATEGORY, allEntries = true, beforeInvocation = true),
            @CacheEvict(value = AppCacheProperties.CacheNames.BOOK_BY_ID, key = "#id", beforeInvocation = true)})
    public UpsertBookRequest update(Long id, UpsertBookRequest request) {
        Book entityForUpdate = repository.findById(id).orElseThrow();
        log.info("Update book id:{} request: {}", id, request);

        Category category = entityForUpdate.getCategory();
        category.setNameCategory(request.getNameCategory());

        entityForUpdate.setName(request.getName());
        entityForUpdate.setAuthor(request.getAuthor());
        entityForUpdate.setCategory(category);

        repository.save(entityForUpdate);
        return request;
    }

    @Override
    @CacheEvict(cacheNames = AppCacheProperties.CacheNames.BOOK_BY_ID, key = "#id", beforeInvocation = true)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private UpsertBookRequest upsertBookRequestFromBook(Book book) {
        UpsertBookRequest bookRequest = new UpsertBookRequest();
        bookRequest.setId(book.getId());
        bookRequest.setName(book.getName());
        bookRequest.setAuthor(book.getAuthor());
        bookRequest.setNameCategory(book.getCategory().getNameCategory());
        return bookRequest;
    }

    /**
     * Init book list for test
     *
     * @return list UpsertBookRequest
     */
    public List<UpsertBookRequest> initBookList() {
        List<UpsertBookRequest> bookRequests = new ArrayList<>();
        List<Book> bookList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Book savedEntity = new Book();
            savedEntity.setName("Name" + (i + 1));
            savedEntity.setAuthor("Author" + (i + 1));

            Category category = new Category();
            category.setNameCategory((i < 5) ? "Категория 1" : "Категория 2");
            savedEntity.setCategory(category);

            bookList.add(repository.save(savedEntity));
            UpsertBookRequest bookRequest = upsertBookRequestFromBook(bookList.get(i));
            bookRequests.add(bookRequest);
        }
        return bookRequests;
    }
}
