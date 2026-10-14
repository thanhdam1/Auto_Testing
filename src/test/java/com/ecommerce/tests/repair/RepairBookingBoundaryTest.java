package com.ecommerce.tests.repair;

import com.ecommerce.base.BaseTest;
import com.ecommerce.dataprovider.RepairBoundaryDataProviders;
import com.ecommerce.pages.repair.RepairBookingPage;
import com.ecommerce.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RepairBookingBoundaryTest extends BaseTest {

    private RepairBookingPage bookingPage;

    @BeforeMethod(alwaysRun = true)
    public void setUpBookingPage() {
        String baseUrl = ConfigReader.get("repair.app.url", "http://localhost:5173");
        openUrl(baseUrl + "/dat-lich");
        bookingPage = new RepairBookingPage();
        Assert.assertTrue(bookingPage.isPageLoaded(), "Trang đặt lịch phải tải trước khi kiểm thử dữ liệu.");
    }

    @Test(dataProvider = "repairBookingInputBoundaries",
            dataProviderClass = RepairBoundaryDataProviders.class,
            groups = {"regression", "repair", "booking"},
            description = "Kiểm tra dữ liệu biên và edge của form đặt lịch")
    public void testBookingFormRetainsBoundaryValues(
            String caseName, String fullName, String phone, String description) {
        bookingPage.fillBookingForm(fullName, phone, futureDate(3), "09:30", description);

        Assert.assertEquals(bookingPage.getEnteredFullName(), fullName,
                caseName + ": họ tên phải giữ nguyên dữ liệu nhập.");
        Assert.assertEquals(bookingPage.getEnteredPhone(), phone,
                caseName + ": số điện thoại phải giữ nguyên dữ liệu nhập.");
        Assert.assertEquals(bookingPage.getEnteredErrorDescription(), description,
                caseName + ": mô tả phải giữ nguyên dữ liệu nhập.");
    }

    @Test(dataProvider = "repairRequiredFields",
            dataProviderClass = RepairBoundaryDataProviders.class,
            groups = {"regression", "repair", "booking"},
            description = "Kiểm tra required validation của form đặt lịch")
    public void testBookingFormRejectsMissingRequiredField(
            String caseName, String fullName, String phone, String description) {
        bookingPage.fillBookingForm(fullName, phone, futureDate(3), "09:30", description);
        bookingPage.submitBooking();

        Assert.assertTrue(bookingPage.hasNativeValidationMessage(),
                caseName + ": form phải báo lỗi khi thiếu trường bắt buộc.");
    }

    private String futureDate(int daysFromNow) {
        return java.time.LocalDate.now().plusDays(daysFromNow).toString();
    }
}
