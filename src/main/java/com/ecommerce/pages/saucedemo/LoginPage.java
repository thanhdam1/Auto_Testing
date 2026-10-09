package com.ecommerce.pages.saucedemo;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {

    // Locators
    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessageContainer = By.cssSelector("[data-test='error']");
    private final By loginLogo = By.className("login_logo");

    public boolean isLoginPageDisplayed() {
        return isDisplayed(loginLogo) && isDisplayed(loginButton);
    }

    public LoginPage enterUsername(String username) {
        type(usernameInput, username, "Username Field");
        return this;
    }

    public LoginPage enterPassword(String password) {
        type(passwordInput, password, "Password Field");
        return this;
    }

    public void clickLoginButton() {
        click(loginButton, "Login Button");
    }

    /**
     * Complete login flow
     */
    public InventoryPage loginAs(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        return new InventoryPage();
    }

    public String getErrorMessage() {
        return getText(errorMessageContainer);
    }

    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessageContainer);
    }
}
