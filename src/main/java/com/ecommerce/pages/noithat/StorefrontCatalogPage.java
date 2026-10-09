package com.ecommerce.pages.noithat;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for Storefront Product Catalog (/san-pham)
 */
public class StorefrontCatalogPage extends BasePage {

    // Locators
    private final By headingCatalog = By.xpath("//h1[contains(text(),'Tất cả sản phẩm')]");
    private final By productCards = By.cssSelector("[data-product]");
    private final By lblResultCount = By.cssSelector("[data-result-count]");
    private final By selectSort = By.cssSelector("select[data-sort]");
    private final By btnResetFilter = By.cssSelector("[data-filter-reset]");
    private final By emptyFilterMessage = By.cssSelector("[data-empty]");

    public boolean isPageLoaded() {
        return isDisplayed(headingCatalog);
    }

    public int getVisibleProductCount() {
        try {
            List<WebElement> cards = getDriver().findElements(productCards);
            int visible = 0;
            for (WebElement card : cards) {
                if (card.isDisplayed()) {
                    visible++;
                }
            }
            return visible;
        } catch (Exception e) {
            return 0;
        }
    }

    public void filterByCategory(String categoryKey) {
        By checkbox = By.cssSelector("input[data-filter='category'][value='" + categoryKey + "']");
        click(checkbox, "Lọc danh mục: " + categoryKey);
    }

    public void filterBySpace(String spaceKey) {
        By checkbox = By.cssSelector("input[data-filter='space'][value='" + spaceKey + "']");
        click(checkbox, "Lọc không gian: " + spaceKey);
    }

    public void filterByPrice(String priceRangeKey) {
        By radio = By.cssSelector("input[data-filter='price'][value='" + priceRangeKey + "']");
        click(radio, "Lọc khoảng giá: " + priceRangeKey);
    }

    public void resetAllFilters() {
        click(btnResetFilter, "Nút Xóa bộ lọc");
    }

    public void selectSortOption(String sortValue) {
        selectDropdownByValue(selectSort, sortValue, "Sắp xếp sản phẩm");
    }

    public boolean isProductWithTitleDisplayed(String title) {
        try {
            List<WebElement> cards = getDriver().findElements(productCards);
            for (WebElement card : cards) {
                if (card.isDisplayed() && card.getText().toLowerCase().contains(title.toLowerCase())) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
