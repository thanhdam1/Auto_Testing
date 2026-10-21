package com.ecommerce.tests.novashop;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.novashop.NovaLoginPage;
import org.openqa.selenium.By;
import com.ecommerce.pages.novashop.NovaSellerDashboardPage;
import com.ecommerce.pages.novashop.NovaSellerProductsPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class NovaSellerManagementTest extends BaseTest {

    private NovaSellerDashboardPage sellerDashboard;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        openUrl(com.ecommerce.utils.ConfigReader.get("novashop.app.url", "https://novashop-web-delta.vercel.app") + "/dang-nhap");
        NovaLoginPage loginPage = new NovaLoginPage();
        loginPage.login("testseller", "Password123!");
        
        com.ecommerce.utils.WaitUtil.waitForUrlContains("/nguoi-ban", 15);
        sellerDashboard = new NovaSellerDashboardPage();
    }

    @Test(groups = {"smoke", "novashop", "seller"}, description = "Xác nhận Kênh người bán hiển thị đầy đủ thông tin thống kê")
    public void testSellerDashboardLoadsAndDisplaysStats() {
        ExtentReportManager.logInfo("Kiểm tra trang Kênh người bán (Seller Dashboard)");
        Assert.assertTrue(sellerDashboard.isPageLoaded(), "Trang Kênh người bán phải hiển thị tiêu đề hợp lệ.");

        int stats = sellerDashboard.getStatsCount();
        ExtentReportManager.logInfo("Số thẻ thống kê tình hình kinh doanh: " + stats);
        Assert.assertTrue(stats > 0, "Phải có các thẻ thống kê doanh thu, đơn hàng.");
    }

    @Test(groups = {"regression", "novashop", "seller"}, description = "Tìm kiếm sản phẩm trong danh sách quản lý của người bán")
    public void testSellerProductSearch() {
        ExtentReportManager.logInfo("Điều hướng đến trang Quản lý sản phẩm người bán");
        NovaSellerProductsPage productsPage = sellerDashboard.navigateToProducts();
        Assert.assertTrue(productsPage.isPageLoaded(), "Trang Quản lý sản phẩm người bán phải hiển thị.");

        String keyword = "Cáp sạc";
        ExtentReportManager.logInfo("Tìm kiếm theo tên sản phẩm: " + keyword);
        productsPage.searchProduct(keyword);
    }

    @Test(groups = {"regression", "novashop", "seller"}, description = "Xác nhận truy cập trang Quản lý đơn hàng của người bán")
    public void testSellerOrdersPage() {
        ExtentReportManager.logInfo("Điều hướng đến trang Quản lý đơn hàng shop (/nguoi-ban/don-hang)");
        com.ecommerce.pages.novashop.NovaSellerOrdersPage ordersPage = sellerDashboard.navigateToOrders();
        Assert.assertTrue(ordersPage.isPageLoaded(), "Trang Quản lý đơn hàng shop phải tải thành công.");
        int orderCount = ordersPage.getOrderCount();
        ExtentReportManager.logInfo("Số lượng đơn hàng hiển thị trong danh sách: " + orderCount);
    }

    @Test(groups = {"regression", "novashop", "seller"}, description = "Cập nhật thông tin cài đặt gian hàng người bán")
    public void testSellerShopSettingsPage() {
        ExtentReportManager.logInfo("Điều hướng đến trang Cài đặt gian hàng (/nguoi-ban/gian-hang)");
        com.ecommerce.pages.novashop.NovaSellerShopSettingsPage settingsPage = sellerDashboard.navigateToShopSettings();
        Assert.assertTrue(settingsPage.isPageLoaded(), "Trang Cài đặt gian hàng phải tải thành công.");

        String updatedName = "Shop Nova Chính Hãng " + System.currentTimeMillis();
        ExtentReportManager.logInfo("Cập nhật tên gian hàng: " + updatedName);
        settingsPage.updateShopName(updatedName);
        settingsPage.clickSave();
    }

    @Test(groups = {"smoke", "novashop", "seller"}, description = "Kiểm tra luồng chuyển hướng giữa các menu thanh Sidebar kênh người bán")
    public void testSellerNavigationMenuFlow() {
        ExtentReportManager.logInfo("Thử nghiệm chuyển đổi giữa các menu sidebar");
        NovaSellerProductsPage productsPage = sellerDashboard.navigateToProducts();
        Assert.assertTrue(productsPage.isPageLoaded(), "Phải mở trang Sản phẩm thành công.");

        openUrl(com.ecommerce.utils.ConfigReader.get("novashop.app.url", "https://novashop-web-delta.vercel.app") + "/nguoi-ban");
        sellerDashboard = new NovaSellerDashboardPage();

        com.ecommerce.pages.novashop.NovaSellerOrdersPage ordersPage = sellerDashboard.navigateToOrders();
        Assert.assertTrue(ordersPage.isPageLoaded(), "Phải mở trang Đơn hàng thành công.");
    }
}

