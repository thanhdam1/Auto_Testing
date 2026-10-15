package com.ecommerce.pages.noithatbang;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;

/**
 * Page Object for Website_NoiThat Admin Login Page (/login)
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangAdminLoginPage extends BasePage {

    // Locators
    private final By headingTitle = By.xpath("//h1[contains(text(),'Nội Thất Admin')]");
    private final By inputEmail = By.cssSelector("input[type='email']");
    private final By inputPassword = By.cssSelector("input[placeholder*='mật khẩu'], input[type='password']");
    private final By btnTogglePassword = By.cssSelector("button[tabindex='-1']");
    private final By btnSubmit = By.cssSelector("button[type='submit']");
    private final By serverErrorAlert = By.cssSelector(".text-rose-700, .bg-rose-50");
    private final By validationError = By.cssSelector("p.text-rose-600");

    public boolean isPageLoaded() {
        return isDisplayed(headingTitle) && isDisplayed(btnSubmit);
    }

    public void enterEmail(String email) {
        type(inputEmail, email, "Ô nhập Email");
    }

    public void enterPassword(String password) {
        type(inputPassword, password, "Ô nhập Mật khẩu");
    }

    public void clickLogin() {
        click(btnSubmit, "Nút Đăng nhập hệ thống");
    }

    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLogin();
    }

    public boolean isServerErrorDisplayed() {
        return isDisplayed(serverErrorAlert);
    }

    public String getServerErrorText() {
        return getText(serverErrorAlert);
    }

    public boolean isValidationErrorDisplayed() {
        return isDisplayed(validationError);
    }

    public String getValidationErrorText() {
        return getText(validationError);
    }

    public void togglePasswordVisibility() {
        click(btnTogglePassword, "Nút ẩn/hiện mật khẩu");
    }

    public String getPasswordInputType() {
        return find(By.cssSelector("input[autoComplete='current-password'], input[name='password']")).getAttribute("type");
    }
}
