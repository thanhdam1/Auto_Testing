package com.ecommerce.pages.noithat;

import com.ecommerce.pages.base.BasePage;
import com.ecommerce.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Page Object for Storefront Register Page (/dang-ky).
 */
public class StorefrontRegisterPage extends BasePage {

    private final By heading = By.cssSelector("h1");
    private final By inputName = By.id("reg-name");
    private final By inputEmail = By.id("reg-email");
    private final By inputPhone = By.id("reg-phone");
    private final By inputPassword = By.id("reg-password");
    private final By inputConfirm = By.id("reg-confirm");
    private final By checkboxTerms = By.id("reg-terms");
    private final By btnSubmit = By.id("btn-submit");
    private final By btnGoogle = By.id("btn-google-signup");

    // Password strength indicator
    private final By strengthContainer = By.id("password-strength-container");
    private final By strengthText = By.id("password-strength-text");

    // Toast
    private final By toast = By.id("auth-toast");
    private final By toastBody = By.id("toast-body");

    // Link login
    private final By linkLogin = By.cssSelector("a[href='/dang-nhap']");

    public boolean isPageLoaded() {
        return isDisplayed(heading) && isDisplayed(btnSubmit);
    }

    public void fillRegistrationForm(String name, String email, String phone, String password, String confirm) {
        if (name != null) type(inputName, name, "Họ và tên");
        if (email != null) type(inputEmail, email, "Email");
        if (phone != null) type(inputPhone, phone, "Số điện thoại");
        if (password != null) type(inputPassword, password, "Mật khẩu");
        if (confirm != null) type(inputConfirm, confirm, "Xác nhận mật khẩu");
    }

    public void setTermsAgreed(boolean agree) {
        WebElement cb = find(checkboxTerms);
        if (cb.isSelected() != agree) {
            click(checkboxTerms, "Đồng ý điều khoản");
        }
    }

    public void submitForm() {
        click(btnSubmit, "Nút Tạo tài khoản");
    }

    public boolean isPasswordStrengthDisplayed() {
        return isDisplayed(strengthContainer);
    }

    public String getPasswordStrengthText() {
        return getText(strengthText);
    }

    public boolean isToastDisplayed() {
        return isDisplayed(toast);
    }

    public String getToastMessage() {
        WaitUtil.waitForVisibility(toast, 5);
        return getText(toastBody);
    }

    public StorefrontLoginPage clickLoginLink() {
        click(linkLogin, "Link Đăng nhập ngay");
        return new StorefrontLoginPage();
    }
}
