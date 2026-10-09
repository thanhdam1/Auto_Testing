package com.ecommerce.pages.novashop;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for ShopNova Product Search & Catalog (/tim-kiem)
 */
public class NovaSearchPage extends BasePage {

    // Locators
    private final By productCards = By.cssSelector("a[href*='/san-pham/']");
    private final By filterSidebar = By.cssSelector("aside");
    private final By btnResetFilters = By.xpath("//button[contains(text(),'Xóa tất cả') or contains(text(),'Xoá')]");
    private final By emptyState = By.cssSelector("div[class*='EmptyState'], div:has(> svg)");

    public boolean isPageLoaded() {
        return isDisplayed(filterSidebar) || isDisplayed(productCards);
    }

    public int getProductCount() {
        try {
            List<WebElement> cards = getDriver().findElements(productCards);
            return cards.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean hasProductWithText(String keyword) {
        try {
            List<WebElement> cards = getDriver().findElements(productCards);
            for (WebElement card : cards) {
                if (card.isDisplayed() && card.getText().toLowerCase().contains(keyword.toLowerCase())) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void filterByCategory(String categoryName) {
        By categoryFilter = By.xpath("//aside//button[contains(.,'" + categoryName + "')] | //aside//a[contains(.,'" + categoryName + "')]");
        click(categoryFilter, "Filter category: " + categoryName);
    }

    public void selectSort(String sortLabel) {
        By sortButton = By.xpath("//button[contains(text(),'" + sortLabel + "')]");
        click(sortButton, "Sort button: " + sortLabel);
    }
}
