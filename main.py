import uvicorn
from fastapi import FastAPI
from app.book.controller import router as book_router
from app.member.controller import router as member_router
from app.borrowing.controller import router as borrowing_router

app = FastAPI(
    title="LibraX - Library Management System (Modular Monolith)",
    version="1.0.0",
    description="Hệ thống quản lý thư viện LibraX được tái cấu trúc theo mô hình Modular Monolith chia theo 3 domain: book, member, borrowing."
)

app.include_router(book_router)
app.include_router(member_router)
app.include_router(borrowing_router)

@app.get("/")
def read_root():
    return {
        "message": "Welcome to LibraX Library Management API!",
        "docs": "/docs"
    }

if __name__ == "__main__":
    uvicorn.run("app.main:app", host="127.0.0.1", port=8000, reload=True)
