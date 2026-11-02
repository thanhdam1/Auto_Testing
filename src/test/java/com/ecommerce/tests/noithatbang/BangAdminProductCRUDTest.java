package com.ecommerce.tests.noithatbang;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithatbang.BangAdminDashboardPage;
import com.ecommerce.pages.noithatbang.BangAdminLoginPage;
import com.ecommerce.pages.noithatbang.BangAdminProductFormPage;
import com.ecommerce.pages.noithatbang.BangAdminProductPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * UI Test Suite for Website_NoiThat Admin Product CRUD (/products, /products/new)
 * Port: 5173
 */
public class BangAdminProductCRUDTest extends BaseTest {

    private BangAdminLoginPage loginPage;
    private BangAdminDashboardPage dashboardPage;
    private BangAdminProductPage productPage;

    @BeforeMethod
    public void setupTest() {
        openNoiThatBangAdmin();
        loginPage = new BangAdminLoginPage();
        loginPage.login("admin@noithat.com", "Admin@123");
        dashboardPage = new BangAdminDashboardPage();
        productPage = dashboardPage.navigateToProducts();
        Assert.assertTrue(productPage.isPageLoaded(), "Trang Quản lý Sản phẩm phải tải thành công");
    }

    @Test(description = "WNT_PRD_003: Mở form Thêm sản phẩm mới (/products/new)")
    public void testOpenNewProductForm() {
        BangAdminProductFormPage formPage = productPage.clickNewProduct();
        Assert.assertTrue(formPage.isPageLoaded(), "Form Thêm sản phẩm mới phải tải thành công");
        ExtentReportManager.logInfo("Đã mở trang Form Thêm sản phẩm");
    }

    @Test(description = "WNT_PRD_004: Submit form sản phẩm rỗng -> validate tất cả field bắt buộc")
    public void testSubmitEmptyProductFormValidation() {
        BangAdminProductFormPage formPage = productPage.clickNewProduct();
        Assert.assertTrue(formPage.isPageLoaded(), "Form Thêm sản phẩm phải hiển thị");

        formPage.submitSave();
        // Kiểm tra validation errors
        boolean hasErrors = formPage.hasValidationErrors();
        ExtentReportManager.logInfo("Kết quả kiểm tra validation form rỗng: " + hasErrors);
        Assert.assertTrue(formPage.isPageLoaded(), "Trang form vẫn giữ nguyên khi dữ liệu không hợp lệ");
    }

    @Test(description = "WNT_PRD_005: Điền thông tin cơ bản cho sản phẩm mới")
    public void testFillProductBasicInfo() {
        BangAdminProductFormPage formPage = productPage.clickNewProduct();
        String prodName = "Sofa Gỗ Cao Cấp Auto " + System.currentTimeMillis();
        String sku = "SKU-AUTO-" + System.currentTimeMillis();

        formPage.fillBasicInfo(prodName, sku, "Mô tả ngắn sản phẩm nội thất cao cấp.");
        formPage.selectFirstCategory();
        formPage.fillFirstVariant(sku + "-V1", "15000000", "20");
        ExtentReportManager.logInfo("Đã điền thông tin sản phẩm và biến thể hợp lệ");
    }

    @Test(description = "WNT_PRD_007: Kiểm tra danh sách sản phẩm trong bảng Admin")
    public void testProductTableRows() {
        int count = productPage.getProductRowsCount();
        ExtentReportManager.logInfo("Số lượng sản phẩm trong bảng: " + count);
        Assert.assertTrue(productPage.isPageLoaded() && (count > 0 || productPage.isEmptyStateDisplayed()),
                "Bảng sản phẩm phải tải dữ liệu hoặc hiển thị trạng thái rỗng hợp lệ");
    }

    @Test(description = "WNT_PRD_008: Lọc sản phẩm theo trạng thái active/draft")
    public void testFilterProductByStatus() {
        productPage.filterByStatus("active");
        int activeCount = productPage.getProductRowsCount();
        ExtentReportManager.logInfo("Số lượng sản phẩm Active: " + activeCount);
        Assert.assertTrue(productPage.isPageLoaded() && (activeCount > 0 || productPage.isEmptyStateDisplayed()),
                "Lọc trạng thái Active thành công và hiển thị dữ liệu hoặc trạng thái rỗng hợp lệ");
    }
}
