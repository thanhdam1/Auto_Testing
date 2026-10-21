# BẢNG TỔNG HỢP LOCATORS THEO MODULE (SHOPNOVA MARKETPLACE)
> Tài liệu kỹ thuật định danh phần tử giao diện (UI Locators Specification) phục vụ viết kịch bản kiểm thử tự động (Automation Testing) với **Playwright**, **Selenium WebDriver**, và **Cypress**.

---

## 1. Module 1: Xác Thực & Tài Khoản (Authentication & Session)

### 1.1. Trang Đăng Nhập (`/dang-nhap`)

| Tên Phần Tử UI | Hành Động | Playwright Locator | Selenium (CSS Selector / XPath) | Cypress Locator | Kỳ Vọng Kiểm Thử |
|---|---|---|---|---|---|
| **Ô nhập Tên đăng nhập** | Fill / Type | `page.getByPlaceholder('minhanh / ankerseller / cocoonseller')` | `input[placeholder*='minhanh']`<br>`//input[contains(@placeholder, 'minhanh')]` | `cy.get("input[placeholder*='minhanh']")` | Nhập username hợp lệ/không hợp lệ |
| **Ô nhập Mật khẩu** | Fill / Type | `page.getByPlaceholder('Mật khẩu tài khoản')` | `input[type='password']`<br>`//input[@type='password']` | `cy.get("input[type='password']")` | Nhập mật khẩu tài khoản |
| **Nút Đăng Nhập** | Click | `page.getByRole('button', { name: 'Đăng Nhập' })` | `button[type='submit']`<br>`//button[@type='submit' and contains(., 'Đăng Nhập')]` | `cy.contains('button', 'Đăng Nhập')` | Gửi form đăng nhập lên Backend API |
| **Thông báo lỗi xác thực** | Assert Text | `page.locator('form > p.text-rose-600')` | `form p.text-rose-600`<br>`//form//p[contains(@class, 'text-rose-600')]` | `cy.get('form p.text-rose-600')` | Hiển thị lỗi `VALIDATION_ERROR` hoặc `INVALID_CREDENTIALS` |
| **Link chuyển sang Đăng ký** | Click | `page.getByRole('link', { name: 'Đăng ký tài khoản mới' })` | `a[href='/dang-ky']`<br>`//a[@href='/dang-ky']` | `cy.get("a[href='/dang-ky']")` | Chuyển hướng sang URL `/dang-ky` |
| **Tài khoản demo: minhanh** | Click | `page.locator('button:has-text("minhanh")')` | `//button[contains(., 'minhanh')]` | `cy.contains('button', 'minhanh')` | Tự động điền username `minhanh` vào form |
| **Tài khoản demo: ankerseller** | Click | `page.locator('button:has-text("ankerseller")')` | `//button[contains(., 'ankerseller')]` | `cy.contains('button', 'ankerseller')` | Tự động điền username người bán `ankerseller` |

---

### 1.2. Trang Đăng Ký (`/dang-ky`)

| Tên Phần Tử UI | Hành Động | Playwright Locator | Selenium (CSS Selector / XPath) | Cypress Locator | Kỳ Vọng Kiểm Thử |
|---|---|---|---|---|---|
| **Tab chọn vai trò: Người Mua** | Click | `page.getByRole('button', { name: 'Người Mua' })` | `//button[contains(.,'Người Mua') or contains(.,'buyer')]`<br>`(//div[contains(@class,'grid-cols-2')]//button)[1]` | `cy.contains('button', 'Người Mua')` | Chọn đăng ký tài khoản `buyer` |
| **Tab chọn vai trò: Người Bán** | Click | `page.getByRole('button', { name: 'Người Bán' })` | `//button[contains(.,'Người Bán') or contains(.,'seller')]`<br>`(//div[contains(@class,'grid-cols-2')]//button)[2]` | `cy.contains('button', 'Người Bán')` | Mở thêm các trường thông tin gian hàng |
| **Họ và tên** | Fill | `page.getByPlaceholder('Nguyễn Văn A')` | `input[placeholder='Nguyễn Văn A']`<br>`//input[@placeholder='Nguyễn Văn A']` | `cy.get("input[placeholder='Nguyễn Văn A']")` | Nhập họ tên đầy đủ (1 - 120 ký tự) |
| **Tên đăng nhập** | Fill | `page.getByPlaceholder('Chỉ gồm chữ, số, dấu chấm')` | `input[placeholder*='Chỉ gồm chữ']`<br>`//input[contains(@placeholder, 'Chỉ gồm chữ')]` | `cy.get("input[placeholder*='Chỉ gồm chữ']")` | Nhập tên đăng nhập chuẩn (3 - 50 ký tự) |
| **Mật khẩu** | Fill | `page.locator("input[type='password']").first()` | `(//input[@type='password'])[1]` | `cy.get("input[type='password']").first()` | Nhập mật khẩu (tối thiểu 6 ký tự) |
| **Nhập lại mật khẩu** | Fill | `page.locator("input[type='password']").nth(1)` | `(//input[@type='password'])[2]` | `cy.get("input[type='password']").last()` | Xác nhận mật khẩu khớp hoàn toàn |
| **Email (tùy chọn)** | Fill | `page.getByPlaceholder('example@email.com')` | `input[type='email']`<br>`//input[@type='email']` | `cy.get("input[type='email']")` | Nhập email hợp lệ |
| **Số điện thoại** | Fill | `page.getByPlaceholder('0901234567')` | `input[type='tel']`<br>`//input[@type='tel']` | `cy.get("input[type='tel']")` | Nhập số điện thoại di động |
| **Tên gian hàng (Seller)** | Fill | `page.getByPlaceholder('Shop Công Nghệ Số / Thời Trang...')` | `input[placeholder*='Shop Công Nghệ']`<br>`//input[contains(@placeholder, 'Shop Công Nghệ')]` | `cy.get("input[placeholder*='Shop Công Nghệ']")` | Bắt buộc khi đăng ký tài khoản seller |
| **Khẩu hiệu shop (Seller)** | Fill | `page.getByPlaceholder('Cam kết chính hãng 100%...')` | `input[placeholder*='Cam kết chính hãng']`<br>`//input[contains(@placeholder, 'Cam kết chính hãng')]` | `cy.get("input[placeholder*='Cam kết chính hãng']")` | Khẩu hiệu đại diện gian hàng |
| **Nút Đăng Ký Submit** | Click | `page.locator("button[type='submit']")` | `button[type='submit']`<br>`//button[@type='submit']` | `cy.get("button[type='submit']")` | Gửi thông tin đăng ký lên hệ thống |
| **Thông báo lỗi form đăng ký** | Assert Text | `page.locator('form > p.text-rose-600')` | `form p.text-rose-600`<br>`//form//p[contains(@class, 'text-rose-600')]` | `cy.get('form p.text-rose-600')` | Hiển thị cảnh báo lỗi nhập liệu hoặc trùng lặp |

---

### 1.3. Menu Tài Khoản trên Header (`AccountMenu`)

| Tên Phần Tử UI | Hành Động | Playwright Locator | Selenium (CSS Selector / XPath) | Cypress Locator | Kỳ Vọng Kiểm Thử |
|---|---|---|---|---|---|
| **Nút mở Menu Tài Khoản** | Click | `page.locator("button[aria-haspopup='menu']")` | `button[aria-haspopup='menu']`<br>`//button[@aria-haspopup='menu']` | `cy.get("button[aria-haspopup='menu']")` | Mở dropdown menu tài khoản |
| **Tên người dùng hiển thị** | Assert Text | `page.locator('button[aria-haspopup="menu"] span.text-ink')` | `button[aria-haspopup='menu'] span.text-ink`<br>`//button[@aria-haspopup='menu']//span[contains(@class, 'text-ink')]` | `cy.get('button[aria-haspopup="menu"] span.text-ink')` | Khớp với `fullName` của tài khoản đăng nhập |
| **Link "Đơn mua của tôi"** | Click | `page.getByRole('link', { name: 'Đơn mua của tôi' })` | `a[href='/don-hang']`<br>`//a[contains(., 'Đơn mua của tôi')]` | `cy.contains('a', 'Đơn mua của tôi')` | Điều hướng tới `/don-hang` |
| **Link "Kênh Người Bán"** | Click | `page.getByRole('link', { name: 'Kênh Người Bán' })` | `a[href='/nguoi-ban']`<br>`//a[contains(., 'Kênh Người Bán')]` | `cy.contains('a', 'Kênh Người Bán')` | Điều hướng tới `/nguoi-ban` |
| **Link "Tài khoản của tôi"** | Click | `page.getByRole('link', { name: 'Tài khoản của tôi' })` | `a[href='/tai-khoan']`<br>`//a[contains(., 'Tài khoản của tôi')]` | `cy.contains('a', 'Tài khoản của tôi')` | Điều hướng tới `/tai-khoan` |
| **Nút Đăng Xuất** | Click | `page.getByRole('button', { name: 'Đăng xuất' })` | `//div[@role='menu']//button[contains(.,'xuất') or contains(.,'Logout')]`<br>`//button[contains(.,'xuất') or contains(.,'Logout')]` | `cy.contains('button', 'Đăng xuất')` | Hủy phiên đăng nhập, xóa token |

---

## 2. Module 2: Điều Hướng, Header & Tìm Kiếm (Nav, Header & Search)

| Tên Phần Tử UI | Hành Động | Playwright Locator | Selenium (CSS Selector / XPath) | Cypress Locator | Kỳ Vọng Kiểm Thử |
|---|---|---|---|---|---|
| **Logo ShopNova** | Click | `page.getByRole('link', { name: 'ShopNova' })` | `a[href='/']:has(span.font-extrabold)`<br>`//a[@href='/' and contains(., 'ShopNova')]` | `cy.get("a[href='/']").first()` | Quay về trang chủ `/` |
| **Ô tìm kiếm trên Header** | Fill / Type | `page.getByLabel('Tìm kiếm sản phẩm')` | `input[aria-label='Tìm kiếm sản phẩm']`<br>`//input[@aria-label='Tìm kiếm sản phẩm']` | `cy.get("input[aria-label='Tìm kiếm sản phẩm']")` | Nhập từ khóa tìm kiếm |
| **Nút Tìm kiếm (Submit)** | Click | `page.locator("form button[type='submit']").last()` | `form button[type='submit']`<br>`//form//button[@type='submit' and contains(., 'Tìm kiếm')]` | `cy.get("form button[type='submit']").last()` | Thực thi tìm kiếm và chuyển hướng `/tim-kiem?q=...` |
| **Hộp gợi ý tìm kiếm (Popup)** | Assert Visible | `page.locator('div.shadow-pop')` | `div.shadow-pop`<br>`//div[contains(@class, 'shadow-pop')]` | `cy.get('div.shadow-pop')` | Xuất hiện khi focus hoặc gõ từ khóa |
| **Mục gợi ý sản phẩm đầu tiên** | Click | `page.locator('div.shadow-pop a[href^="/san-pham/"]').first()` | `(//div[contains(@class, 'shadow-pop')]//a[starts-with(@href, '/san-pham/')])[1]` | `cy.get('div.shadow-pop a[href^="/san-pham/"]').first()` | Điều hướng trực tiếp tới trang chi tiết SP |
| **Nút "Xem tất cả kết quả"** | Click | `page.getByRole('button', { name: /Xem tất cả kết quả cho/ })` | `//button[contains(., 'Xem tất cả kết quả')]` | `cy.contains('button', 'Xem tất cả kết quả')` | Chuyển sang trang kết quả tìm kiếm đầy đủ |
| **Icon Giỏ hàng** | Click | `page.locator("a[href='/gio-hang']")` | `a[href='/gio-hang']`<br>`//a[@href='/gio-hang']` | `cy.get("a[href='/gio-hang']")` | Điều hướng sang `/gio-hang` |
| **Badge số lượng trong Giỏ hàng** | Assert Text | `page.locator("a[href='/gio-hang'] span.bg-brand")` | `a[href='/gio-hang'] span.bg-brand`<br>`//a[@href='/gio-hang']//span[contains(@class, 'bg-brand')]` | `cy.get("a[href='/gio-hang'] span.bg-brand")` | Hiển thị số lượng sản phẩm hiện có |
| **Mục danh mục trên thanh Nav** | Click | `page.locator('nav a[href*="/tim-kiem?category="]').first()` | `(//nav//a[contains(@href, 'category=')])[1]` | `cy.get("nav a[href*='category=']").first()` | Lọc danh mục tương ứng |

---

## 3. Module 3: Bộ Lọc & Tìm Kiếm Nâng Cao (`/tim-kiem`)

| Tên Phần Tử UI | Hành Động | Playwright Locator | Selenium (CSS Selector / XPath) | Cypress Locator | Kỳ Vọng Kiểm Thử |
|---|---|---|---|---|---|
| **Lọc danh mục (Checkbox/Link)** | Click | `page.locator('aside a[href*="category="]').first()` | `(//aside//a[contains(@href, 'category=')])[1]` | `cy.get("aside a[href*='category=']").first()` | Lọc sản phẩm theo danh mục con |
| **Khoảng giá: Dưới 200.000₫ / 200k-500k** | Click | `page.locator('aside label:has-text("200.000₫")')` | `//aside//label[contains(., '200.000') or contains(., 'Dưới')]`<br>`aside label` | `cy.get('aside label').contains('200.000₫')` | Lọc theo các khoảng giá định sẵn |
| **Lọc Nơi bán: TP. Hồ Chí Minh** | Check / Click | `page.locator("label:has-text('TP. Hồ Chí Minh') input")` | `//label[contains(., 'TP. Hồ Chí Minh')]//input` | `cy.contains('label', 'TP. Hồ Chí Minh').find('input')` | Lọc sản phẩm có kho tại TP.HCM |
| **Lọc Nơi bán: Hà Nội** | Check / Click | `page.locator("label:has-text('Hà Nội') input")` | `//label[contains(., 'Hà Nội')]//input` | `cy.contains('label', 'Hà Nội').find('input')` | Lọc sản phẩm có kho tại Hà Nội |
| **Lọc Đánh giá: Từ 4 sao** | Click | `page.locator('button:has-text("từ 4 sao")')` | `//button[contains(., 'từ 4 sao')]` | `cy.contains('button', 'từ 4 sao')` | Lọc sản phẩm có rating >= 4.0 |
| **Nút "Xóa tất cả bộ lọc"** | Click | `page.getByRole('button', { name: 'Xoá tất cả' })` | `//button[contains(., 'Xoá tất cả')]` | `cy.contains('button', 'Xoá tất cả')` | Reset toàn bộ bộ lọc về mặc định |
| **Dropdown sắp xếp sản phẩm** | Select | `page.locator('select')` | `//select` | `cy.get('select').first()` | Chọn: Phổ biến, Mới nhất, Bán chạy, Giá tăng/giảm |
| **Card sản phẩm kết quả** | Click | `page.locator('a[href^="/san-pham/"]').first()` | `(//a[starts-with(@href, '/san-pham/')])[1]` | `cy.get("a[href^='/san-pham/']").first()` | Mở trang chi tiết sản phẩm |
| **Màn hình không có sản phẩm** | Assert Visible | `page.getByText('Không tìm thấy sản phẩm')` | `//h3[contains(., 'Không tìm thấy sản phẩm')]` | `cy.contains('Không tìm thấy sản phẩm')` | Hiển thị khi không có kết quả phù hợp |

---

## 4. Module 4: Chi Tiết Sản Phẩm & Mua Hàng (`/san-pham/:slug`)

| Tên Phần Tử UI | Hành Động | Playwright Locator | Selenium (CSS Selector / XPath) | Cypress Locator | Kỳ Vọng Kiểm Thử |
|---|---|---|---|---|---|
| **Tên sản phẩm chi tiết** | Assert Text | `page.locator('h1.text-ink')` | `h1.text-ink`<br>`//h1[contains(@class, 'text-ink')]` | `cy.get('h1.text-ink')` | Khớp tên sản phẩm |
| **Giá bán khuyến mãi** | Assert Text | `page.locator('div.bg-page span.text-brand')` | `div.bg-page span.text-brand`<br>`//div[contains(@class, 'bg-page')]//span[contains(@class, 'text-brand')]` | `cy.get('div.bg-page span.text-brand')` | Hiển thị giá bán hiện tại |
| **Giá gốc (gạch ngang)** | Assert Text | `page.locator('span.line-through')` | `span.line-through`<br>`//span[contains(@class, 'line-through')]` | `cy.get('span.line-through')` | Hiển thị giá niêm yết cũ |
| **Nút chọn Biến thể (Option button)**| Click | `page.locator('button.rounded-\\[2px\\]:has-text("Size M")')` | `//button[contains(@class, 'rounded-[2px]') and contains(., 'Size M')]` | `cy.contains('button', 'Size M')` | Chọn biến thể kích cỡ / màu sắc |
| **Nút Giảm số lượng (-)** | Click | `page.locator('button[aria-label="Giảm số lượng"]')` | `button[aria-label='Giảm số lượng']`<br>`//button[@aria-label='Giảm số lượng']` | `cy.get("button[aria-label='Giảm số lượng']")` | Giảm số lượng mua (tối thiểu 1) |
| **Nút Tăng số lượng (+)** | Click | `page.locator('button[aria-label="Tăng số lượng"]')` | `button[aria-label='Tăng số lượng']`<br>`//button[@aria-label='Tăng số lượng']` | `cy.get("button[aria-label='Tăng số lượng']")` | Tăng số lượng mua (tối đa tồn kho) |
| **Ô nhập số lượng** | Fill / Type | `page.locator('input[aria-label="Số lượng"]')` | `input[aria-label='Số lượng']`<br>`//input[@aria-label='Số lượng']` | `cy.get("input[aria-label='Số lượng']")` | Nhập số lượng mong muốn |
| **Nút "Thêm Vào Giỏ Hàng"** | Click | `page.getByRole('button', { name: 'Thêm Vào Giỏ Hàng' })` | `//button[contains(., 'Thêm Vào Giỏ Hàng')]` | `cy.contains('button', 'Thêm Vào Giỏ Hàng')` | Thêm vào giỏ và hiện thông báo Toast |
| **Nút "Mua Ngay"** | Click | `page.getByRole('button', { name: 'Mua Ngay' })` | `//button[contains(., 'Mua Ngay')]` | `cy.contains('button', 'Mua Ngay')` | Thêm vào giỏ và chuyển ngay tới `/gio-hang` |
| **Nút "Theo Dõi" Shop** | Click | `page.getByRole('button', { name: /Theo Dõi|Đang Theo Dõi/ })` | `//button[contains(., 'Theo Dõi') or contains(., 'Đang Theo Dõi')]` | `cy.contains('button', /Theo Dõi|Đang Theo Dõi/)` | Bật/tắt trạng thái theo dõi gian hàng |
| **Link "Xem Shop"** | Click | `page.getByRole('link', { name: 'Xem Shop' })` | `a[href^='/shop/']`<br>`//a[contains(., 'Xem Shop')]` | `cy.contains('a', 'Xem Shop')` | Mở trang hồ sơ gian hàng người bán |

---

## 5. Module 5: Giỏ Hàng Đa Shop (`/gio-hang`)

| Tên Phần Tử UI | Hành Động | Playwright Locator | Selenium (CSS Selector / XPath) | Cypress Locator | Kỳ Vọng Kiểm Thử |
|---|---|---|---|---|---|
| **Checkbox "Chọn Tất Cả"** | Click / Check | `page.getByText('Chọn Tất Cả').locator('..')` | `//span[contains(., 'Chọn Tất Cả')]/preceding-sibling::button` | `cy.contains('Chọn Tất Cả')` | Chọn/bỏ chọn toàn bộ sản phẩm |
| **Checkbox chọn theo từng Shop** | Click / Check | `page.locator('div.border-line button[role="checkbox"]').first()` | `(//div[contains(@class, 'border-line')]//button[@role='checkbox'])[1]` | `cy.get("button[role='checkbox']").first()` | Chọn toàn bộ sản phẩm thuộc shop đó |
| **Checkbox chọn sản phẩm đơn lẻ** | Click / Check | `page.locator('div[data-cart-item] button[role="checkbox"]')` | `//div[contains(@class, 'cart-item')]//button[@role='checkbox']` | `cy.get("button[role='checkbox']")` | Chọn/bỏ chọn sản phẩm cụ thể |
| **Nút Giảm số lượng trong giỏ** | Click | `page.locator('div[data-cart-item] button[aria-label="Giảm số lượng"]').first()` | `(//button[@aria-label='Giảm số lượng'])[1]` | `cy.get("button[aria-label='Giảm số lượng']").first()` | Giảm số lượng item trong giỏ |
| **Nút Tăng số lượng trong giỏ** | Click | `page.locator('div[data-cart-item] button[aria-label="Tăng số lượng"]').first()` | `(//button[@aria-label='Tăng số lượng'])[1]` | `cy.get("button[aria-label='Tăng số lượng']").first()` | Tăng số lượng item trong giỏ |
| **Nút "Xóa" sản phẩm đơn lẻ** | Click | `page.locator('button:has-text("Xoá")').first()` | `(//button[contains(., 'Xoá')])[1]` | `cy.contains('button', 'Xoá').first()` | Xóa 1 sản phẩm khỏi giỏ |
| **Nút "Xóa" các mục đã chọn** | Click | `page.locator('div.fixed.bottom-0 button:has-text("Xoá")')` | `//div[contains(@class, 'fixed')]//button[contains(., 'Xoá')]` | `cy.get('div.fixed button').contains('Xoá')` | Xóa hàng loạt sản phẩm được tích chọn |
| **Tổng tiền thanh toán** | Assert Text | `page.locator('div.fixed.bottom-0 span.text-brand')` | `//div[contains(@class, 'fixed')]//span[contains(@class, 'text-brand')]` | `cy.get('div.fixed span.text-brand')` | Cập nhật theo các sản phẩm được chọn |
| **Nút "Mua Hàng"** | Click | `page.getByRole('button', { name: 'Mua Hàng' })` | `//button[contains(., 'Mua Hàng')]` | `cy.contains('button', 'Mua Hàng')` | Chuyển sang trang đặt hàng `/dat-hang` |

---

## 6. Module 6: Đặt Hàng & Thanh Toán (`/dat-hang`)

| Tên Phần Tử UI | Hành Động | Playwright Locator | Selenium (CSS Selector / XPath) | Cypress Locator | Kỳ Vọng Kiểm Thử |
|---|---|---|---|---|---|
| **Nút "Thay Đổi" Địa Chỉ** | Click | `page.getByRole('button', { name: 'Thay Đổi' })` | `//button[contains(., 'Thay Đổi')]` | `cy.contains('button', 'Thay Đổi')` | Mở form chỉnh sửa địa chỉ giao hàng |
| **Ô nhập Họ tên người nhận** | Fill | `page.getByPlaceholder('Họ và tên người nhận')` | `input[placeholder*='Họ và tên người nhận']` | `cy.get("input[placeholder*='Họ và tên người nhận']")` | Nhập tên người nhận hàng |
| **Ô nhập Số điện thoại nhận** | Fill | `page.getByPlaceholder('Số điện thoại')` | `input[placeholder*='Số điện thoại']` | `cy.get("input[placeholder*='Số điện thoại']")` | Nhập SĐT người nhận |
| **Dropdown Tỉnh / Thành phố** | Select | `page.locator('select:has-text("Chọn Tỉnh / Thành phố")')` | `//select[option[contains(., 'Chọn Tỉnh')]]` | `cy.get('select').first()` | Chọn tỉnh thành giao hàng |
| **Ô nhập Quận / Huyện** | Fill | `page.getByPlaceholder('Quận / Huyện')` | `input[placeholder*='Quận / Huyện']` | `cy.get("input[placeholder*='Quận / Huyện']")` | Nhập quận huyện |
| **Ô nhập Phường / Xã** | Fill | `page.getByPlaceholder('Phường / Xã')` | `input[placeholder*='Phường / Xã']` | `cy.get("input[placeholder*='Phường / Xã']")` | Nhập phường xã |
| **Ô nhập Địa chỉ chi tiết** | Fill | `page.getByPlaceholder('Địa chỉ chi tiết (số nhà, đường)')` | `input[placeholder*='Địa chỉ chi tiết']` | `cy.get("input[placeholder*='Địa chỉ chi tiết']")` | Số nhà, tên đường |
| **Nút "Lưu địa chỉ"** | Click | `page.getByRole('button', { name: 'Lưu địa chỉ' })` | `//button[contains(., 'Lưu địa chỉ')]` | `cy.contains('button', 'Lưu địa chỉ')` | Lưu địa chỉ giao hàng |
| **Nút chọn Voucher NOVA20K** | Click | `page.locator('button:has-text("NOVA20K")')` | `//button[contains(., 'NOVA20K')]` | `cy.contains('button', 'NOVA20K')` | Áp dụng giảm 20.000₫ đơn từ 500k |
| **Nút chọn Voucher NOVA50K** | Click | `page.locator('button:has-text("NOVA50K")')` | `//button[contains(., 'NOVA50K')]` | `cy.contains('button', 'NOVA50K')` | Áp dụng giảm 50.000₫ đơn từ 1.5M |
| **Phương thức Thanh toán COD** | Click | `page.locator('button:has-text("Thanh toán khi nhận hàng")')` | `//button[contains(., 'Thanh toán khi nhận hàng')]` | `cy.contains('Thanh toán khi nhận hàng')` | Chọn COD |
| **Phương thức Chuyển khoản QR** | Click | `page.locator('button:has-text("Chuyển khoản ngân hàng")')` | `//button[contains(., 'Chuyển khoản ngân hàng')]` | `cy.contains('Chuyển khoản ngân hàng')` | Chọn Bank transfer |
| **Phương thức Ví điện tử** | Click | `page.locator('button:has-text("Ví điện tử")')` | `//button[contains(., 'Ví điện tử')]` | `cy.contains('Ví điện tử')` | Chọn E-wallet |
| **Lời nhắn cho người bán** | Fill | `page.getByPlaceholder('Ví dụ: giao giờ hành chính...')` | `textarea[placeholder*='giao giờ hành chính']` | `cy.get("textarea[placeholder*='giao giờ hành chính']")` | Nhập ghi chú giao hàng |
| **Tổng thanh toán cuối cùng** | Assert Text | `page.locator('div.fixed.bottom-0 span.text-brand')` | `//div[contains(@class, 'fixed')]//span[contains(@class, 'text-brand')]` | `cy.get('div.fixed span.text-brand')` | Đã trừ voucher và cộng phí ship |
| **Nút "Đặt Hàng" Submit** | Click | `page.getByRole('button', { name: 'Đặt Hàng' })` | `//button[contains(., 'Đặt Hàng')]` | `cy.contains('button', 'Đặt Hàng')` | Tạo đơn hàng thành công |

---

## 7. Module 7: Quản Lý Đơn Mua (`/don-hang`)

| Tên Phần Tử UI | Hành Động | Playwright Locator | Selenium (CSS Selector / XPath) | Cypress Locator | Kỳ Vọng Kiểm Thử |
|---|---|---|---|---|---|
| **Tab "Tất Cả"** | Click | `page.getByRole('button', { name: 'Tất cả' })` | `//button[contains(., 'Tất cả')]` | `cy.contains('button', 'Tất cả')` | Hiển thị toàn bộ lịch sử đơn |
| **Tab "Chờ xác nhận"** | Click | `page.getByRole('button', { name: 'Chờ xác nhận' })` | `//button[contains(., 'Chờ xác nhận')]` | `cy.contains('button', 'Chờ xác nhận')` | Lọc các đơn đang chờ xử lý |
| **Tab "Đang giao"** | Click | `page.getByRole('button', { name: 'Đang giao' })` | `//button[contains(., 'Đang giao')]` | `cy.contains('button', 'Đang giao')` | Lọc đơn shipper đang giao |
| **Tab "Đã giao"** | Click | `page.getByRole('button', { name: 'Đã giao' })` | `//button[contains(., 'Đã giao')]` | `cy.contains('button', 'Đã giao')` | Lọc đơn hoàn tất thành công |
| **Tab "Đã hủy"** | Click | `page.getByRole('button', { name: 'Đã hủy' })` | `//button[contains(., 'Đã hủy')]` | `cy.contains('button', 'Đã hủy')` | Lọc đơn bị hủy |
| **Nút "Hủy Đơn Hàng"** | Click | `page.locator('button:has-text("Hủy đơn hàng")').first()` | `(//button[contains(., 'Hủy đơn hàng')])[1]` | `cy.contains('button', 'Hủy đơn hàng').first()` | Khách tự hủy đơn khi đang chờ xác nhận |
| **Nút "Mua Lại"** | Click | `page.locator('button:has-text("Mua lại")').first()` | `(//button[contains(., 'Mua lại')])[1]` | `cy.contains('button', 'Mua lại').first()` | Thêm lại các món của đơn vào giỏ |
| **Link "Xem Chi Tiết"** | Click | `page.locator('a:has-text("Xem chi tiết")').first()` | `(//a[contains(., 'Xem chi tiết')])[1]` | `cy.contains('a', 'Xem Chi Tiết').first()` | Xem trang chi tiết đơn hàng |

---

## 8. Module 8: Kênh Người Bán (`/nguoi-ban/*`)

| Tên Phần Tử UI | Hành Động | Playwright Locator | Selenium (CSS Selector / XPath) | Cypress Locator | Kỳ Vọng Kiểm Thử |
|---|---|---|---|---|---|
| **Sidebar: Tổng Quan** | Click | `page.getByRole('link', { name: 'Tổng quan' })` | `a[href='/nguoi-ban']`<br>`//a[@href='/nguoi-ban' and contains(., 'Tổng quan')]` | `cy.contains('a', 'Tổng quan')` | Điều hướng Dashboard người bán |
| **Sidebar: Quản Lý Sản Phẩm** | Click | `page.getByRole('link', { name: 'Sản phẩm' })` | `a[href='/nguoi-ban/san-pham']`<br>`//a[@href='/nguoi-ban/san-pham']` | `cy.contains('a', 'Sản phẩm')` | Xem danh sách sản phẩm của gian hàng |
| **Sidebar: Đơn Hàng Shop** | Click | `page.getByRole('link', { name: 'Đơn hàng' })` | `a[href='/nguoi-ban/don-hang']`<br>`//a[@href='/nguoi-ban/don-hang']` | `cy.contains('a', 'Đơn hàng')` | Quản lý đơn hàng khách đặt của shop |
| **Sidebar: Cài Đặt Gian Hàng** | Click | `page.getByRole('link', { name: 'Gian hàng' })` | `a[href='/nguoi-ban/gian-hang']`<br>`//a[@href='/nguoi-ban/gian-hang']` | `cy.contains('a', 'Gian hàng')` | Cập nhật thông tin gian hàng |
| **Nút "Thêm Sản Phẩm Mới"** | Click | `page.getByRole('link', { name: 'Thêm sản phẩm mới' })` | `a[href='/nguoi-ban/san-pham/them-moi']`<br>`//a[contains(., 'Thêm sản phẩm mới')]` | `cy.contains('a', 'Thêm sản phẩm mới')` | Mở form đăng sản phẩm mới |
| **Form SP: Tên sản phẩm** | Fill | `page.getByPlaceholder('Nhập tên sản phẩm đầy đủ...')` | `input[placeholder*='Nhập tên sản phẩm']` | `cy.get("input[placeholder*='Nhập tên sản phẩm']")` | Nhập tên sản phẩm (10 - 255 ký tự) |
| **Form SP: Chọn Danh mục** | Select | `page.locator('select')` | `//select[option[contains(., 'Thời trang')]]` | `cy.get('select').first()` | Chọn ngành hàng phù hợp |
| **Form SP: Giá bán** | Fill | `page.getByPlaceholder('Ví dụ: 150000')` | `input[placeholder*='150000']` | `cy.get("input[placeholder*='150000']")` | Nhập giá bán VND |
| **Form SP: Giá gốc (niêm yết)**| Fill | `page.getByPlaceholder('Ví dụ: 200000')` | `input[placeholder*='200000']` | `cy.get("input[placeholder*='200000']")` | Nhập giá gốc để sinh % giảm |
| **Form SP: Số lượng kho** | Fill | `page.getByPlaceholder('Ví dụ: 100')` | `input[placeholder*='100']` | `cy.get("input[placeholder*='100']")` | Nhập số lượng tồn kho |
| **Form SP: Phân loại / Biến thể**| Fill | `page.getByPlaceholder('Size M, Size L, Màu Đen')` | `input[placeholder*='Size M']`<br>`//input[contains(@placeholder, 'Size')]` | `cy.get("input[placeholder*='Size M']")` | Nhập các biến thể cách nhau bởi dấu phẩy |
| **Form SP: Mô tả chi tiết** | Fill | `page.getByPlaceholder('Mô tả đặc điểm nổi bật...')` | `textarea[placeholder*='Mô tả đặc điểm']` | `cy.get("textarea[placeholder*='Mô tả đặc điểm']")` | Nhập nội dung mô tả sản phẩm |
| **Form SP: Checkbox Freeship** | Check | `page.locator('label:has-text("Miễn phí vận chuyển") input')` | `//label[contains(., 'Miễn phí vận chuyển')]//input` | `cy.contains('label', 'Miễn phí vận chuyển').find('input')` | Gán nhãn Free shipping |
| **Form SP: Nút "Lưu Sản Phẩm"** | Click | `page.getByRole('button', { name: 'Lưu sản phẩm' })` | `//button[contains(., 'Lưu sản phẩm')]` | `cy.contains('button', 'Lưu sản phẩm')` | Lưu sản phẩm vào CSDL |
| **Đơn hàng: Nút "Xác nhận & Giao"**| Click | `page.locator('button:has-text("Xác nhận & Giao hàng")').first()` | `(//button[contains(., 'Xác nhận & Giao hàng')])[1]` | `cy.contains('button', 'Xác nhận & Giao hàng').first()` | Chuyển đơn sang trạng thái `shipping` |

---

## 9. Code Mẫu Kiểm Thử Tự Động (Playwright Code Example)

```javascript
import { test, expect } from '@playwright/test';

test.describe('E2E Flow: Đăng nhập & Mua hàng', () => {
  test('Người mua đăng nhập, thêm sản phẩm vào giỏ và đặt hàng', async ({ page }) => {
    // 1. Đi tới trang đăng nhập
    await page.goto('http://localhost:5173/dang-nhap');
    
    // 2. Điền thông tin tài khoản (tham chiếu TEST_DATA.md)
    await page.getByPlaceholder('minhanh / ankerseller / cocoonseller').fill('minhanh');
    await page.getByPlaceholder('Mật khẩu tài khoản').fill('123456');
    await page.getByRole('button', { name: 'Đăng Nhập' }).click();

    // 3. Xác nhận đăng nhập thành công
    await expect(page).toHaveURL('http://localhost:5173/');
    await expect(page.locator("button[aria-haspopup='menu']")).toBeVisible();

    // 4. Tìm kiếm sản phẩm
    await page.getByLabel('Tìm kiếm sản phẩm').fill('áo thun');
    await page.locator("form button[type='submit']").last().click();

    // 5. Chọn sản phẩm đầu tiên
    await page.locator('a[href^="/san-pham/"]').first().click();

    // 6. Thêm vào giỏ hàng
    await page.getByRole('button', { name: 'Thêm Vào Giỏ Hàng' }).click();
    await expect(page.locator('.toast')).toContainText('Đã thêm');

    // 7. Đi tới giỏ hàng & thanh toán
    await page.locator("a[href='/gio-hang']").click();
    await page.getByRole('button', { name: 'Mua Hàng' }).click();

    // 8. Đặt hàng tại trang Checkout
    await page.getByRole('button', { name: 'Đặt Hàng' }).click();
    await expect(page).toHaveURL(/\/dat-hang-thanh-cong\//);
  });
});
```

---

## 10. Tài Liệu Tham Chiếu Dữ Liệu Test
- File dữ liệu kiểm thử hoàn chỉnh: [TEST_DATA.md](file:///c:/Users/PC/Downloads/nova_shop/TEST_DATA.md)
