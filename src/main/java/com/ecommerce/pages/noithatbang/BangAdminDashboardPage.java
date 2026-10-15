package com.ecommerce.pages.noithatbang;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;

/**
 * Page Object for Website_NoiThat Admin Dashboard Page (/)
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangAdminDashboardPage extends BasePage {

    // Sidebar navigation locators
    private final By brandTitle = By.xpath("//aside//span[contains(text(),'NỘI THẤT ADMIN')]");
    private final By navDashboard = By.cssSelector("aside nav a[href='/']");
    private final By navProducts = By.cssSelector("aside nav a[href='/products']");
    private final By navCategories = By.cssSelector("aside nav a[href='/categories']");
    private final By navBrands = By.cssSelector("aside nav a[href='/brands']");
    private final By navOrders = By.cssSelector("aside nav a[href='/orders']");
    private final By navCustomers = By.cssSelector("aside nav a[href='/customers']");
    private final By navCoupons = By.cssSelector("aside nav a[href='/coupons']");

    // Dashboard content locators
    private final By overviewStatCards = By.cssSelector(".grid > div.bg-white, .grid > div.rounded-2xl");

    public boolean isPageLoaded() {
        return isDisplayed(brandTitle) && isDisplayed(navProducts);
    }

    public BangAdminProductPage navigateToProducts() {
        click(navProducts, "Menu Quản lý Sản phẩm");
        return new BangAdminProductPage();
    }

    public void navigateToOrders() {
        click(navOrders, "Menu Quản lý Đơn hàng");
    }

    public void navigateToCategories() {
        click(navCategories, "Menu Quản lý Danh mục");
    }

    public void navigateToCoupons() {
        click(navCoupons, "Menu Quản lý Mã giảm giá");
    }

    public int getStatCardsCount() {
        try {
            return findAll(overviewStatCards).size();
        } catch (Exception e) {
            return 0;
        }
    }
}
