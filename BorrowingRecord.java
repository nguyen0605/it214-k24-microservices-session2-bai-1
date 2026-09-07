package com.librax.library.borrowing;

import java.time.LocalDateTime;

public class BorrowingRecord {
    private int id;
    private int memberId;
    private int bookId;
    private LocalDateTime borrowDate;
    private boolean isReturned;

    public BorrowingRecord(int id, int memberId, int bookId, LocalDateTime borrowDate) {
        this.id = id;
        this.memberId = memberId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.isReturned = false;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    public LocalDateTime getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDateTime borrowDate) { this.borrowDate = borrowDate; }
    public boolean isReturned() { return isReturned; }
    public void setReturned(boolean returned) { isReturned = returned; }
}