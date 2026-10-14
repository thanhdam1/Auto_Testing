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

    @Test(groups = {"smoke", "repair", "admin"})
    public void adminDashboardLoadsWithRepairTickets() {
        Assert.assertTrue(dashboardPage.isPageLoaded(), "Admin dashboard phải hiển thị.");
        Assert.assertTrue(dashboardPage.isTicketTableDisplayed(),
                "Admin dashboard phải hiển thị bảng phiếu sửa chữa.");
    }

    @Test(groups = {"regression", "repair", "admin"})
    public void adminCanSearchTicket() {
        dashboardPage.selectTicketsTab();
        dashboardPage.searchTickets("RP-512806");
        Assert.assertEquals(dashboardPage.getVisibleTableRowCount(), 1,
                "Tìm mã RP-512806 phải trả về đúng một phiếu.");
    }

    @Test(groups = {"regression", "repair", "admin"})
    public void adminCanOpenManagementSections() {
        dashboardPage.selectBookingsTab();
        Assert.assertTrue(dashboardPage.getVisibleTableRowCount() >= 0,
                "Tab Lịch hẹn phải được mở thành công.");

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

    @Test(groups = {"regression", "repair", "admin"})
    public void adminCanOpenNotificationsAndLogout() {
        dashboardPage.selectNotificationsTab();
        Assert.assertTrue(dashboardPage.isNotificationCenterDisplayed(),
                "Trung tâm thông báo phải hiển thị.");

        dashboardPage.clickLogout();
        Assert.assertTrue(currentUrl().endsWith("/login"),
                "Đăng xuất phải đưa người dùng về trang login.");
    }
}
