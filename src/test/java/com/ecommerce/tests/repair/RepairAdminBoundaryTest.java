package com.ecommerce.tests.repair;

import com.ecommerce.base.BaseTest;
import com.ecommerce.dataprovider.RepairBoundaryDataProviders;
import com.ecommerce.pages.repair.RepairAdminDashboardPage;
import com.ecommerce.pages.repair.RepairLoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RepairAdminBoundaryTest extends BaseTest {

    private RepairAdminDashboardPage dashboardPage;

    @BeforeMethod(alwaysRun = true)
    public void setupAdmin() {
        openRepairAdmin();
        new RepairLoginPage().login("admin", "123456");
        dashboardPage = new RepairAdminDashboardPage();
        dashboardPage.waitUntilLoaded();
    }

    @Test(
            groups = {"boundary", "repair", "admin"},
            dataProvider = "repairAdminTicketSearchBoundaries",
            dataProviderClass = RepairBoundaryDataProviders.class)
    public void adminTicketSearchHandlesBoundaryQueries(
            String caseName, String query, int expectedRowCount) {
        dashboardPage.selectTicketsTab();
        dashboardPage.searchTickets(query);

        Assert.assertEquals(
                dashboardPage.getTicketSearchValue(),
                query,
                "Giá trị tìm kiếm phải được giữ nguyên cho case " + caseName);
        Assert.assertEquals(
                dashboardPage.getTicketDataRowCount(),
                expectedRowCount,
                "Số dòng kết quả không đúng cho case " + caseName);
        if (expectedRowCount == 0) {
            Assert.assertTrue(
                    dashboardPage.isTicketNoResultsDisplayed(),
                    "UI phải hiển thị trạng thái không có kết quả cho case " + caseName);
        }
    }

    @Test(
            groups = {"boundary", "repair", "admin"},
            dataProvider = "repairInventoryThresholdBoundaries",
            dataProviderClass = RepairBoundaryDataProviders.class)
    public void inventoryThresholdHonorsConfiguredLimits(
            String caseName, String threshold, String expectedNormalizedValue, boolean valid) {
        dashboardPage.selectInventoryTab();
        dashboardPage.setInventoryThreshold(threshold);

        Assert.assertEquals(
                dashboardPage.getInventoryThresholdValue(),
                expectedNormalizedValue,
                "Ngưỡng tồn kho phải được chuẩn hóa theo min/max cho case " + caseName);
        Assert.assertEquals(
                dashboardPage.getInventoryThresholdValidationMessage().isEmpty(),
                valid,
                "Trạng thái native validation không đúng cho case " + caseName);
    }

    @Test(groups = {"boundary", "repair", "admin"})
    public void inventoryStatusFilterAndSearchHandleEmptyAndKnownValues() {
        dashboardPage.selectInventoryTab();
        dashboardPage.searchInventory("");
        dashboardPage.filterInventoryByStatus("Còn hàng");

        Assert.assertTrue(
                dashboardPage.getInventoryRowCount() >= 1,
                "Bộ lọc Còn hàng phải trả về ít nhất một linh kiện hiện có.");

        dashboardPage.searchInventory("component-does-not-exist");
        Assert.assertEquals(
                dashboardPage.getInventoryRowCount(),
                0,
                "Tìm linh kiện không tồn tại phải trả về 0 dòng.");
    }

    @Test(groups = {"boundary", "repair", "admin"})
    public void softwarePackageControlsRespectSelectionDependencies() {
        dashboardPage.selectSoftwareTab();

        Assert.assertTrue(
                dashboardPage.getSoftwarePackageCount() >= 1,
                "Tab phần mềm phải hiển thị danh sách package.");
        Assert.assertEquals(
                dashboardPage.getEnabledSoftwarePackageCount(),
                0,
                "Checkbox package phải bị khóa khi chưa chọn phiếu.");
        Assert.assertTrue(
                dashboardPage.isSaveSoftwareButtonDisabled(),
                "Nút lưu phải bị khóa khi chưa chọn phiếu và package.");

        dashboardPage.selectSoftwareTicket("RP-512806 — Khach Test Booking (CHỜ KHÁM)");
        dashboardPage.selectSoftwareGroup("Windows");

        Assert.assertTrue(
                dashboardPage.getEnabledSoftwarePackageCount() >= 1,
                "Chọn phiếu phải mở khóa các package phần mềm.");
    }

    @Test(groups = {"boundary", "repair", "admin"})
    public void reportPeriodSelectorSupportsBothAvailableRanges() {
        dashboardPage.selectReportsTab();
        String defaultPeriod = dashboardPage.getSelectedReportPeriod();

        dashboardPage.selectReportPeriod("Tháng này");
        String monthPeriod = dashboardPage.getSelectedReportPeriod();

        Assert.assertNotEquals(
                monthPeriod,
                defaultPeriod,
                "Bộ lọc báo cáo phải thay đổi được từ giá trị mặc định.");
        Assert.assertTrue(
                dashboardPage.isReportsDisplayed(),
                "Nội dung báo cáo phải tiếp tục hiển thị sau khi đổi khoảng thời gian.");
    }
}
