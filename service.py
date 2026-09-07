from app.borrowing.repository import BorrowingRepository
from app.book.repository import BookRepository
from app.member.repository import MemberRepository

class BorrowingService:
    def __init__(self):
        self.borrowing_repository = BorrowingRepository()
        self.book_repository = BookRepository()
        self.member_repository = MemberRepository()

    def can_borrow_book(self, current_borrowed_by_member: int) -> bool:
        if current_borrowed_by_member >= 5:
            return False
        return True

    def borrow_book(self, member_id: int, book_id: int):
        member = self.member_repository.get_by_id(member_id)
        if not member:
            return {"success": False, "message": f"Member {member_id} does not exist"}

        book = self.book_repository.get_by_id(book_id)
        if not book:
            return {"success": False, "message": f"Book {book_id} does not exist"}

        if self.borrowing_repository.is_book_already_borrowed(book_id):
            return {"success": False, "message": f"Book {book_id} is already borrowed"}

        current_borrowed = self.borrowing_repository.get_borrowed_count_by_member(member_id)

        if not self.can_borrow_book(current_borrowed):
            return {"success": False, "message": f"Member {member_id} has reached the maximum borrowing limit of 5 books"}

        record = self.borrowing_repository.add_borrowing(member_id, book_id)
        return {"success": True, "message": f"Member {member_id} successfully borrowed book {book_id}", "data": record}

    def list_borrowings(self):
        return self.borrowing_repository.get_all_borrowings()
