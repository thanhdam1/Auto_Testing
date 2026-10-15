package com.ecommerce.pages.noithatbang;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;

/**
 * Page Object for Website_NoiThat Storefront (Homepage, Header, Cart Drawer, Auth Modal)
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangStorefrontPage extends BasePage {

    // Header locators
    private final By brandLogo = By.xpath("//span[contains(text(),'GÓC NHÀ ĐẸP')]");
    private final By navProducts = By.cssSelector("a[href='/products']");
    private final By navContact = By.cssSelector("a[href='/contact']");
    private final By btnCart = By.cssSelector("button[title='Giỏ hàng']");
    private final By btnAuth = By.cssSelector("button[title*='Đăng nhập'], button[title*='Tài khoản']");

    // Search Bar
    private final By inputSearch = By.cssSelector("input[placeholder*='Tìm sản phẩm'], input[placeholder*='Tìm kiếm']");

    // Auth Modal locators
    private final By authModal = By.xpath("//div[contains(@class,'fixed') and .//span[contains(text(),'GÓC NHÀ ĐẸP')]]");
    private final By btnCloseAuthModal = By.cssSelector("button[title='Đóng']");
    private final By tabLogin = By.xpath("//button[contains(text(),'ĐĂNG NHẬP')]");
    private final By tabRegister = By.xpath("//button[contains(text(),'ĐĂNG KÝ')]");
    private final By inputAuthEmail = By.cssSelector("form input[type='email']");
    private final By inputAuthPassword = By.cssSelector("form input[type='password']");
    private final By btnAuthSubmit = By.cssSelector("form button[type='submit']");

    // Register Fields
    private final By inputRegisterName = By.xpath("//form//input[@placeholder='Nguyễn Văn A']");
    private final By inputRegisterPhone = By.xpath("//form//input[@type='tel']");

    // Cart Drawer locators
    private final By cartDrawer = By.xpath("//h2[contains(text(),'Giỏ hàng của bạn')]");
    private final By btnCloseCartDrawer = By.cssSelector("button[aria-label='Đóng']");
    private final By cartEmptyMsg = By.xpath("//p[contains(text(),'Giỏ hàng của bạn đang trống')]");
    private final By cartDrawerItems = By.cssSelector(".divide-y > div");
    private final By linkCheckoutFromDrawer = By.cssSelector("a[href='/checkout']");

    // Showroom & Product Cards
    private final By firstProductCard = By.cssSelector("a[href*='/products/']");
    private final By btnQuickAddToCart = By.cssSelector("button[title='Thêm vào giỏ']");

    public boolean isPageLoaded() {
        return isDisplayed(brandLogo);
    }

    public void navigateToProducts() {
        click(navProducts, "Menu Sản phẩm trên Header");
    }

    public void navigateToContact() {
        click(navContact, "Menu Liên hệ trên Header");
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

    public void switchAuthTab(String tab) {
        if ("register".equalsIgnoreCase(tab)) {
            click(tabRegister, "Chuyển tab Đăng ký");
        } else {
            click(tabLogin, "Chuyển tab Đăng nhập");
        }
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

    public int getCartDrawerItemsCount() {
        try {
            return findAll(cartDrawerItems).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public BangCheckoutPage proceedToCheckoutFromDrawer() {
        click(linkCheckoutFromDrawer, "Nút Thanh toán từ Giỏ hàng");
        return new BangCheckoutPage();
    }

    public BangStorefrontProductDetailPage clickFirstProductCard() {
        click(firstProductCard, "Sản phẩm đầu tiên trên trang chủ/catalog");
        return new BangStorefrontProductDetailPage();
    }

    public void clickQuickAddToCartFirstProduct() {
        click(btnQuickAddToCart, "Nút Thêm vào giỏ nhanh trên thẻ sản phẩm");
    }
}
