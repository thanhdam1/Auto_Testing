package com.ecommerce.pages.repair;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;

/**
 * Page Object for Repair Appointment Booking Form (/dat-lich)
 */
public class RepairBookingPage extends BasePage {

    // Form Controls
    private final By headingTitle = By.xpath("//h1[contains(text(),'Đặt Lịch Hẹn Sửa Chữa')]");
    private final By selectServiceType = By.cssSelector("form select");
    private final By inputFullName = By.cssSelector("input[placeholder*='Nguyễn Văn A']");
    private final By inputPhone = By.cssSelector("input[placeholder*='0901234567']");
    private final By inputDate = By.cssSelector("input[type='date']");
    private final By inputTime = By.cssSelector("input[type='time']");
    private final By txtErrorDescription = By.cssSelector("form textarea");
    private final By btnSubmit = By.cssSelector("form button[type='submit']");

    // Success Screen
    private final By successMessage = By.xpath("//h2[contains(text(),'Đặt lịch thành công!')]");

    public boolean isPageLoaded() {
        return isDisplayed(headingTitle);
    }

    public void fillBookingForm(String fullName, String phone, String date, String time, String errorDesc) {
        type(inputFullName, fullName, "Họ và tên khách hàng");
        type(inputPhone, phone, "Số điện thoại");
        if (date != null && !date.isEmpty()) {
            type(inputDate, date, "Ngày hẹn");
        }
        if (time != null && !time.isEmpty()) {
            type(inputTime, time, "Giờ hẹn");
        }
        type(txtErrorDescription, errorDesc, "Mô tả chi tiết lỗi");
    }

    public void submitBooking() {
        click(btnSubmit, "Nút ĐẶT LỊCH NGAY");
    }

    public boolean isBookingSuccessDisplayed() {
        return isDisplayed(successMessage);
    }
}
