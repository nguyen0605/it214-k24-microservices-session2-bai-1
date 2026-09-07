from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from app.borrowing.service import BorrowingService

router = APIRouter(prefix="/api/borrowings", tags=["borrowings"])
borrowing_service = BorrowingService()

class BorrowRequest(BaseModel):
    member_id: int
    book_id: int

@router.post("")
def borrow_book(request: BorrowRequest):
    result = borrowing_service.borrow_book(request.member_id, request.book_id)
    if not result["success"]:
        raise HTTPException(status_code=400, detail=result["message"])
    return result

@router.get("")
def list_borrowings():
    return borrowing_service.list_borrowings()
