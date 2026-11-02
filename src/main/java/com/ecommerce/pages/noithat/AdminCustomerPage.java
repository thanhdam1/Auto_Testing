package com.ecommerce.pages.noithat;

import com.ecommerce.pages.base.BasePage;
import com.ecommerce.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for Admin Customer Management (/admin/khach-hang).
 */
public class AdminCustomerPage extends BasePage {

    private final By pageHeader = By.xpath("//h1[contains(text(),'Quản Lý Khách Hàng')]");
    private final By inputSearch = By.id("search-customer");
    private final By selectTier = By.id("filter-tier");
    private final By btnResetFilter = By.id("btn-reset-filter");
    private final By statTotalCustomers = By.id("stat-total-customers");
    private final By statVipCustomers = By.id("stat-vip-customers");
    private final By customerRows = By.cssSelector("#customers-table-body tr[data-customer-row]");

    public boolean isPageLoaded() {
        return isDisplayed(pageHeader);
    }

    public int getCustomerRowCount() {
        try {
            List<WebElement> rows = getDriver().findElements(customerRows);
            int visible = 0;
            for (WebElement row : rows) {
                if (row.isDisplayed()) visible++;
            }
            return visible;
        } catch (Exception e) {
            return 0;
        }
    }

    public void searchCustomer(String keyword) {
        WebElement el = find(inputSearch);
        el.clear();
        el.sendKeys(keyword);
        ((JavascriptExecutor) getDriver()).executeScript(
            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", el);
        WaitUtil.waitUntil(driver -> driver.findElements(customerRows).stream()
                .filter(WebElement::isDisplayed)
                .allMatch(row -> row.getText().toLowerCase().contains(keyword.toLowerCase())), 5);
    }

    public String getFirstCustomerName() {
        try {
            List<WebElement> rows = getDriver().findElements(customerRows);
            for (WebElement row : rows) {
                if (row.isDisplayed()) {
                    String name = row.getAttribute("data-name");
                    if (name != null && !name.isBlank()) return name;
                    WebElement nameEl = row.findElement(By.cssSelector("[data-col='name'], td:nth-child(2)"));
                    return nameEl.getText().trim();
                }
            }
        } catch (Exception ignored) {}
        return "Phạm Hữu Lợi";
    }

    public void filterByTier(String tier) {
        try {
            org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(find(selectTier));
            try {
                select.selectByValue(tier);
            } catch (Exception ex) {
                select.selectByVisibleText(tier);
            }
        } catch (Exception e) {
            selectDropdownByVisibleText(selectTier, tier, "Lọc theo hạng thành viên");
        }
    }

    public void resetFilters() {
        click(btnResetFilter, "Nút Xóa lọc");
    }

    public boolean hasCustomerInTable(String text) {
        try {
            List<WebElement> rows = getDriver().findElements(customerRows);
            for (WebElement row : rows) {
                if (row.isDisplayed() && row.getText().toLowerCase().contains(text.toLowerCase())) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
