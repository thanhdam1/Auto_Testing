package com.ecommerce.pages.novashop;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;

/**
 * Page Object for ShopNova Seller Shop Settings (/nguoi-ban/gian-hang)
 * Conforms to LOCATORS.md Module 8 specification.
 */
public class NovaSellerShopSettingsPage extends BasePage {

    // Locators
    private final By headingTitle = By.cssSelector("main h1");
    private final By inputShopName = By.cssSelector("input[placeholder*='Tên gian hàng'], form input");
    private final By btnSave = By.cssSelector("button[type='submit']");

    public boolean isPageLoaded() {
        try {
            boolean urlOk = getDriver().getCurrentUrl().contains("/nguoi-ban/gian-hang");
            boolean hasHeading = isDisplayed(headingTitle) || isDisplayed(btnSave);
            return urlOk || hasHeading;
        } catch (Exception e) {
            return false;
        }
    }

    public void updateShopName(String newName) {
        if (isDisplayed(inputShopName)) {
            type(inputShopName, newName, "Tên gian hàng");
        }
    }

    public void clickSave() {
        if (isDisplayed(btnSave)) {
            click(btnSave, "Nút Lưu cài đặt gian hàng");
        }
    }
}
