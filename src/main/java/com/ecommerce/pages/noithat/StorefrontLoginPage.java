package com.ecommerce.pages.noithat;

import com.ecommerce.pages.base.BasePage;
import com.ecommerce.utils.WaitUtil;
import org.openqa.selenium.By;

/**
 * Page Object for Storefront Login Page (/dang-nhap)
 */
public class StorefrontLoginPage extends BasePage {

    // Locators
    private final By headingLogin = By.xpath("//h1[contains(text(),'Đăng nhập')]");
    private final By inputEmail = By.id("login-email");
    private final By inputPassword = By.id("login-password");
    private final By btnSubmitLogin = By.cssSelector("#login-form button[type='submit']");
    private final By btnGoogleLogin = By.id("btn-google-login");
    private final By toastContainer = By.id("auth-toast");
    private final By toastBody = By.id("toast-body");
    private final By linkRegister = By.cssSelector("a[href='/dang-ky']");

    public boolean isPageLoaded() {
        return isDisplayed(headingLogin);
    }

    public void enterEmail(String email) {
        type(inputEmail, email, "Ô nhập email");
    }

    public void enterPassword(String password) {
        type(inputPassword, password, "Ô nhập mật khẩu");
    }

    public void clickSubmit() {
        click(btnSubmitLogin, "Nút Đăng nhập");
    }

    public void loginWithCredentials(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickSubmit();
    }

    public void clickGoogleLogin() {
        click(btnGoogleLogin, "Nút Đăng nhập nhanh bằng Gmail");
    }

    public boolean isToastDisplayed() {
        return isDisplayed(toastContainer);
    }

    public String getToastMessage() {
        return getText(toastBody);
    }

    public boolean isRegisterLinkDisplayed() {
        return isDisplayed(linkRegister);
    }
}
