package com.ecommerce.pages.novashop;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for ShopNova Marketplace Homepage (/)
 */
public class NovaHomePage extends BasePage {

    // Header & Search
    private final By inputSearch = By.cssSelector("input[aria-label='Tìm kiếm sản phẩm']");
    private final By btnSearchSubmit = By.cssSelector("form button[type='submit']");
    private final By linkLogin = By.cssSelector("a[href='/dang-nhap']");
    private final By linkRegister = By.cssSelector("a[href='/dang-ky']");
    private final By linkCart = By.cssSelector("a[href='/gio-hang']");
    private final By linkSellerChannel = By.cssSelector("a[href='/nguoi-ban']");

    // Sections & Widgets
    private final By bannerSection = By.cssSelector("section, div.grid");
    private final By productCards = By.cssSelector("a[href*='/san-pham/']");
    private final By categoryIcons = By.cssSelector("a[href*='/tim-kiem?c=']");

    public boolean isPageLoaded() {
        return isDisplayed(inputSearch);
    }

    public void searchProduct(String keyword) {
        type(inputSearch, keyword, "Ô tìm kiếm ShopNova");
        click(btnSearchSubmit, "Nút tìm kiếm ShopNova");
    }

    public NovaLoginPage navigateToLogin() {
        click(linkLogin, "Đường dẫn Đăng nhập");
        return new NovaLoginPage();
    }

    public void navigateToRegister() {
        click(linkRegister, "Đường dẫn Đăng ký");
    }

    public void navigateToCart() {
        click(linkCart, "Đường dẫn Giỏ hàng");
    }

    public void navigateToSellerChannel() {
        click(linkSellerChannel, "Kênh người bán");
    }

    public int getVisibleProductCardCount() {
        try {
            List<WebElement> cards = getDriver().findElements(productCards);
            return cards.size();
        } catch (Exception e) {
            return 0;
        }
    }
}
