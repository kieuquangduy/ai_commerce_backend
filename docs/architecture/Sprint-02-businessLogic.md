Register Request

        │
        ▼
Kiểm tra email
│
▼
Email đã tồn tại?
│
┌────┴────┐
│         │
Có        Không
│          │
▼          ▼
Đã verify?   Tạo User mới
│
┌─┴──┐
│    │
Có   Chưa
│    │
▼    ▼
Báo lỗi  Dùng lại User cũ
│
▼
Lấy Role CUSTOMER
│
▼
Hash Password
│
▼
verified = false
│
▼
Save User
│
▼
Xóa VERIFY_EMAIL Token cũ
│
▼
Sinh UUID Token mới
│
▼
Lưu VerificationToken
│
▼
Gửi Email (@Async)
│
▼
Return UserDTO