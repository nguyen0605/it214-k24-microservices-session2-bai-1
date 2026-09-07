package com.librax.library.book;

import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class BookRepository {
    private final Map<Integer, Book> books = new ConcurrentHashMap<>();

    public BookRepository() {
        books.put(1, new Book(1, "Clean Code", "Robert C. Martin"));
        books.put(2, new Book(2, "Design Patterns", "Erich Gamma"));
        books.put(3, new Book(3, "Refactoring", "Martin Fowler"));
    }

    public Book findById(int id) {
        return books.get(id);
    }

    public void save(Book book) {
        books.put(book.getId(), book);
    }
}