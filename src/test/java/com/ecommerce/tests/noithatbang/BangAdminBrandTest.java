package com.ecommerce.tests.noithatbang;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithatbang.BangAdminBrandPage;
import com.ecommerce.pages.noithatbang.BangAdminDashboardPage;
import com.ecommerce.pages.noithatbang.BangAdminLoginPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * UI Test Suite for Website_NoiThat Admin Brand Management (/brands)
 * Port: 5173
 */
public class BangAdminBrandTest extends BaseTest {

    private BangAdminLoginPage loginPage;
    private BangAdminDashboardPage dashboardPage;
    private BangAdminBrandPage brandPage;

    @BeforeMethod
    public void setupTest() {
        openNoiThatBangAdmin();
        loginPage = new BangAdminLoginPage();
        loginPage.login("admin@noithat.com", "Admin@123");
        dashboardPage = new BangAdminDashboardPage();
        brandPage = dashboardPage.navigateToBrands();
        Assert.assertTrue(brandPage.isPageLoaded(), "Trang Quản lý Thương hiệu phải tải thành công");
    }

    @Test(description = "WNT_BRD_001: Trang Thương hiệu tải đúng, có bảng và nút Thêm mới")
    public void testBrandPageLoads() {
        Assert.assertTrue(brandPage.isPageLoaded(), "Trang Quản lý Thương hiệu phải hiển thị tiêu đề và nút Thêm mới");
        ExtentReportManager.logInfo("Trang Thương hiệu tải thành công với " + brandPage.getBrandCount() + " thương hiệu");
    }

    @Test(description = "WNT_BRD_002: Mở và đóng form Thêm thương hiệu mới")
    public void testOpenAndCloseBrandModal() {
        brandPage.openCreateModal();
        Assert.assertTrue(brandPage.isModalOpen(), "Modal thêm thương hiệu phải mở ra");
        brandPage.closeModal();
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {}
        Assert.assertFalse(brandPage.isModalOpen(), "Modal phải đóng khi bấm Hủy");
    }

    @Test(description = "WNT_BRD_003: Thêm thương hiệu mới thành công với tên unique")
    public void testCreateValidBrand() {
        brandPage.openCreateModal();
        String brandName = "Thương Hiệu " + System.currentTimeMillis();
        String brandSlug = "thuong-hieu-" + System.currentTimeMillis();

        ExtentReportManager.logInfo("Tạo thương hiệu mới: " + brandName);
        brandPage.fillBrandForm(brandName, brandSlug);
        brandPage.submitSave();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}
        Assert.assertFalse(brandPage.isModalOpen(), "Modal phải đóng lại sau khi lưu thành công");
    }

    @Test(description = "WNT_BRD_004: Xóa thương hiệu -> Xác nhận -> Hủy")
    public void testCancelDeleteBrandDialog() {
        if (brandPage.hasBrands()) {
            brandPage.clickDeleteFirstBrand();
            Assert.assertTrue(brandPage.isConfirmDeleteDialogDisplayed(), "Dialog Xác nhận xóa thương hiệu phải hiển thị");
            brandPage.cancelDeleteDialog();
            ExtentReportManager.logInfo("Đã hủy thao tác xóa thương hiệu");
        } else {
            ExtentReportManager.logInfo("Không có thương hiệu nào để test xóa");
        }
    }
}
