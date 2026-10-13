package com.ecommerce.tests.noithat;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithat.AdminDashboardPage;
import com.ecommerce.pages.noithat.AdminProductPage;
import com.ecommerce.reports.ExtentReportManager;
import com.ecommerce.utils.LogUtil;
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

    @Test(groups = {"regression", "admin", "products"}, description = "Thêm sản phẩm mới: kiểm tra toast, tìm thấy trong bảng rồi dọn dẹp dữ liệu test")
    public void testAddNewProductForm() {
        String suffix = uniqueSuffix();
        String prodName = "Bàn Trà Bắc Âu " + suffix;
        String prodSlug = "ban-tra-bac-au-" + suffix;
        String price = "8500000";

        ExtentReportManager.logInfo("Mở modal và điền thông tin sản phẩm mới: " + prodName);
        productPage.openAddProductModal();
        productPage.fillProductForm(
                prodName,
                prodSlug,
                price,
                "ban-an",
                "phong-khach",
                "Gỗ sồi tự nhiên nhập khẩu",
                "con-hang",
                "Bàn trà phòng khách phong cách Bắc Âu tối giản, mặt kính cường lực chịu nhiệt."
        );

        ExtentReportManager.logInfo("Lưu sản phẩm mới");
        productPage.submitProductForm();

        // F-02: trước đây test này chỉ log chứ không assert gì
        Assert.assertTrue(productPage.isToastMessageDisplayed(), "Phải hiển thị toast sau khi lưu sản phẩm.");
        String toastText = productPage.getToastText();
        ExtentReportManager.logInfo("Toast thông báo: " + toastText);
        Assert.assertTrue(toastText.contains(prodName),
                "Toast phải chứa tên sản phẩm vừa tạo. Thực tế: " + toastText);

        productPage.searchProduct(prodName);
        Assert.assertTrue(productPage.hasProductInTable(prodName),
                "Sản phẩm mới phải xuất hiện trong bảng khi tìm kiếm theo tên.");

        ExtentReportManager.logInfo("Dọn dẹp: xoá sản phẩm vừa tạo để tránh rác dữ liệu");
        try {
            productPage.deleteProductByName(prodName);
            Assert.assertFalse(productPage.hasProductInTable(prodName),
                    "Sản phẩm đã xoá không được còn trong bảng.");
        } catch (Exception e) {
            LogUtil.warn("Không dọn được sản phẩm test '{}': {}", prodName, e.getMessage());
        }
    }

    @Test(groups = {"regression", "admin", "products"}, description = "Chuyển đổi trạng thái kho hàng (Đang bán / Hết hàng / Nháp) cập nhật đúng dữ liệu dòng")
    public void testToggleProductStatus() {
        String productName = productPage.getFirstProductName();
        String before = productPage.getFirstProductStatusValue();
        String expected = AdminProductPage.nextStatusInCycle(before);
        ExtentReportManager.logInfo("Sản phẩm: " + productName + " | trạng thái trước: " + before + " | mong đợi: " + expected);

        productPage.toggleFirstProductStatus();

        // F-02: trước đây test này chỉ click mà không kiểm tra kết quả
        String after = productPage.getFirstProductStatusValue();
        Assert.assertNotEquals(after, before,
                "Trạng thái dòng sản phẩm phải thay đổi sau khi bấm nút chuyển trạng thái.");
        Assert.assertEquals(after, expected,
                "Trạng thái phải chuyển theo vòng con-hang -> het-hang -> nhap -> con-hang.");
        Assert.assertFalse(productPage.getFirstProductStatusLabel().isBlank(),
                "Nhãn trạng thái trên nút phải có nội dung.");
    }
}
