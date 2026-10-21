package com.ecommerce.pages.noithatbang;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * Page Object for Website_NoiThat Admin Product Create/Edit Form (/products/new or /products/:id)
 * Port: 5173
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangAdminProductFormPage extends BasePage {

    private final By headingForm = By.xpath("//h1[contains(.,'Sản phẩm') or contains(.,'Thêm sản phẩm') or contains(.,'Chỉnh sửa')]");

    // General Information
    private final By inputName = By.xpath("//input[@name='name']");
    private final By inputSku = By.xpath("//input[@name='sku']");
    private final By selectCategory = By.xpath("//select[@name='category_id']");
    private final By selectBrand = By.xpath("//select[@name='brand_id']");
    private final By textareaShortDesc = By.xpath("//textarea[@name='short_desc']");
    private final By selectStatus = By.xpath("//select[@name='status']");

    // Variants
    private final By btnAddVariant = By.xpath("//button[contains(.,'Thêm biến thể')]");
    private final By inputFirstVariantSku = By.xpath("//input[contains(@name,'variants.0.sku') or (contains(@placeholder,'SKU') and not(@name='sku'))]");
    private final By inputFirstVariantPrice = By.xpath("//input[contains(@name,'variants.0.price')]");
    private final By inputFirstVariantStock = By.xpath("//input[contains(@name,'variants.0.stock_quantity')]");

    // Submit & Validation Errors
    private final By btnSave = By.xpath("//button[@type='submit']");
    private final By validationErrors = By.cssSelector("p.text-rose-600, .text-rose-500, p.text-red-500");

    public boolean isPageLoaded() {
        return isDisplayed(headingForm) && isDisplayed(inputName);
    }

    public void fillBasicInfo(String name, String sku, String shortDesc) {
        type(inputName, name, "Tên sản phẩm");
        type(inputSku, sku, "Mã SKU chung");
        if (shortDesc != null && isDisplayed(textareaShortDesc)) {
            type(textareaShortDesc, shortDesc, "Mô tả ngắn");
        }
    }

    public void selectFirstCategory() {
        if (isDisplayed(selectCategory)) {
            Select select = new Select(getDriver().findElement(selectCategory));
            if (select.getOptions().size() > 1) {
                select.selectByIndex(1);
            }
        }
    }

    public void fillFirstVariant(String sku, String price, String stock) {
        if (isDisplayed(inputFirstVariantSku)) {
            type(inputFirstVariantSku, sku, "SKU biến thể 1");
        }
        if (isDisplayed(inputFirstVariantPrice)) {
            type(inputFirstVariantPrice, price, "Giá biến thể 1");
        }
        if (isDisplayed(inputFirstVariantStock)) {
            type(inputFirstVariantStock, stock, "Tồn kho biến thể 1");
        }
    }

    public void submitSave() {
        click(btnSave, "Nút Lưu sản phẩm");
    }

    public boolean hasValidationErrors() {
        try {
            List<WebElement> errors = findAll(validationErrors);
            return !errors.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public int getValidationErrorsCount() {
        try {
            return findAll(validationErrors).size();
        } catch (Exception e) {
            return 0;
        }
    }
}
