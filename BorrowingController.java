package com.librax.library.borrowing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/borrowings")
public class BorrowingController {
    private final BorrowingService borrowingService;

    @Autowired
    public BorrowingController(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    @PostMapping
    public ResponseEntity<?> borrowBook(@RequestBody BorrowRequest request) {
        try {
            BorrowingRecord record = borrowingService.borrowBook(request.getMemberId(), request.getBookId());
            return ResponseEntity.status(HttpStatus.CREATED).body(record);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<BorrowingRecord>> getAllBorrowings() {
        return ResponseEntity.ok(borrowingService.getAllBorrowings());
    } dream
}