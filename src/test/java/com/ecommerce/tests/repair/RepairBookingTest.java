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

    @Test(groups = {"regression", "repair", "booking"}, description = "Điền form đặt lịch hẹn sửa chữa: dữ liệu nhập phải được giữ đúng trên form")
    public void testFillBookingForm() {
        String fullName = "Trần Văn Bình";
        String phone = "0987654321";
        String description = "Laptop Asus bị lỗi quạt tản nhiệt kêu to và nóng máy.";

        ExtentReportManager.logInfo("Điền thông tin đặt lịch hẹn");
        bookingPage.fillBookingForm(fullName, phone, futureDate(3), "09:30", description);

        // F-02: trước đây test này chỉ điền rồi submit mà không assert gì
        Assert.assertEquals(bookingPage.getEnteredFullName(), fullName,
                "Ô họ tên phải giữ đúng giá trị đã nhập.");
        Assert.assertEquals(bookingPage.getEnteredPhone(), phone,
                "Ô số điện thoại phải giữ đúng giá trị đã nhập.");
        Assert.assertEquals(bookingPage.getEnteredErrorDescription(), description,
                "Ô mô tả lỗi phải giữ đúng giá trị đã nhập.");
    }

    @Test(groups = {"regression", "repair", "booking"}, description = "Submit form hợp lệ phải có phản hồi từ hệ thống (thành công hoặc thông báo)")
    public void testSubmitBookingWithValidDataShowsFeedback() {
        ExtentReportManager.logInfo("Điền form hợp lệ với ngày hẹn trong tương lai rồi submit");
        bookingPage.fillBookingForm(
                "Nguyễn Thị Hoa",
                "0912345678",
                futureDate(3),
                "14:00",
                "Máy in không nhận giấy và báo lỗi đèn đỏ liên tục."
        );

        bookingPage.submitBooking();

        Assert.assertTrue(bookingPage.hasSubmitFeedback(),
                "Sau khi submit, hệ thống phải hiển thị màn hình đặt lịch thành công hoặc thông báo lỗi/validate.");
    }

    /**
     * Ngày hẹn cách hôm nay {@code daysFromNow} ngày, định dạng yyyy-MM-dd.
     */
    private String futureDate(int daysFromNow) {
        return java.time.LocalDate.now().plusDays(daysFromNow).toString();
    }
}
