package com.ecommerce.tests.noithatbang;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithatbang.BangAdminDashboardPage;
import com.ecommerce.pages.noithatbang.BangAdminLoginPage;
import com.ecommerce.pages.noithatbang.BangAdminOrdersPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * UI Test Suite for Website_NoiThat Admin Order Management (/orders)
 * Port: 5173
 */
public class BangAdminOrderTest extends BaseTest {

    private BangAdminLoginPage loginPage;
    private BangAdminDashboardPage dashboardPage;
    private BangAdminOrdersPage ordersPage;

    @BeforeMethod
    public void setupTest() {
        openNoiThatBangAdmin();
        loginPage = new BangAdminLoginPage();
        loginPage.login("admin@noithat.com", "Admin@123");
        dashboardPage = new BangAdminDashboardPage();
        ordersPage = dashboardPage.navigateToOrders();
        Assert.assertTrue(ordersPage.isPageLoaded(), "Trang Quản lý Đơn hàng phải tải thành công");
    }

    @Test(description = "WNT_ORD_001: Trang Đơn hàng admin tải đúng, hiển thị tiêu đề và thanh tìm kiếm")
    public void testOrdersPageLoads() {
        Assert.assertTrue(ordersPage.isPageLoaded(), "Trang Quản lý Đơn hàng phải hiển thị");
        ExtentReportManager.logInfo("Trang Đơn hàng tải thành công với " + ordersPage.getOrderRowCount() + " đơn");
    }

    @Test(description = "WNT_ORD_002: Bảng đơn hàng hiển thị đúng cấu trúc cột")
    public void testOrdersTableColumns() {
        Assert.assertTrue(ordersPage.areTableColumnsDisplayed(), "Các cột trong bảng đơn hàng phải hiển thị");
        ExtentReportManager.logInfo("Cấu trúc cột bảng đơn hàng hợp lệ");
    }

    @Test(description = "WNT_ORD_003: Lọc đơn hàng theo trạng thái Pending (Chờ xác nhận)")
    public void testFilterOrdersPending() {
        ordersPage.filterByStatus("pending");
        int count = ordersPage.getOrderRowCount();
        ExtentReportManager.logInfo("Số đơn Chờ xác nhận: " + count);
        Assert.assertTrue(ordersPage.isPageLoaded() && (count > 0 || ordersPage.isEmptyStateDisplayed()),
                "Lọc trạng thái Pending thành công và hiển thị danh sách hoặc trạng thái rỗng hợp lệ");
    }

    @Test(description = "WNT_ORD_004: Lọc đơn hàng theo trạng thái Confirmed (Đã xác nhận)")
    public void testFilterOrdersConfirmed() {
        ordersPage.filterByStatus("confirmed");
        int count = ordersPage.getOrderRowCount();
        ExtentReportManager.logInfo("Số đơn Đã xác nhận: " + count);
        Assert.assertTrue(ordersPage.isPageLoaded() && (count > 0 || ordersPage.isEmptyStateDisplayed()),
                "Lọc trạng thái Confirmed thành công và hiển thị danh sách hoặc trạng thái rỗng hợp lệ");
    }

    @Test(description = "WNT_ORD_005: Lọc đơn hàng theo trạng thái Shipping (Đang giao)")
    public void testFilterOrdersShipping() {
        ordersPage.filterByStatus("shipping");
        int count = ordersPage.getOrderRowCount();
        ExtentReportManager.logInfo("Số đơn Đang giao: " + count);
        Assert.assertTrue(ordersPage.isPageLoaded() && (count > 0 || ordersPage.isEmptyStateDisplayed()),
                "Lọc trạng thái Shipping thành công và hiển thị danh sách hoặc trạng thái rỗng hợp lệ");
    }

    @Test(description = "WNT_ORD_006: Lọc đơn hàng theo trạng thái Delivered (Hoàn thành)")
    public void testFilterOrdersDelivered() {
        ordersPage.filterByStatus("delivered");
        int count = ordersPage.getOrderRowCount();
        ExtentReportManager.logInfo("Số đơn Hoàn thành: " + count);
        Assert.assertTrue(ordersPage.isPageLoaded() && (count > 0 || ordersPage.isEmptyStateDisplayed()),
                "Lọc trạng thái Delivered thành công và hiển thị danh sách hoặc trạng thái rỗng hợp lệ");
    }

    @Test(description = "WNT_ORD_007: Tìm kiếm đơn hàng theo mã đơn")
    public void testSearchOrderByCode() {
        ordersPage.searchOrder("FS-");
        int count = ordersPage.getOrderRowCount();
        ExtentReportManager.logInfo("Kết quả tìm kiếm đơn hàng: " + count);
        Assert.assertTrue(ordersPage.isPageLoaded() && (count > 0 || ordersPage.isEmptyStateDisplayed()),
                "Tìm kiếm đơn hàng hoạt động tốt và hiển thị danh sách hoặc trạng thái rỗng hợp lệ");
    }
}
