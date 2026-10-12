package com.ecommerce.pages.repair;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;

/**
 * Page Object for IT Support Staff / Technician Login (/login)
 */
public class RepairLoginPage extends BasePage {

    // Locators
    private final By headingPortal = By.xpath("//h2[contains(text(),'Cổng Nội Bộ')]");
    private final By inputUsername = By.name("username");
    private final By inputPassword = By.name("password");
    private final By btnSubmit = By.cssSelector("button[type='submit']");
    private final By errorAlert = By.cssSelector("div.bg-red-50");

    public boolean isPageLoaded() {
        return isDisplayed(headingPortal);
    }

    public void enterUsername(String username) {
        type(inputUsername, username, "Tên đăng nhập nội bộ");
    }

    public void enterPassword(String password) {
        type(inputPassword, password, "Mật khẩu nội bộ");
    }

    public void clickSubmit() {
        click(btnSubmit, "Nút ĐĂNG NHẬP");
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickSubmit();
    }

    public boolean isErrorAlertDisplayed() {
        return isDisplayed(errorAlert);
    }

    public String getErrorAlertText() {
        return getText(errorAlert);
    }
}
