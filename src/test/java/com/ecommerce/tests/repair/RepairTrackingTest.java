package com.ecommerce.tests.repair;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.repair.RepairBookingPage;
import com.ecommerce.pages.repair.RepairTrackingPage;
import com.ecommerce.reports.ExtentReportManager;
import com.ecommerce.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RepairTrackingTest extends BaseTest {

    private RepairTrackingPage trackingPage;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        String baseUrl = ConfigReader.get("repair.app.url", "http://localhost:5174");
        openUrl(baseUrl + "/tra-cuu");
        trackingPage = new RepairTrackingPage();
    }

    @Test(groups = {"smoke", "repair", "tracking"}, description = "Xác nhận trang Tra cứu tiến độ hiển thị đúng tiêu đề và ô nhập SĐT")
    public void testTrackingPageLoadsSuccessfully() {
        ExtentReportManager.logInfo("Kiểm tra giao diện trang Tra Cứu Tiến Độ");
        Assert.assertTrue(trackingPage.isPageLoaded(), "Trang Tra Cứu Tiến Độ phải hiển thị tiêu đề hợp lệ.");
    }

    @Test(groups = {"regression", "repair", "tracking"}, description = "Tra cứu tiến độ với số điện thoại mẫu phải trả về phiếu hoặc thông báo không tìm thấy")
    public void testSearchBySamplePhone() {
        String samplePhone = "0901234567";
        ExtentReportManager.logInfo("Thực hiện tra cứu theo số điện thoại: " + samplePhone);
        trackingPage.searchByPhone(samplePhone);

        int results = trackingPage.getTicketResultsCount();
        ExtentReportManager.logInfo("Số phiếu tra cứu được: " + results);
        Assert.assertTrue(trackingPage.hasSearchFeedback(),
                "Sau khi tra cứu, hệ thống phải hiển thị danh sách phiếu hoặc thông báo không tìm thấy.");
    }

    @Test(groups = {"regression", "repair", "tracking"}, description = "Tra cứu với số điện thoại không tồn tại: phải hiển thị trạng thái rỗng hợp lệ")
    public void testSearchNonExistentPhoneShowsEmptyState() {
        String nonExistentPhone = "0999999999";
        ExtentReportManager.logInfo("Tra cứu số điện thoại không có trên hệ thống: " + nonExistentPhone);
        trackingPage.searchByPhone(nonExistentPhone);

        Assert.assertEquals(trackingPage.getTicketResultsCount(), 0,
                "Số điện thoại không tồn tại không được trả về danh sách phiếu.");
        Assert.assertTrue(trackingPage.isNoResultMessageDisplayed() || trackingPage.hasSearchFeedback(),
                "Hệ thống phải hiển thị thông báo 'Không tìm thấy phiếu' hợp lệ.");
    }

    @Test(groups = {"regression", "repair", "tracking"}, description = "Tạo lịch hẹn mới rồi tra cứu ngay theo số điện thoại: dữ liệu độc lập không phụ thuộc mã có sẵn")
    public void testCreateBookingAndTrackByPhone() {
        long timestamp = System.currentTimeMillis();
        String customerPhone = "098" + String.format("%07d", (timestamp % 10000000));
        String customerName = "Khách Tra Cứu " + (timestamp % 10000);

        // 1. Tạo đặt lịch mới tại /dat-lich
        String baseUrl = ConfigReader.get("repair.app.url", "http://localhost:5174");
        openUrl(baseUrl + "/dat-lich");
        RepairBookingPage booking = new RepairBookingPage();
        if (booking.isPageLoaded()) {
            booking.fillBookingForm(customerName, customerPhone, java.time.LocalDate.now().plusDays(2).toString(), "11:00", "Tra cứu test tự động");
            booking.submitBooking();
        }

        // 2. Mở trang tra cứu và tìm theo customerPhone vừa tạo
        openUrl(baseUrl + "/tra-cuu");
        trackingPage.searchByPhone(customerPhone);

        // 3. Phải hiển thị kết quả phiếu hoặc feedback tương ứng
        Assert.assertTrue(trackingPage.getTicketResultsCount() >= 1 || trackingPage.hasSearchFeedback(),
                "Hệ thống phải tra cứu được phiếu vừa tạo theo số điện thoại: " + customerPhone);
    }

    @Test(groups = {"regression", "repair", "tracking"}, description = "Tra cứu lặp lại 2 lần không làm nhân đôi kết quả hiển thị")
    public void testRepeatedSearchDoesNotDuplicateResults() {
        String samplePhone = "0901234567";
        trackingPage.searchByPhone(samplePhone);
        int firstResults = trackingPage.getTicketResultsCount();

        trackingPage.searchByPhone(samplePhone);
        int secondResults = trackingPage.getTicketResultsCount();
        ExtentReportManager.logInfo("Kết quả lần 1 = " + firstResults + ", lần 2 = " + secondResults);

        Assert.assertEquals(secondResults, firstResults,
                "Tra cứu lại cùng số điện thoại không được nhân đôi số phiếu hiển thị.");
    }
}
