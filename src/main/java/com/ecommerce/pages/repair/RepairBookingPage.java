package com.ecommerce.pages.repair;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

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

    // ==================================================================
    // BỔ SUNG PHỤC VỤ F-02: test đặt lịch cần assert được dữ liệu đã nhập và phản hồi khi submit
    // ==================================================================

    private final By anyFeedbackMessage = By.xpath(
            "//*[contains(@class,'red') or contains(@class,'rose') or contains(@class,'emerald')]"
                    + "[normalize-space(text())!='']");

    public String getEnteredFullName() {
        return find(inputFullName).getAttribute("value");
    }

    public String getEnteredPhone() {
        return find(inputPhone).getAttribute("value");
    }

    public String getEnteredErrorDescription() {
        return getDriver().findElement(txtErrorDescription).getAttribute("value");
    }

    /**
     * Kiểm tra màn hình thành công "ngay lập tức" (không chờ 10 giây như {@link #isBookingSuccessDisplayed()}).
     */
    public boolean isBookingSuccessVisibleNow() {
        try {
            return getDriver().findElement(successMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Sau khi submit, hệ thống phải phản hồi: hoặc màn hình đặt lịch thành công, hoặc thông báo lỗi/validate.
     */
    public boolean hasSubmitFeedback() {
        if (isBookingSuccessVisibleNow()) {
            return true;
        }
        try {
            return getDriver().findElement(anyFeedbackMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasNativeValidationMessage() {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        Object message = js.executeScript(
                "return Array.from(document.querySelectorAll('form input, form textarea, form select'))"
                        + ".map(function(e) { return e.validationMessage; })"
                        + ".find(function(value) { return value && value.trim() !== ''; });");
        return message != null && !message.toString().isBlank();
    }
}
