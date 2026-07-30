# Phần 1:Users với Role phục vụ Auth trước
- Lý do nên tách 2 bảng vì phục vụ One-to-Many, không nên dùng enum 
- Role phục vụ Spring Security
Quan hệ giữa Role và Users là One-To-Many
* Bảng Role bao gồm
- id: Bigint primary key
- name: varchar ADMIN, SELLER, CUSTOMER

* Bảng Users
- id: Bigint PK
- username: varchar unique
- email: varchar unique
- password: varchar 
- f_name: varchar
- l_name: varchar 
- enabled: boolean
- role_id: FK

* Khái niệm Owner side và Inverse Side
Thằng nào giữ FK thằng đó là Owner Side
@JoinColumn đặt ở Owner Side.
mappedBy đặt ở Inverse Side.