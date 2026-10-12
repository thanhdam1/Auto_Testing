package com.ecommerce.tests.repair;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.repair.RepairBookingPage;
import com.ecommerce.pages.repair.RepairHomePage;
import com.ecommerce.pages.repair.RepairLoginPage;
import com.ecommerce.pages.repair.RepairTrackingPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RepairNavigationTest extends BaseTest {

    private RepairHomePage homePage;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        openRepairApp();
        homePage = new RepairHomePage();
    }

    @Test(groups = {"smoke", "repair", "navigation"}, description = "Xác nhận trang chủ hệ thống tiếp nhận sửa chữa hiển thị đúng")
    public void testHomePageLoadsSuccessfully() {
        ExtentReportManager.logInfo("Kiểm tra trang chủ QR Repair System");
        Assert.assertTrue(homePage.isPageLoaded(), "Trang chủ phải hiển thị logo và thanh menu.");
    }

    @Test(groups = {"smoke", "repair", "navigation"}, description = "Điều hướng từ trang chủ đến trang Tra cứu tiến độ")
    public void testNavigateToTracking() {
        ExtentReportManager.logInfo("Nhấp menu Tra Cứu Tiến Độ");
        RepairTrackingPage trackingPage = homePage.navigateToTracking();
        Assert.assertTrue(trackingPage.isPageLoaded(), "Trang Tra Cứu Tiến Độ phải tải thành công.");
    }

    @Test(groups = {"smoke", "repair", "navigation"}, description = "Điều hướng từ trang chủ vào Cổng KTV / Nội bộ")
    public void testNavigateToAdminPortal() {
        ExtentReportManager.logInfo("Nhấp nút Cổng KTV");
        RepairLoginPage loginPage = homePage.navigateToAdminPortal();
        Assert.assertTrue(loginPage.isPageLoaded(), "Cổng Nội Bộ KTV phải tải thành công.");
    }

    @Test(groups = {"regression", "repair", "navigation"}, description = "Kiểm tra hiển thị nút liên hệ nhanh Zalo / Hotline")
    public void testFloatingWidgetsPresence() {
        ExtentReportManager.logInfo("Kiểm tra widget Zalo & Hotline nổi trên màn hình");
        Assert.assertTrue(homePage.isFloatingWidgetsDisplayed(), "Widget liên hệ nhanh phải hiển thị trên trang chủ.");
    }
}
