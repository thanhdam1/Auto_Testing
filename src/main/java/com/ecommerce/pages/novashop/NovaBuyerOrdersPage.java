package com.ecommerce.pages.novashop;

import com.ecommerce.pages.base.BasePage;
import com.ecommerce.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for ShopNova Buyer Orders Page (/don-hang)
 * Conforms to LOCATORS.md Module 7 specification.
 */
public class NovaBuyerOrdersPage extends BasePage {

    // Locators
    private final By headingTitle = By.cssSelector("h1");
    private final By inputSearchOrder = By.cssSelector("input[placeholder*='Tìm theo mã đơn']");
    private final By tabAll = By.xpath("//button[contains(., 'Tất cả')]");
    private final By tabPending = By.xpath("//button[contains(., 'Chờ xác nhận')]");
    private final By tabShipping = By.xpath("//button[contains(., 'Đang giao')]");
    private final By tabDelivered = By.xpath("//button[contains(., 'Đã giao')]");
    private final By tabCancelled = By.xpath("//button[contains(., 'Đã hủy')]");
    private final By orderCards = By.cssSelector("div.divide-y > div, div.space-y-3 > div");

    public boolean isPageLoaded() {
        try {
            boolean urlOk = getDriver().getCurrentUrl().contains("/don-hang");
            boolean hasElement = isDisplayed(headingTitle) || isDisplayed(inputSearchOrder);
            return urlOk || hasElement;
        } catch (Exception e) {
            return false;
        }
    }

    public void filterByTab(String tabName) {
        By targetTab = By.xpath("//button[contains(., '" + tabName + "')]");
        click(targetTab, "Tab lọc đơn hàng: " + tabName);
    }

    public void searchOrder(String keyword) {
        type(inputSearchOrder, keyword, "Ô tìm kiếm đơn hàng");
    }

    public int getOrderCount() {
        try {
            List<WebElement> orders = getDriver().findElements(orderCards);
            return orders.size();
        } catch (Exception e) {
            return 0;
        }
    }
}
