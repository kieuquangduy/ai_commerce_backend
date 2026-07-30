# Sprint 01 - Domain Foundation

## Goal
Xây dựng nền tảng Domain đầu tiên của hệ thống AI Commerce Platform.

## Deliverables

- [ ] Thiết kế package structure: mỗi feature sẽ có đủ controller, service, repository,...
  + Không phải mọi thứ đều thuộc một feature, các phần chung (common module) như Exception, Config, Security, Utils sẽ đặt trong Common
- [ ] Tìm hiểu JPA & Hibernate
  + JPA là một đặc tả không phải framework, quy định Muốn một thư viện Java làm ORM thì phải hỗ trợ những annotation, interface and quy tắc nào
   Ví dụ: @Entity
          @Id
          @GeneratedValue
  + Hibernate: là framework hay là implementation của JPA
- [ ] Tạo User Entity
 + Domain là gì ? - Domain là tập hợp các đối tượng và quy tắc nghiệp vụ của hệ thống
- [ ] Tạo Role Entity
- [ ] Thiết lập quan hệ giữa User và Role
 + Chưa tính đến các cột chi tiết thông tin ngay mà ưu tiên các cột phục vụ mục đích Authorization trước 
- [ ] Kiểm tra Hibernate tạo bảng trong PostgreSQL
- [ ] Commit Sprint 01

## Definition of Done

- Dự án có cấu trúc package chuẩn.
- PostgreSQL có bảng user và role.
- User và Role có quan hệ đúng.
- Ứng dụng chạy thành công.