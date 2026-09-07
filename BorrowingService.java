package com.librax.library.borrowing;

import com.librax.library.book.BookService;
import com.librax.library.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BorrowingService {
    private final BorrowingRepository borrowingRepository;
    private final BookService bookService;
    private final MemberService memberService;

    @Autowired
    public BorrowingService(BorrowingRepository borrowingRepository, 
                            BookService bookService, 
                            MemberService memberService) {
        this.borrowingRepository = borrowingRepository;
        this.bookService = bookService;
        this.memberService = memberService;
    }

    public boolean canBorrowBook(int currentBorrowedByMember) {
        // Sửa lỗi logic: Nếu đang mượn từ 5 cuốn trở lên, không cho mượn nữa
        return currentBorrowedByMember < 5;
    }

    public BorrowingRecord borrowBook(int memberId, int bookId) {
        if (memberService.getMemberById(memberId) == null) {
            throw new IllegalArgumentException("Member not found");
        }
        if (bookService.getBookById(bookId) == null) {
            throw new IllegalArgumentException("Book not found");
        }

        // Loại bỏ hoàn toàn biến static dùng chung, lấy số lượng thực tế từ Repository
        int currentBorrowedCount = borrowingRepository.findActiveBorrowingsByMember(memberId).size();

        if (!canBorrowBook(currentBorrowedCount)) {
            throw new IllegalStateException("Member has reached the limit of 5 borrowed books.");
        }

        BorrowingRecord record = new BorrowingRecord(0, memberId, bookId, LocalDateTime.now());
        return borrowingRepository.save(record);
    }

    public List<BorrowingRecord> getAllBorrowings() {
        return borrowingRepository.findAll();
    }
}