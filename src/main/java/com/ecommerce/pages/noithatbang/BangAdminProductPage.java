package com.ecommerce.pages.noithatbang;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * Page Object for Website_NoiThat Admin Product List Page (/products)
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangAdminProductPage extends BasePage {

    // Locators
    private final By pageTitle = By.xpath("//h1[contains(.,'Quản lý Sản phẩm')]");
    private final By btnNewProduct = By.cssSelector("a[href='/products/new']");
    private final By inputSearch = By.cssSelector("input[placeholder*='Tìm theo tên']");
    private final By selectCategory = By.xpath("//select[option[contains(text(),'Tất cả danh mục')]]");
    private final By selectStatus = By.xpath("//select[option[contains(text(),'Tất cả trạng thái')]]");
    private final By tableRows = By.cssSelector("table tbody tr");
    private final By emptyState = By.xpath("//div[contains(text(),'Không tìm thấy sản phẩm')]");

    public boolean isPageLoaded() {
        return isDisplayed(pageTitle);
    }

    public void searchProduct(String keyword) {
        type(inputSearch, keyword, "Tìm kiếm sản phẩm: " + keyword);
        // Chờ debounce 400ms và fetch
        try {
            Thread.sleep(600);
        } catch (InterruptedException ignored) {}
    }

    public void filterByCategory(String categoryText) {
        WebElement el = find(selectCategory);
        new Select(el).selectByVisibleText(categoryText);
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {}
    }

    public void filterByStatus(String statusValue) {
        WebElement el = find(selectStatus);
        new Select(el).selectByValue(statusValue);
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {}
    }

    public int getProductRowsCount() {
        try {
            List<WebElement> rows = findAll(tableRows);
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isNewProductButtonDisplayed() {
        return isDisplayed(btnNewProduct);
    }

    public boolean isEmptyStateDisplayed() {
        return isDisplayed(emptyState);
    }
}
