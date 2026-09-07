package com.librax.library.borrowing;

public class BorrowRequest {
    private int memberId;
    private int bookId;

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
}