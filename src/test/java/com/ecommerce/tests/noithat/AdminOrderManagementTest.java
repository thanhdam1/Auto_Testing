package com.ecommerce.tests.noithat;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithat.AdminDashboardPage;
import com.ecommerce.pages.noithat.AdminOrderPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AdminOrderManagementTest extends BaseTest {

    private AdminOrderPage orderPage;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        openAdmin();
        AdminDashboardPage dashboardPage = new AdminDashboardPage();
        orderPage = dashboardPage.navigateToOrders();
    }

    @Test(groups = {"smoke", "admin", "orders"}, description = "Xác nhận trang Quản lý đơn hàng tải thành công và có thẻ thống kê")
    public void testAdminOrderPageLoadsSuccessfully() {
        ExtentReportManager.logInfo("Kiểm tra trang Quản lý đơn hàng sẵn sàng");
        Assert.assertTrue(orderPage.isPageLoaded(), "Trang Quản lý đơn hàng phải hiển thị tiêu đề hợp lệ.");

        int statsCards = orderPage.getStatsCardsCount();
        ExtentReportManager.logInfo("Số thẻ thống kê KPI đơn hàng: " + statsCards);
        Assert.assertTrue(statsCards >= 4, "Phải có ít nhất 4 thẻ thống kê số liệu đơn hàng và doanh thu.");

        int orderCount = orderPage.getVisibleOrderRowCount();
        ExtentReportManager.logInfo("Số lượng đơn hàng hiển thị trong bảng: " + orderCount);
        Assert.assertTrue(orderCount > 0, "Bảng đơn hàng phải có ít nhất một đơn hiển thị.");
    }

    @Test(groups = {"regression", "admin", "orders"}, description = "Tìm kiếm đơn hàng theo mã đơn hàng")
    public void testSearchOrderByCode() {
        String orderCode = "NT-202610-001";
        ExtentReportManager.logInfo("Tìm kiếm theo mã đơn hàng: " + orderCode);
        orderPage.searchOrder(orderCode);

        Assert.assertTrue(orderPage.hasOrderByCode(orderCode), 
                "Bảng đơn hàng phải tìm thấy đơn có mã: " + orderCode);
    }

    @Test(groups = {"regression", "admin", "orders"}, description = "Lọc đơn hàng theo trạng thái giao hàng")
    public void testFilterOrdersByStatus() {
        ExtentReportManager.logInfo("Lọc đơn hàng theo trạng thái 'dang-giao'");
        orderPage.filterByStatus("dang-giao");

        int filteredCount = orderPage.getVisibleOrderRowCount();
        ExtentReportManager.logInfo("Số đơn hàng đang giao: " + filteredCount);

        ExtentReportManager.logInfo("Xóa bộ lọc");
        orderPage.resetFilters();
        int allCount = orderPage.getVisibleOrderRowCount();
        Assert.assertTrue(allCount >= filteredCount, "Tổng số đơn sau khi xóa lọc phải >= số đơn khi đang lọc.");
    }
}
