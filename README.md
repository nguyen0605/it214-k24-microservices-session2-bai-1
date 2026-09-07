# Dự án Tái cấu trúc Monolithic Architecture - LibraX

## 1. Phân tích & Sửa lỗi logic

### Lỗi 1: Hàm `canBorrowBook` cho phép mượn vượt quá giới hạn 5 cuốn
- **Nguyên nhân:** 
  Trong mã nguồn cũ, hàm được viết như sau:
  ```java
  public boolean canBorrowBook(int currentBorrowedByMember) {
      if (currentBorrowedByMember > 5) {
          return false;
      }
      return true;
  }
  ```
  Khi độc giả đang mượn đúng **5 cuốn** (`currentBorrowedByMember = 5`), biểu thức `5 > 5` trả về `false`. Do đó, hàm bỏ qua câu lệnh rẽ nhánh và trả về `true`. Điều này cho phép độc giả tiếp tục mượn thêm cuốn thứ 6, vi phạm quy định mượn tối đa 5 cuốn.
- **Cách khắc phục:** 
  Sửa lại điều kiện là: `currentBorrowedByMember < 5` để chỉ trả về `true` khi độc giả đang mượn ít hơn 5 cuốn.

### Lỗi 2: Vấn đề của biến `totalBorrowedBooks` tĩnh (static)
- **Nguyên nhân:** 
  Biến `totalBorrowedBooks` được khai báo là `static`, nghĩa là nó được chia sẻ giữa toàn bộ các request và tất cả các độc giả trong ứng dụng. 
  - Đây là lỗi **Shared Mutable State** nghiêm trọng: nếu độc giả A mượn sách, biến này tăng lên và vô tình ảnh hưởng đến số lượng tính toán giới hạn của độc giả B.
  - Gây ra lỗi tranh chấp luồng (race conditions) khi nhiều người mượn sách đồng thời.
- **Cách khắc phục:** 
  Loại bỏ hoàn toàn biến static này. Thay vào đó, thiết lập thực thể `BorrowingRecord` để lưu vết từng giao dịch mượn. Khi cần kiểm tra số sách một độc giả đang mượn, ta truy vấn thông qua `BorrowingRepository` dựa trên `memberId` của độc giả đó.

---

## 2. Cấu trúc thư mục ứng dụng (Package by Domain)

Ứng dụng được tổ chức theo cấu trúc Modular Monolith. Mỗi domain nghiệp vụ nắm giữ trọn vẹn cả 3 tầng (Controller - Service - Repository):

```text
com.librax.library
│
├── LibraryApplication.java           # Entry point của ứng dụng Spring Boot
│
├── book                              # Domain Book
│   ├── Book.java                     # Model
│   ├── BookRepository.java           # Tầng Repository
│   ├── BookService.java              # Tầng Service
│   └── BookController.java           # Tầng Controller
│
├── member                            # Domain Member
│   ├── Member.java
│   ├── MemberRepository.java
│   ├── MemberService.java
│   └── MemberController.java
│
└── borrowing                         # Domain Borrowing (Mượn trả)
    ├── BorrowingRecord.java
    ├── BorrowRequest.java
    ├── BorrowingRepository.java
    ├── BorrowingService.java
    └── BorrowingController.java
```

---

## 3. Vì sao đây vẫn là Monolithic Architecture chứ chưa phải Microservices?

Mặc dù chúng ta đã phân chia cấu trúc code rõ ràng theo từng domain (`book`, `member`, `borrowing`) nhưng kiến trúc này vẫn là **Monolithic Architecture** (cụ thể là **Modular Monolith**) vì:
1. **Chung một tiến trình chạy (Single Process):** Toàn bộ ứng dụng được build ra một file JAR duy nhất và chạy trong cùng một máy ảo Java (JVM).
2. **Gọi hàm trực tiếp (In-memory calls):** `BorrowingService` gọi trực tiếp các hàm của `BookService` và `MemberService` thông qua cơ chế Dependency Injection của Spring thay vì gọi qua Network (gRPC, HTTP REST, Message Broker).
3. **Cơ sở dữ liệu tập trung / Chia sẻ bộ nhớ:** Các repositories đang dùng chung một bộ nhớ RAM (hoặc chung 1 database nếu kết nối DB). Nếu một domain gặp sự cố nghiêm trọng gây sập JVM, toàn bộ ứng dụng sẽ ngừng hoạt động.

---

## 4. Hướng dẫn chạy ứng dụng

### Yêu cầu
- JDK 17 trở lên

### Khởi chạy dự án
Chạy lệnh sau tại thư mục gốc chứa file `build.gradle`:
```bash
./gradlew bootRun
```

### Các Endpoint REST API mẫu để test

#### 1. Lấy thông tin Book mẫu
- **URL:** `GET http://localhost:8080/api/books/1`
- **Phản hồi:**
  ```json
  {
    "id": 1,
    "title": "Clean Code",
    "author": "Robert C. Martin"
  }
  ```

#### 2. Lấy thông tin Member mẫu
- **URL:** `GET http://localhost:8080/api/members/101`
- **Phản hồi:**
  ```json
  {
    "id": 101,
    "name": "Alice"
  }
  ```

#### 3. Thực hiện mượn sách
- **URL:** `POST http://localhost:8080/api/borrowings`
- **Body (JSON):**
  ```json
  {
    "memberId": 101,
    "bookId": 1
  }
  ```
- **Phản hồi (201 Created):**
  ```json
  {
    "id": 1,
    "memberId": 101,
    "bookId": 1,
    "borrowDate": "2023-10-24T15:30:00.123456",
    "returned": false
  }
  ```