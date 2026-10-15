package com.ecommerce.pages.noithatbang;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;

/**
 * Page Object for Website_NoiThat Storefront (Homepage & Header)
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangStorefrontPage extends BasePage {

    // Header locators
    private final By brandLogo = By.xpath("//span[contains(text(),'GÓC NHÀ ĐẸP')]");
    private final By navProducts = By.cssSelector("a[href='/products']");
    private final By navContact = By.cssSelector("a[href='/contact']");
    private final By btnCart = By.cssSelector("button[title='Giỏ hàng']");
    private final By btnAuth = By.cssSelector("button[title*='Đăng nhập'], button[title*='Tài khoản']");

    // Auth Modal locators
    private final By authModal = By.xpath("//div[contains(@class,'fixed') and .//span[contains(text(),'GÓC NHÀ ĐẸP')]]");
    private final By btnCloseAuthModal = By.cssSelector("button[title='Đóng']");
    private final By inputAuthEmail = By.cssSelector("form input[placeholder='name@example.com']");
    private final By inputAuthPassword = By.cssSelector("form input[placeholder*='mật khẩu']");
    private final By btnAuthSubmit = By.cssSelector("form button[type='submit']");

    // Cart Drawer locators
    private final By cartDrawer = By.xpath("//h2[contains(text(),'Giỏ hàng của bạn')]");
    private final By btnCloseCartDrawer = By.cssSelector("button[aria-label='Đóng']");
    private final By cartEmptyMsg = By.xpath("//p[contains(text(),'Giỏ hàng của bạn đang trống')]");
    private final By linkCheckoutFromDrawer = By.cssSelector("a[href='/checkout']");

    public boolean isPageLoaded() {
        return isDisplayed(brandLogo);
    }

    public void openAuthModal() {
        click(btnAuth, "Nút Đăng nhập trên Header");
    }

    public boolean isAuthModalOpen() {
        return isDisplayed(authModal);
    }

    public void closeAuthModal() {
        click(btnCloseAuthModal, "Nút đóng Auth Modal");
    }

    public void openCartDrawer() {
        click(btnCart, "Icon Giỏ hàng trên Header");
    }

    public boolean isCartDrawerOpen() {
        return isDisplayed(cartDrawer);
    }

    public void closeCartDrawer() {
        click(btnCloseCartDrawer, "Nút đóng Giỏ hàng");
    }

    public boolean isCartDrawerEmpty() {
        return isDisplayed(cartEmptyMsg);
    }

    public BangCheckoutPage proceedToCheckoutFromDrawer() {
        click(linkCheckoutFromDrawer, "Nút Thanh toán từ Giỏ hàng");
        return new BangCheckoutPage();
    }
}
