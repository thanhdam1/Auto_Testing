package com.ecommerce.pages.novashop;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for ShopNova Seller Products Management (/nguoi-ban/san-pham)
 */
public class NovaSellerProductsPage extends BasePage {

    // Locators
    private final By headingTitle = By.xpath("//h1[contains(text(),'Quản Lý Sản Phẩm')]");
    private final By inputKeyword = By.cssSelector("input[placeholder*='Tìm theo tên']");
    private final By btnAddNew = By.cssSelector("a[href='/nguoi-ban/san-pham/them-moi']");
    private final By productItems = By.cssSelector("table tbody tr, div.space-y-3 > div");

    public boolean isPageLoaded() {
        return isDisplayed(headingTitle);
    }

    public void searchProduct(String keyword) {
        type(inputKeyword, keyword, "Tìm kiếm sản phẩm người bán");
    }

    public void clickAddNewProduct() {
        click(btnAddNew, "Thêm sản phẩm mới");
    }

    public int getVisibleProductCount() {
        try {
            List<WebElement> items = getDriver().findElements(productItems);
            return items.size();
        } catch (Exception e) {
            return 0;
        }
    }
}
