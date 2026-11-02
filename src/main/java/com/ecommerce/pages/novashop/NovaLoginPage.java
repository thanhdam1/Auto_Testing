package com.ecommerce.pages.novashop;

import com.ecommerce.pages.base.BasePage;
import com.ecommerce.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Page Object for ShopNova Login Page (/dang-nhap)
 */
public class NovaLoginPage extends BasePage {

    // Locators
    private final By headingLogin = By.xpath("//h1[contains(text(),'Đăng Nhập')]");
    private final By inputUsername = By.cssSelector("input[placeholder*='minhanh']");
    private final By inputPassword = By.cssSelector("input[type='password'][placeholder*='Mật khẩu']");
    private final By btnSubmit = By.cssSelector("form button[type='submit']");
    private final By errorMessage = By.cssSelector("form p.text-rose-600, form p[class*='text-rose'], form p[class*='bg-rose'], p.text-rose-600");
    private final By errorMessageFallback = By.xpath("//form//p | //*[contains(text(),'đầy đủ') or contains(text(),'sai') or contains(text(),'không chính xác')]");
    private final By linkRegister = By.cssSelector("a[href='/dang-ky']");
    private final By btnDemoMinhAnh = By.xpath("//button[contains(.,'minhanh')]");
    private final By btnDemoAnker = By.xpath("//button[contains(.,'ankerseller')]");

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
        waitForSubmissionResult();
    }

    public boolean isErrorMessageDisplayed() {
        try {
            WaitUtil.waitUntil(driver -> hasVisibleError() || !driver.getCurrentUrl().contains("/dang-nhap"), 5);
        } catch (Exception ignored) {
            // The assertion caller decides whether the missing result is a failure.
        }
        return hasVisibleError();
    }

    private void waitForSubmissionResult() {
        try {
            WaitUtil.waitUntil(driver -> hasVisibleError() || !driver.getCurrentUrl().contains("/dang-nhap"), 10);
        } catch (Exception ignored) {
            // Preserve the page state so the test assertion can report the actual outcome.
        }
    }

    private boolean hasVisibleError() {
        for (WebElement element : getDriver().findElements(errorMessage)) {
            if (element.isDisplayed() && !element.getText().trim().isEmpty()) {
                return true;
            }
        }
        for (WebElement element : getDriver().findElements(errorMessageFallback)) {
            if (element.isDisplayed() && !element.getText().trim().isEmpty()) {
                return true;
            }
        }
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


    public boolean isRegisterLinkDisplayed() {
        return isDisplayed(linkRegister);
    }

    // Account Menu Locators (Module 1.3 in LOCATORS.md)
    private final By btnAccountMenu = By.cssSelector("button[aria-haspopup='menu']");
    private final By menuItemOrders = By.xpath("//a[contains(.,'Đơn mua của tôi') or @href='/don-hang']");
    private final By menuItemSeller = By.xpath("//a[contains(.,'Kênh Người Bán') or @href='/nguoi-ban']");
    private final By menuItemLogout = By.xpath("//div[@role='menu']//button[contains(.,'xuất') or contains(.,'Logout') or contains(.,'thoát')] | //button[contains(.,'xuất') or contains(.,'Logout') or contains(.,'Đăng xuất')]");
    private final By linkLoginHeader = By.cssSelector("a[href='/dang-nhap']");

    public boolean isAccountMenuDisplayed() {
        return isDisplayed(btnAccountMenu);
    }

    public void openAccountMenu() {
        click(btnAccountMenu, "Nút mở Menu Tài Khoản");
    }

    public boolean isOrdersLinkInMenuDisplayed() {
        return isDisplayed(menuItemOrders);
    }

    public void clickLogout() {
        if (!isDisplayed(menuItemLogout)) {
            openAccountMenu();
        }
        click(menuItemLogout, "Nút Đăng xuất");
    }

    public boolean isLoggedOut() {
        return isDisplayed(linkLoginHeader);
    }
}
