package com.ecommerce.tests.noithat;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithat.AdminDashboardPage;
import com.ecommerce.pages.noithat.AdminProductPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AdminProductManagementTest extends BaseTest {

    private AdminProductPage productPage;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        openAdmin();
        AdminDashboardPage dashboardPage = new AdminDashboardPage();
        productPage = dashboardPage.navigateToProducts();
    }

    @Test(groups = {"smoke", "admin", "products"}, description = "Xác nhận trang Quản lý sản phẩm tải thành công")
    public void testAdminProductPageLoadsSuccessfully() {
        ExtentReportManager.logInfo("Kiểm tra trang Quản lý sản phẩm đã sẵn sàng");
        Assert.assertTrue(productPage.isPageLoaded(), "Trang Quản lý sản phẩm phải hiển thị tiêu đề hợp lệ.");
        
        int count = productPage.getVisibleProductRowCount();
        ExtentReportManager.logInfo("Số lượng sản phẩm ban đầu: " + count);
        Assert.assertTrue(count > 0, "Bảng sản phẩm phải có ít nhất một dòng dữ liệu.");
    }

    @Test(groups = {"regression", "admin", "products"}, description = "Mở modal thêm sản phẩm mới và kiểm tra các trường nhập liệu")
    public void testOpenAddProductModal() {
        ExtentReportManager.logInfo("Mở modal Thêm sản phẩm");
        productPage.openAddProductModal();
        Assert.assertTrue(productPage.isAddProductModalDisplayed(), "Modal Thêm sản phẩm phải hiển thị trên màn hình.");

        ExtentReportManager.logInfo("Đóng modal sau khi kiểm tra");
        productPage.closeAddProductModal();
    }

    @Test(groups = {"regression", "admin", "products"}, description = "Tìm kiếm sản phẩm theo tên chính xác")
    public void testSearchProductByName() {
        String keyword = "Sofa da Ý";
        ExtentReportManager.logInfo("Thực hiện tìm kiếm sản phẩm với từ khóa: " + keyword);
        productPage.searchProduct(keyword);

        Assert.assertTrue(productPage.hasProductInTable(keyword), 
                "Kết quả bảng sản phẩm phải chứa từ khóa tìm kiếm: " + keyword);
    }

    @Test(groups = {"regression", "admin", "products"}, description = "Lọc sản phẩm theo danh mục và hoàn tác bộ lọc")
    public void testFilterByCategoryAndReset() {
        ExtentReportManager.logInfo("Lọc sản phẩm theo danh mục: 'sofa'");
        productPage.filterByCategory("sofa");
        int sofaCount = productPage.getVisibleProductRowCount();
        ExtentReportManager.logInfo("Số sản phẩm sau khi lọc Sofa: " + sofaCount);

        ExtentReportManager.logInfo("Nhấn Xóa lọc để khôi phục danh sách đầy đủ");
        productPage.resetFilters();
        int resetCount = productPage.getVisibleProductRowCount();
        Assert.assertTrue(resetCount >= sofaCount, "Số sản phẩm sau khi xóa lọc phải lớn hơn hoặc bằng lúc đang lọc.");
    }

    @Test(groups = {"regression", "admin", "products"}, description = "Điền form và thêm sản phẩm mẫu mới vào hệ thống")
    public void testAddNewProductForm() {
        String prodName = "Bàn Trà Bắc Âu Luxury";
        String prodSlug = "ban-tra-bac-au-luxury";
        String price = "8500000";

        ExtentReportManager.logInfo("Mở modal và điền thông tin sản phẩm mới");
        productPage.openAddProductModal();
        productPage.fillProductForm(
                prodName,
                prodSlug,
                price,
                "ban",
                "phong-khach",
                "Gỗ sồi tự nhiên nhập khẩu",
                "con-hang",
                "Bàn trà phòng khách phong cách Bắc Âu tối giản, mặt kính cường lực chịu nhiệt."
        );

        ExtentReportManager.logInfo("Lưu sản phẩm mới");
        productPage.submitProductForm();

        boolean toastAppeared = productPage.isToastMessageDisplayed();
        ExtentReportManager.logInfo("Toast thông báo thành công: " + toastAppeared);
    }

    @Test(groups = {"regression", "admin", "products"}, description = "Chuyển đổi trạng thái kho hàng (Đang bán / Hết hàng / Nháp)")
    public void testToggleProductStatus() {
        ExtentReportManager.logInfo("Nhấp nút chuyển đổi trạng thái sản phẩm đầu tiên");
        productPage.toggleFirstProductStatus();
        ExtentReportManager.logInfo("Trạng thái sản phẩm đã được kích hoạt chuyển đổi");
    }
}
