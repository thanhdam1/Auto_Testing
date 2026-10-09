package com.ecommerce.pages.novashop;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for ShopNova Seller Dashboard (/nguoi-ban)
 */
public class NovaSellerDashboardPage extends BasePage {

    // Locators
    private final By headingTitle = By.xpath("//h1[contains(text(),'Tổng Quan Gian Hàng')]");
    private final By btnNewProduct = By.cssSelector("a[href='/nguoi-ban/san-pham/them-moi']");
    private final By btnManageOrders = By.cssSelector("a[href='/nguoi-ban/don-hang']");
    private final By navProducts = By.cssSelector("a[href='/nguoi-ban/san-pham']");
    private final By statsCards = By.cssSelector("div.grid > div, div.space-y-3 > div");

    public boolean isPageLoaded() {
        return isDisplayed(headingTitle);
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
            List<WebElement> elements = getDriver().findElements(statsCards);
            return elements.size();
        } catch (Exception e) {
            return 0;
        }
    }
}
