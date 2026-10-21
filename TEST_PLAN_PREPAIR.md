# Thông tin dữ liệu và tài khoản test

> Chỉ dùng cho môi trường local/test. Không sử dụng các tài khoản này trên production.

## 1. Tài khoản test

### Admin

- **Username:** `admin`
- **Password:** `123456`
- **Quyền:** `ADMIN`
- **Trạng thái:** `HOAT_DONG`

### Kỹ thuật viên

- **Username:** `ktv.test`
- **Password:** `123456`
- **Quyền:** `KY_THUAT_VIEN`
- **Trạng thái:** `HOAT_DONG`

## 2. Dữ liệu phiếu sửa chữa

### Phiếu test tạo từ flow đặt lịch

- **Số điện thoại:** `09121014130639`
- **Booking ID:** `3`
- **Ngày hẹn:** `2026-10-22`
- **Giờ hẹn:** `10:30`
- **Trạng thái booking:** `KHACH_DA_DEN`
- **Mã phiếu:** `RP-512806`
- **Trạng thái phiếu:** `CHỜ_KHÁM`
- **Mô tả lỗi:** `May test tao tu form dat lich`

Frontend tra cứu:

- [Tra cứu phiếu](http://localhost:5173/tra-cuu)
- [Chi tiết phiếu](http://localhost:5173/track/RP-512806)

API tra cứu:

- [API phiếu RP-512806](http://localhost:5001/api/public/track/RP-512806)

### Các mã phiếu seed bổ sung

| Mã phiếu | Khách hàng | Số điện thoại | Thiết bị | Trạng thái |
|---|---|---|---|---|
| `TEST-2026-0001` | Nguyen Van An | `0909000001` | Dell Latitude 5420 | `DANG_SUA` |
| `TEST-2026-0002` | Tran Thi Binh | `0909000002` | iPhone 13 | `CHO_BAO_GIA` |

API:

- [API TEST-2026-0001](http://localhost:5001/api/public/track/TEST-2026-0001)
- [API TEST-2026-0002](http://localhost:5001/api/public/track/TEST-2026-0002)

### Negative case

- Mã không tồn tại: `NOT-FOUND-TEST`
- Kết quả mong đợi: HTTP `404`

## 3. Hợp đồng route frontend

| Chức năng | Route |
|---|---|
| Trang chủ | `/` |
| Đặt lịch | `/dat-lich` |
| Tra cứu theo số điện thoại | `/tra-cuu` |
| Tra cứu chi tiết phiếu | `/track/:ticketCode` |
| Đăng nhập | `/login` |
| Admin | `/admin` |
| Tạo phiếu tiếp nhận trực tiếp | `/intake` |
| Online intake | `/online-intake` |
| Bảng giá | `/bang-gia` |
| Pricing alias | `/pricing` |

Base URL local:

- Frontend: `http://localhost:5173`
- Backend API: `http://localhost:5001`
- API prefix: `http://localhost:5001/api`

## 4. Flow tạo dữ liệu phiếu

1. Mở [form đặt lịch](http://localhost:5173/dat-lich).
2. Nhập họ tên, số điện thoại, ngày hẹn, giờ hẹn và mô tả lỗi.
3. Gửi form để tạo bản ghi trong `lich_hen`.
4. Đăng nhập tại [trang login](http://localhost:5173/login).
5. Trong Admin, chuyển booking sang trạng thái `KHACH_DA_DEN`.
6. Hệ thống tạo thiết bị và phiếu sửa chữa với mã `RP-xxxxxx`.
7. Dùng mã phiếu tại trang [tra cứu](http://localhost:5173/tra-cuu).

## 5. Quy tắc dữ liệu test

- Có thể tạo dữ liệu mới trong database local.
- Không xóa dữ liệu cũ khi chạy seed hiện tại.
- Database đang dùng là môi trường local được cấu hình trong `backend/.env`.
- Dữ liệu test nên dùng số điện thoại, mã phiếu và tên định danh riêng.
- Hiện chưa cấu hình reset database trước mỗi lần chạy test.
- Nên chạy test tuần tự để tránh trùng dữ liệu và ảnh hưởng rate limit.
- API gửi form giới hạn tối đa **10 yêu cầu trong 10 phút** theo IP.
- API tra cứu giới hạn tối đa **20 lần trong 5 phút** theo IP.
- Form `/dat-lich` ban đầu tạo lịch hẹn; phiếu sửa chữa được sinh khi booking chuyển sang `KHACH_DA_DEN`.

## 6. Kiểm tra nhanh bằng API

### Đăng nhập Admin

```powershell
$body = @{ username = 'admin'; password = '123456' } | ConvertTo-Json
Invoke-RestMethod `
  -Method Post `
  -Uri 'http://localhost:5001/api/auth/login' `
  -ContentType 'application/json' `
  -Body $body
```

### Tra cứu phiếu

```powershell
Invoke-RestMethod `
  -Method Get `
  -Uri 'http://localhost:5001/api/public/track/RP-512806'
```

