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

        Assert.assertEquals(bookingPage.getEnteredFullName(), fullName,
                "Ô họ tên phải giữ đúng giá trị đã nhập.");
        Assert.assertEquals(bookingPage.getEnteredPhone(), phone,
                "Ô số điện thoại phải giữ đúng giá trị đã nhập.");
        Assert.assertEquals(bookingPage.getEnteredErrorDescription(), description,
                "Ô mô tả lỗi phải giữ đúng giá trị đã nhập.");
    }

    @Test(groups = {"smoke", "repair", "booking"}, description = "Submit form đặt lịch hợp lệ: hệ thống phải lưu lịch hẹn và hiển thị màn hình đặt lịch thành công")
    public void testSubmitBookingSuccess() {
        long timestamp = System.currentTimeMillis();
        String customerName = "Khách Hàng Test " + (timestamp % 10000);
        String phone = "09" + String.format("%08d", (timestamp % 100000000));
        String description = "Máy tính bàn khởi động không lên nguồn sau sự cố điện áp.";

        ExtentReportManager.logInfo("Điền form hợp lệ với ngày hẹn tương lai rồi thực hiện submit");
        bookingPage.fillBookingForm(customerName, phone, futureDate(3), "10:00", description);
        bookingPage.submitBooking();

        Assert.assertTrue(bookingPage.isBookingSuccessDisplayed(),
                "Sau khi submit form hợp lệ, hệ thống phải lưu lịch hẹn và hiển thị màn hình 'Đặt lịch thành công!'.");
    }

    @Test(groups = {"regression", "repair", "booking"}, description = "Submit form khi để trống trường bắt buộc: hệ thống phải validate và không được tạo phiếu thành công")
    public void testSubmitBookingValidationRejectsMissingFields() {
        ExtentReportManager.logInfo("Submit form khi để trống toàn bộ thông tin bắt buộc");
        bookingPage.submitBooking();

        Assert.assertFalse(bookingPage.isBookingSuccessVisibleNow(),
                "Hệ thống không được hiển thị màn hình thành công khi form còn để trống.");
        Assert.assertTrue(bookingPage.hasNativeValidationMessage(),
                "Trình duyệt/hệ thống phải kích hoạt validation message cho các trường bắt buộc.");
    }

    /**
     * Ngày hẹn cách hôm nay {@code daysFromNow} ngày, định dạng yyyy-MM-dd.
     */
    private String futureDate(int daysFromNow) {
        return java.time.LocalDate.now().plusDays(daysFromNow).toString();
    }
}
