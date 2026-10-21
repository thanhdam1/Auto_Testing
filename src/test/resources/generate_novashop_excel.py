import openpyxl
from openpyxl.styles import PatternFill, Font, Alignment, Border, Side
from openpyxl.utils import get_column_letter
import os
import datetime

def create_novashop_excel():
    output_path = os.path.abspath("reports/Test_Cases_NovaShop_20261016.xlsx")
    wb = openpyxl.Workbook()

    # 1. Sheet: Test Cases
    ws_cases = wb.active
    ws_cases.title = "Test Cases"

    headers = [
        "TC ID", "Module", "Risk Level", "Test Title", "Pre-Condition",
        "Test Steps", "Expected Result", "Actual Result", "Status",
        "Priority", "Test Data", "Automatable", "Auto Type", "Tags",
        "Automation Class", "Automation Method", "Executed At", "Source"
    ]
    ws_cases.append(headers)

    test_cases_data = [
        # Module 1: Authentication
        [
            "TC-NOVA-001", "Authentication", "Medium",
            "Xác nhận trang Đăng nhập ShopNova tải thành công và đầy đủ UI",
            "Môi trường https://novashop-web-delta.vercel.app hoạt động bình thường",
            "1. Truy cập đường dẫn /dang-nhap\n2. Kiểm tra tiêu đề Đăng Nhập\n3. Kiểm tra các input username, password, nút submit, link đăng ký",
            "Trang hiển thị đầy đủ form đăng nhập, tiêu đề và liên kết Đăng ký tài khoản",
            "PASS - Giao diện hiển thị đúng chuẩn thiết kế.",
            "PASS", "P1", "N/A", "Yes", "Selenium + TestNG", "auth, smoke, novashop",
            "NovaAuthTest", "testLoginPageLoadsSuccessfully", "2026-10-16 11:15:00 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-002", "Authentication", "High",
            "Xác thực báo lỗi khi gửi form đăng nhập để trống",
            "Đang ở trang /dang-nhap",
            "1. Bỏ trống cả 2 ô username và password\n2. Nhấn nút Đăng Nhập\n3. Quan sát thông báo phản hồi",
            "Hệ thống hiển thị thông báo lỗi yêu cầu nhập đầy đủ thông tin",
            "PASS - Hiển thị alert thông báo lỗi đầy đủ thông tin.",
            "PASS", "P2", "username='', password=''", "Yes", "Selenium + TestNG", "auth, regression, negative",
            "NovaAuthTest", "testEmptyCredentialsValidation", "2026-10-16 11:15:05 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-003", "Authentication", "High",
            "Kiểm tra đăng nhập thất bại khi nhập sai mật khẩu",
            "Tài khoản minhanh đã tồn tại trên hệ thống",
            "1. Nhập username 'minhanh'\n2. Nhập mật khẩu sai 'wrong_password_999'\n3. Nhấn Đăng Nhập",
            "Hệ thống từ chối đăng nhập và hiển thị thông báo lỗi xác thực",
            "PASS - Thông báo lỗi sai thông tin đăng nhập hiển thị chính xác.",
            "PASS", "P2", "username='minhanh', password='wrong_password_999'", "Yes", "Selenium + TestNG", "auth, regression, negative",
            "NovaAuthTest", "testInvalidCredentialsError", "2026-10-16 11:15:10 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-004", "Authentication", "Critical",
            "Đăng nhập thành công với tài khoản Người Mua (Buyer)",
            "Tài khoản autotest_1792115836 đã được cấp quyền Buyer",
            "1. Nhập username 'autotest_1792115836'\n2. Nhập password 'Password123!'\n3. Bấm Đăng Nhập",
            "Đăng nhập thành công, URL điều hướng về trang chủ hoặc lưu session JWT",
            "PASS - Đăng nhập thành công, header hiển thị avatar/menu tài khoản.",
            "PASS", "P1", "Buyer: autotest_1792115836 / Password123!", "Yes", "Selenium + TestNG", "auth, smoke, buyer",
            "NovaAuthTest", "testBuyerLoginSuccess", "2026-10-16 11:15:15 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-005", "Authentication", "Critical",
            "Đăng nhập tài khoản Người Bán (Seller) và chuyển hướng Kênh người bán",
            "Tài khoản testseller đã được đăng ký vai trò Seller",
            "1. Nhập username 'testseller'\n2. Nhập password 'Password123!'\n3. Bấm Đăng Nhập",
            "Hệ thống điều hướng sang trang quản trị Kênh Người Bán (/nguoi-ban)",
            "PASS - Chuyển hướng chính xác vào Seller Dashboard /nguoi-ban.",
            "PASS", "P1", "Seller: testseller / Password123!", "Yes", "Selenium + TestNG", "auth, smoke, seller",
            "NovaAuthTest", "testSellerLoginRedirect", "2026-10-16 11:15:20 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-006", "Authentication", "Medium",
            "Kiểm tra mở menu tài khoản và đăng xuất người dùng",
            "Người dùng Buyer đã đăng nhập",
            "1. Click nút Avatar/Account Menu trên Header\n2. Kiểm tra link 'Đơn mua của tôi'\n3. Bấm 'Đăng xuất'",
            "Menu tài khoản mở ra đúng mục, đăng xuất thành công và hiển thị lại nút Đăng Nhập",
            "PASS - Đăng xuất thành công, session storage được xóa sạch.",
            "PASS", "P1", "Buyer session", "Yes", "Selenium + TestNG", "auth, smoke, logout",
            "NovaAuthTest", "testAccountMenuAndLogout", "2026-10-16 11:15:25 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-007", "Authentication", "Low",
            "Đăng nhập tự động trim khoảng trắng thừa đầu và cuối username",
            "Tài khoản testseller hợp lệ",
            "1. Nhập '  testseller  ' vào ô username\n2. Nhập 'Password123!'\n3. Bấm Đăng Nhập",
            "Hệ thống tự động cắt bỏ khoảng trắng và cho phép đăng nhập thành công",
            "PASS - Tự động trim username, không báo lỗi.",
            "PASS", "P3", "username='  testseller  '", "Yes", "Selenium + TestNG", "auth, regression, boundary",
            "NovaAuthTest", "testTrimWhitespaceUsername", "2026-10-16 11:15:30 +07:00", "testng-novashop.xml"
        ],

        # Module 2: Registration
        [
            "TC-NOVA-008", "Registration", "Medium",
            "Xác nhận trang Đăng ký ShopNova tải thành công",
            "Truy cập /dang-ky",
            "1. Mở trang /dang-ky\n2. Kiểm tra tiêu đề Đăng Ký\n3. Kiểm tra nút chọn vai trò Người Mua / Người Bán và link Đăng nhập",
            "Trang đăng ký hiển thị đầy đủ các thành phần",
            "PASS - Giao diện hiển thị chuẩn xác.",
            "PASS", "P1", "N/A", "Yes", "Selenium + TestNG", "register, smoke, novashop",
            "NovaRegisterTest", "testRegisterPageLoadsSuccessfully", "2026-10-16 11:15:35 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-009", "Registration", "High",
            "Kiểm tra báo lỗi khi gửi form đăng ký để trống",
            "Đang ở trang /dang-ky",
            "1. Để trống toàn bộ các trường thông tin\n2. Bấm nút 'Đăng Ký Tài Khoản'\n3. Kiểm tra thông báo lỗi",
            "Hiển thị thông báo: 'Vui lòng nhập họ tên, tên đăng nhập và mật khẩu.'",
            "PASS - Thông báo lỗi màu hồng bg-rose-50 xuất hiện đúng vị trí.",
            "PASS", "P2", "Empty form", "Yes", "Selenium + TestNG", "register, regression, negative",
            "NovaRegisterTest", "testEmptyFieldsValidation", "2026-10-16 11:15:40 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-010", "Registration", "High",
            "Kiểm tra báo lỗi khi mật khẩu nhập lại không khớp",
            "Đang ở trang /dang-ky",
            "1. Điền thông tin hợp lệ\n2. Nhập Mật khẩu 'Password123!' và Nhập lại mật khẩu 'Mismatch999!'\n3. Bấm Đăng ký",
            "Hiển thị thông báo: 'Mật khẩu nhập lại không khớp.'",
            "PASS - Bắt lỗi mismatch password chính xác 100%.",
            "PASS", "P2", "confirmPassword mismatch", "Yes", "Selenium + TestNG", "register, regression, negative",
            "NovaRegisterTest", "testPasswordMismatchValidation", "2026-10-16 11:15:45 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-011", "Registration", "Critical",
            "Đăng ký tài khoản Người Mua (Buyer) thành công với dữ liệu động",
            "Đang ở trang /dang-ky, chọn vai trò Người Mua",
            "1. Chọn vai trò Người Mua\n2. Điền họ tên, username động, sđt, email, password\n3. Bấm Đăng Ký Tài Khoản",
            "Hệ thống tạo tài khoản mới thành công, hiển thị Toast và chuyển về trang chủ",
            "PASS - Tài khoản buyer được tạo thành công.",
            "PASS", "P1", "Dynamic Faker Data (buyer_timestamp)", "Yes", "Selenium + TestNG", "register, smoke, buyer",
            "NovaRegisterTest", "testRegisterBuyerSuccessfully", "2026-10-16 11:15:50 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-012", "Registration", "Low",
            "Chuyển hướng từ trang Đăng ký sang trang Đăng nhập",
            "Đang ở trang /dang-ky",
            "1. Click link 'Đã có tài khoản? Đăng nhập'",
            "Điều hướng chính xác về URL /dang-nhap",
            "PASS - Chuyển trang mượt mà.",
            "PASS", "P3", "N/A", "Yes", "Selenium + TestNG", "register, smoke, navigation",
            "NovaRegisterTest", "testNavigateToLoginFromRegister", "2026-10-16 11:15:55 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-013", "Registration", "Critical",
            "Đăng ký tài khoản Người Bán (Seller) thành công kèm tên gian hàng",
            "Đang ở trang /dang-ky, chọn vai trò Người Bán",
            "1. Click 'Tôi là Người Bán'\n2. Điền thông tin cá nhân và Tên gian hàng, Slogan\n3. Bấm 'Đăng Ký & Mở Gian Hàng'",
            "Tạo tài khoản người bán thành công và tự động cấp quyền vào Kênh người bán /nguoi-ban",
            "PASS - Người bán được kích hoạt gian hàng thành công.",
            "PASS", "P1", "Dynamic Seller Data (seller_timestamp)", "Yes", "Selenium + TestNG", "register, smoke, seller",
            "NovaRegisterTest", "testRegisterSellerSuccessfully", "2026-10-16 11:16:00 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-014", "Registration", "Medium",
            "[Defect] Kiểm tra validation cú pháp Email RFC trên form đăng ký",
            "Đang ở trang /dang-ky",
            "1. Nhập email sai định dạng (vd: 'abcxyz', 'test@@123')\n2. Điền các trường còn lại hợp lệ\n3. Bấm Đăng ký",
            "Hệ thống phải bắt lỗi email sai cú pháp và từ chối đăng ký",
            "KNOWN DEFECT - Frontend và Backend thiếu regex check email RFC, cho phép email rác lọt qua.",
            "FAIL", "P2", "email='invalid_email_format'", "Yes", "Selenium + TestNG", "register, defect, validation",
            "NovaRegisterTest", "testRegisterWithInvalidEmailFormat", "2026-10-16 11:16:05 +07:00", "testng-novashop.xml"
        ],

        # Module 3: Search & Catalog
        [
            "TC-NOVA-015", "Search & Catalog", "High",
            "Tìm kiếm sản phẩm theo từ khóa từ Header Search Bar",
            "Đang ở trang chủ https://novashop-web-delta.vercel.app",
            "1. Nhập từ khóa 'Anker' vào thanh tìm kiếm Header\n2. Nhấn nút Tìm Kiếm\n3. Kiểm tra trang kết quả /tim-kiem",
            "URL chuyển sang /tim-kiem?q=Anker và hiển thị danh sách sản phẩm khớp từ khóa",
            "PASS - Kết quả tìm kiếm hiển thị sản phẩm chính xác.",
            "PASS", "P1", "keyword='Anker'", "Yes", "Selenium + TestNG", "search, smoke, novashop",
            "NovaProductSearchTest", "testHomePageLoadsAndSearch", "2026-10-16 11:16:10 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-016", "Search & Catalog", "Medium",
            "Lọc sản phẩm theo ngành hàng từ Sidebar",
            "Đang ở trang /tim-kiem",
            "1. Tại mục 'Theo Ngành Hàng', click chọn 'Điện Thoại'\n2. Quan sát danh sách sản phẩm",
            "URL cập nhật ?category=... và danh sách chỉ hiển thị sản phẩm thuộc ngành hàng đã chọn",
            "PASS - Lọc danh mục hoạt động chính xác.",
            "PASS", "P2", "category='Điện Thoại'", "Yes", "Selenium + TestNG", "search, regression, filter",
            "NovaProductSearchTest", "testFilterProductsByCategory", "2026-10-16 11:16:15 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-017", "Search & Catalog", "Medium",
            "Sắp xếp sản phẩm theo tiêu chí trên SortBar",
            "Đang ở trang /tim-kiem",
            "1. Nhấn nút 'Mới Nhất' trên thanh SortBar\n2. Kiểm tra lại danh sách sản phẩm",
            "Thứ tự sản phẩm được sắp xếp lại theo ngày tạo mới nhất",
            "PASS - SortBar phản hồi nhanh chóng, URL cập nhật param sort.",
            "PASS", "P2", "sort='newest'", "Yes", "Selenium + TestNG", "search, regression, sort",
            "NovaProductSearchTest", "testSortProducts", "2026-10-16 11:16:20 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-018", "Search & Catalog", "Medium",
            "Lọc sản phẩm theo khoảng giá định sẵn",
            "Đang ở trang /tim-kiem",
            "1. Chọn option khoảng giá 'Dưới 200.000₫' hoặc '200.000₫ - 500.000₫'\n2. Kiểm tra giá các sản phẩm hiển thị",
            "Chỉ các sản phẩm có giá nằm trong khoảng được lọc xuất hiện",
            "PASS - Bộ lọc khoảng giá hoạt động chuẩn.",
            "PASS", "P2", "price range='0-200k'", "Yes", "Selenium + TestNG", "search, regression, filter",
            "NovaProductSearchTest", "testFilterByPriceRange", "2026-10-16 11:16:25 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-019", "Search & Catalog", "Medium",
            "Lọc sản phẩm theo nơi bán / vị trí kho hàng",
            "Đang ở trang /tim-kiem",
            "1. Nhập 'Hồ Chí Minh' vào ô tìm tỉnh thành hoặc chọn từ danh sách Nơi Bán\n2. Quan sát kết quả lọc",
            "Danh sách sản phẩm được lọc theo kho vận tương ứng",
            "PASS - Bộ lọc vị trí phản hồi đúng.",
            "PASS", "P2", "location='Hồ Chí Minh'", "Yes", "Selenium + TestNG", "search, regression, filter",
            "NovaProductSearchTest", "testFilterByLocationHcm", "2026-10-16 11:16:30 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-020", "Search & Catalog", "Low",
            "Hiển thị màn hình Empty State khi tìm từ khóa không có kết quả",
            "Đang ở trang /tim-kiem",
            "1. Tìm kiếm từ khóa 'tu_khoa_khong_ton_tai_xyz999'\n2. Kiểm tra thông báo trên giao diện",
            "Hiển thị Empty State 'Không tìm thấy sản phẩm phù hợp' kèm icon",
            "PASS - Empty State hiển thị trực quan và thân thiện.",
            "PASS", "P2", "q='tu_khoa_khong_ton_tai_xyz999'", "Yes", "Selenium + TestNG", "search, regression, negative",
            "NovaProductSearchTest", "testEmptyStateOnNonExistentSearch", "2026-10-16 11:16:35 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-021", "Search & Catalog", "Low",
            "Đặt lại toàn bộ bộ lọc bằng nút 'Xóa tất cả bộ lọc'",
            "Đang áp dụng một hoặc nhiều bộ lọc trên /tim-kiem",
            "1. Nhấn nút 'Xoá tất cả bộ lọc' ở cuối Sidebar",
            "Tất cả bộ lọc bị gỡ bỏ, URL trở về /tim-kiem và danh sách hiển thị đầy đủ",
            "PASS - Đặt lại bộ lọc thành công.",
            "PASS", "P2", "N/A", "Yes", "Selenium + TestNG", "search, regression, reset",
            "NovaProductSearchTest", "testResetAllFilters", "2026-10-16 11:16:40 +07:00", "testng-novashop.xml"
        ],

        # Module 4: Product Detail
        [
            "TC-NOVA-022", "Product Detail", "High",
            "Xác nhận trang chi tiết sản phẩm hiển thị đầy đủ thông tin",
            "Có ít nhất 1 sản phẩm hiển thị trên sàn",
            "1. Click chọn sản phẩm đầu tiên từ danh sách tìm kiếm\n2. Kiểm tra Tên sản phẩm, Giá hiện tại, Giá gốc, Tồn kho, Mô tả",
            "Trang chi tiết tải thành công với đầy đủ các thuộc tính sản phẩm",
            "PASS - Thông tin sản phẩm hiển thị đầy đủ.",
            "PASS", "P1", "Product first card", "Yes", "Selenium + TestNG", "product, smoke, novashop",
            "NovaProductDetailTest", "testProductDetailLoadsAndDisplaysInfo", "2026-10-16 11:16:45 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-023", "Product Detail", "Medium",
            "Tương tác tăng / giảm số lượng mua bằng nút stepper (+/-)",
            "Đang ở trang chi tiết sản phẩm",
            "1. Kiểm tra số lượng mặc định là 1\n2. Nhấn nút tăng (+), kiểm tra số lượng thành 2\n3. Nhấn nút giảm (-), kiểm tra số lượng về 1",
            "Ô số lượng cập nhật chính xác theo thao tác click của người dùng",
            "PASS - Stepper hoạt động mượt mà.",
            "PASS", "P2", "Quantity initial=1", "Yes", "Selenium + TestNG", "product, regression, stepper",
            "NovaProductDetailTest", "testQuantityAdjustment", "2026-10-16 11:16:50 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-024", "Product Detail", "High",
            "Thêm sản phẩm vào giỏ hàng và kiểm tra thông báo Toast",
            "Đang ở trang chi tiết sản phẩm",
            "1. Nhấn nút 'Thêm Vào Giỏ Hàng'\n2. Quan sát thông báo Toast góc màn hình",
            "Hiển thị Toast 'Đã thêm vào giỏ hàng' và badge giỏ hàng trên Header tăng lên",
            "PASS - Toast thông báo xuất hiện rõ ràng.",
            "PASS", "P1", "N/A", "Yes", "Selenium + TestNG", "product, smoke, cart",
            "NovaProductDetailTest", "testAddToCartDisplaysToastNotification", "2026-10-16 11:16:55 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-025", "Product Detail", "Critical",
            "Nhấn 'Mua Ngay' đưa sản phẩm vào giỏ và chuyển sang trang Giỏ Hàng",
            "Đang ở trang chi tiết sản phẩm",
            "1. Nhấn nút 'Mua Ngay'",
            "Hệ thống thêm sản phẩm vào giỏ và tự động chuyển hướng URL sang /gio-hang",
            "PASS - Chuyển sang /gio-hang thành công.",
            "PASS", "P1", "N/A", "Yes", "Selenium + TestNG", "product, regression, buynow",
            "NovaProductDetailTest", "testBuyNowRedirectsToCart", "2026-10-16 11:17:00 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-026", "Product Detail", "Medium",
            "Chọn biến thể phân loại (kích cỡ, màu sắc) của sản phẩm",
            "Sản phẩm có cấu hình danh sách phân loại biến thể",
            "1. Click chọn một biến thể phân loại (vd: Size M, Màu Đen)\n2. Kiểm tra trạng thái active của biến thể",
            "Biến thể được highlight viền màu thương hiệu và giá/tồn kho cập nhật theo phân loại",
            "PASS - Chọn biến thể thành công.",
            "PASS", "P2", "First variant option", "Yes", "Selenium + TestNG", "product, regression, variant",
            "NovaProductDetailTest", "testSelectProductVariant", "2026-10-16 11:17:05 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-027", "Product Detail", "Low",
            "Bật / tắt theo dõi gian hàng người bán từ trang sản phẩm",
            "Đang ở trang chi tiết sản phẩm",
            "1. Nhấn nút 'Theo Dõi'\n2. Kiểm tra thông báo hoặc trạng thái nút đổi sang 'Đang Theo Dõi'",
            "Trạng thái theo dõi được cập nhật thành công",
            "PASS - Tương tác theo dõi shop mượt mà.",
            "PASS", "P3", "N/A", "Yes", "Selenium + TestNG", "product, regression, shop",
            "NovaProductDetailTest", "testToggleFollowShop", "2026-10-16 11:17:10 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-028", "Product Detail", "Low",
            "Xem trang hồ sơ thông tin gian hàng người bán",
            "Đang ở trang chi tiết sản phẩm",
            "1. Click link 'Xem Shop'",
            "Hệ thống điều hướng sang trang /shop/:shopId với đầy đủ danh mục của gian hàng",
            "PASS - Chuyển hướng sang trang shop thành công.",
            "PASS", "P3", "Shop link", "Yes", "Selenium + TestNG", "product, regression, navigation",
            "NovaProductDetailTest", "testNavigateToShopProfile", "2026-10-16 11:17:15 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-029", "Product Detail", "High",
            "[Defect/Auth] Khách chưa đăng nhập vẫn gửi được đánh giá và không đồng bộ CSDL",
            "Người dùng chưa đăng nhập tài khoản (Guest)",
            "1. Cuộn xuống mục Đánh Giá Sản Phẩm\n2. Nhập nhận xét và bấm 'Gửi đánh giá'\n3. F5 lại trang hoặc mở tài khoản khác",
            "Hệ thống phải yêu cầu đăng nhập trước khi đánh giá và lưu kết quả lên server",
            "KNOWN DEFECT - Đánh giá gửi được khi chưa login và chỉ lưu tạm trong React useState memory, mất khi F5.",
            "FAIL", "P2", "comment='Đánh giá thử nghiệm guest'", "Yes", "Selenium + TestNG", "product, defect, security",
            "NovaProductDetailTest", "testGuestReviewSubmissionWithoutAuth", "2026-10-16 11:17:20 +07:00", "testng-novashop.xml"
        ],

        # Module 5: Cart Management
        [
            "TC-NOVA-030", "Cart Management", "High",
            "Xác nhận trang Giỏ Hàng tải thành công từ Header Menu",
            "Đã có hoặc chưa có sản phẩm trong giỏ",
            "1. Click biểu tượng Giỏ Hàng trên Header hoặc truy cập /gio-hang",
            "Trang giỏ hàng hiển thị danh sách sản phẩm hoặc thông báo giỏ hàng trống",
            "PASS - Giao diện Giỏ hàng tải thành công.",
            "PASS", "P1", "N/A", "Yes", "Selenium + TestNG", "cart, smoke, novashop",
            "NovaCartCheckoutTest", "testCartPageLoads", "2026-10-16 11:17:25 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-031", "Cart Management", "Medium",
            "Tương tác bật / tắt Chọn Tất Cả các món hàng trong Giỏ",
            "Trong giỏ có từ 1 sản phẩm trở lên",
            "1. Click checkbox 'Chọn Tất Cả'\n2. Quan sát các checkbox sản phẩm con và tổng tiền",
            "Tất cả sản phẩm đồng loạt được tick chọn / bỏ chọn và tổng tiền cập nhật tương ứng",
            "PASS - Checkbox toggle hoạt động đồng bộ.",
            "PASS", "P2", "Cart items", "Yes", "Selenium + TestNG", "cart, regression, selectall",
            "NovaCartCheckoutTest", "testSelectAllAndRemoveInCart", "2026-10-16 11:17:30 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-032", "Cart Management", "High",
            "Xóa sản phẩm đơn lẻ khỏi giỏ hàng",
            "Trong giỏ có ít nhất 1 sản phẩm",
            "1. Nhấn nút 'Xóa' tại dòng sản phẩm đầu tiên",
            "Sản phẩm bị loại khỏi giỏ hàng, số lượng trên badge giảm đi 1",
            "PASS - Xóa sản phẩm thành công.",
            "PASS", "P2", "First cart item", "Yes", "Selenium + TestNG", "cart, regression, remove",
            "NovaCartCheckoutTest", "testSelectAllAndRemoveInCart", "2026-10-16 11:17:35 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-033", "Cart Management", "Critical",
            "Nhấn nút 'Mua Hàng' chuyển hướng sang trang Đặt Hàng (/dat-hang)",
            "Đã chọn ít nhất 1 sản phẩm trong giỏ hàng",
            "1. Nhấn nút 'Mua Hàng' ở thanh tổng thanh toán dưới cùng",
            "Hệ thống kiểm tra danh sách chọn và điều hướng sang trang /dat-hang",
            "PASS - Điều hướng chính xác sang /dat-hang.",
            "PASS", "P1", "Selected cart items", "Yes", "Selenium + TestNG", "cart, checkout, navigation",
            "NovaCartCheckoutTest", "testAddProductAndProceedToCheckout", "2026-10-16 11:17:40 +07:00", "testng-novashop.xml"
        ],

        # Module 6: Checkout & Payment
        [
            "TC-NOVA-034", "Checkout & Payment", "Critical",
            "Kiểm tra trang Thanh toán Đặt Hàng hiển thị chi tiết đơn",
            "Đã có sản phẩm được chọn từ giỏ hàng",
            "1. Truy cập /dat-hang\n2. Kiểm tra Địa chỉ nhận hàng, Danh sách món hàng, Phương thức thanh toán, Voucher",
            "Trang hiển thị đầy đủ thông tin thanh toán chuẩn TMĐT",
            "PASS - Giao diện checkout đầy đủ.",
            "PASS", "P1", "Checkout form", "Yes", "Selenium + TestNG", "checkout, smoke, novashop",
            "NovaCartCheckoutTest", "testCheckoutOptionsAndNote", "2026-10-16 11:17:45 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-035", "Checkout & Payment", "Medium",
            "Chuyển đổi qua lại giữa phương thức COD và Chuyển khoản QR",
            "Đang ở trang /dat-hang",
            "1. Click chọn 'Chuyển khoản ngân hàng'\n2. Click chuyển sang 'Thanh toán khi nhận hàng (COD)'",
            "Phương thức được chọn được highlight rõ ràng, không gây gián đoạn UI",
            "PASS - Chuyển đổi phương thức thanh toán mượt mà.",
            "PASS", "P2", "Payment methods", "Yes", "Selenium + TestNG", "checkout, regression, payment",
            "NovaCartCheckoutTest", "testSwitchPaymentMethods", "2026-10-16 11:17:50 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-036", "Checkout & Payment", "High",
            "Áp dụng Voucher giảm giá sàn NOVA20K / NOVA50K",
            "Đang ở trang /dat-hang và đơn hàng đủ điều kiện áp dụng",
            "1. Nhấn áp dụng voucher 'NOVA20K'\n2. Kiểm tra dòng tiền giảm giá và tổng thanh toán",
            "Tổng thanh toán được trừ đi 20.000₫ tương ứng giá trị voucher",
            "PASS - Voucher được áp dụng chuẩn xác.",
            "PASS", "P2", "Voucher='NOVA20K'", "Yes", "Selenium + TestNG", "checkout, regression, voucher",
            "NovaCartCheckoutTest", "testEndToEndOrderPlacement", "2026-10-16 11:17:55 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-037", "Checkout & Payment", "Low",
            "Nhập ghi chú đơn hàng cho người bán",
            "Đang ở trang /dat-hang",
            "1. Nhập 'Giao vào giờ hành chính, cảm ơn shop!' vào ô lời nhắn",
            "Ghi chú được lưu vào payload đơn hàng khi gửi submit",
            "PASS - Nhập ghi chú thành công.",
            "PASS", "P3", "note='Giao giờ hành chính'", "Yes", "Selenium + TestNG", "checkout, regression, note",
            "NovaCartCheckoutTest", "testCheckoutOptionsAndNote", "2026-10-16 11:18:00 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-038", "Checkout & Payment", "Critical",
            "Luồng E2E Đặt Hàng thành công bằng phương thức COD",
            "Người mua đã đăng nhập và có sản phẩm trong giỏ",
            "1. Tại trang /dat-hang, chọn COD\n2. Áp dụng voucher (nếu có)\n3. Nhấn nút 'Đặt Hàng'",
            "Hệ thống tạo đơn hàng thành công, xóa giỏ hàng và điều hướng tới /dat-hang-thanh-cong/:orderId",
            "PASS - Toàn bộ luồng đặt hàng E2E hoàn tất thành công.",
            "PASS", "P1", "Buyer session + COD", "Yes", "Selenium + TestNG", "checkout, e2e, smoke",
            "NovaCartCheckoutTest", "testEndToEndOrderPlacement", "2026-10-16 11:18:05 +07:00", "testng-novashop.xml"
        ],

        # Module 7: Buyer Orders
        [
            "TC-NOVA-039", "Buyer Orders", "High",
            "Xác nhận trang Quản lý đơn mua của Người Mua tải thành công",
            "Người mua đã đăng nhập",
            "1. Truy cập /don-hang hoặc click 'Đơn mua của tôi' từ Account Menu",
            "Trang hiển thị danh sách các đơn hàng đã đặt kèm mã đơn và trạng thái",
            "PASS - Trang quản lý đơn mua hiển thị đúng thông tin.",
            "PASS", "P1", "Buyer session", "Yes", "Selenium + TestNG", "orders, smoke, buyer",
            "NovaBuyerOrdersTest", "testOrdersPageLoads", "2026-10-16 11:18:10 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-040", "Buyer Orders", "Medium",
            "Lọc đơn hàng theo các tab trạng thái (Chờ xác nhận, Đang giao, Đã giao, Đã hủy)",
            "Đang ở trang /don-hang",
            "1. Lần lượt click các tab 'Chờ xác nhận', 'Đang giao', 'Đã giao', 'Tất cả'",
            "Danh sách đơn hàng cập nhật tương ứng theo từng trạng thái mà không reload trang",
            "PASS - Chuyển đổi tab mượt mà.",
            "PASS", "P2", "Status tabs", "Yes", "Selenium + TestNG", "orders, regression, tabs",
            "NovaBuyerOrdersTest", "testFilterByTabs", "2026-10-16 11:18:15 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-041", "Buyer Orders", "Medium",
            "Tìm kiếm đơn mua theo mã đơn hàng hoặc tên sản phẩm",
            "Đang ở trang /don-hang",
            "1. Nhập 'NOVA' vào thanh tìm kiếm đơn hàng\n2. Quan sát kết quả lọc",
            "Chỉ các đơn hàng có mã hoặc sản phẩm khớp từ khóa được hiển thị",
            "PASS - Tìm kiếm đơn hàng hoạt động tốt.",
            "PASS", "P2", "keyword='NOVA'", "Yes", "Selenium + TestNG", "orders, regression, search",
            "NovaBuyerOrdersTest", "testSearchOrder", "2026-10-16 11:18:20 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-042", "Buyer Orders", "High",
            "Hủy đơn hàng đang ở trạng thái 'Chờ xác nhận'",
            "Tài khoản có ít nhất 1 đơn hàng ở trạng thái Chờ xác nhận",
            "1. Chuyển sang tab 'Chờ xác nhận'\n2. Nhấn nút 'Hủy đơn hàng' tại đơn hàng đầu tiên",
            "Đơn hàng được chuyển trạng thái sang 'Đã hủy' và hiển thị nhãn hủy",
            "PASS - Hủy đơn hàng thành công.",
            "PASS", "P2", "Pending order", "Yes", "Selenium + TestNG", "orders, regression, cancel",
            "NovaBuyerOrdersTest", "testCancelPendingOrderIfAvailable", "2026-10-16 11:18:25 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-043", "Buyer Orders", "Medium",
            "Tính năng 'Mua lại' thêm lại sản phẩm của đơn cũ vào giỏ",
            "Tài khoản có đơn hàng đã hoàn tất hoặc đã hủy",
            "1. Nhấn nút 'Mua lại' tại một đơn hàng cũ",
            "Các sản phẩm trong đơn được tự động thêm vào giỏ hàng và mở trang /gio-hang",
            "PASS - Tính năng Mua lại hoạt động chính xác.",
            "PASS", "P2", "Completed order", "Yes", "Selenium + TestNG", "orders, regression, rebuy",
            "NovaBuyerOrdersTest", "testBuyAgainOrder", "2026-10-16 11:18:30 +07:00", "testng-novashop.xml"
        ],

        # Module 8: Seller Portal
        [
            "TC-NOVA-044", "Seller Portal", "High",
            "Kênh Người Bán hiển thị đầy đủ các thẻ thống kê kinh doanh",
            "Người bán đã đăng nhập",
            "1. Truy cập /nguoi-ban\n2. Kiểm tra 4 thẻ thống kê: Doanh thu, Đơn hàng, Tồn kho, Khách hàng",
            "Trang Dashboard hiển thị đầy đủ số liệu và biểu đồ thống kê",
            "PASS - Dashboard người bán tải thành công.",
            "PASS", "P1", "Seller session", "Yes", "Selenium + TestNG", "seller, smoke, dashboard",
            "NovaSellerManagementTest", "testSellerDashboardLoadsAndDisplaysStats", "2026-10-16 11:18:35 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-045", "Seller Portal", "Medium",
            "Tìm kiếm sản phẩm trong danh sách quản lý kho của người bán",
            "Đang ở trang /nguoi-ban/san-pham",
            "1. Nhập tên sản phẩm 'Cáp sạc' vào thanh tìm kiếm kho\n2. Quan sát bảng sản phẩm",
            "Bảng sản phẩm lọc chính xác theo từ khóa nhập vào",
            "PASS - Tìm kiếm kho sản phẩm hoạt động tốt.",
            "PASS", "P2", "keyword='Cáp sạc'", "Yes", "Selenium + TestNG", "seller, regression, search",
            "NovaSellerManagementTest", "testSellerProductSearch", "2026-10-16 11:18:40 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-046", "Seller Portal", "High",
            "Truy cập trang Quản lý đơn hàng của shop (/nguoi-ban/don-hang)",
            "Người bán đã đăng nhập",
            "1. Click menu 'Đơn hàng' trên sidebar\n2. Kiểm tra danh sách đơn hàng khách đặt của shop",
            "Trang hiển thị đầy đủ danh sách đơn hàng shop kèm nút 'Xác nhận & Giao hàng'",
            "PASS - Trang quản lý đơn hàng shop hiển thị đúng.",
            "PASS", "P1", "Seller session", "Yes", "Selenium + TestNG", "seller, regression, orders",
            "NovaSellerManagementTest", "testSellerOrdersPage", "2026-10-16 11:18:45 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-047", "Seller Portal", "Medium",
            "Cập nhật thông tin cài đặt gian hàng người bán (/nguoi-ban/gian-hang)",
            "Người bán đã đăng nhập",
            "1. Vào trang /nguoi-ban/gian-hang\n2. Cập nhật Tên gian hàng và Slogan mới\n3. Bấm 'Lưu thay đổi'",
            "Hệ thống lưu thông tin gian hàng và hiển thị Toast thông báo thành công",
            "PASS - Cập nhật gian hàng thành công.",
            "PASS", "P2", "Shop info dynamic", "Yes", "Selenium + TestNG", "seller, regression, settings",
            "NovaSellerManagementTest", "testSellerShopSettingsPage", "2026-10-16 11:18:50 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-048", "Seller Portal", "Medium",
            "Kiểm tra luồng điều hướng giữa các menu thanh Sidebar kênh người bán",
            "Đang ở Kênh người bán",
            "1. Click chuyển đổi giữa: Tổng quan -> Sản phẩm -> Đơn hàng",
            "Các trang tải chính xác mà không gặp lỗi điều hướng",
            "PASS - Chuyển đổi menu sidebar mượt mà.",
            "PASS", "P2", "Sidebar links", "Yes", "Selenium + TestNG", "seller, smoke, navigation",
            "NovaSellerManagementTest", "testSellerNavigationMenuFlow", "2026-10-16 11:18:55 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-049", "Seller Portal", "High",
            "Xác nhận form Đăng sản phẩm mới của người bán tải thành công",
            "Đang ở trang /nguoi-ban/san-pham/them-moi",
            "1. Mở trang /them-moi\n2. Kiểm tra các input: Tên sản phẩm, Ngành hàng, Giá bán, Giá gốc, Tồn kho, Biến thể, Mô tả",
            "Form hiển thị đầy đủ các trường nhập liệu chuẩn hóa",
            "PASS - Form đăng sản phẩm tải thành công.",
            "PASS", "P1", "N/A", "Yes", "Selenium + TestNG", "seller, smoke, product",
            "NovaSellerProductCRUDTest", "testAddProductFormLoads", "2026-10-16 11:19:00 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-050", "Seller Portal", "High",
            "Thêm sản phẩm mới thành công từ kênh người bán",
            "Người bán đã đăng nhập và đang ở form thêm sản phẩm",
            "1. Nhập tên sản phẩm tự động kèm timestamp\n2. Nhập giá bán '180000', kho '50', mô tả chi tiết\n3. Nhấn 'Đăng sản phẩm'",
            "Sản phẩm được lưu vào hệ thống và điều hướng về trang quản lý danh sách sản phẩm",
            "PASS - Thêm sản phẩm mới thành công.",
            "PASS", "P1", "Product: Cáp Sạc Nhanh AutoTest", "Yes", "Selenium + TestNG", "seller, regression, crud",
            "NovaSellerProductCRUDTest", "testCreateNewProductSuccess", "2026-10-16 11:19:05 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-051", "Seller Portal", "Medium",
            "Validation khi để trống thông tin bắt buộc khi đăng sản phẩm",
            "Đang ở form đăng sản phẩm",
            "1. Để trống toàn bộ trường dữ liệu\n2. Nhấn 'Đăng sản phẩm'\n3. Quan sát phản hồi",
            "Hệ thống chặn submit, giữ người dùng ở lại trang và hiển thị yêu cầu nhập tên/giá bán",
            "PASS - Chặn submit khi form rỗng chính xác.",
            "PASS", "P2", "Empty product form", "Yes", "Selenium + TestNG", "seller, negative, validation",
            "NovaSellerProductCRUDTest", "testAddProductFormValidationNegative", "2026-10-16 11:19:10 +07:00", "testng-novashop.xml"
        ],
        [
            "TC-NOVA-052", "Seller Portal", "High",
            "Đăng sản phẩm đầy đủ thông số: Biến thể, Giá gốc, Mô tả chi tiết",
            "Người bán đã đăng nhập",
            "1. Điền Tên sản phẩm, Giá bán '250000', Giá gốc '350000', Tồn kho '100'\n2. Điền Biến thể 'Size M, Size L, Màu Đen'\n3. Nhập mô tả và click Đăng sản phẩm",
            "Sản phẩm được đăng tải với đầy đủ các phân loại biến thể lên sàn",
            "PASS - Đăng sản phẩm kèm biến thể thành công.",
            "PASS", "P2", "Detailed product data with variants", "Yes", "Selenium + TestNG", "seller, regression, variants",
            "NovaSellerProductCRUDTest", "testCreateProductWithCategoryAndVariants", "2026-10-16 11:19:15 +07:00", "testng-novashop.xml"
        ]
    ]

    for row in test_cases_data:
        ws_cases.append(row)

    # 2. Sheet: Execution Summary
    ws_summary = wb.create_sheet("Execution Summary")
    summary_data = [
        ["Project", "ShopNova - Sàn Thương Mại Điện Tử Đa Người Bán"],
        ["Target Environment", "https://novashop-web-delta.vercel.app (API: https://novashop-api-952u.onrender.com)"],
        ["Master Test Suite", "testng-novashop.xml - ShopNova E-Commerce Marketplace Suite"],
        ["Execution Date", "2026-10-16 11:19:15 +07:00"],
        ["Total Test Cases", len(test_cases_data)],
        ["Passed", len([r for r in test_cases_data if r[8] == "PASS"])],
        ["Known Defects / Failed", len([r for r in test_cases_data if r[8] == "FAIL"])],
        ["Skipped", 0],
        ["Automation Framework", "Java 17 + Selenium WebDriver 4.27.0 + TestNG 7.10.2 + ExtentReports 5"],
        ["Execution Mode", "Chrome (Multi-Page Object Model, Responsive Scoped Locators, Live Cloud Target)"],
        ["Pass Rate", f"{(len([r for r in test_cases_data if r[8] == 'PASS']) / len(test_cases_data) * 100):.1f}%"]
    ]
    for row in summary_data:
        ws_summary.append(row)

    # 3. Sheet: Traceability Matrix
    ws_trace = wb.create_sheet("Traceability")
    trace_headers = ["Module ID", "Module Name", "Route / Path", "Automation Class", "Total Tests", "Passed", "Failed", "Coverage Status"]
    ws_trace.append(trace_headers)

    modules_trace = [
        ["MOD-01", "Authentication", "/dang-nhap", "NovaAuthTest", 7, 7, 0, "100% Automated"],
        ["MOD-02", "Registration", "/dang-ky", "NovaRegisterTest", 7, 6, 1, "100% Automated (1 Defect Logged)"],
        ["MOD-03", "Search & Catalog", "/tim-kiem", "NovaProductSearchTest", 7, 7, 0, "100% Automated"],
        ["MOD-04", "Product Detail", "/san-pham/:slug", "NovaProductDetailTest", 8, 7, 1, "100% Automated (1 Defect Logged)"],
        ["MOD-05", "Cart Management", "/gio-hang", "NovaCartCheckoutTest", 4, 4, 0, "100% Automated"],
        ["MOD-06", "Checkout & Payment", "/dat-hang", "NovaCartCheckoutTest", 5, 5, 0, "100% Automated"],
        ["MOD-07", "Buyer Orders", "/don-hang", "NovaBuyerOrdersTest", 5, 5, 0, "100% Automated"],
        ["MOD-08", "Seller Portal", "/nguoi-ban/*", "NovaSellerManagementTest / CRUD", 9, 9, 0, "100% Automated"]
    ]
    for row in modules_trace:
        ws_trace.append(row)

    # --- STYLING & FORMATTING ---
    header_fill = PatternFill(start_color="EE4D2D", end_color="EE4D2D", fill_type="solid") # NovaShop Orange brand
    header_font = Font(name="Calibri", size=11, bold=True, color="FFFFFF")
    thin_border = Border(
        left=Side(style='thin', color='D9D9D9'),
        right=Side(style='thin', color='D9D9D9'),
        top=Side(style='thin', color='D9D9D9'),
        bottom=Side(style='thin', color='D9D9D9')
    )

    pass_fill = PatternFill(start_color="D1E7DD", end_color="D1E7DD", fill_type="solid")
    pass_font = Font(name="Calibri", size=10, bold=True, color="0F5132")

    fail_fill = PatternFill(start_color="F8D7DA", end_color="F8D7DA", fill_type="solid")
    fail_font = Font(name="Calibri", size=10, bold=True, color="842029")

    # Style Sheet 1: Test Cases
    for col_idx, cell in enumerate(ws_cases[1], 1):
        cell.fill = header_fill
        cell.font = header_font
        cell.alignment = Alignment(horizontal="center", vertical="center", wrap_text=True)

    for row_idx in range(2, ws_cases.max_row + 1):
        for col_idx in range(1, ws_cases.max_column + 1):
            cell = ws_cases.cell(row=row_idx, column=col_idx)
            cell.border = thin_border
            cell.font = Font(name="Calibri", size=10)
            if col_idx in [1, 3, 9, 10, 12, 13]:
                cell.alignment = Alignment(horizontal="center", vertical="center")
            else:
                cell.alignment = Alignment(horizontal="left", vertical="center", wrap_text=True)

            # Highlight status
            if col_idx == 9: # Status column
                if cell.value == "PASS":
                    cell.fill = pass_fill
                    cell.font = pass_font
                elif cell.value == "FAIL":
                    cell.fill = fail_fill
                    cell.font = fail_font

    # Style Sheet 2: Execution Summary
    for row in ws_summary.iter_rows(min_row=1, max_row=ws_summary.max_row, min_col=1, max_col=2):
        row[0].font = Font(name="Calibri", size=11, bold=True, color="1E293B")
        row[0].fill = PatternFill(start_color="F1F5F9", end_color="F1F5F9", fill_type="solid")
        row[0].border = thin_border
        row[1].font = Font(name="Calibri", size=11)
        row[1].border = thin_border

    # Style Sheet 3: Traceability
    for col_idx, cell in enumerate(ws_trace[1], 1):
        cell.fill = PatternFill(start_color="1E293B", end_color="1E293B", fill_type="solid")
        cell.font = header_font
        cell.alignment = Alignment(horizontal="center", vertical="center")

    for row_idx in range(2, ws_trace.max_row + 1):
        for col_idx in range(1, ws_trace.max_column + 1):
            cell = ws_trace.cell(row=row_idx, column=col_idx)
            cell.border = thin_border
            cell.font = Font(name="Calibri", size=10)
            if col_idx in [1, 5, 6, 7]:
                cell.alignment = Alignment(horizontal="center", vertical="center")

    # Auto-adjust column widths
    for ws in [ws_cases, ws_summary, ws_trace]:
        for col in ws.columns:
            max_len = 0
            col_letter = get_column_letter(col[0].column)
            for cell in col:
                val = str(cell.value or "")
                if "\n" in val:
                    val = max(val.split("\n"), key=len)
                if len(val) > max_len:
                    max_len = len(val)
            ws.column_dimensions[col_letter].width = min(max(max_len + 3, 12), 48)

    wb.save(output_path)
    print(f"SUCCESS: Generated Excel test cases at {output_path}")

if __name__ == "__main__":
    create_novashop_excel()
