package com.ecommerce.pages.noithat;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for Admin Order Management (/admin/don-hang)
 */
public class AdminOrderPage extends BasePage {

    // Header & Stats
    private final By pageHeader = By.xpath("//h1[contains(text(),'Quản Lý Đơn Hàng')]");
    private final By statsCards = By.cssSelector(".grid.grid-cols-2 > div, section.grid > div.bg-sand-50");

    // Search & Filter
    private final By inputSearchOrder = By.id("order-search");
    private final By selectOrderStatus = By.id("order-status-filter");
    private final By selectOrderShip = By.id("order-ship-filter");
    private final By btnResetOrder = By.id("btn-order-reset");
    private final By lblOrderCount = By.id("order-count-label");

    // Table & Rows
    private final By orderTableRows = By.cssSelector("#order-table-body tr[data-order-row]");

    public boolean isPageLoaded() {
        return isDisplayed(pageHeader);
    }

    public void searchOrder(String keyword) {
        type(inputSearchOrder, keyword, "Ô tìm kiếm đơn hàng");
    }

    public void filterByStatus(String statusValue) {
        selectDropdownByValue(selectOrderStatus, statusValue, "Lọc trạng thái đơn");
    }

    public void filterByShippingMethod(String shipMethodValue) {
        selectDropdownByValue(selectOrderShip, shipMethodValue, "Lọc phương thức giao hàng");
    }

    public void resetFilters() {
        click(btnResetOrder, "Nút Xóa lọc đơn hàng");
    }

    public int getVisibleOrderRowCount() {
        try {
            List<WebElement> rows = getDriver().findElements(orderTableRows);
            int visible = 0;
            for (WebElement row : rows) {
                if (row.isDisplayed()) {
                    visible++;
                }
            }
            return visible;
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean hasOrderByCode(String orderCode) {
        try {
            List<WebElement> rows = getDriver().findElements(orderTableRows);
            for (WebElement row : rows) {
                if (row.isDisplayed() && row.getText().contains(orderCode)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public int getStatsCardsCount() {
        try {
            return findAll(statsCards).size();
        } catch (Exception e) {
            return 0;
        }
    }
}
