package com.ecommerce.pages.novashop;

import com.ecommerce.pages.base.BasePage;
import com.ecommerce.utils.WaitUtil;
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
        try {
            List<WebElement> items = getDriver().findElements(By.cssSelector("aside label, aside button, aside a"));
            for (WebElement item : items) {
                if (item.isDisplayed() && item.getText().toLowerCase().contains(categoryName.toLowerCase())) {
                    item.click();
                    return;
                }
            }
        } catch (Exception ignored) {}
        By categoryFilter = By.xpath("//aside//*[self::label or self::button or self::a or self::span][contains(.,'" + categoryName + "')]");
        click(categoryFilter, "Filter category: " + categoryName);
    }

    public void selectSort(String sortLabel) {
        By sortButton = By.xpath("//button[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + sortLabel.toLowerCase() + "')]");
        click(sortButton, "Sort button: " + sortLabel);
    }

    public void clickFirstProduct() {
        click(productCards, "Sản phẩm đầu tiên trong kết quả tìm kiếm");
    }

    public void filterByPriceRange(String minPrice, String maxPrice) {
        // Trong Shop NOVA, khoảng giá là danh sách checkbox/radio options: Dưới 200k, 200k-500k, 500k-1tr...
        By priceOption = By.xpath("//aside//label[contains(., '200.000') or contains(., '500.000') or contains(., 'Dưới')] | (//aside//label)[4]");
        try {
            if (!getDriver().findElements(priceOption).isEmpty()) {
                WebElement el = getDriver().findElement(priceOption);
                ((org.openqa.selenium.JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView({block: 'center'});", el);
                ((org.openqa.selenium.JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", el);
            }
        } catch (Exception ignored) {}
    }

    public void filterByLocation(String locationName) {
        // Nơi bán trong Shop NOVA có ô search và danh sách tỉnh thành
        By inputSearchCity = By.cssSelector("aside input[placeholder*='Tìm tỉnh']");
        try {
            if (isDisplayed(inputSearchCity)) {
                type(inputSearchCity, locationName, "Tìm tỉnh / thành phố");
            }
            By targetCity = By.xpath("//aside//div[contains(@class,'thin-scroll')]//label[1] | //aside//label[contains(., 'Toàn quốc')]");
            WaitUtil.waitForVisibility(targetCity, 5);
            if (!getDriver().findElements(targetCity).isEmpty()) {
                WebElement el = getDriver().findElement(targetCity);
                ((org.openqa.selenium.JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView({block: 'center'});", el);
                ((org.openqa.selenium.JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", el);
            }
        } catch (Exception ignored) {}
    }


    public void clickResetFilters() {
        click(btnResetFilters, "Nút Xoá tất cả bộ lọc");
    }

    public boolean isEmptyStateDisplayed() {
        By emptyNotice = By.xpath("//*[contains(., 'Không tìm thấy sản phẩm') or contains(., 'không tìm thấy')]");
        return isDisplayed(emptyNotice);
    }
}
