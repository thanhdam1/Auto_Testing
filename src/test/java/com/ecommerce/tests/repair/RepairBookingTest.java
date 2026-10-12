package com.ecommerce.tests.repair;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.repair.RepairBookingPage;
import com.ecommerce.reports.ExtentReportManager;
import com.ecommerce.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RepairBookingTest extends BaseTest {

    private RepairBookingPage bookingPage;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        String baseUrl = ConfigReader.get("repair.app.url", "http://localhost:5174");
        openUrl(baseUrl + "/dat-lich");
        bookingPage = new RepairBookingPage();
    }

    @Test(groups = {"smoke", "repair", "booking"}, description = "Xác nhận trang Đặt lịch hẹn sửa chữa tải thành công")
    public void testBookingPageLoadsSuccessfully() {
        ExtentReportManager.logInfo("Kiểm tra trang Đặt Lịch Hẹn Sửa Chữa");
        Assert.assertTrue(bookingPage.isPageLoaded(), "Trang Đặt Lịch Hẹn phải hiển thị form hợp lệ.");
    }

    @Test(groups = {"regression", "repair", "booking"}, description = "Điền form đặt lịch hẹn sửa chữa thiết bị")
    public void testFillBookingForm() {
        ExtentReportManager.logInfo("Điền thông tin đặt lịch hẹn");
        bookingPage.fillBookingForm(
                "Trần Văn Bình",
                "0987654321",
                "2026-10-20",
                "09:30",
                "Laptop Asus bị lỗi quạt tản nhiệt kêu to và nóng máy."
        );
        ExtentReportManager.logInfo("Gửi yêu cầu đặt lịch hẹn");
        bookingPage.submitBooking();
    }
}
