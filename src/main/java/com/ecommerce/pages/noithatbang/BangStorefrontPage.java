package com.ecommerce.pages.noithatbang;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

/**
 * Page Object for Website_NoiThat Storefront (Homepage, Header, Cart Drawer, Auth Modal)
 * Port: 4321
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangStorefrontPage extends BasePage {

    // Header locators
    private final By brandLogo = By.xpath("//span[contains(text(),'GÓC NHÀ ĐẸP')]");
    private final By navProducts = By.cssSelector("a[href='/products']");
    private final By navContact = By.cssSelector("a[href='/contact'], a[href='/lien-he']");
    private final By navOrders = By.cssSelector("a[href='/orders'], a[href='/account']");
    private final By btnCart = By.cssSelector("button[title='Giỏ hàng']");
    private final By btnAuth = By.cssSelector("button[title*='Đăng nhập'], button[title*='Tài khoản']");
    private final By userGreeting = By.xpath("//button[contains(@class,'cursor-pointer')]//span[contains(@class,'text-xs') and contains(@class,'font-bold')]");

    // Search Bar
    private final By inputSearch = By.cssSelector("form input[placeholder*='Tìm sản phẩm'], form input[placeholder*='Tìm kiếm']");

    // Auth Modal locators
    private final By authModal = By.xpath("//div[contains(@class,'fixed') and .//span[contains(text(),'GÓC NHÀ ĐẸP')]]");
    private final By btnCloseAuthModal = By.cssSelector("button[title='Đóng']");
    private final By tabLogin = By.xpath("//button[contains(text(),'ĐĂNG NHẬP')]");
    private final By tabRegister = By.xpath("//button[contains(text(),'ĐĂNG KÝ')]");

    // Login Form Fields
    private final By inputLoginEmail = By.xpath("//form//input[@type='email']");
    private final By inputLoginPassword = By.xpath("//form//input[@placeholder='Nhập mật khẩu của bạn']");
    private final By btnTogglePassword = By.xpath("//form//button[contains(@class,'cursor-pointer') and (.//*[name()='svg'])]");
    private final By btnForgotPassword = By.xpath("//button[contains(text(),'Quên mật khẩu?')]");
    private final By btnLoginSubmit = By.xpath("//form//button[@type='submit' and contains(.,'ĐĂNG NHẬP')]");
    private final By linkSwitchToRegister = By.xpath("//form//button[contains(text(),'Đăng ký ngay')]");

    // Register Form Fields
    private final By inputRegisterName = By.xpath("//form//input[@placeholder='Nguyễn Văn A']");
    private final By inputRegisterEmail = By.xpath("//form//input[@placeholder='name@example.com']");
    private final By inputRegisterPhone = By.xpath("//form//input[@placeholder='0931 883 988']");
    private final By inputRegisterPassword = By.xpath("//form//input[@placeholder='••••••••']");
    private final By btnRegisterSubmit = By.xpath("//form//button[@type='submit' and contains(.,'TẠO TÀI KHOẢN')]");
    private final By linkSwitchToLogin = By.xpath("//form//button[contains(text(),'Đăng nhập')]");

    // Cart Drawer locators
    private final By cartDrawer = By.xpath("//h2[contains(text(),'Giỏ hàng của bạn')]");
    private final By btnCloseCartDrawer = By.cssSelector("button[aria-label='Đóng']");
    private final By cartEmptyMsg = By.xpath("//p[contains(text(),'Giỏ hàng của bạn đang trống')]");
    private final By cartDrawerItems = By.cssSelector(".divide-y > div");
    private final By linkCheckoutFromDrawer = By.cssSelector("a[href='/checkout']");
    private final By btnIncreaseCartDrawerQty = By.xpath("//div[contains(@class,'divide-y')]//button[.//*[name()='svg' and contains(@class,'lucide-plus')]]");
    private final By btnDecreaseCartDrawerQty = By.xpath("//div[contains(@class,'divide-y')]//button[.//*[name()='svg' and contains(@class,'lucide-minus')]]");
    private final By btnRemoveCartDrawerItem = By.xpath("//div[contains(@class,'divide-y')]//button[@title='Xóa' or .//*[name()='svg' and contains(@class,'lucide-trash')]]");

    // Showroom & Product Cards
    private final By firstProductCard = By.cssSelector("a[href*='/products/']");
    private final By btnQuickAddToCart = By.cssSelector("button[title='Thêm vào giỏ']");

    // Sonner Toast Notifications
    private final By toastContainer = By.cssSelector("[data-sonner-toast], [data-sonner-toaster], [role='status']");

    public boolean isPageLoaded() {
        return isDisplayed(brandLogo);
    }

    public void navigateToProducts() {
        click(navProducts, "Menu Sản phẩm trên Header");
    }

    public void navigateToContact() {
        click(navContact, "Menu Liên hệ trên Header");
    }

    public void navigateToOrders() {
        click(navOrders, "Menu Đơn hàng trên Header");
    }

    public void searchProduct(String keyword) {
        type(inputSearch, keyword, "Tìm kiếm sản phẩm: " + keyword);
        getDriver().findElement(inputSearch).sendKeys(Keys.ENTER);
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

    public void fillLoginForm(String email, String password) {
        type(inputLoginEmail, email, "Email đăng nhập");
        type(inputLoginPassword, password, "Mật khẩu đăng nhập");
    }

    public void submitLogin() {
        click(btnLoginSubmit, "Nút ĐĂNG NHẬP NGAY");
    }

    public void clickForgotPassword() {
        click(btnForgotPassword, "Nút Quên mật khẩu?");
    }

    public void togglePasswordVisibility() {
        click(btnTogglePassword, "Nút bật/tắt hiển thị mật khẩu");
    }

    public void fillRegisterForm(String fullName, String email, String phone, String password) {
        type(inputRegisterName, fullName, "Họ và tên đăng ký");
        type(inputRegisterEmail, email, "Email đăng ký");
        type(inputRegisterPhone, phone, "Số điện thoại đăng ký");
        type(inputRegisterPassword, password, "Mật khẩu đăng ký");
    }

    public void submitRegister() {
        click(btnRegisterSubmit, "Nút TẠO TÀI KHOẢN");
    }

    public void switchToRegisterViaLink() {
        click(linkSwitchToRegister, "Link Đăng ký ngay trong form đăng nhập");
    }

    public void switchToLoginViaLink() {
        click(linkSwitchToLogin, "Link Đăng nhập trong form đăng ký");
    }

    public boolean isToastDisplayed() {
        return isDisplayed(toastContainer);
    }

    public String getToastText() {
        try {
            return getText(toastContainer);
        } catch (Exception e) {
            return "";
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

    public void increaseFirstCartDrawerItem() {
        click(btnIncreaseCartDrawerQty, "Tăng số lượng sản phẩm trong Cart Drawer");
    }

    public void decreaseFirstCartDrawerItem() {
        click(btnDecreaseCartDrawerQty, "Giảm số lượng sản phẩm trong Cart Drawer");
    }

    public void removeFirstCartDrawerItem() {
        click(btnRemoveCartDrawerItem, "Xóa sản phẩm khỏi Cart Drawer");
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
