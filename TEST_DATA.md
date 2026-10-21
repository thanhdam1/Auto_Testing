# SHOPNOVA — BỘ DỮ LIỆU KIỂM THỬ MẪU (TEST DATA SPECIFICATION)
> Tài liệu dữ liệu mẫu phục vụ kiểm thử tự động (Automation Testing) và kiểm thử thủ công (Manual Testing) cho hệ thống Sàn thương mại điện tử đa người bán ShopNova.

---

## 1. Danh Sách Tài Khoản Kiểm Thử (Authentication Credentials)

> Tất cả mật khẩu mặc định của môi trường phát triển cục bộ đã được đồng bộ chuẩn hóa: `123456` (hoặc mật khẩu chuyên biệt cho integration test nếu có ghi chú).

### 1.1. Tài khoản Người Mua (Buyer Persona)

| ID | Tên đăng nhập (`username`) | Mật khẩu (`password`) | Họ và tên (`fullName`) | Vai trò (`role`) | Trạng thái | Ghi chú & Mục đích test |
|---|---|---|---|---|---|---|
| `u_1` | `minhanh` | `123456` | Nguyễn Minh Anh | `buyer` | `active` | Tài khoản mua hàng chính, hạng Vàng, có sẵn địa chỉ TP.HCM |
| `u_2` | `quockhanh` | `123456` | Trần Quốc Khánh | `buyer` | `active` | Tài khoản mua hàng phụ, hạng Bạc, địa chỉ Hà Nội |
| `u_3` | `demo_buyer` | `123456` | Người mua mẫu | `buyer` | `active` | Tài khoản buyer khởi tạo từ MySQL seed |

### 1.2. Tài khoản Người Bán (Seller / Shop Owner Persona)

| ID | Tên đăng nhập (`username`) | Mật khẩu (`password`) | Họ và tên (`fullName`) | Tên gian hàng (`shopName`) | Slug gian hàng | Trạng thái |
|---|---|---|---|---|---|---|
| `s_1` | `ankerseller` | `123456` | Anker Official Store | Anker Official | `anker-official` | Người bán Mall, ngành Điện thoại & Phụ kiện |
| `s_2` | `cocoonseller` | `123456` | Cocoon Việt Nam | Cocoon Official | `cocoon-official` | Người bán uy tín, ngành Chăm sóc & Sắc đẹp |
| `s_3` | `demo_seller_a` | `123456` | Người bán mẫu A | Shop mẫu A | `shop-mau-a` | Seller khởi tạo từ seed, có sẵn sản phẩm mẫu |
| `s_4` | `demo_seller_b` | `123456` | Người bán mẫu B | Shop mẫu B | `shop-mau-b` | Seller khởi tạo từ seed, kho tại Hà Nội |

### 1.3. Tài khoản Kiểm Thử Biên & Ngoại Lệ (Negative / Edge Cases)

| Kịch bản kiểm thử | Tên đăng nhập | Mật khẩu | Mã lỗi mong đợi | Ý nghĩa kiểm thử |
|---|---|---|---|---|
| Mật khẩu sai | `minhanh` | `WrongPass@999` | `INVALID_CREDENTIALS` (401) | Báo lỗi tên đăng nhập hoặc mật khẩu |
| Tài khoản không tồn tại | `non_existing_user_xyz` | `123456` | `INVALID_CREDENTIALS` (401) | Không để lộ username có tồn tại hay không |
| Tài khoản bị khóa | `test_blocked_be003a` | `BlockedPass123!@#` | `ACCOUNT_BLOCKED` (403) | Chặn đăng nhập với user có status = 'blocked' |
| Chữ hoa thường | `MinhAnh` hoặc `MINHANH` | `123456` | Thành công (200) | Hệ thống tự lowercase username |
| Khoảng trắng thừa | `  minhanh  ` | `123456` | Thành công (200) | Hệ thống tự trim khoảng trắng đầu cuối |
| Mật khẩu vượt 72 byte | `minhanh` | 73 ký tự `a` | `INVALID_CREDENTIALS` (401) | Chống tấn công DoS thuật toán Bcrypt |

---

## 2. Bộ Dữ Liệu Đăng Ký Tài Khoản (Registration Test Matrix)

### 2.1. Đăng ký Người Mua (Buyer Registration)

| Trường dữ liệu | Giá trị hợp lệ (Happy Path) | Giá trị không hợp lệ (Negative) | Giới hạn & Quy chuẩn |
|---|---|---|---|
| `fullName` | `Trần Hoàng Nam` | Rỗng `""`, hoặc chuỗi dài > 120 ký tự | Bắt buộc, 1-120 ký tự |
| `username` | `hoangnam2026` | `hn`, `user@!#$`, chuỗi dài > 50 ký tự | Bắt buộc, 3-50 ký tự, `[a-zA-Z0-9_.-]` |
| `password` | `MatKhauAnToan123!` | `12345` (< 6 ký tự) | Tối thiểu 6 ký tự, tối đa 72 bytes |
| `confirmPassword` | `MatKhauAnToan123!` | `KhacMatKhau123!` | Phải khớp 100% với `password` |
| `email` | `hoangnam.tran@gmail.com` | `hoangnam@`, `chua-co-domain` | Định dạng email hợp lệ nếu có nhập |
| `phone` | `0988776655` | `abc1234`, `090` (< 10 số) | Chuỗi số điện thoại 10 số |
| `role` | `buyer` | `admin`, `guest` | Giá trị hợp lệ: `buyer` hoặc `seller` |

### 2.2. Đăng ký Người Bán (Seller Registration)

| Trường dữ liệu | Giá trị mẫu | Lưu ý nghiệp vụ |
|---|---|---|
| `role` | `seller` | Khi chọn seller, form bắt buộc mở thêm mục Gian hàng |
| `shopName` | `Gia Dụng Thông Minh Nam Anh` | Bắt buộc đối với người bán, tối đa 160 ký tự |
| `shopTagline` | `Chuyên đồ gia dụng chính hãng giá tốt` | Khẩu hiệu đại diện gian hàng (tùy chọn) |

---

## 3. Bộ Dữ Liệu Địa Chỉ Nhận Hàng & Thanh Toán (Checkout Test Data)

### 3.1. Địa Chỉ Nhận Hàng (Shipping Address)

| Kịch bản | Họ và tên | Số điện thoại | Tỉnh / Thành phố | Quận / Huyện | Phường / Xã | Địa chỉ chi tiết |
|---|---|---|---|---|---|---|
| **TP. Hồ Chí Minh (Chuẩn)** | Lê Văn Thịnh | `0918234567` | TP. Hồ Chí Minh | Quận 1 | Phường Bến Nghé | 45 Lê Duẩn, Tòa nhà Diamond Plaza |
| **Hà Nội (Chuẩn)** | Phạm Thu Trang | `0987654321` | Hà Nội | Quận Cầu Giấy | Phường Dịch Vọng | 88 Cầu Giấy, Tầng 5 Chung cư Sky |
| **Đà Nẵng** | Nguyễn Văn Hùng | `0905112233` | Đà Nẵng | Quận Hải Châu | Phường Thạch Thang | 12 Bạch Đằng |
| **Biên: Số điện thoại lỗi** | Lê Văn A | `0123` | Hà Nội | Quận Hoàn Kiếm | Phường Tràng Tiền | 1 Đinh Tiên Hoàng |
| **Biên: Thiếu địa chỉ chi tiết** | Trần B | `0911222333` | Cần Thơ | Ninh Kiều | Tân An | `""` (Bỏ trống) |

### 3.2. Mã Giảm Giá / Voucher Mẫu

| Mã Voucher (`code`) | Tên hiển thị | Giá trị giảm | Đơn tối thiểu (`minSubtotal`) | Trạng thái kiểm thử |
|---|---|---|---|---|
| `NOVA20K` | Giảm 20.000₫ đơn từ 500k | `20,000 VND` | `500,000 VND` | Khả dụng (Áp dụng cho đơn >= 500k) |
| `NOVA50K` | Giảm 50.000₫ đơn từ 1.5M | `50,000 VND` | `1,500,000 VND` | Khả dụng (Áp dụng cho đơn >= 1.500.000₫) |
| `VOUCHER_EXPIRED` | Mã hết hạn | `30,000 VND` | `300,000 VND` | Không hợp lệ (Hệ thống từ chối) |
| `VOUCHER_INVALID` | `MABATKY99` | — | — | Mã không tồn tại |

### 3.3. Phương Thức Thanh Toán (Payment Methods)

| Mã phương thức (`id`) | Tên phương thức | Ghi chú kịch bản |
|---|---|---|
| `cod` | Thanh toán khi nhận hàng (COD) | Mặc định chọn sẵn, không yêu cầu cổng thanh toán |
| `bank` | Chuyển khoản ngân hàng (QR Code) | Hiển thị thông tin STK chuyển khoản |
| `wallet` | Ví điện tử (ShopeePay / MoMo) | Chuyển tiếp quét mã ví |

---

## 4. Bộ Dữ Liệu Tìm Kiếm & Lọc Sản Phẩm (Search & Catalog Filter)

| Từ khóa tìm kiếm (`q`) | Bộ lọc Danh mục (`categoryId`) | Khoảng giá (`priceRange`) | Nơi bán (`location`) | Đánh giá (`rating`) | Kết quả mong đợi |
|---|---|---|---|---|---|
| `"áo thun"` | `thoi-trang-nam` | `100000 - 300000` | `TP. Hồ Chí Minh` | `4 sao trở lên` | Trả về danh sách áo thun nam |
| `"củ sạc"` | `dien-thoai` | `200000 - 500000` | `Hà Nội` | `Tất cả` | Trả về củ sạc đa năng |
| `"anker"` | Tất cả | Tất cả | Tất cả | Tất cả | Trả về sản phẩm thuộc Anker Official |
| `"từ_khóa_không_tồn_tại_12345"` | Tất cả | Tất cả | Tất cả | Tất cả | Hiển thị màn hình `EmptyState` (Không tìm thấy sản phẩm) |

---

## 5. Bộ Dữ Liệu Kênh Người Bán (Seller Hub Management)

### 5.1. Dữ liệu Thêm / Sửa Sản Phẩm

| Trường dữ liệu | Sản phẩm mẫu 1 (Thời trang) | Sản phẩm mẫu 2 (Công nghệ) | Quy chuẩn |
|---|---|---|---|
| `name` | Áo Polo Nam Phối Cổ Trắng Cao Cấp | Cáp Sạc Nhanh Type-C to Lightning 20W | Tối thiểu 10 ký tự, tối đa 255 ký tự |
| `categoryId` | `thoi-trang-nam` | `dien-thoai` | Bắt buộc chọn từ danh mục hệ thống |
| `price` | `250000` | `180000` | Số nguyên > 0 |
| `originalPrice` | `350000` | `250000` | Lớn hơn hoặc bằng `price` (để sinh % giảm giá) |
| `stock` | `50` | `200` | Tồn kho khởi tạo >= 0 |
| `variants` | `Trắng - M, Trắng - L, Đen - M, Đen - L` | `1 Mét, 2 Mét` | Phân cách bằng dấu phẩy |
| `tags` | `["freeship", "mall"]` | `["freeship", "extra"]` | Checkbox chọn nhãn tiếp thị |
| `description` | Vải cotton cá sấu 100% co giãn thoáng mát | Chuẩn MFi chứng nhận an toàn, sạc nhanh 50% trong 30 phút | Mô tả chi tiết sản phẩm |

### 5.2. Chuyển Trạng Thái Đơn Hàng (Order State Transitions)

| Trạng thái ban đầu | Trạng thái kế tiếp | Hành động người bán |
|---|---|---|
| `pending` (Chờ xác nhận) | `shipping` (Đang giao hàng) | Nhấn "Xác nhận & Giao đơn" |
| `shipping` (Đang giao hàng) | `delivered` (Giao thành công) | Đơn vị vận chuyển báo giao thành công |
| `pending` (Chờ xác nhận) | `cancelled` (Đã hủy) | Nhấn "Hủy đơn hàng" do hết hàng |
