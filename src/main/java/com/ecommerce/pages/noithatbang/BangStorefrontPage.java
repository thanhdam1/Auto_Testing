package com.ecommerce.pages.noithatbang;

import com.ecommerce.pages.base.BasePage;
import com.ecommerce.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

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

    // Auth Modal / Unified Login Page locators
    private final By authModal = By.xpath("//div[contains(@class,'fixed') and .//span[contains(text(),'GÓC NHÀ ĐẸP')]] | //div[contains(@class,'max-w-md')]");
    private final By btnCloseAuthModal = By.cssSelector("button[title='Đóng'], a[href='/']");
    private final By tabLogin = By.xpath("//button[contains(.,'ĐĂNG NHẬP') or contains(.,'Đăng nhập')]");
    private final By tabRegister = By.xpath("//button[contains(.,'ĐĂNG KÝ') or contains(.,'Đăng ký')]");

    // Login Form Fields
    private final By inputLoginEmail = By.xpath("//form//input[@type='email']");
    private final By inputLoginPassword = By.xpath("//form//input[@type='password' or @placeholder='••••••••' or @placeholder='Nhập mật khẩu của bạn']");
    private final By btnTogglePassword = By.xpath("//form//input[@placeholder='••••••••' or @type='password']/following-sibling::button | //form//button[contains(@class,'absolute') and descendant::*[local-name()='svg']]");
    private final By btnForgotPassword = By.xpath("//button[contains(.,'Quên mật khẩu')] | //a[contains(.,'Quên mật khẩu')]");
    private final By btnLoginSubmit = By.xpath("//form//button[@type='submit']");
    private final By linkSwitchToRegister = By.xpath("//form//button[contains(.,'Đăng ký') or contains(.,'ĐĂNG KÝ')]");

    // Register Form Fields
    private final By inputRegisterName = By.xpath("(//form//input[@type='text'])[1]");
    private final By inputRegisterEmail = By.xpath("//form//input[@type='email']");
    private final By inputRegisterPhone = By.xpath("//form//input[@type='tel'] | //form//input[contains(@placeholder,'09')]");
    private final By inputRegisterPassword = By.xpath("(//form//input[@type='password'])[1]");
    private final By btnRegisterSubmit = By.xpath("//form//button[@type='submit']");
    private final By linkSwitchToLogin = By.xpath("//form//button[contains(.,'Đăng nhập') or contains(.,'ĐĂNG NHẬP')]");

    // Cart Drawer locators (Scoped strictly inside Cart Drawer container)
    private final By cartDrawer = By.xpath("//div[contains(@class,'fixed') and .//h2[contains(text(),'Giỏ hàng của bạn')]]");
    private final By btnCloseCartDrawer = By.xpath("//div[contains(@class,'fixed') and .//h2[contains(text(),'Giỏ hàng của bạn')]]//button[@aria-label='Đóng']");
    private final By cartEmptyMsg = By.xpath("//div[contains(@class,'fixed') and .//h2[contains(text(),'Giỏ hàng của bạn')]]//p[contains(text(),'Giỏ hàng của bạn đang trống')]");
    private final By cartDrawerItems = By.xpath("//div[contains(@class,'fixed') and .//h2[contains(text(),'Giỏ hàng của bạn')]]//button[@title='Xóa khỏi giỏ hàng']/ancestor::div[contains(@class,'flex') and contains(@class,'gap-3.5')]");
    private final By linkCheckoutFromDrawer = By.xpath("//div[contains(@class,'fixed') and .//h2[contains(text(),'Giỏ hàng của bạn')]]//a[@href='/checkout']");
    private final By btnIncreaseCartDrawerQty = By.xpath("//div[contains(@class,'fixed') and .//h2[contains(text(),'Giỏ hàng của bạn')]]//button[@title='Tăng số lượng']");
    private final By btnDecreaseCartDrawerQty = By.xpath("//div[contains(@class,'fixed') and .//h2[contains(text(),'Giỏ hàng của bạn')]]//button[@title='Giảm số lượng']");
    private final By btnRemoveCartDrawerItem = By.xpath("//div[contains(@class,'fixed') and .//h2[contains(text(),'Giỏ hàng của bạn')]]//button[@title='Xóa khỏi giỏ hàng']");

    // Showroom & Product Cards
    private final By firstProductCard = By.cssSelector("a[href*='/products/']");
    private final By btnQuickAddToCart = By.cssSelector("button[title='Thêm vào giỏ']");
    private final By catalogProductCards = By.cssSelector(".grid > div.group, a[href*='/products/']");
    private final By catalogProductTitles = By.cssSelector(".grid h4, .group h4");
    private final By catalogSearchNotice = By.xpath("//div[contains(.,'Kết quả tìm kiếm cho:')]");
    private final By catalogEmptyState = By.xpath("//div[contains(.,'Không tìm thấy sản phẩm')] | //p[contains(.,'Không tìm thấy sản phẩm')]");

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
        WebElement input = WaitUtil.waitForVisibility(inputSearch);
        input.clear();
        input.sendKeys(keyword);
        try {
            ((JavascriptExecutor) getDriver()).executeScript(
                    "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));"
                    + "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", input);
            Thread.sleep(300);
        } catch (Exception ignored) {}
        input.sendKeys(Keys.ENTER);
    }

    public void openAuthModal() {
        click(btnAuth, "Nút Đăng nhập trên Header");
        try {
            WaitUtil.waitUntil(driver -> driver.getCurrentUrl().contains("/login") || isDisplayed(authModal), 5);
        } catch (Exception ignored) {}
    }

    public boolean isAuthModalOpen() {
        return getDriver().getCurrentUrl().contains("/login")
                || isDisplayed(authModal);
    }

    public void closeAuthModal() {
        if (getDriver().getCurrentUrl().contains("/login")) {
            getDriver().navigate().back();
            try {
                WaitUtil.waitForVisibility(brandLogo, 3);
            } catch (Exception e) {
                String baseUrl = com.ecommerce.utils.ConfigReader.get("noithatbang.app.url", "http://localhost:4321");
                getDriver().navigate().to(baseUrl);
            }
        } else if (isDisplayed(btnCloseAuthModal)) {
            click(btnCloseAuthModal, "Nút đóng Auth Modal");
        }
    }

    public void switchAuthTab(String tab) {
        if ("register".equalsIgnoreCase(tab)) {
            By target = By.xpath("//button[contains(.,'ĐĂNG KÝ') or contains(.,'Đăng ký')]");
            click(target, "Chuyển tab Đăng ký");
        } else if ("forgot".equalsIgnoreCase(tab)) {
            By target = By.xpath("//button[contains(.,'Quên mật khẩu')] | //a[contains(.,'Quên mật khẩu')]");
            click(target, "Chuyển tab Quên mật khẩu");
        } else {
            By target = By.xpath("//button[contains(.,'ĐĂNG NHẬP') or contains(.,'Đăng nhập')]");
            click(target, "Chuyển tab Đăng nhập");
        }
    }

    public boolean isRegisterFormVisible() {
        return isDisplayed(inputRegisterName) || isDisplayed(btnRegisterSubmit);
    }

    public boolean isLoginFormVisible() {
        return isDisplayed(inputLoginEmail) && isDisplayed(btnLoginSubmit);
    }

    public String getFirstCartDrawerItemQuantity() {
        try {
            By qtyLocator = By.xpath("//div[contains(@class,'divide-y')]//span[contains(@class,'font-semibold') or contains(@class,'w-6') or contains(@class,'text-center')]");
            return getText(qtyLocator);
        } catch (Exception e) {
            return "";
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
        return isDisplayed(toastContainer)
                || isDisplayed(By.cssSelector("[data-sonner-toast]"))
                || isDisplayed(By.xpath("//*[contains(@class,'text-red') or contains(@class,'text-rose') or contains(@class,'bg-red')][normalize-space()!='']"));
    }

    public String getToastText() {
        try {
            if (isDisplayed(toastContainer)) {
                return getText(toastContainer);
            }
            By inlineError = By.xpath("//*[contains(@class,'text-red') or contains(@class,'text-rose') or contains(@class,'bg-red')][normalize-space()!='']");
            if (isDisplayed(inlineError)) {
                return getText(inlineError);
            }
        } catch (Exception ignored) {}
        return "";
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

    public int getCatalogProductCount() {
        try {
            return findAll(catalogProductCards).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public java.util.List<String> getCatalogProductTitles() {
        try {
            return findAll(catalogProductTitles).stream()
                    .map(WebElement::getText)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList();
        } catch (Exception e) {
            return java.util.List.of();
        }
    }

    public boolean isCatalogEmptyStateDisplayed() {
        return isDisplayed(catalogEmptyState) || getCatalogProductCount() == 0;
    }

    public String getCatalogSearchNotice() {
        try {
            return getText(catalogSearchNotice);
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isAlertPresent() {
        try {
            getDriver().switchTo().alert();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
