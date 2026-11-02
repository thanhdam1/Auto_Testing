package com.ecommerce.tests.noithat;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithat.AdminCustomerPage;
import com.ecommerce.pages.noithat.AdminDashboardPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AdminCustomerManagementTest extends BaseTest {

    private AdminCustomerPage customerPage;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        openAdmin();
        AdminDashboardPage dashboardPage = new AdminDashboardPage();
        customerPage = dashboardPage.navigateToCustomers();
    }

    @Test(groups = {"smoke", "admin", "customers"}, description = "Trang Quản lý khách hàng tải thành công với bảng danh sách khách hàng")
    public void testCustomerPageLoadsWithData() {
        ExtentReportManager.logInfo("Kiểm tra trang Quản lý khách hàng");
        Assert.assertTrue(customerPage.isPageLoaded(), "Trang quản lý khách hàng phải hiển thị tiêu đề.");
        int count = customerPage.getCustomerRowCount();
        ExtentReportManager.logInfo("Số lượng khách hàng trong bảng: " + count);
        Assert.assertTrue(count > 0, "Bảng khách hàng phải có dữ liệu hiển thị.");
    }

    @Test(groups = {"regression", "admin", "customers"}, description = "Tìm kiếm khách hàng theo tên trong danh sách")
    public void testSearchCustomerByName() {
        String keyword = customerPage.getFirstCustomerName();
        ExtentReportManager.logInfo("Tìm kiếm khách hàng theo tên thực tế: " + keyword);
        customerPage.searchCustomer(keyword);
        Assert.assertTrue(customerPage.hasCustomerInTable(keyword), "Phải tìm thấy khách hàng: " + keyword);
    }

    @Test(groups = {"regression", "admin", "customers"}, description = "Lọc khách hàng theo hạng thành viên và hoàn tác lọc")
    public void testFilterCustomerByTier() {
        ExtentReportManager.logInfo("Lọc khách hàng hạng Đồng");
        customerPage.filterByTier("Đồng");
        int countFiltered = customerPage.getCustomerRowCount();
        ExtentReportManager.logInfo("Số khách hàng sau khi lọc: " + countFiltered);

        ExtentReportManager.logInfo("Xóa bộ lọc");
        customerPage.resetFilters();
        int countReset = customerPage.getCustomerRowCount();
        Assert.assertTrue(countReset >= countFiltered, "Số khách hàng sau khi xóa lọc phải lớn hơn hoặc bằng lúc lọc.");
    }
}
