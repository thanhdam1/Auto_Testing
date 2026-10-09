# E-Commerce Test Automation Framework 🚀

> **Enterprise-Grade Automated Testing Framework** dành cho sàn Thương Mại Điện Tử (E-Commerce) được xây dựng theo mô hình chuẩn Doanh nghiệp: **Java + Selenium 4 + TestNG + Maven + Page Object Model (POM) + ExtentReports 5**.

---

## 🌟 1. Tổng Quan & Công Nghệ Sử Dụng

Dự án kiểm thử tự động hóa toàn diện các luồng nghiệp vụ cốt lõi của sàn TMĐT (Authentication, Catalog Browsing & Sorting, Cart Management, Checkout & Payment Calculation, Search & Category Navigation).

| Thành phần | Công nghệ / Thư viện | Vai trò |
| :--- | :--- | :--- |
| **Language** | **Java (JDK 17+)** | Ngôn ngữ lập trình chính, hỗ trợ Type-safe và OOP |
| **Core Automation** | **Selenium WebDriver 4.27.0** | Tương tác trình duyệt với Selenium Manager tích hợp |
| **Test Framework** | **TestNG 7.10.2** | Quản lý Suite, Data-Driven (`@DataProvider`), Groups, Parallel execution |
| **Design Pattern** | **Page Object Model (POM)** | Tách biệt mã nguồn trang (Locators & Actions) khỏi kịch bản kiểm thử |
| **Thread-Safety** | **ThreadLocal Driver** | Hỗ trợ chạy kiểm thử song song (Parallel execution) mượt mà |
| **Reporting** | **ExtentReports 5.1.2** | Báo cáo kiểm thử HTML đồ họa đẹp mắt, chụp ảnh màn hình khi fail |
| **Logging** | **Log4j2 2.24.1** | Ghi log chi tiết ra Console và File xoay vòng (`logs/automation.log`) |
| **Test Data** | **DataFaker 2.4.2** | Sinh dữ liệu khách hàng ngẫu nhiên thực tế (Name, Address, Zip code) |
| **Build Tool** | **Apache Maven 3.9+** | Quản lý dependencies, lifecycle và cấu hình CI/CD |

---

## 📁 2. Cấu Trúc Thư Mục Dự Án

```
Auto_Testing/
├── pom.xml                               # Quản lý dependencies và build plugins
├── testng.xml                            # Suite chạy mặc định (SauceDemo Core Flows)
├── testng-smoke.xml                      # Suite kiểm thử khói (Smoke Test)
├── testng-all.xml                        # Suite kiểm thử toàn diện (SauceDemo + AutomationExercise)
├── testng-parallel.xml                   # Suite chạy song song đa luồng (Multi-threading)
├── README.md                             # Tài liệu hướng dẫn sử dụng chi tiết
├── logs/                                 # File log chạy kiểm thử (Tự động tạo)
│   └── automation.log
├── reports/                              # Báo cáo HTML ExtentReports & Screenshots
│   ├── ExtentReport_YYYYMMDD_HHmmss.html
│   └── screenshots/
├── src/
│   ├── main/java/com/ecommerce/
│   │   ├── constants/
│   │   │   └── FrameworkConstants.java   # Hằng số toàn cục (Timeouts, URLs, Credentials)
│   │   ├── driver/
│   │   │   ├── TargetBrowser.java        # Enum trình duyệt (CHROME, EDGE, FIREFOX)
│   │   │   ├── DriverFactory.java        # Khởi tạo WebDriver với Options chuẩn hóa
│   │   │   └── DriverManager.java        # ThreadLocal WebDriver quản lý session độc lập
│   │   ├── pages/                        # Page Object Model (POM)
│   │   │   ├── base/
│   │   │   │   └── BasePage.java         # Lớp cơ sở đóng gói Waits, Click, Type, JS actions
│   │   │   ├── saucedemo/
│   │   │   │   ├── LoginPage.java
│   │   │   │   ├── InventoryPage.java    # Danh mục, Sort A-Z/Z-A, Giá Low-High/High-Low
│   │   │   │   ├── CartPage.java         # Giỏ hàng, kiểm tra danh sách & xóa sản phẩm
│   │   │   │   ├── CheckoutInformationPage.java  # Nhập thông tin khách hàng
│   │   │   │   ├── CheckoutOverviewPage.java     # Kiểm tra giá, thuế và tính toán tổng tiền
│   │   │   │   └── CheckoutCompletePage.java     # Màn hình đặt hàng thành công
│   │   │   └── automationexercise/
│   │   │       ├── AEHomePage.java       # Điều hướng danh mục & menu
│   │   │       ├── AEProductsPage.java   # Tìm kiếm theo từ khóa & Lọc danh mục Women/Men
│   │   │       └── AECartPage.java       # Xem sản phẩm trong giỏ hàng
│   │   ├── reports/
│   │   │   └── ExtentReportManager.java  # Khởi tạo và ghi log vào ExtentReports 5
│   │   └── utils/
│   │       ├── ConfigReader.java         # Đọc file config.properties với System override
│   │       ├── DataFakerUtil.java        # Tạo dữ liệu giả lập (Faker)
│   │       ├── LogUtil.java              # Wrapper ghi log Log4j2
│   │       ├── ScreenshotUtil.java       # Chụp ảnh màn hình đính kèm báo cáo
│   │       └── WaitUtil.java             # Quản lý Explicit Waits (Wait for visibility, clickable)
│   └── test/
│       ├── java/com/ecommerce/
│       │   ├── base/
│       │   │   └── BaseTest.java         # TestNG Setup/Teardown, Suite init, Screenshot on fail
│       │   ├── dataprovider/
│       │   │   └── TestDataProviders.java # Dữ liệu kiểm thử (@DataProvider)
│       │   ├── listeners/
│       │   │   └── TestListener.java     # TestNG ITestListener tích hợp ExtentReports
│       │   └── tests/
│       │       ├── saucedemo/
│       │       │   ├── LoginTest.java            # Đăng nhập hợp lệ, sai thông tin, logout
│       │       │   ├── ProductCatalogTest.java   # Sắp xếp theo tên & giá thực tế
│       │       │   ├── CartTest.java             # Thêm, cập nhật badge giỏ hàng, xóa item
│       │       │   └── CheckoutE2ETest.java      # Toàn bộ quy trình đặt hàng E2E & tính toán
│       │       └── automationexercise/
│       │           └── ProductSearchTest.java    # Tìm kiếm sản phẩm & lọc danh mục
│       └── resources/
│           ├── config.properties                 # File cấu hình trình duyệt, URL, timeout
│           └── log4j2.xml                        # Cấu hình log console & file
```

---

## 🛒 3. Các Luồng Nghiệp Vụ TMĐT Đã Được Tự Động Hóa

### A. Luồng Đăng nhập & Xác thực (Authentication)
- ✅ Đăng nhập thành công với tài khoản tiêu chuẩn `standard_user`.
- ✅ Data-Driven Testing kiểm thử các trường hợp lỗi: tài khoản bị khóa (`locked_out_user`), sai mật khẩu, bỏ trống username, bỏ trống password.
- ✅ Đăng xuất an toàn và chuyển hướng về trang Login.

### B. Danh Mục & Sắp Xếp Sản Phẩm (Catalog Browsing & Sorting)
- ✅ Kiểm tra hiển thị đầy đủ danh sách sản phẩm.
- ✅ Sắp xếp theo Tên từ A đến Z (`Name A to Z`).
- ✅ Sắp xếp theo Tên từ Z đến A (`Name Z to A`).
- ✅ Sắp xếp theo Giá từ Thấp đến Cao (`Price low to high`) - **Thuật toán tự động parse số thực và kiểm tra toán học**.
- ✅ Sắp xếp theo Giá từ Cao đến Thấp (`Price high to low`).

### C. Quản Lý Giỏ Hàng (Cart Management)
- ✅ Thêm sản phẩm đơn lẻ vào giỏ hàng và kiểm tra huy hiệu số lượng (`badge count = 1`).
- ✅ Thêm nhiều sản phẩm liên tiếp và kiểm tra số lượng tăng chính xác (`badge count = 3`).
- ✅ Chuyển đến trang Giỏ hàng và đối soát tên, giá tiền khớp với danh mục.
- ✅ Xóa sản phẩm khỏi giỏ hàng và kiểm tra cập nhật lại số lượng và danh sách.

### D. Toàn Bộ Quy Trình Thanh Toán E2E (End-to-End Checkout)
- ✅ Thêm sản phẩm vào giỏ hàng -> Đến giỏ hàng -> Bắt đầu Checkout.
- ✅ Nhập thông tin khách hàng ngẫu nhiên tự động bằng **DataFaker** (First Name, Last Name, Zip code).
- ✅ **Kiểm tra toán học tự động**: `Tổng tiền (Total) = Giá sản phẩm (Subtotal) + Tiền thuế (Tax)` với độ chính xác số thập phân.
- ✅ Xác nhận đơn hàng thành công: Kiểm tra tiêu đề `"Thank you for your order!"` và biểu tượng xác nhận.
- ✅ Quay trở về Catalog và kiểm tra giỏ hàng được reset về 0.
- ✅ Kiểm tra validation khi bỏ trống First Name, Last Name, hoặc Postal Code.

### E. Tìm Kiếm & Lọc Danh Mục TMĐT (Search & Category Filter)
- ✅ Tìm kiếm sản phẩm theo từ khóa (VD: `dress`, `tshirt`, `jeans`) và kiểm tra kết quả trả về khớp từ khóa.
- ✅ Lọc theo danh mục cây TMĐT (`Women > Dress`, `Men > Tshirts`).

---

## 🚀 4. Hướng Dẫn Chạy Kiểm Thử

### Bước 1: Mở Terminal tại thư mục dự án
```powershell
cd c:\Users\PC\OneDrive\Desktop\Auto_Testing
```

### Bước 2: Lựa chọn lệnh chạy theo nhu cầu

#### 1. Chạy Suite mặc định (SauceDemo Core Regression):
```powershell
mvn test
```

#### 2. Chạy nhanh các kịch bản kiểm thử khói (Smoke Tests):
```powershell
mvn test "-DsuiteFile=testng-smoke.xml"
```

#### 3. Chạy toàn bộ (SauceDemo + Automation Exercise):
```powershell
mvn test "-DsuiteFile=testng-all.xml"
```

#### 4. Chạy song song đa luồng (Parallel Execution):
```powershell
mvn test "-DsuiteFile=testng-parallel.xml"
```

#### 5. Chạy ẩn danh (Headless Mode - Thích hợp cho CI/CD):
```powershell
mvn test "-Dheadless=true"
```

#### 6. Chạy trên trình duyệt Microsoft Edge:
```powershell
mvn test "-Dbrowser=edge"
```

---

## 📊 5. Xem Báo Cáo Kiểm Thử (ExtentReports) & Logs

Sau khi chạy xong, kết quả kiểm thử được tạo tự động:
1. **Báo cáo HTML đẹp mắt:** Nằm trong thư mục `reports/`
   - Ví dụ: `reports/ExtentReport_20261006_122600.html`
   - Nhấp đúp chuột để mở trực tiếp trên trình duyệt Chrome/Edge để xem Dashboard biểu đồ tròn, trạng thái từng Step, thời gian thực thi và ảnh chụp màn hình khi có test fail.
2. **File Logs:** Nằm trong thư mục `logs/automation.log` ghi lại từng thao tác bấm nút, gõ phím, điều hướng URL.
