package com.ecommerce.tests.noithatbang;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithatbang.BangAdminDashboardPage;
import com.ecommerce.pages.noithatbang.BangAdminLoginPage;
import com.ecommerce.pages.noithatbang.BangAdminOrdersPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

/**
 * UI Test Suite for Website_NoiThat Admin Order Management (/orders)
 * Port: 5173 / 1234
 * Project: NguyenBang01/Website_NoiThat
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
        ordersPage.waitForTableLoaded();
        Assert.assertTrue(ordersPage.isPageLoaded(), "Trang Quản lý Đơn hàng phải tải thành công");
    }

    @Test(description = "WNT_ORD_001: Trang Đơn hàng admin tải đúng, hiển thị tiêu đề và thanh tìm kiếm")
    public void testOrdersPageLoads() {
        Assert.assertTrue(ordersPage.isPageLoaded(), "Trang Quản lý Đơn hàng phải hiển thị");
        int count = ordersPage.getOrderRowCount();
        ExtentReportManager.logInfo("Trang Đơn hàng tải thành công với " + count + " đơn");
        Assert.assertTrue(count >= 0, "Số lượng đơn hàng phải là số không âm");
    }

    @Test(description = "WNT_ORD_002: Bảng đơn hàng hiển thị đúng cấu trúc cột")
    public void testOrdersTableColumns() {
        Assert.assertTrue(ordersPage.areTableColumnsDisplayed(), "Các cột trong bảng đơn hàng phải hiển thị");
        ExtentReportManager.logInfo("Cấu trúc cột bảng đơn hàng hợp lệ");
    }

    @Test(description = "WNT_ORD_003: Lọc đơn hàng theo trạng thái Pending (Chờ xác nhận) và assert từng dòng")
    public void testFilterOrdersPending() {
        ordersPage.filterByStatus("pending");
        ordersPage.waitForTableLoaded();
        int count = ordersPage.getOrderRowCount();
        ExtentReportManager.logInfo("Số đơn Chờ xác nhận sau khi lọc: " + count);

        if (count > 0) {
            List<String> statuses = ordersPage.getAllOrderStatuses();
            for (String statusText : statuses) {
                boolean matchesPending = statusText.contains("Chờ xác nhận") || statusText.contains("Chờ chuyển khoản");
                Assert.assertTrue(matchesPending, "Tất cả các dòng phải thuộc trạng thái Chờ xác nhận/Chờ chuyển khoản, thực tế: " + statusText);
            }
        } else {
            Assert.assertTrue(ordersPage.isEmptyStateDisplayed(), "Khi không có đơn pending, phải hiển thị empty state rõ ràng");
        }
    }

    @Test(description = "WNT_ORD_004: Lọc đơn hàng theo trạng thái Confirmed (Đã xác nhận) và assert từng dòng")
    public void testFilterOrdersConfirmed() {
        ordersPage.filterByStatus("confirmed");
        ordersPage.waitForTableLoaded();
        int count = ordersPage.getOrderRowCount();
        ExtentReportManager.logInfo("Số đơn Đã xác nhận sau khi lọc: " + count);

        if (count > 0) {
            List<String> statuses = ordersPage.getAllOrderStatuses();
            for (String statusText : statuses) {
                Assert.assertTrue(statusText.contains("Đã xác nhận"), "Tất cả các dòng phải thuộc trạng thái Đã xác nhận, thực tế: " + statusText);
            }
        } else {
            Assert.assertTrue(ordersPage.isEmptyStateDisplayed(), "Khi không có đơn confirmed, phải hiển thị empty state rõ ràng");
        }
    }

    @Test(description = "WNT_ORD_005: Lọc đơn hàng theo trạng thái Shipping (Đang giao) và assert từng dòng")
    public void testFilterOrdersShipping() {
        ordersPage.filterByStatus("shipping");
        ordersPage.waitForTableLoaded();
        int count = ordersPage.getOrderRowCount();
        ExtentReportManager.logInfo("Số đơn Đang giao sau khi lọc: " + count);

        if (count > 0) {
            List<String> statuses = ordersPage.getAllOrderStatuses();
            for (String statusText : statuses) {
                Assert.assertTrue(statusText.contains("Đang giao"), "Tất cả các dòng phải thuộc trạng thái Đang giao, thực tế: " + statusText);
            }
        } else {
            Assert.assertTrue(ordersPage.isEmptyStateDisplayed(), "Khi không có đơn shipping, phải hiển thị empty state rõ ràng");
        }
    }

    @Test(description = "WNT_ORD_006: Lọc đơn hàng theo trạng thái Delivered (Hoàn thành) và assert từng dòng")
    public void testFilterOrdersDelivered() {
        ordersPage.filterByStatus("delivered");
        ordersPage.waitForTableLoaded();
        int count = ordersPage.getOrderRowCount();
        ExtentReportManager.logInfo("Số đơn Hoàn thành sau khi lọc: " + count);

        if (count > 0) {
            List<String> statuses = ordersPage.getAllOrderStatuses();
            for (String statusText : statuses) {
                Assert.assertTrue(statusText.contains("Hoàn thành"), "Tất cả các dòng phải thuộc trạng thái Hoàn thành, thực tế: " + statusText);
            }
        } else {
            Assert.assertTrue(ordersPage.isEmptyStateDisplayed(), "Khi không có đơn delivered, phải hiển thị empty state rõ ràng");
        }
    }

    @Test(description = "WNT_ORD_007: Tìm kiếm đơn hàng theo mã đơn thực tế và mã đơn không tồn tại")
    public void testSearchOrderByCode() {
        // 1. Lấy mã đơn hàng đầu tiên đang có sẵn trên bảng
        List<String> orderNumbers = ordersPage.getAllOrderNumbers();
        if (!orderNumbers.isEmpty()) {
            String targetOrderNumber = orderNumbers.get(0).replace("#", "").trim();
            ExtentReportManager.logInfo("Tìm kiếm với mã đơn hiện có: " + targetOrderNumber);
            ordersPage.searchOrder(targetOrderNumber);
            ordersPage.waitForTableLoaded();

            List<String> resultNumbers = ordersPage.getAllOrderNumbers();
            Assert.assertFalse(resultNumbers.isEmpty(), "Phải tìm thấy đơn hàng khi nhập đúng mã đơn: " + targetOrderNumber);
            for (String num : resultNumbers) {
                Assert.assertTrue(num.contains(targetOrderNumber), "Mã đơn hàng trả về phải khớp với từ khóa tìm kiếm: " + num);
            }
        }

        // 2. Tìm kiếm với mã đơn không tồn tại -> Bắt buộc phải có empty state và 0 dòng
        String nonExistentCode = "NON_EXIST_ORD_" + System.currentTimeMillis();
        ExtentReportManager.logInfo("Tìm kiếm với mã đơn không tồn tại: " + nonExistentCode);
        ordersPage.searchOrder(nonExistentCode);
        ordersPage.waitForTableLoaded();

        Assert.assertEquals(ordersPage.getOrderRowCount(), 0, "Tìm kiếm mã không tồn tại phải trả về 0 dòng");
        Assert.assertTrue(ordersPage.isEmptyStateDisplayed(), "Hệ thống phải hiển thị trạng thái rỗng khi không tìm thấy đơn");
    }
}
