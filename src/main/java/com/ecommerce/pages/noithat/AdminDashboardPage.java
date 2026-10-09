package com.ecommerce.pages.noithat;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for Admin Dashboard (/admin)
 */
public class AdminDashboardPage extends BasePage {

    // Locators
    private final By pageHeader = By.xpath("//h1[contains(text(),'Tổng Quan Hoạt Động Doanh Nghiệp')]");
    private final By btnQuickAddProduct = By.xpath("//a[@href='/admin/san-pham' and contains(.,'Thêm Sản Phẩm')]");
    private final By kpiCards = By.cssSelector(".grid.grid-cols-1.sm\\:grid-cols-2 > div, .grid > div.bg-sand-50");
    private final By revenueChartTitle = By.xpath("//h2[contains(text(),'Biểu Đồ Doanh Thu')]");
    private final By navProductsLink = By.xpath("//a[@href='/admin/san-pham']");
    private final By navOrdersLink = By.xpath("//a[@href='/admin/don-hang']");
    private final By navDashboardLink = By.xpath("//a[@href='/admin']");

    public boolean isDashboardHeaderDisplayed() {
        return isDisplayed(pageHeader);
    }

    public boolean isRevenueChartDisplayed() {
        return isDisplayed(revenueChartTitle);
    }

    public int getKpiCardsCount() {
        try {
            List<WebElement> cards = findAll(kpiCards);
            return cards.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public AdminProductPage clickQuickAddProduct() {
        click(btnQuickAddProduct, "Quick Add Product button");
        return new AdminProductPage();
    }

    public AdminProductPage navigateToProducts() {
        click(navProductsLink, "Sidebar - Quản lý sản phẩm");
        return new AdminProductPage();
    }

    public AdminOrderPage navigateToOrders() {
        click(navOrdersLink, "Sidebar - Quản lý đơn hàng");
        return new AdminOrderPage();
    }
}
