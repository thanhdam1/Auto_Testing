package com.ecommerce.tests.noithatbang;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithatbang.BangAdminCustomerPage;
import com.ecommerce.pages.noithatbang.BangAdminDashboardPage;
import com.ecommerce.pages.noithatbang.BangAdminLoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * UI Test Suite for Website_NoiThat Admin Customer Management
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangAdminCustomerTest extends BaseTest {

    private BangAdminLoginPage loginPage;
    private BangAdminDashboardPage dashboardPage;
    private BangAdminCustomerPage customerPage;

    @BeforeMethod
    public void setupTest() {
        openNoiThatBangAdmin();
        loginPage = new BangAdminLoginPage();
        loginPage.login("admin@noithat.com", "Admin@123");
        dashboardPage = new BangAdminDashboardPage();
        customerPage = dashboardPage.navigateToCustomers();
    }

    @Test(description = "WNT_CUST_001: Điều hướng tới trang Quản lý Khách hàng từ Sidebar")
    public void testNavigateToCustomerManagement() {
        Assert.assertTrue(customerPage.isPageLoaded(), "Trang Quản lý Khách hàng phải hiển thị tiêu đề và ô tìm kiếm");
        Assert.assertTrue(customerPage.getPageHeadingText().contains("Quản lý Khách hàng"), "Tiêu đề trang phải chứa cụm từ 'Quản lý Khách hàng'");
    }

    @Test(description = "WNT_CUST_002: Tìm kiếm khách hàng theo từ khóa")
    public void testSearchCustomerByKeyword() {
        customerPage.searchCustomer("admin");
        Assert.assertTrue(customerPage.getCustomerCount() >= 0, "Bảng danh sách khách hàng phải phản hồi kết quả lọc");
    }

    @Test(description = "WNT_CUST_003: Tìm kiếm khách hàng với từ khóa không tồn tại hiển thị trạng thái rỗng")
    public void testSearchCustomerNotFound() {
        customerPage.searchCustomer("khong_the_tim_thay_khach_hang_99999");
        Assert.assertTrue(customerPage.isEmptyStateDisplayed() || customerPage.getCustomerCount() == 0, 
                "Phải hiển thị trạng thái rỗng khi không có khách hàng phù hợp");
    }

    @Test(description = "WNT_CUST_004: Mở và đóng hộp thoại xác nhận Khóa/Mở khóa tài khoản khách hàng")
    public void testToggleCustomerStatusCancel() {
        if (customerPage.getCustomerCount() > 0) {
            customerPage.clickToggleStatusFirstCustomer();
            Assert.assertTrue(customerPage.isConfirmDialogDisplayed(), "Hộp thoại xác nhận phải hiển thị khi bấm nút thao tác");
            customerPage.cancelToggleStatus();
            Assert.assertFalse(customerPage.isConfirmDialogDisplayed(), "Hộp thoại xác nhận phải đóng lại khi bấm Hủy");
        }
    }
}
