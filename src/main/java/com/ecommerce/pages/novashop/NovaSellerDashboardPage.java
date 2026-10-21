package com.ecommerce.pages.novashop;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for ShopNova Seller Dashboard (/nguoi-ban)
 */
public class NovaSellerDashboardPage extends BasePage {

    // Locators based on DOM inspection
    private final By headingTitle = By.cssSelector("main h1");
    private final By btnNewProduct = By.cssSelector("a[href*='/nguoi-ban/san-pham/them-moi']");
    private final By btnManageOrders = By.cssSelector("a[href*='/nguoi-ban/don-hang']");
    private final By navProducts = By.cssSelector("aside a[href*='/nguoi-ban/san-pham']");
    private final By statsCards = By.cssSelector("main div.grid > div");

    public boolean isPageLoaded() {
        try {
            return isDisplayed(headingTitle) || isDisplayed(navProducts);
        } catch (Exception e) {
            return false;
        }
    }

    public NovaSellerProductsPage navigateToProducts() {
        click(navProducts, "Menu Quản lý sản phẩm");
        return new NovaSellerProductsPage();
    }

    public void clickAddNewProduct() {
        click(btnNewProduct, "Nút + Đăng sản phẩm mới");
    }

    public int getStatsCount() {
        try {
            com.ecommerce.utils.WaitUtil.waitForPresence(statsCards, 10);
            List<WebElement> elements = getDriver().findElements(statsCards);
            return elements.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public NovaSellerOrdersPage navigateToOrders() {
        click(btnManageOrders, "Menu Quản lý đơn hàng");
        return new NovaSellerOrdersPage();
    }

    public NovaSellerShopSettingsPage navigateToShopSettings() {
        By navSettings = By.cssSelector("aside a[href*='/nguoi-ban/gian-hang']");
        click(navSettings, "Menu Cài đặt gian hàng");
        return new NovaSellerShopSettingsPage();
    }
}
