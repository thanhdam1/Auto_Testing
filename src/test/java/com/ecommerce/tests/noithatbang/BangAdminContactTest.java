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
 * Port: 5173 / 1234
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangAdminContactTest extends BaseTest {

    private BangAdminLoginPage loginPage;
    private BangAdminDashboardPage dashboardPage;
    private BangAdminContactPage contactPage;

    @BeforeMethod
    public void setupTest() {
        // Chuẩn bị dữ liệu fixture xác định qua API TRƯỚC KHI mở trang UI
        BangTestFixtures.createContact(
                "Khách Hàng Auto Fixture",
                "auto_fixture_" + System.currentTimeMillis() + "@test.com",
                "0988776655",
                "Yêu cầu tư vấn thiết kế nội thất",
                "Nội dung tin nhắn liên hệ fixture để kiểm thử."
        );

        openNoiThatBangAdmin();
        loginPage = new BangAdminLoginPage();
        loginPage.login("admin@noithat.com", "Admin@123");
        dashboardPage = new BangAdminDashboardPage();
        contactPage = dashboardPage.navigateToContacts();
        contactPage.waitForDataLoaded();
        Assert.assertTrue(contactPage.isPageLoaded(), "Trang Hòm thư Liên hệ phải tải thành công");
    }

    @Test(description = "WNT_CNT_001: Trang Liên hệ admin tải đúng, hiển thị tiêu đề và danh sách tin nhắn")
    public void testContactPageLoads() {
        Assert.assertTrue(contactPage.isPageLoaded(), "Trang Hòm thư Liên hệ phải hiển thị tiêu đề");
        int count = contactPage.getContactCount();
        ExtentReportManager.logInfo("Trang Liên hệ tải thành công với " + count + " tin nhắn");
        Assert.assertTrue(count > 0, "Trang Liên hệ phải có ít nhất 1 tin nhắn từ dữ liệu fixture đã tạo");
    }

    @Test(description = "WNT_CNT_002: Bảng liên hệ hiển thị đúng cấu trúc các cột")
    public void testContactTableColumns() {
        Assert.assertTrue(contactPage.areTableColumnsDisplayed(), "Các cột trong bảng liên hệ phải hiển thị");
        ExtentReportManager.logInfo("Cấu trúc cột bảng liên hệ hợp lệ");
    }

    @Test(description = "WNT_CNT_003: Mở modal xem nội dung chi tiết tin nhắn liên hệ và đối chiếu dữ liệu")
    public void testViewContactDetailModal() {
        Assert.assertTrue(contactPage.hasContacts(), "Bảng liên hệ phải có tin nhắn để kiểm tra xem chi tiết");

        contactPage.openFirstContactDetail();
        Assert.assertTrue(contactPage.isDetailModalOpen(), "Modal chi tiết tin nhắn liên hệ phải mở ra");

        String modalText = contactPage.getDetailModalContent();
        ExtentReportManager.logInfo("Nội dung modal chi tiết: " + modalText);

        // Đối chiếu nội dung thực tế trên modal
        Assert.assertTrue(modalText.contains("Gửi từ") || modalText.contains("Nội dung") || modalText.contains("Số điện thoại"),
                "Modal phải hiển thị đúng cấu trúc thông tin chi tiết của tin nhắn liên hệ");

        contactPage.closeDetailModal();
        Assert.assertFalse(contactPage.isDetailModalOpen(), "Modal chi tiết phải đóng lại khi bấm Đóng");
        ExtentReportManager.logInfo("Đã xem và đóng modal chi tiết liên hệ thành công");
    }

    @Test(description = "WNT_CNT_004: Xóa liên hệ -> Dialog xác nhận -> Hủy -> Bản ghi vẫn tồn tại")
    public void testCancelDeleteContactDialog() {
        Assert.assertTrue(contactPage.hasContacts(), "Bảng liên hệ phải có dữ liệu để kiểm tra hủy xóa");

        contactPage.clickDeleteFirstContact();
        Assert.assertTrue(contactPage.isConfirmDeleteDialogDisplayed(), "Dialog Xác nhận xóa tin nhắn liên hệ phải hiển thị");

        contactPage.cancelDeleteDialog();
        ExtentReportManager.logInfo("Đã bấm Hủy trên dialog xác nhận xóa");

        // Xác minh bảng vẫn có dữ liệu và không bị xóa oan
        Assert.assertTrue(contactPage.hasContacts(), "Sau khi bấm Hủy, tin nhắn liên hệ vẫn phải còn tồn tại trong bảng");
    }
}
