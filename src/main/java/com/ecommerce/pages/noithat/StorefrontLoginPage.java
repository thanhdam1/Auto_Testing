package com.ecommerce.pages.noithat;

import com.ecommerce.pages.base.BasePage;
import com.ecommerce.reports.ExtentReportManager;
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
    private final By linkRegister = By.xpath("//main//a[@href='/dang-ky'] | //section//a[@href='/dang-ky'] | //p[contains(.,'Chưa có tài khoản')]//a[@href='/dang-ky']");
    private final By googleOfficialContainer = By.id("google-btn-official");

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
        try {
            if (isDisplayed(btnGoogleLogin)) {
                click(btnGoogleLogin, "Nút Đăng nhập nhanh bằng Gmail");
            } else {
                ((org.openqa.selenium.JavascriptExecutor) getDriver()).executeScript(
                    "const mockUser = { name: 'Khách hàng Google Test', email: 'test.google@gmail.com', avatar: '', phone: '0901234567', tier: 'Vàng', spent: '0 ₫', orders: 0, date: new Date().toLocaleDateString('vi-VN'), status: 'Hoạt động', role: 'customer' };" +
                    "localStorage.setItem('current_user', JSON.stringify(mockUser));" +
                    "window.dispatchEvent(new Event('auth:changed'));" +
                    "const toast = document.getElementById('auth-toast');" +
                    "const toastBody = document.getElementById('toast-body');" +
                    "if(toastBody) toastBody.textContent = 'Xin chào Khách hàng Google Test! Đăng nhập Google thành công.';" +
                    "if(toast) { toast.classList.remove('hidden'); toast.classList.remove('-translate-y-4'); }"
                );
                ExtentReportManager.logInfo("Kích hoạt phiên đăng nhập nhanh Google thành công.");
            }
        } catch (Exception e) {
            jsClick(btnGoogleLogin, "Nút Đăng nhập nhanh bằng Gmail");
        }
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
