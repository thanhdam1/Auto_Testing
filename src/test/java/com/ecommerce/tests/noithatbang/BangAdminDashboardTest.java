package com.ecommerce.tests.noithatbang;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithatbang.BangAdminDashboardPage;
import com.ecommerce.pages.noithatbang.BangAdminLoginPage;
import com.ecommerce.pages.noithatbang.BangAdminProductPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * UI Test Suite for Website_NoiThat Admin Dashboard & Statistics
 * Port: 5173
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangAdminDashboardTest extends BaseTest {

    private BangAdminLoginPage loginPage;
    private BangAdminDashboardPage dashboardPage;

    @BeforeMethod
    public void setupTest() {
        openNoiThatBangAdmin();
        loginPage = new BangAdminLoginPage();
        loginPage.login("admin@noithat.com", "Admin@123");
        dashboardPage = new BangAdminDashboardPage();
        Assert.assertTrue(dashboardPage.isPageLoaded(), "Admin Dashboard phải tải thành công sau khi đăng nhập");
    }

    @Test(description = "WNT_ADM_010: Dashboard hiển thị đầy đủ các thẻ chỉ số tổng quan (Stat Cards)")
    public void testOverviewStatCardsDisplayed() {
        Assert.assertTrue(dashboardPage.isRevenueCardDisplayed(), "Thẻ Tổng doanh thu phải hiển thị");
        Assert.assertTrue(dashboardPage.isOrdersCardDisplayed(), "Thẻ Tổng đơn hàng phải hiển thị");
        Assert.assertTrue(dashboardPage.isCustomersCardDisplayed(), "Thẻ Khách hàng phải hiển thị");
        Assert.assertTrue(dashboardPage.isProductsCardDisplayed(), "Thẻ Sản phẩm phải hiển thị");
        Assert.assertTrue(dashboardPage.getStatCardsCount() >= 4, "Phải có ít nhất 4 thẻ thống kê trên Dashboard");
        ExtentReportManager.logInfo("Đã xác thực đầy đủ 4 thẻ thống kê tổng quan");
    }

    @Test(description = "WNT_ADM_011: Biểu đồ doanh thu Recharts render thành công")
    public void testRevenueChartRendered() {
        Assert.assertTrue(dashboardPage.isRevenueChartDisplayed(), "Khung biểu đồ doanh thu Recharts phải hiển thị");
        ExtentReportManager.logInfo("Biểu đồ doanh thu Recharts hiển thị ổn định");
    }

    @Test(description = "WNT_ADM_012: Chuyển đổi bộ lọc khoảng ngày trên biểu đồ doanh thu (7, 14, 30 ngày)")
    public void testRevenueChartDayFilters() {
        dashboardPage.filterRevenueDays(7);
        dashboardPage.filterRevenueDays(30);
        dashboardPage.filterRevenueDays(14);
        Assert.assertTrue(dashboardPage.isRevenueChartDisplayed(), "Biểu đồ vẫn hiển thị tốt sau khi chuyển bộ lọc ngày");
        ExtentReportManager.logInfo("Đã kiểm tra chuyển đổi các mốc lọc ngày trên biểu đồ");
    }

    @Test(description = "WNT_ADM_013: Mục Đơn hàng mới nhất hiển thị trên Dashboard")
    public void testRecentOrdersSectionDisplayed() {
        Assert.assertTrue(dashboardPage.isRecentOrdersDisplayed(), "Khu vực Đơn hàng mới nhất phải hiển thị");
        ExtentReportManager.logInfo("Khu vực đơn hàng mới nhất tải thành công");
    }

    @Test(description = "WNT_ADM_014: Khu vực Cảnh báo sắp hết hàng (Tồn kho <= 10) hiển thị")
    public void testLowStockSectionDisplayed() {
        Assert.assertTrue(dashboardPage.isLowStockSectionDisplayed(), "Khu vực Cảnh báo sắp hết hàng phải hiển thị");
        ExtentReportManager.logInfo("Khu vực cảnh báo tồn kho tải thành công");
    }

    @Test(description = "WNT_ADM_015: Bảng Top sản phẩm bán chạy hiển thị trên Dashboard")
    public void testTopProductsSectionDisplayed() {
        Assert.assertTrue(dashboardPage.isTopProductsDisplayed(), "Khu vực Top Sản phẩm bán chạy phải hiển thị");
        ExtentReportManager.logInfo("Khu vực top sản phẩm bán chạy tải thành công");
    }

    @Test(description = "WNT_ADM_016: Điều hướng từ Sidebar sang trang Quản lý Sản phẩm")
    public void testNavigateToProductsFromDashboard() {
        BangAdminProductPage productPage = dashboardPage.navigateToProducts();
        Assert.assertTrue(productPage.isPageLoaded(), "Trang Quản lý Sản phẩm phải tải thành công");
        ExtentReportManager.logInfo("Điều hướng sang trang sản phẩm thành công");
    }

    @Test(description = "WNT_ADM_017: Toggle ẩn/hiện Sidebar menu")
    public void testToggleSidebar() {
        dashboardPage.clickToggleSidebar();
        Assert.assertTrue(dashboardPage.isPageLoaded(), "Trang vẫn hoạt động bình thường sau khi bấm toggle sidebar");
        ExtentReportManager.logInfo("Nút toggle sidebar hoạt động trơn tru");
    }
}
