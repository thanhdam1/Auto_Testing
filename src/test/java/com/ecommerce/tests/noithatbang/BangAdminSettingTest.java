package com.ecommerce.tests.noithatbang;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithatbang.BangAdminDashboardPage;
import com.ecommerce.pages.noithatbang.BangAdminLoginPage;
import com.ecommerce.pages.noithatbang.BangAdminSettingPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * UI Test Suite for Website_NoiThat Admin Settings (/settings)
 * Port: 5173
 */
public class BangAdminSettingTest extends BaseTest {

    private BangAdminLoginPage loginPage;
    private BangAdminDashboardPage dashboardPage;
    private BangAdminSettingPage settingPage;

    @BeforeMethod
    public void setupTest() {
        openNoiThatBangAdmin();
        loginPage = new BangAdminLoginPage();
        loginPage.login("admin@noithat.com", "Admin@123");
        dashboardPage = new BangAdminDashboardPage();
        settingPage = dashboardPage.navigateToSettings();
        Assert.assertTrue(settingPage.isPageLoaded(), "Trang Cài đặt Cửa hàng phải tải thành công");
    }

    @Test(description = "WNT_SET_001: Trang Cài đặt tải đúng các trường cấu hình")
    public void testSettingsPageLoads() {
        Assert.assertTrue(settingPage.isPageLoaded(), "Trang Cài đặt phải hiển thị tiêu đề và nút Lưu");
        ExtentReportManager.logInfo("Trang Cài đặt cửa hàng tải thành công");
    }

    @Test(description = "WNT_SET_002: Đọc thông tin tên cửa hàng & hotline cấu hình hiện tại")
    public void testReadCurrentStoreConfig() {
        String shopName = settingPage.getShopName();
        String hotline = settingPage.getHotline();
        ExtentReportManager.logInfo("Cấu hình hiện tại - Tên shop: " + shopName + " | Hotline: " + hotline);
        Assert.assertNotNull(shopName, "Tên cửa hàng không được null");
    }

    @Test(description = "WNT_SET_003: Cập nhật thông tin cửa hàng thành công")
    public void testUpdateStoreInfo() {
        settingPage.updateStoreInfo("Góc Nhà Đẹp - Luxury Furniture", "0931883988", "admin@noithat.com");
        settingPage.submitSettings();
        ExtentReportManager.logInfo("Đã lưu thông tin cấu hình cửa hàng mới");
        Assert.assertTrue(settingPage.isPageLoaded(), "Trang Cài đặt vẫn hoạt động ổn định sau khi lưu");
    }
}
