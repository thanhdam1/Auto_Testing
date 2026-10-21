package com.ecommerce.pages.novashop;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;

/**
 * Page Object for ShopNova Register Page (/dang-ky)
 * Follows Smart Locator Agent priority: Role, Semantic, Unique Attributes
 */
public class NovaRegisterPage extends BasePage {

    // Locators
    private final By headingRegister = By.xpath("//h1[contains(text(),'Đăng Ký')]");
    private final By btnRoleBuyer = By.xpath("//button[contains(.,'Người Mua') or contains(.,'buyer')] | (//div[contains(@class,'grid-cols-2')]//button)[1]");
    private final By btnRoleSeller = By.xpath("//button[contains(.,'Người Bán') or contains(.,'seller')] | (//div[contains(@class,'grid-cols-2')]//button)[2]");
    private final By inputFullName = By.cssSelector("input[placeholder='Họ và tên']");
    private final By inputUsername = By.cssSelector("input[placeholder='Tên đăng nhập']");
    private final By inputPhone = By.cssSelector("input[placeholder='Số điện thoại']");
    private final By inputEmail = By.cssSelector("input[placeholder='Email']");
    private final By inputPassword = By.cssSelector("input[type='password'][placeholder='Mật khẩu']");
    private final By inputConfirmPassword = By.cssSelector("input[type='password'][placeholder='Nhập lại mật khẩu']");
    private final By inputShopName = By.cssSelector("input[placeholder*='Tên gian hàng']");
    private final By inputShopTagline = By.cssSelector("input[placeholder*='Slogan gian hàng']");
    private final By btnSubmit = By.cssSelector("form button[type='submit']");
    private final By linkLogin = By.cssSelector("a[href='/dang-nhap']");
    private final By errorMessage = By.cssSelector("form p.text-rose-600, form p[class*='text-rose'], form p[class*='bg-rose'], p.text-rose-600");
    private final By errorMessageFallback = By.xpath("//form//p | //*[contains(text(),'Vui lòng') or contains(text(),'không khớp')]");

    public boolean isPageLoaded() {
        return isDisplayed(headingRegister);
    }

    public void selectRoleBuyer() {
        click(btnRoleBuyer, "Chọn vai trò Người Mua");
    }

    public void selectRoleSeller() {
        click(btnRoleSeller, "Chọn vai trò Người Bán");
    }

    public void fillRegistrationForm(String fullName, String username, String phone, String email, String password, String confirmPassword) {
        type(inputFullName, fullName, "Họ và tên");
        type(inputUsername, username, "Tên đăng nhập");
        type(inputPhone, phone, "Số điện thoại");
        type(inputEmail, email, "Email");
        type(inputPassword, password, "Mật khẩu");
        type(inputConfirmPassword, confirmPassword, "Nhập lại mật khẩu");
    }

    public void clickSubmit() {
        click(btnSubmit, "Nút Đăng Ký");
    }

    public void registerBuyer(String fullName, String username, String phone, String email, String password) {
        selectRoleBuyer();
        fillRegistrationForm(fullName, username, phone, email, password, password);
        clickSubmit();
    }

    public void registerSeller(String fullName, String username, String phone, String email, String password) {
        selectRoleSeller();
        fillRegistrationForm(fullName, username, phone, email, password, password);
        if (isDisplayed(inputShopName)) {
            type(inputShopName, "Shop " + username, "Tên gian hàng");
        }
        if (isDisplayed(inputShopTagline)) {
            type(inputShopTagline, "Chất lượng là danh dự", "Slogan gian hàng");
        }
        clickSubmit();
    }

    public boolean isErrorMessageDisplayed() {
        try {
            Thread.sleep(600);
        } catch (InterruptedException ignored) {}

        try {
            if (isDisplayed(errorMessage)) {
                return true;
            }
        } catch (Exception ignored) {}

        try {
            java.util.List<org.openqa.selenium.WebElement> elements = getDriver().findElements(errorMessageFallback);
            for (org.openqa.selenium.WebElement el : elements) {
                if (el.isDisplayed() && !el.getText().trim().isEmpty()) {
                    return true;
                }
            }
        } catch (Exception ignored) {}

        return false;
    }

    public String getErrorMessageText() {
        try {
            if (isDisplayed(errorMessage)) {
                return getText(errorMessage);
            }
        } catch (Exception ignored) {}

        try {
            java.util.List<org.openqa.selenium.WebElement> elements = getDriver().findElements(errorMessageFallback);
            for (org.openqa.selenium.WebElement el : elements) {
                if (el.isDisplayed() && !el.getText().trim().isEmpty()) {
                    return el.getText().trim();
                }
            }
        } catch (Exception ignored) {}

        return "";
    }

    public boolean isLoginLinkDisplayed() {
        return isDisplayed(linkLogin);
    }

    public void clickLoginLink() {
        click(linkLogin, "Chuyển sang trang Đăng nhập");
    }
}

