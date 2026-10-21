package com.ecommerce.tests.noithatbang;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithatbang.BangAdminBannerPage;
import com.ecommerce.pages.noithatbang.BangAdminDashboardPage;
import com.ecommerce.pages.noithatbang.BangAdminLoginPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * UI Test Suite for Website_NoiThat Admin Banner Management (/banners)
 * Port: 5173
 */
public class BangAdminBannerTest extends BaseTest {

    private BangAdminLoginPage loginPage;
    private BangAdminDashboardPage dashboardPage;
    private BangAdminBannerPage bannerPage;

    @BeforeMethod
    public void setupTest() {
        openNoiThatBangAdmin();
        loginPage = new BangAdminLoginPage();
        loginPage.login("admin@noithat.com", "Admin@123");
        dashboardPage = new BangAdminDashboardPage();
        bannerPage = dashboardPage.navigateToBanners();
        Assert.assertTrue(bannerPage.isPageLoaded(), "Trang Quản lý Banner phải tải thành công");
    }

    @Test(description = "WNT_BNR_001: Trang Banner tải đúng, có bảng và nút Thêm banner")
    public void testBannerPageLoads() {
        Assert.assertTrue(bannerPage.isPageLoaded(), "Trang Quản lý Banner phải hiển thị tiêu đề và nút Thêm");
        ExtentReportManager.logInfo("Trang Banner tải thành công với " + bannerPage.getBannerCount() + " banner");
    }

    @Test(description = "WNT_BNR_002: Mở và đóng form Thêm banner mới")
    public void testOpenAndCloseBannerModal() {
        bannerPage.openCreateModal();
        Assert.assertTrue(bannerPage.isModalOpen(), "Modal thêm banner phải mở ra");
        bannerPage.closeModal();
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {}
        Assert.assertFalse(bannerPage.isModalOpen(), "Modal phải đóng khi bấm Hủy");
    }

    @Test(description = "WNT_BNR_003: Thêm thông tin banner mới")
    public void testFillBannerForm() {
        bannerPage.openCreateModal();
        String title = "Khuyến Mãi Mùa Hè " + System.currentTimeMillis();
        bannerPage.fillBannerForm(title, "Giảm giá lên đến 30% toàn bộ sofa cao cấp", "/products?category=sofa");
        ExtentReportManager.logInfo("Đã điền thông tin banner: " + title);
        bannerPage.closeModal();
    }

    @Test(description = "WNT_BNR_004: Xóa banner -> Xác nhận -> Hủy")
    public void testCancelDeleteBannerDialog() {
        if (bannerPage.hasBanners()) {
            bannerPage.clickDeleteFirstBanner();
            Assert.assertTrue(bannerPage.isConfirmDeleteDialogDisplayed(), "Dialog Xác nhận xóa banner phải hiển thị");
            bannerPage.cancelDeleteDialog();
            ExtentReportManager.logInfo("Đã hủy thao tác xóa banner");
        } else {
            ExtentReportManager.logInfo("Không có banner nào để test xóa");
        }
    }
}
