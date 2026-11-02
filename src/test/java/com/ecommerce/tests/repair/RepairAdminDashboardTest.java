package com.ecommerce.tests.repair;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.repair.RepairAdminDashboardPage;
import com.ecommerce.pages.repair.RepairLoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RepairAdminDashboardTest extends BaseTest {

    private RepairAdminDashboardPage dashboardPage;

    @BeforeMethod(alwaysRun = true)
    public void setupAdmin() {
        openRepairAdmin();
        RepairLoginPage loginPage = new RepairLoginPage();
        loginPage.login("admin", "123456");
        dashboardPage = new RepairAdminDashboardPage();
        dashboardPage.waitUntilLoaded();
    }

    @Test(groups = {"smoke", "repair", "admin"}, description = "Xác nhận Admin dashboard và bảng phiếu sửa chữa tải thành công")
    public void adminDashboardLoadsWithRepairTickets() {
        Assert.assertTrue(dashboardPage.isPageLoaded(), "Admin dashboard phải hiển thị.");
        Assert.assertTrue(dashboardPage.isTicketTableDisplayed(),
                "Admin dashboard phải hiển thị bảng phiếu sửa chữa.");
    }

    @Test(groups = {"regression", "repair", "admin"}, description = "Tìm kiếm phiếu sửa chữa: phải trả về đúng phiếu khớp với mã tìm kiếm")
    public void adminCanSearchTicket() {
        dashboardPage.selectTicketsTab();
        
        // Lấy mã phiếu thực tế đầu tiên có sẵn trong bảng để test tìm kiếm động độc lập
        String ticketCode = dashboardPage.getFirstVisibleTicketCode();
        if (ticketCode == null || ticketCode.isBlank()) {
            ticketCode = "RP-512806";
        }
        
        dashboardPage.searchTickets(ticketCode);
        Assert.assertTrue(dashboardPage.getTicketDataRowCount() >= 1,
                "Tìm mã phiếu '" + ticketCode + "' phải trả về ít nhất một kết quả.");
        Assert.assertTrue(dashboardPage.allVisibleRowsContain(ticketCode),
                "Tất cả các dòng kết quả tìm kiếm phải khớp với mã phiếu '" + ticketCode + "'.");
    }

    @Test(groups = {"regression", "repair", "admin"}, description = "Tìm kiếm mã phiếu không tồn tại: phải hiển thị trạng thái rỗng hợp lệ")
    public void adminSearchNonExistentTicketShowsEmptyState() {
        dashboardPage.selectTicketsTab();
        dashboardPage.searchTickets("RP-NOT-EXIST-" + System.currentTimeMillis());
        
        Assert.assertEquals(dashboardPage.getTicketDataRowCount(), 0,
                "Mã phiếu không tồn tại phải trả về 0 dòng kết quả.");
        Assert.assertTrue(dashboardPage.isTicketNoResultsDisplayed(),
                "Phải hiển thị thông báo 'Không tìm thấy phiếu phù hợp.'");
    }

    @Test(groups = {"regression", "repair", "admin"}, description = "Chuyển đổi giữa các tab quản lý: tab phải được kích hoạt và hiển thị bảng hoặc trạng thái hợp lệ")
    public void adminCanOpenManagementSections() {
        dashboardPage.selectBookingsTab();
        Assert.assertTrue(dashboardPage.isBookingsContentDisplayed() || dashboardPage.isTabActive(1),
                "Tab Lịch hẹn phải được mở thành công.");
        Assert.assertTrue(dashboardPage.isTicketTableDisplayed() || dashboardPage.isTicketNoResultsDisplayed(),
                "Tab Lịch hẹn phải hiển thị bảng dữ liệu hoặc trạng thái rỗng hợp lệ.");

        dashboardPage.selectInventoryTab();
        Assert.assertTrue(dashboardPage.isInventoryDisplayed(),
                "Tab Kho linh kiện phải hiển thị nội dung.");

        dashboardPage.selectSoftwareTab();
        Assert.assertTrue(dashboardPage.isSoftwareDisplayed(),
                "Tab Gói phần mềm phải hiển thị nội dung.");

        dashboardPage.selectReportsTab();
        Assert.assertTrue(dashboardPage.isReportsDisplayed(),
                "Tab Báo cáo phải hiển thị nội dung.");
    }

    @Test(groups = {"regression", "repair", "admin"}, description = "Mở trung tâm thông báo và đăng xuất an toàn")
    public void adminCanOpenNotificationsAndLogout() {
        dashboardPage.selectNotificationsTab();
        Assert.assertTrue(dashboardPage.isNotificationCenterDisplayed(),
                "Trung tâm thông báo phải hiển thị.");

        dashboardPage.clickLogout();
        Assert.assertTrue(currentUrl().endsWith("/login"),
                "Đăng xuất phải đưa người dùng về trang login.");
    }
}
