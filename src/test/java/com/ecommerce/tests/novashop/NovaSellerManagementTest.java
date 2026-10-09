package com.ecommerce.tests.novashop;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.novashop.NovaLoginPage;
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
        openNovaShop();
        openUrl(com.ecommerce.utils.ConfigReader.get("novashop.app.url", "http://localhost:5173") + "/dang-nhap");
        NovaLoginPage loginPage = new NovaLoginPage();
        loginPage.login("ankerseller", "123456");
        openNovaShopSeller();
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
}
