package com.ecommerce.tests.repair;

import com.ecommerce.base.BaseTest;
import com.ecommerce.dataprovider.RepairBoundaryDataProviders;
import com.ecommerce.pages.repair.RepairLoginPage;
import com.ecommerce.pages.repair.RepairTechnicianDashboardPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RepairTechnicianDashboardTest extends BaseTest {

    private RepairTechnicianDashboardPage dashboardPage;

    @BeforeMethod(alwaysRun = true)
    public void setupTechnician() {
        openRepairAdmin();
        RepairLoginPage loginPage = new RepairLoginPage();
        loginPage.login("ktv.test", "123456");
        dashboardPage = new RepairTechnicianDashboardPage();
        dashboardPage.waitUntilLoaded();
    }

    @Test(groups = {"smoke", "repair", "technician"})
    public void technicianDashboardLoadsWithTicketActions() {
        Assert.assertTrue(dashboardPage.isLoaded(),
                "Dashboard KTV phải hiển thị sau khi đăng nhập.");
        Assert.assertTrue(dashboardPage.getTicketRowCount() >= 1,
                "Dashboard KTV phải có ít nhất một phiếu để thao tác.");
        Assert.assertTrue(dashboardPage.getPrintTicketButtonCount() >= 1,
                "Mỗi phiếu hiển thị phải có nút in phiếu.");
    }

    @Test(dataProvider = "repairTechnicianTicketSearchBoundaries",
            dataProviderClass = RepairBoundaryDataProviders.class,
            groups = {"boundary", "repair", "technician"})
    public void technicianTicketSearchHandlesBoundaryData(
            String caseName, String query, String expectedResult) {
        dashboardPage.searchTickets(query);

        Assert.assertEquals(dashboardPage.getSearchValue(), query,
                "Ô tìm kiếm phải giữ nguyên dữ liệu nhập cho case " + caseName);

        int rows = dashboardPage.getTicketRowCount();
        switch (expectedResult) {
            case "all":
                Assert.assertTrue(rows >= 1,
                        "Query rỗng/khoảng trắng phải hiển thị danh sách phiếu cho case " + caseName);
                break;
            case "one":
                Assert.assertEquals(rows, 1,
                        "Mã phiếu xác định phải trả về đúng một dòng cho case " + caseName);
                break;
            case "one-or-more":
                Assert.assertTrue(rows >= 1,
                        "Query khách hàng/số điện thoại phải trả về dữ liệu cho case " + caseName);
                break;
            case "none":
                Assert.assertEquals(rows, 0,
                        "Query không tồn tại phải không có dòng dữ liệu cho case " + caseName);
                Assert.assertTrue(dashboardPage.isNoTicketResultDisplayed(),
                        "Query không tồn tại phải hiển thị trạng thái không có kết quả.");
                break;
            default:
                Assert.fail("Expected result chưa được khai báo: " + expectedResult);
        }
    }

    @Test(groups = {"regression", "repair", "technician"})
    public void technicianCanOpenNotificationsAndLogout() {
        dashboardPage.openNotifications();
        Assert.assertTrue(dashboardPage.isNotificationCenterDisplayed(),
                "KTV phải mở được Trung tâm thông báo.");

        dashboardPage.logout();
        Assert.assertTrue(currentUrl().endsWith("/login"),
                "Đăng xuất KTV phải đưa người dùng về trang login.");
    }
}
