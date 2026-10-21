package com.ecommerce.pages.novashop;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;

/**
 * Page Object for ShopNova Seller Add Product Page (/nguoi-ban/san-pham/them-moi)
 * Conforms to LOCATORS.md Module 8 specification.
 */
public class NovaSellerAddProductPage extends BasePage {

    // Locators based on DOM inspection
    private final By headingTitle = By.cssSelector("main h1, form h1");
    private final By inputName = By.cssSelector("input[placeholder*='Tai nghe'], input[placeholder*='tai nghe'], section:first-of-type input");
    private final By selectCategory = By.cssSelector("select");
    private final By inputPrice = By.cssSelector("input[placeholder='199000'], input[placeholder*='199']");
    private final By inputOriginalPrice = By.cssSelector("input[placeholder='299000'], input[placeholder*='299']");
    private final By inputStock = By.xpath("//label[contains(.,'kho')]//input | (//input[@inputmode='numeric'])[3]");
    private final By inputVariants = By.cssSelector("input[placeholder*='Size M'], input[placeholder*='Size'], input[placeholder*='Màu Đen']");
    private final By textareaDescription = By.cssSelector("textarea");
    private final By btnSave = By.cssSelector("button[type='submit']");

    public boolean isPageLoaded() {
        return isDisplayed(inputName) || isDisplayed(headingTitle);
    }

    public void enterProductName(String name) {
        type(inputName, name, "Tên sản phẩm");
    }

    public void selectCategoryByValue(String categoryId) {
        selectDropdownByValue(selectCategory, categoryId, "Ngành hàng sản phẩm");
    }

    public void enterPrice(String price) {
        type(inputPrice, price, "Giá bán sản phẩm");
    }

    public void enterOriginalPrice(String originalPrice) {
        type(inputOriginalPrice, originalPrice, "Giá niêm yết cũ");
    }

    public void enterStock(String stock) {
        type(inputStock, stock, "Số lượng tồn kho");
    }

    public void enterVariants(String variants) {
        type(inputVariants, variants, "Các biến thể phân loại");
    }

    public void enterDescription(String description) {
        type(textareaDescription, description, "Mô tả chi tiết sản phẩm");
    }

    public void clickSave() {
        click(btnSave, "Nút Lưu sản phẩm");
    }

    public void fillAndSubmitProduct(String name, String price, String stock, String description) {
        enterProductName(name);
        enterPrice(price);
        enterStock(stock);
        enterDescription(description);
        clickSave();
    }
}
