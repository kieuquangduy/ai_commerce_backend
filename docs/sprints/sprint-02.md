# Sprint 2: Build first function (register)

Sprint Authentication

1. Register
   ↓
2. Login (JWT)
   ↓
3. Refresh Token
   ↓
4. Logout
   ↓
5. Forgot Password
   ↓
6. Reset Password
   ↓
7. Email Verification
   ↓
8. OAuth2 (Google)


* Khái niệm Bean Validation 

 + Là bước validate ngay trên DTO, nếu có lỗi thì sẽ chưa cần chạy đến Service 
 + Sử dụng Dependency Validation để sử dụng nó

* Khái niệm Validation LifeCycle trong Spring


* Validation được chia thành 2 loại:
 1. Syntax Validation: Rỗng, đúng định dạng hay không, đủ kí tự hay không => Bean Validation
 2. Business Validation: Email đã tồn tại hay chưa, Sản phẩm còn hàng hay không => Service

* BCrypt: Có chức năng hashing và tự động thêm salt để mã hóa một chiều với thuật toán BlowFish

* SimpleMailSender:  just sending message
* Mime Email: this method is more suitable for this project because it can send HTML and File. 


* Cách tư duy để nghĩ ra method ở repository: Business -> Use Case -> Repository Method

* Nếu sử dụng @ManytoMany thì không được gắng @JoinColumn trực tiếp mà phải dùng @JoinTable

* HttpSecurity là hàm build dưới dạng các chuỗi
* SecurityFilterChain là bộ lọc đóng HttpSecurity thành các bộ lọc phải chạy qua trong đó có 3 bộ lọc bắt buộc phải chạy qua trước: 
   + 1: Kiểm tra xem có tấn công csrf hay không
   + 2: Kiểm tra người dùng đã đăng nhập hay chưa
   + 3: Người dùng có đủ quyền hay role để đăng nhập hay không
- Lý do nếu dùng jwt thì tắt csrf là vì jwt lưu ở LocalStorage

* Cơ chế Persistence Context và Dirty Checking


* LazyInitializationException xảy ra khi Hibernate cần lazy-load một relationship nhưng persistence context/session đã đóng. Một cách xử lý là đảm bảo việc truy cập relationship xảy ra trong transaction, ví dụ dùng @Transactional.

