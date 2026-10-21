package com.ecommerce.tests.noithatbang;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithatbang.BangAdminContactPage;
import com.ecommerce.pages.noithatbang.BangAdminDashboardPage;
import com.ecommerce.pages.noithatbang.BangAdminLoginPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * UI Test Suite for Website_NoiThat Admin Contact Management (/contacts)
 * Port: 5173
 */
public class BangAdminContactTest extends BaseTest {

    private BangAdminLoginPage loginPage;
    private BangAdminDashboardPage dashboardPage;
    private BangAdminContactPage contactPage;

    @BeforeMethod
    public void setupTest() {
        openNoiThatBangAdmin();
        loginPage = new BangAdminLoginPage();
        loginPage.login("admin@noithat.com", "Admin@123");
        dashboardPage = new BangAdminDashboardPage();
        contactPage = dashboardPage.navigateToContacts();
        Assert.assertTrue(contactPage.isPageLoaded(), "Trang Hòm thư Liên hệ phải tải thành công");
    }

    @Test(description = "WNT_CNT_001: Trang Liên hệ admin tải đúng, hiển thị tiêu đề và danh sách tin nhắn")
    public void testContactPageLoads() {
        Assert.assertTrue(contactPage.isPageLoaded(), "Trang Hòm thư Liên hệ phải hiển thị tiêu đề");
        ExtentReportManager.logInfo("Trang Liên hệ tải thành công với " + contactPage.getContactCount() + " tin nhắn");
    }

    @Test(description = "WNT_CNT_002: Bảng liên hệ hiển thị đúng cấu trúc các cột")
    public void testContactTableColumns() {
        Assert.assertTrue(contactPage.areTableColumnsDisplayed(), "Các cột trong bảng liên hệ phải hiển thị");
        ExtentReportManager.logInfo("Cấu trúc cột bảng liên hệ hợp lệ");
    }

    @Test(description = "WNT_CNT_003: Mở modal xem nội dung chi tiết tin nhắn liên hệ")
    public void testViewContactDetailModal() {
        if (contactPage.hasContacts()) {
            contactPage.openFirstContactDetail();
            Assert.assertTrue(contactPage.isDetailModalOpen(), "Modal chi tiết tin nhắn liên hệ phải mở ra");
            contactPage.closeDetailModal();
            ExtentReportManager.logInfo("Đã xem và đóng modal chi tiết liên hệ thành công");
        } else {
            ExtentReportManager.logInfo("Chưa có tin nhắn liên hệ nào trong hệ thống");
        }
    }

    @Test(description = "WNT_CNT_004: Xóa liên hệ -> Dialog xác nhận -> Hủy")
    public void testCancelDeleteContactDialog() {
        if (contactPage.hasContacts()) {
            contactPage.clickDeleteFirstContact();
            Assert.assertTrue(contactPage.isConfirmDeleteDialogDisplayed(), "Dialog Xác nhận xóa tin nhắn liên hệ phải hiển thị");
            contactPage.cancelDeleteDialog();
            ExtentReportManager.logInfo("Đã hủy thao tác xóa tin nhắn liên hệ");
        } else {
            ExtentReportManager.logInfo("Không có tin nhắn nào để test xóa");
        }
    }
}
