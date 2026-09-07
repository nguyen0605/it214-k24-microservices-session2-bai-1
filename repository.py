from app.database import BORROWINGS_DB

class BorrowingRepository:
    def get_borrowed_count_by_member(self, member_id: int) -> int:
        return sum(1 for item in BORROWINGS_DB if item["member_id"] == member_id)

    def is_book_already_borrowed(self, book_id: int) -> bool:
        return any(item["book_id"] == book_id for item in BORROWINGS_DB)

    def add_borrowing(self, member_id: int, book_id: int):
        borrowing_record = {"member_id": member_id, "book_id": book_id}
        BORROWINGS_DB.append(borrowing_record)
        return borrowing_record

    def get_all_borrowings(self):
        return BORROWINGS_DB
