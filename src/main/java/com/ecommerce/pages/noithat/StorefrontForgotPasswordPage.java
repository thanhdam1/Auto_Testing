package com.ecommerce.pages.noithat;

import com.ecommerce.pages.base.BasePage;
import com.ecommerce.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object cho tính năng Quên mật khẩu & Xác thực OTP 3 bước (/quen-mat-khau).
 *
 * <p>Locators được đối chiếu trực tiếp với DOM frontend/src/pages/quen-mat-khau/index.astro:</p>
 * <ul>
 *   <li>Bước 1: #step-1-container, #step1-email, #btn-send-otp, #err-step1-email</li>
 *   <li>Bước 2: #step-2-container, #otp-real-input, .otp-cell, #btn-verify-otp, #err-step2-otp, #otp-timer-display, #btn-resend-otp, #btn-back-to-step1</li>
 *   <li>Bước 3: #step-3-container, #step3-password, #step3-confirm, #btn-save-password, #password-strength-container</li>
 *   <li>Toast: #auth-toast, #toast-body</li>
 * </ul>
 */
public class StorefrontForgotPasswordPage extends BasePage {

    // Tiêu đề & thanh tiến trình
    private final By headingForgotPassword = By.xpath("//h1[contains(text(),'Quên mật khẩu?')]");
    private final By step1Indicator = By.id("step-indicator-1");
    private final By step2Indicator = By.id("step-indicator-2");
    private final By step3Indicator = By.id("step-indicator-3");

    // Khối từng bước
    private final By step1Container = By.id("step-1-container");
    private final By step2Container = By.id("step-2-container");
    private final By step3Container = By.id("step-3-container");

    // Bước 1: Nhập email gửi OTP
    private final By inputEmail = By.id("step1-email");
    private final By btnSendOtp = By.id("btn-send-otp");
    private final By errEmail = By.id("err-step1-email");
    private final By linkBackToLogin = By.cssSelector("a[href='/dang-nhap']");

    // Bước 2: Nhập OTP 6 số
    private final By displayTargetEmail = By.id("display-target-email");
    private final By inputRealOtp = By.id("otp-real-input");
    private final By otpCells = By.cssSelector(".otp-cell");
    private final By btnVerifyOtp = By.id("btn-verify-otp");
    private final By errOtp = By.id("err-step2-otp");
    private final By otpTimer = By.id("otp-timer-display");
    private final By btnResendOtp = By.id("btn-resend-otp");
    private final By btnBackToStep1 = By.id("btn-back-to-step1");

    // Bước 3: Đặt mật khẩu mới
    private final By inputNewPassword = By.id("step3-password");
    private final By inputConfirmPassword = By.id("step3-confirm");
    private final By toggleNewPassword = By.id("toggle-step3-password");
    private final By toggleConfirmPassword = By.id("toggle-step3-confirm");
    private final By passwordStrengthContainer = By.id("password-strength-container");
    private final By passwordStrengthText = By.id("password-strength-text");
    private final By errNewPassword = By.id("err-step3-password");
    private final By errConfirmPassword = By.id("err-step3-confirm");
    private final By btnSavePassword = By.id("btn-save-password");

    // Toast thông báo
    private final By toastNotification = By.id("auth-toast");
    private final By toastBody = By.id("toast-body");

    // =========================================================
    // HÀM KIỂM TRA TRẠNG THÁI GIAO DIỆN
    // =========================================================

    public boolean isPageLoaded() {
        return isDisplayed(headingForgotPassword);
    }

    public boolean isStep1Active() {
        return isDisplayed(step1Container) && !isElementHidden(step1Container);
    }

    public boolean isStep2Active() {
        try {
            WebElement el = find(step2Container);
            return el.isDisplayed() && !el.getAttribute("class").contains("hidden");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isStep3Active() {
        try {
            WebElement el = find(step3Container);
            return el.isDisplayed() && !el.getAttribute("class").contains("hidden");
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isElementHidden(By locator) {
        try {
            WebElement el = find(locator);
            String classes = el.getAttribute("class");
            return classes != null && classes.contains("hidden");
        } catch (Exception e) {
            return true;
        }
    }

    // =========================================================
    // HÀM THAO TÁC BƯỚC 1
    // =========================================================

    public void enterEmail(String email) {
        type(inputEmail, email, "Địa chỉ Email khôi phục");
    }

    public void clickSendOtp() {
        click(btnSendOtp, "Nút Gửi mã xác thực OTP");
    }

    public String getEmailErrorMessage() {
        return getText(errEmail);
    }

    public boolean isEmailErrorDisplayed() {
        try {
            WebElement el = find(errEmail);
            return el.isDisplayed() && !el.getAttribute("class").contains("hidden");
        } catch (Exception e) {
            return false;
        }
    }

    public void clickBackToLogin() {
        click(linkBackToLogin, "Liên kết Đăng nhập ngay");
    }

    // =========================================================
    // HÀM THAO TÁC BƯỚC 2
    // =========================================================

    public void enterOtp(String otp) {
        WebElement el = getDriver().findElement(inputRealOtp);
        ((org.openqa.selenium.JavascriptExecutor) getDriver()).executeScript(
            "arguments[0].value = arguments[1];" +
            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", el, otp);
    }

    public void clickVerifyOtp() {
        click(btnVerifyOtp, "Nút Xác nhận mã OTP");
    }

    public String getTargetEmailDisplay() {
        return getText(displayTargetEmail);
    }

    public String getOtpErrorMessage() {
        return getText(errOtp);
    }

    public boolean isOtpErrorDisplayed() {
        try {
            WebElement el = find(errOtp);
            return el.isDisplayed() && !el.getAttribute("class").contains("hidden");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isResendOtpButtonDisplayed() {
        return isDisplayed(btnResendOtp);
    }

    public void clickBackToStep1() {
        click(btnBackToStep1, "Nút Thay đổi địa chỉ email");
    }

    // =========================================================
    // HÀM THAO TÁC BƯỚC 3
    // =========================================================

    public void enterNewPassword(String newPass, String confirmPass) {
        if (newPass != null) type(inputNewPassword, newPass, "Mật khẩu mới");
        if (confirmPass != null) type(inputConfirmPassword, confirmPass, "Xác nhận mật khẩu mới");
    }

    public void clickSavePassword() {
        click(btnSavePassword, "Nút Lưu mật khẩu mới");
    }

    public boolean isPasswordStrengthDisplayed() {
        try {
            WebElement el = find(passwordStrengthContainer);
            return el.isDisplayed() && !el.getAttribute("class").contains("hidden");
        } catch (Exception e) {
            return false;
        }
    }

    public String getPasswordStrengthText() {
        return getText(passwordStrengthText);
    }

    // =========================================================
    // TOAST
    // =========================================================

    public boolean isToastDisplayed() {
        try {
            WaitUtil.waitForVisibility(toastNotification, 5);
            return isDisplayed(toastNotification);
        } catch (Exception e) {
            return false;
        }
    }

    public String getToastMessage() {
        return getText(toastBody);
    }
}
