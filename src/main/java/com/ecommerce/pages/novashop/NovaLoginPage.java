package com.ecommerce.pages.novashop;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;

/**
 * Page Object for ShopNova Login Page (/dang-nhap)
 */
public class NovaLoginPage extends BasePage {

    // Locators
    private final By headingLogin = By.xpath("//h1[contains(text(),'Đăng Nhập')]");
    private final By inputUsername = By.cssSelector("form input:not([type='password'])");
    private final By inputPassword = By.cssSelector("form input[type='password']");
    private final By btnSubmit = By.cssSelector("form button[type='submit']");
    private final By errorMessage = By.cssSelector("form p.text-rose-600");
    private final By linkRegister = By.cssSelector("a[href='/dang-ky']");

    public boolean isPageLoaded() {
        return isDisplayed(headingLogin);
    }

    public void enterUsername(String username) {
        type(inputUsername, username, "Tên đăng nhập ShopNova");
    }

    public void enterPassword(String password) {
        type(inputPassword, password, "Mật khẩu ShopNova");
    }

    public void clickSubmit() {
        click(btnSubmit, "Nút Đăng Nhập");
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickSubmit();
    }

    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }

    public String getErrorMessageText() {
        return getText(errorMessage);
    }

    public boolean isRegisterLinkDisplayed() {
        return isDisplayed(linkRegister);
    }
}
