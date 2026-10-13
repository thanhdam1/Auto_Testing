# TEST PLAN BỔ SUNG — 3 DỰ ÁN AUTOMATION

> Sinh theo skill **`qa_automation_engineer`** + workflow **`generate_application_test_plan`** (Mode PLAN).
> Ngày phân tích: lượt làm việc hiện tại. Người thực hiện: QA Automation Engineer (AI agent).
> Nguồn dữ liệu: đọc trực tiếp mã nguồn framework (`src/`), DOM contract và môi trường chạy của Project 1, 6 file TestNG suite, log & báo cáo trong `reports/`, `logs/automation.log`, `target/surefire-reports/`.

---

## 1. TỔNG QUAN 3 DỰ ÁN

| # | Dự án | Loại | URL cấu hình (`config.properties`) | Tech stack nhận diện được | Trạng thái app khi phân tích |
|:-:|:---|:---|:---|:---|:---|
| 1 | **Nội Thất & Phong Thủy E-Commerce** | Web TMĐT (storefront + admin) | `https://86h0mht3-4321.asse.devtunnels.ms` | Astro + Tailwind, API Node/Express + Prisma (`localhost:4000/api`), cart dùng `localStorage` | ❌ Không truy cập được (devtunnel đứt kết nối) |
| 2 | **ShopNova Marketplace** | Web sàn TMĐT đa người bán | `http://localhost:5173` (`/nguoi-ban` cho seller) | SPA (Vite) — không có source trong repo | ❌ Port 5173 không mở |
| 3 | **QR Repair Ticket System (HungHau IT Support)** | Web tiếp nhận & tra cứu sửa chữa | `http://localhost:5174` (`/login` cho KTV) | SPA — không có source trong repo | ❌ Port 5174 không mở |

**Bằng chứng hiện trạng:** `target/surefire-reports/testng-results.xml` của lần chạy gần nhất (10/10) ghi `total="15" passed="0" failed="0" skipped="15"`; log cho thấy toàn bộ 15 test bị **SKIP** do `@BeforeMethod` không mở được URL (`TimeoutException: Timed out receiving message from renderer`).

---

## 2. HIỆN TRẠNG COVERAGE

| Dự án | Suite | Test class | Số test | Phạm vi đang phủ |
|:--|:--|:--|:-:|:--|
| 1 | `testng.xml`, `testng-smoke.xml`, `testng-parallel.xml` | AdminProductManagement, AdminOrderManagement, StorefrontAuth, StorefrontCatalog | **15** | Admin: sản phẩm (list/search/filter/mở modal/thêm), đơn hàng (list/search/filter). Storefront: login UI + validate rỗng + Google login, catalog (đếm, filter sofa, sort) |
| 2 | `testng-novashop.xml` | NovaAuth, NovaProductSearch, NovaSellerManagement | **10** | Login (5 case), tìm kiếm & filter & sort, seller dashboard + search sản phẩm |
| 3 | `testng-repair.xml` | RepairNavigation, RepairTracking, RepairBooking, RepairAuth | **10** | Điều hướng trang chủ, tra cứu theo SĐT, điền form đặt lịch, login sai |
| | `testng-all.xml` | (gộp cả 3 dự án) | 35 | — |
| **TỔNG** | | **11 file** | **35** | |

---

## 3. ⚠️ CÁC VẤN ĐỀ PHÁT HIỆN ĐƯỢC (phải xử lý trước khi viết thêm test)

| # | Vấn đề | Bằng chứng | Ảnh hưởng |
|:-:|:---|:---|:---|
| B1 | **Cả 3 app đều không chạy** → không thể verify bất kỳ test nào | Log 10/10: 15/15 SKIP; probe hiện tại: devtunnel lỗi kết nối, port 5173/5174/4000 đều closed | Mọi test mới viết ra đều "mù", không chạy được |
| B2 | **6 test không có assertion nào** (chỉ click rồi log) | `NovaAuthTest.testBuyerLoginSuccess`, `testSellerLoginRedirect`, `RepairTrackingTest.testSearchBySamplePhone`, `RepairBookingTest.testFillBookingForm`, `AdminProductManagementTest.testAddNewProductForm`, `testToggleProductStatus` | Test luôn PASS dù chức năng hỏng → false green |
| B3 | **Nhiều assertion quá yếu** | `AdminProductManagementTest.testFilterByCategoryAndReset`, `testSortProducts` (chỉ `count > 0`), `NovaProductSearchTest.testFilterProductsByCategory` (không assert gì) | Không bắt được regression logic |
| B4 | **Page Object viết rồi nhưng chưa có test nào dùng** | `CartPage.java`, `ProductDetailPage.java` (git: **untracked**), `RepairAdminDashboardPage.java` (0 tham chiếu) | Đã có sẵn locator nhưng 3 module lớn chưa được test: **Giỏ hàng, Chi tiết SP, Dashboard KTV** |
| B5 | **Không có test API** dù backend có sẵn 12 endpoint | `pom.xml` không có REST Assured/JsonPath; backend Express có `/api/auth/login|register|customers`, `/api/products` (GET/POST/PUT/DELETE), `/api/products/categories/all`, `/api/orders` (POST/GET), `/api/orders/:code`, `/health` | Toàn bộ tầng API không được kiểm thử |
| B6 | **Test data hard-code + không dọn dẹp dữ liệu** | `"Sofa da Ý"`, `"NT-202610-001"`, `"0901234567"`, `"Bàn Trà Bắc Âu Luxury"` nằm trong test; test thêm sản phẩm/đặt lịch/đặt hàng không teardown | Chạy lần 2 dễ fail (dữ liệu rác), không chạy song song an toàn |
| B7 | **Code chết**: `TestDataProviders` (data SauceDemo/Automation Exercise) và `DataFakerUtil` (0 lượt dùng) | Grep: `DataProvider` không xuất hiện trong test nào; `DataFakerUtil` = 0 usage | Sai lệch tài liệu (README/BAO_CAO vẫn mô tả SauceDemo) |
| B8 | **CI chỉ chạy Project 1** | `.github/workflows/regression-test.yml` → `-DsuiteFile=testng.xml` | Project 2 & 3 không được bảo vệ hồi quy |
| B9 | **Không fail-fast khi môi trường chết** | Mỗi test chờ ~30–240s rồi SKIP; suite 15 test tốn >10 phút vô ích | Lãng phí thời gian CI, báo cáo nhiễu |
| B10 | **Không có retry cho test flaky** | Không có `IRetryAnalyzer` nào trong `src/test` | Devtunnel/page chậm → fail giả |
| B11 | `testng-parallel.xml` chỉ chạy 2 class của Project 1 | — | Chưa tận dụng `ThreadLocal<WebDriver>` đã xây |

---

## 4. TEST ĐỀ XUẤT BỔ SUNG — PROJECT 1 (NỘI THẤT)

> Project 1 có lợi thế lớn: **có source frontend trong repo** nên locator lấy trực tiếp từ DOM thật (không đoán).
> Hiện tại mới phủ ~15/60+ chức năng. Đề xuất thêm **42 test**.

### 4.1. Giỏ hàng — `/gio-hang` (POM `CartPage` đã có sẵn, chưa dùng) 🔴 P1

| ID | Scenario | Loại | Locator / ghi chú |
|:--|:---|:---|:---|
| NT-C01 | Giỏ trống lần đầu: hiện `[data-cart-empty]`, ẩn `[data-cart-content]` | Happy | `isCartEmpty()` |
| NT-C02 | Thêm 1 SP từ trang chi tiết → badge header `[data-cart-count]`=1, giỏ có 1 dòng | Happy | `data-row`, `data-cart-items` |
| NT-C03 | Thêm cùng SP 2 lần → gộp dòng, qty=2 (không tạo dòng mới) | Edge | Logic `addToCart` gộp theo `slug` |
| NT-C04 | Nút `[data-row-up]` tăng qty, `[data-row-down]` giảm qty → input value đổi đúng | Happy | `getItemQuantity()` |
| NT-C05 | Biên số lượng: giảm tại qty=1 không xuống 0; tăng không vượt **99** | Boundary | `cart.ts` clamp `min 99 / max 1` |
| NT-C06 | `[data-row-remove]` xoá 1 dòng → số dòng giảm, subtotal cập nhật | Happy | `removeItem()` |
| NT-C07 | `[data-cart-clear]` + accept confirm → giỏ trống hoàn toàn | Happy | `clearCart()` |
| NT-C08 | **Toán học**: `[data-cart-subtotal]` = Σ(price × qty) sau khi parse VND; `[data-cart-total]` ≥ subtotal | Data | Parse `1.234.567 đ` |
| NT-C09 | `[data-cart-count-label]` khớp tổng qty thực tế | Data | — |
| NT-C10 | Giỏ **bền vững** qua F5 (localStorage key `noithat_cart`) | Edge | — |
| NT-C11 | Badge giỏ ở header đồng bộ giữa trang catalog và trang giỏ | Edge | `data-cart-count` |
| NT-C12 | Dữ liệu localStorage hỏng (`"abc"`) → giỏ rỗng, không crash | Negative | Nhánh `try/catch` trong `readCart()` |

### 4.2. Chi tiết sản phẩm — `/san-pham/{slug}` (POM `ProductDetailPage` đã có, chưa dùng) 🔴 P1

| ID | Scenario | Loại | Locator |
|:--|:---|:---|:---|
| NT-D01 | Trang chi tiết render đúng tên / giá / trạng thái kho | Happy | `data-name`, `data-price`, `isInStock()` |
| NT-D02 | `[#btn-qty-plus]`/`[#btn-qty-minus]` cập nhật `#input-qty`; biên 1 và 99 | Boundary | `increaseQuantity()`, `getCurrentQuantity()` |
| NT-D03 | `#btn-add-to-cart` → toast `#cart-toast-notification` xuất hiện | Happy | `showCartToast()` |
| NT-D04 | `#btn-buy-now` → điều hướng `/thanh-toan` | Happy | POM chưa có method assert URL |
| NT-D05 | Giá & tên trên card catalog **khớp** với trang chi tiết | Data | So sánh 2 trang |
| NT-D06 | Slug không tồn tại → trang 404/empty, không crash JS | Negative | `/san-pham/khong-ton-tai` |

### 4.3. Thanh toán 3 bước — `/thanh-toan` (CẦN VIẾT MỚI `CheckoutPage`) 🔴 P1

DOM đã xác minh: `[data-checkout-root|empty|content|steps|grid|items|subtotal|shipping|total|success]`, `[data-address-form]`, `[data-error-for="fullname|phone|email|province|district|address"]`, `[data-step-panel="0|1|2"]`, `[data-step-next]`, `[data-step-back]`, `input[name=payment][data-payment]` (`cod`/`bank`), `#ck-province`, `[data-review-address|payment|items]`, `[data-place-order]`, `[data-order-code]` (format `NT-XXXXXX`).

| ID | Scenario | Loại | Ghi chú |
|:--|:---|:---|:---|
| NT-P01 | Vào checkout khi giỏ trống → `[data-checkout-empty]` hiển thị, ẩn content | Negative | — |
| NT-P02 | Step 1: submit rỗng → cả 6 `[data-error-for]` hiện message tương ứng | Negative | `novalidate` + `initAuthForm` |
| NT-P03 | Step 1: email sai định dạng + SĐT sai định dạng → báo lỗi đúng field | Negative | Regex `email`/`phone` trong `validate.ts` |
| NT-P04 | Step 1: SĐT hợp lệ dạng `+84…` và `0…` đều pass | Boundary | `^(0|\+84)[0-9]{9}$` |
| NT-P05 | Điều hướng Step 0 → 1 → 2: `[data-step-label]` đổi trạng thái active | Happy | — |
| NT-P06 | `[data-step-back]` quay lại giữ nguyên dữ liệu đã nhập | Edge | — |
| NT-P07 | Chọn `bank` → `[data-review-payment]` hiển thị "Chuyển khoản"; chọn `cod` → "COD" | Data | 2 case |
| NT-P08 | Step 2 phản ánh đúng địa chỉ/tên/SĐT đã nhập ở `[data-review-address]` | Happy | — |
| NT-P09 | **Toán học**: `[data-checkout-total]` = subtotal (+ phí ship nếu `[data-checkout-shipping]` ≠ "Miễn phí") | Data | — |
| NT-P10 | `[data-place-order]` → `[data-checkout-success]` hiện, `[data-order-code]` khớp regex `NT-\d{6}` | Happy | — |
| NT-P11 | Sau khi đặt hàng thành công → giỏ hàng reset (badge = 0 / localStorage rỗng) | Edge | — |
| NT-P12 | **E2E xuyên module**: Đặt hàng → mã đơn xuất hiện trong Admin `/admin/don-hang` khi search `#order-search` | E2E | Test giá trị cao nhất của dự án |

### 4.4. Đăng ký — `/dang-ky` (CẦN VIẾT MỚI `RegisterPage`) 🟡 P2

DOM đã xác minh: `#register-form`, `#reg-name`, `#reg-email`, `#reg-phone`, `#reg-password`, `#reg-confirm`, `#reg-terms`, `#btn-submit`, `#btn-google-signup`, `#err-fullname|email|phone|password|confirm`, `#password-strength-container|bar|text`, `#auth-toast`.

| ID | Scenario | Loại |
|:--|:---|:---|
| NT-R01 | Trang đăng ký hiển thị đủ 6 trường + link điều khoản | Happy |
| NT-R02 | Submit rỗng → cả 5 `#err-*` hiển thị đúng thứ tự, focus field đầu tiên bị lỗi | Negative |
| NT-R03 | Email sai định dạng (`abc`, `a@b`) | Negative |
| NT-R04 | SĐT sai định dạng: `123`, `09012345`, `09012345678`, `+8490123456` | Boundary |
| NT-R05 | Password biên độ dài: ngắn hơn minlength (báo lỗi) vs đúng minlength (pass) | Boundary |
| NT-R06 | `#reg-confirm` khác `#reg-password` → `#err-confirm` | Negative |
| NT-R07 | Không tick `#reg-terms` → báo lỗi bắt buộc đồng ý điều khoản | Negative |
| NT-R08 | Thanh đo độ mạnh mật khẩu đổi khi nhập `123456` vs `Abc@12345` | Edge |
| NT-R09 | Đăng ký trùng email (đã tồn tại) → hiện lỗi từ API, không tạo tài khoản | Negative |

### 4.5. Admin Đơn hàng — sâu hơn (đang chỉ có 3 test nông) 🟡 P2

DOM đã xác minh: `[data-btn-view-order]`, `#order-detail-modal`, `#modal-order-code|cust-name|cust-phone|cust-email|cust-address|items-tbody|subtotal|ship-fee|fee|total|pay-method|pay-status|carrier|tracking|note`, `#update-order-status`, `#btn-save-order-status`, `#modal-carrier`, `[data-status-badge]`, `#order-empty-label`, `#order-count-label`, `#order-toast`.

| ID | Scenario | Loại |
|:--|:---|:---|
| NT-AO01 | Mở modal chi tiết đơn → hiển thị đúng mã đơn vừa search | Happy |
| NT-AO02 | **Toán học modal**: `#modal-subtotal` + `#modal-ship-fee` = `#modal-total` | Data |
| NT-AO03 | Đổi trạng thái đơn qua `#update-order-status` + lưu → `[data-status-badge]` trong bảng đổi theo | Happy |
| NT-AO04 | Cập nhật `#modal-carrier` + `#modal-tracking` → lưu → mở lại modal vẫn giữ giá trị | Edge |
| NT-AO05 | Lọc theo phương thức giao hàng (`filterByShippingMethod()` — đã có method, chưa dùng) | Happy |
| NT-AO06 | Search không có kết quả → `#order-empty-label` hiển thị | Negative |
| NT-AO07 | `#order-count-label` khớp số dòng thực tế sau filter | Data |
| NT-AO08 | Xoá hết filter → số dòng trở về ban đầu (assert **bằng**, không phải `>=`) | Data |

### 4.6. Admin Sản phẩm — sâu hơn 🟡 P2

Các method/PO **đã có sẵn nhưng chưa được test**: `clickFirstProductDelete()`, `isDeleteConfirmModalDisplayed()`, `cancelDelete()`, `getToastText()`, `filterByStatus()`, `clearSearch()`, `#prod-slug`, `#prod-dimensions`, `#prod-warranty`, `#btn-edit`, `p[data-adm-empty]`, `p[data-adm-count]`.

| ID | Scenario | Loại |
|:--|:---|:---|
| NT-AP01 | `#btn-edit` → modal mở **pre-fill** đúng dữ liệu dòng → sửa tên/giá → lưu → bảng cập nhật | Happy |
| NT-AP02 | Xoá SP: mở modal confirm → **Cancel** → dòng vẫn còn | Negative |
| NT-AP03 | Xoá SP: **Confirm** → dòng biến mất, toast hiện | Happy |
| NT-AP04 | Bỏ trống tên/giá → form chặn submit + báo lỗi | Negative |
| NT-AP05 | Giá không hợp lệ: `abc`, `-100`, `0` | Boundary |
| NT-AP06 | Tìm kiếm không có kết quả → `p[data-adm-empty]` | Negative |
| NT-AP07 | Lọc theo trạng thái (`con-hang`/`het-hang`/`nhap`) → mọi dòng khớp trạng thái | Happy |
| NT-AP08 | Nội dung `#toast-message` sau khi thêm SP phải chứa tên SP vừa tạo | Data |
| NT-AP09 | Thêm SP với slug trùng → báo lỗi, không tạo bản ghi trùng | Negative |
| NT-AP10 | `p[data-adm-count]` khớp số dòng sau mỗi lần search/filter | Data |
| NT-AP11 | Toggle trạng thái SP → badge trạng thái **đổi giá trị thật** (hiện test không assert) | Edge |

### 4.7. Admin Dashboard & các module admin còn lại 🟢 P3–P4

`AdminDashboardPage` có `getKpiCardsCount()`, `isRevenueChartDisplayed()`, `clickQuickAddProduct()` — **chưa dùng**:

| ID | Scenario | Loại | Priority |
|:--|:---|:---|:-:|
| NT-AD01 | Dashboard: header + ≥4 thẻ KPI + biểu đồ doanh thu hiển thị | Happy | P2 |
| NT-AD02 | Nút "Thêm Sản Phẩm" nhanh → sang `/admin/san-pham` | Happy | P3 |
| NT-AD03 | **Data-driven smoke 9 module admin**: `/admin/danh-muc`, `ma-giam-gia`, `khach-hang`, `nhan-vien`, `danh-gia`, `doanh-thu`, `thong-bao`, `cai-dat`, `phong-thuy` → mỗi trang load đúng heading + không lỗi JS console | Smoke | P3 |
| NT-AD04 | Khách hàng: `#search-customer` lọc bảng `#customers-table-body`; `#stat-total-customers` khớp số dòng | Happy | P3 |
| NT-AD05 | Phong thủy (admin): `#pt-search` + `#pt-room-filter` + `#pt-count-label`; mở `#advice-modal` xem chi tiết; `#btn-pt-reset` khôi phục | Happy | P3 |

### 4.8. Storefront Home & Phong thủy 🟢 P3

| ID | Scenario | Loại |
|:--|:---|:---|
| NT-H01 | Trang chủ load đủ section neo: `#showroom`, `#du-an`, `#doi-tac`, `#cau-chuyen`, `#mua-theo-khong-gian` | Happy |
| NT-H02 | Menu mobile: `#menu-btn` → `#menu-panel` mở, `#menu-close`/`#menu-backdrop` đóng | Edge |
| NT-H03 | Tìm kiếm trên header (`[data-menu-search]`) → điều hướng sang `/san-pham` kèm kết quả | Happy |
| NT-H04 | Form tư vấn phong thủy `#kt-year` + `#kt-gender` + `#kt-room` → sinh kết quả tư vấn | Happy |

### 4.9. Catalog — bổ sung test có "răng" 🟡 P2

Method `filterBySpace()`, `filterByPrice()`, `isProductWithTitleDisplayed()`, `[data-result-count]`, `[data-empty]` — **chưa dùng**:

| ID | Scenario | Loại |
|:--|:---|:---|
| NT-CT01 | `filterBySpace('phong-khach')` → mọi card thuộc không gian đó | Happy |
| NT-CT02 | Lọc khoảng giá → giá mọi card nằm trong khoảng | Data |
| NT-CT03 | Kết hợp danh mục + không gian (2 filter cùng lúc) | Edge |
| NT-CT04 | **Assert thật** sort giá tăng dần: parse toàn bộ giá → dãy không giảm (hiện chỉ `count > 0`) | Data |
| NT-CT05 | Sort giá giảm dần / tên A-Z / tên Z-A (3 case data-driven) | Data |
| NT-CT06 | `[data-result-count]` khớp số card hiển thị | Data |
| NT-CT07 | Lọc không có kết quả → `[data-empty]` hiện | Negative |
| NT-CT08 | Click card → điều hướng `/san-pham/{slug}` đúng slug | Happy |

---

## 5. TEST ĐỀ XUẤT BỔ SUNG — PROJECT 2 (SHOPNOVA)

> ⚠️ Không có source trong repo → locator phải **recon DOM thật khi app chạy** (theo đúng nguyên tắc của workflow: *KHÔNG đoán*). Danh sách dưới đây là **phạm vi chức năng** cần phủ, dựa trên route đã biết: `/`, `/dang-nhap`, `/dang-ky`, `/tim-kiem`, `/gio-hang`, `/san-pham/{slug}`, `/nguoi-ban`, `/nguoi-ban/san-pham`, `/nguoi-ban/san-pham/them-moi`, `/nguoi-ban/don-hang`.
> Đề xuất thêm **14 test** (10 test hiện tại chưa từng chạy xanh lần nào).

| ID | Module | Scenario | Loại | Priority | Trạng thái POM |
|:--|:---|:---|:---|:-:|:--|
| SN-01 | Auth | **Bổ sung assertion** cho buyer login: URL không còn `/dang-nhap`, hiện tên user / menu tài khoản | Happy | P1 | Sửa test hiện có |
| SN-02 | Auth | **Bổ sung assertion** cho seller login: redirect đúng `/nguoi-ban` | Happy | P1 | Sửa test hiện có |
| SN-03 | Auth | Đăng nhập sai → không vào được trang bảo vệ | Negative | P1 | Có |
| SN-04 | Auth | **Auth guard**: truy cập `/nguoi-ban` khi chưa login → chuyển `/dang-nhap` | Negative | P2 | Cần thêm |
| SN-05 | Register | Đăng ký mới: validate email/SĐT/mật khẩu, đăng ký thành công | Happy+Neg | P2 | Cần `RegisterPage` |
| SN-06 | Search | Search không có kết quả → empty state | Negative | P2 | Cần locator empty |
| SN-07 | Search | **Assert thật** filter ngành hàng: mọi card phải thuộc ngành đã lọc + count giảm | Data | P1 | Sửa test hiện có |
| SN-08 | Search | **Assert thật** sort giá tăng/giảm: dãy giá đơn điệu | Data | P1 | Cần thêm method parse giá |
| SN-09 | Search | Xoá tất cả filter → số lượng card trở về ban đầu (`assertEquals`) | Data | P2 | Method "Xóa tất cả" đã có locator |
| SN-10 | Product | Trang chi tiết `/san-pham/{slug}`: tên/giá/tồn kho + nút thêm giỏ | Happy | P1 | Cần `NovaProductDetailPage` |
| SN-11 | Cart | `/gio-hang`: thêm/sửa số lượng/xoá, tổng tiền = Σ(giá×SL) | Happy+Data | P1 | Cần `NovaCartPage` |
| SN-12 | Checkout | Luồng đặt hàng E2E → tạo đơn thành công có mã đơn | E2E | P1 | Cần `NovaCheckoutPage` |
| SN-13 | Seller | Đăng sản phẩm mới (`/nguoi-ban/san-pham/them-moi`): validate + tạo thành công + xuất hiện trong danh sách | Happy+Neg | P2 | Cần `NovaSellerProductFormPage` |
| SN-14 | Seller | Danh sách đơn của người bán (`/nguoi-ban/don-hang`): load, lọc trạng thái, cập nhật trạng thái | Happy | P2 | Cần `NovaSellerOrdersPage` |

---

## 6. TEST ĐỀ XUẤT BỔ SUNG — PROJECT 3 (QR REPAIR)

> Cũng thiếu source → cần recon DOM. Đề xuất thêm **13 test**.

| ID | Module | Scenario | Loại | Priority | Ghi chú |
|:--|:---|:---|:---|:-:|:--|
| RP-01 | Tracking | **Bổ sung assertion**: SĐT có dữ liệu → `getTicketResultsCount() > 0` + hiện trạng thái phiếu | Happy | P1 | Method đã có, chưa dùng |
| RP-02 | Tracking | SĐT sai định dạng (`abc`, `123`) → báo lỗi validation | Negative | P2 | — |
| RP-03 | Tracking | SĐT không tồn tại → empty state "không tìm thấy phiếu" (không phải màn hình trắng) | Negative | P1 | — |
| RP-04 | Tracking | Tra cứu lại 2 lần liên tiếp → kết quả không nhân đôi (idempotent render) | Edge | P3 | — |
| RP-05 | Tracking | `navigateBackHome()` (method chưa dùng) → về trang chủ | Happy | P3 | Đã có method |
| RP-06 | Booking | **Bổ sung assertion**: submit thành công → `isBookingSuccessDisplayed()` = true | Happy | P1 | Method đã có, chưa assert |
| RP-07 | Booking | Submit thiếu Họ tên / SĐT / mô tả lỗi → form chặn + báo lỗi | Negative | P1 | — |
| RP-08 | Booking | SĐT sai định dạng; ngày hẹn trong quá khứ; mô tả lỗi quá ngắn | Boundary | P2 | — |
| RP-09 | Booking | Sau khi đặt thành công, form được reset / không còn dữ liệu cũ | Edge | P3 | — |
| RP-10 | Booking→Admin | **E2E**: khách đặt lịch → lịch hẹn xuất hiện ở tab "Lịch Hẹn Khách Hàng" của Admin | E2E | P1 | Cần credential KTV hợp lệ |
| RP-11 | Admin | **Happy path login KTV** (hiện chỉ test login SAI, không hề test login đúng) → vào `RepairAdminDashboardPage` | Happy | P1 | ❗Cần user cung cấp tài khoản |
| RP-12 | Admin | Tab "Phiếu Yêu Cầu": danh sách phiếu + đổi trạng thái phiếu | Happy | P2 | POM đã có tab, chưa dùng |
| RP-13 | Admin | Auth guard: vào `/admin` khi chưa login → chuyển `/login`; `clickLogout()` → quay lại `/login` | Negative | P2 | Method đã có |
| RP-14 | Navigation | `#btnOnlineIntake` (`/online-intake`) + `clickBookingService()` + `btnTrackNow` — 3 locator đã khai báo nhưng **chưa test** | Happy | P3 | Đã có locator |

---

## 7. NÂNG CẤP FRAMEWORK (áp dụng cho cả 3 dự án)

| ID | Hạng mục | Chi tiết | Priority |
|:--|:---|:---|:-:|
| F-01 | **Fail-fast theo môi trường** | `@BeforeSuite` kiểm tra reachability của 3 base URL (HTTP HEAD, timeout 10s); nếu chết → `SkipException` kèm thông điệp rõ ràng thay vì 15 test SKIP rải rác 10 phút | P1 |
| F-02 | **Bỏ test không assertion** | Bắt buộc mỗi `@Test` có ≥1 `Assert`; thêm checklist vào `automation_rules` | P1 |
| F-03 | **Tầng API test** | Thêm `rest-assured` + `json-path` vào `pom.xml`; tạo `src/test/java/com/ecommerce/api/` (`ApiBase`, `ApiConfigReader`) → bộ test API cho 12 endpoint backend (auth register/login, products CRUD + categories, orders create/get-by-code, health) | P1 |
| F-04 | **Test data ngoài code** | `TestDataProviders` mới cho 3 dự án, đọc JSON/CSV trong `src/test/resources/testdata/`; xoá data SauceDemo/AE đã chết; dùng `DataFakerUtil` + prefix timestamp để dữ liệu traceable | P2 |
| F-05 | **Dọn dẹp dữ liệu test** | `@AfterMethod`/`@AfterClass` gọi API xoá sản phẩm/đơn/lịch hẹn đã tạo; hoặc tạo dữ liệu với prefix `AUTO_<timestamp>` và chỉ assert trên dữ liệu đó | P2 |
| F-06 | **Retry cho flaky** | `IRetryAnalyzer` (retry 1 lần) + `IAnnotationTransformer`, chỉ bật cho nhóm `flaky-prone` | P2 |
| F-07 | **CI phủ cả 3 dự án** | Sửa `regression-test.yml` chạy `testng-all.xml` headless; thêm matrix browser (chrome/edge); URL devtunnel tạm thời → chuyển sang staging cố định, hoặc job khởi động app trước khi test | P2 |
| F-08 | **Song song hoá đa dự án** | Mở rộng `testng-parallel.xml` cho cả 3 dự án (đã có `ThreadLocal<WebDriver>`), thread-count theo số class | P3 |
| F-09 | **Cập nhật tài liệu** | `README.md` + `BAO_CAO_TUAN_AUTOMATION_TESTING.md` đang mô tả SauceDemo/Automation Exercise (đã xoá) → viết lại theo 3 dự án thực tế | P3 |

---

## 8. LỘ TRÌNH ĐỀ XUẤT

| Phase | Nội dung | Số test thêm | Điều kiện |
|:--|:---|:-:|:--|
| **Phase 1 — Ổn định & lấp lỗ hổng P1** | F-01, F-02, F-03 + Project 1: Giỏ hàng (NT-C01→C12), Chi tiết SP (NT-D01→D06), Thanh toán (NT-P01→P12) + sửa assertion cho 6 test "rỗng" của P2/P3 | ~40 | Cần app P1 chạy (devtunnel/local) |
| **Phase 2 — Mở rộng nghiệp vụ** | Project 1: Đăng ký (NT-R01→R09), Admin Orders/Products sâu (NT-AO01→AO08, NT-AP01→AP11), Catalog (NT-CT01→CT08) + Project 2 recon & viết SN-01→SN-14 | ~48 | Cần app P2 chạy + tài khoản test |
| **Phase 3 — Phủ nốt & tối ưu** | Project 3 RP-01→RP-14, admin smoke 9 module P1 (NT-AD03→AD05), Home/Phong thủy (NT-H01→H04), F-04→F-09, song song hoá + CI 3 dự án | ~30 | Cần app P3 chạy + **credential KTV** |

**Kết quả kỳ vọng:** 35 test hiện tại → **~110–120 test**, phủ đủ 3 dự án ở cả tầng UI (storefront/admin/seller/KTV) và tầng API.

---

## 9. CẦN USER CUNG CẤP / XÁC NHẬN

1. **URL chạy được** cho 3 app (devtunnel hiện đã chết; localhost:5173/5174/4000 đang tắt). Project 1 cần được triển khai/chạy độc lập bên ngoài framework test.
2. **Tài khoản test**: KTV/Admin của Project 3 (chưa có happy-path login), tài khoản ShopNova khác nếu có (hiện dùng `minhanh/123456`, `ankerseller/123456`).
3. **Xác nhận scope ưu tiên**: dồn vào Phase 1 (Giỏ hàng + Thanh toán + API — rủi ro cao nhất) hay dàn đều 3 dự án?
4. Có cho phép **thêm dependency REST Assured** vào `pom.xml` và gọi API backend để tạo/dọn dữ liệu test không?

---

## 10. TRẠNG THÁI TRIỂN KHAI — PHASE 1 (đã viết code)

> Phạm vi đã chọn: **"Chỉ viết code Phase 1, không cần chạy app"**. Code đã **biên dịch thành công**
> (`mvn -o test-compile`) và **kiểm chứng cơ chế skip / fail-fast** bằng suite chạy thật (không có app).

### 10.1. Đã hoàn thành

| Hạng mục | File | Ghi chú |
|:--|:--|:--|
| **F-01** Fail-fast môi trường | `utils/EnvironmentGuard.java` (mới), `base/BaseTest.java` | Chặn ở `@BeforeMethod` **trước khi mở browser**, cache probe theo URL. Đo thực tế: suite smoke **226s → 7,7s**, 33 test SKIP, **0 failure** |
| **F-03** Tầng API test | `api/ApiClient.java`, `api/ApiResponse.java`, `tests/api/NoiThatApiTest.java` (mới), `testng-api.xml` | 14 test phủ 12 endpoint; dùng JDK `HttpClient` + `selenium-json` nên **không cần thêm dependency** (REST Assured không resolve được vì `~/.m2` bị chặn ghi) |
| **NT-C01→C13** Giỏ hàng | `tests/noithat/CartTest.java` (mới), `pages/noithat/CartPage.java` (mở rộng) | 13 test |
| **NT-D01→D07** Chi tiết sản phẩm | `tests/noithat/ProductDetailTest.java` (mới), `pages/noithat/ProductDetailPage.java` (mở rộng) | 7 test |
| **NT-P01→P11** Thanh toán 3 bước | `pages/noithat/CheckoutPage.java` (mới), `tests/noithat/CheckoutFlowTest.java` (mới) | 11 test, gồm E2E đặt hàng → kiểm tra lại ở Admin |
| **F-02** Bổ sung assertion | `NovaAuthTest`, `RepairTrackingTest`, `RepairBookingTest`, `AdminProductManagementTest` (sửa), `pages/repair/*` (thêm method) | 6 test "rỗng" nay có assertion thật; thêm 2 test mới |
| Sửa lỗi hạ tầng phát hiện khi chạy | `listeners/TestListener`, `reports/ExtentReportManager` | Report tự khởi tạo (trước đây suite API bị NullPointer tại `createTest`) + flush ở `onFinish` |
| Suite | `testng.xml`, `testng-smoke.xml`, `testng-all.xml`, `testng-parallel.xml`, `testng-api.xml` (mới) | Tổng **82 test** (trước Phase 1: 35) |

**Số test hiện tại:** Nội Thất UI 46 · ShopNova 10 · QR Repair 12 · API 14 = **82 test**.

### 10.2. Lỗi / điểm bất thường phát hiện khi đọc source & khi chạy

| # | Phát hiện | Ảnh hưởng |
|:-:|:--|:--|
| D1 | Nút `+` ở trang chi tiết vượt `max` của ô số lượng (JS `getQty() + 1` không kẹp theo tồn kho) → test **NT-D07** được viết để phơi bày lỗi này và **dự kiến FAIL cho tới khi dev sửa** | Cho phép đặt số lượng lớn hơn tồn kho |
| D2 | Test cũ `testAddNewProductForm` truyền category `"ban"` — không tồn tại trong danh mục (`sofa`, `ban-an`, `ghe`, `giuong`, `tu-ke`, `den`, `trang-tri`) → `selectByValue` ném lỗi | Test cũ không thể pass; đã sửa thành `ban-an` |
| D3 | Nhãn trạng thái render ban đầu ghi không dấu (`Dang ban`) nhưng sau khi toggle ghi có dấu (`Đang bán`) | Sai lệch hiển thị nhỏ |
| D4 | Mã đơn thật của backend là `NT-YYMM-XXXX` (không phải `NT-XXXXXX`) | Đã cập nhật regex kiểm tra mã đơn |
| D5 | Trang chi tiết lấy giá từ API; khi backend chạy, giá có thể khác dữ liệu tĩnh của catalog | Tránh assert chéo catalog ↔ detail; so sánh trong cùng trang |
| D6 | `TestDataProviders` và `DataFakerUtil` vẫn là code chết (data SauceDemo / Automation Exercise) | Chưa xử lý — thuộc F-04 (Phase 2/3) |

### 10.3. Việc còn lại của Phase 1 (cần app chạy để verify)

1. Chạy `mvn test "-DsuiteFile=testng.xml"` khi devtunnel sống → xác nhận 46 test Nội Thất UI thực tế.
2. Chạy backend Project 1 độc lập rồi `mvn test "-DsuiteFile=testng-api.xml"` → 14 test API.
3. Xác nhận lại NT-D07 là lỗi thật (không phải hiểu nhầm về `stock`) và tạo ticket cho dev.
