package com.ecommerce.pages.novashop;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for ShopNova Seller Orders Management (/nguoi-ban/don-hang)
 * Conforms to LOCATORS.md Module 8 specification.
 */
public class NovaSellerOrdersPage extends BasePage {

    // Locators
    private final By headingTitle = By.cssSelector("main h1");
    private final By orderItems = By.cssSelector("div.divide-y > div, main div.space-y-3 > div");
    private final By btnConfirmShip = By.xpath("(//button[contains(., 'Xác nhận') or contains(., 'Giao hàng')])[1]");

    public boolean isPageLoaded() {
        try {
            boolean urlOk = getDriver().getCurrentUrl().contains("/nguoi-ban/don-hang");
            boolean hasHeading = isDisplayed(headingTitle);
            return urlOk || hasHeading;
        } catch (Exception e) {
            return false;
        }
    }

    public int getOrderCount() {
        try {
            List<WebElement> orders = getDriver().findElements(orderItems);
            return orders.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void clickConfirmFirstOrderIfPresent() {
        if (isDisplayed(btnConfirmShip)) {
            click(btnConfirmShip, "Nút Xác nhận & Giao hàng");
        }
    }
}
