package com.ecommerce.pages.noithatbang;

import com.ecommerce.pages.base.BasePage;
import com.ecommerce.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for Website_NoiThat Admin Order Management Page (/orders)
 * Port: 5173
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangAdminOrdersPage extends BasePage {

    // Header & Search
    private final By headingTitle = By.xpath("//h1[contains(.,'Quản lý Đơn hàng')]");
    private final By inputSearch = By.cssSelector("input[placeholder*='Tìm theo mã đơn']");

    // Status Filter Tabs
    private final By tabAll = By.xpath("//button[contains(.,'Tất cả đơn')]");
    private final By tabPending = By.xpath("//button[contains(.,'Chờ xác nhận')]");
    private final By tabConfirmed = By.xpath("//button[contains(.,'Đã xác nhận')]");
    private final By tabShipping = By.xpath("//button[contains(.,'Đang giao')]");
    private final By tabDelivered = By.xpath("//button[contains(.,'Hoàn thành')]");
    private final By tabCancelled = By.xpath("//button[contains(.,'Đã hủy')]");

    // Table
    private final By tableHeaders = By.cssSelector("table thead th");
    private final By tableRows = By.cssSelector("table tbody tr");
    private final By linkFirstOrderDetail = By.xpath("(//table tbody tr//a[contains(@href,'/orders/')])[1]");
    private final By emptyState = By.xpath("//div[contains(.,'Không tìm thấy đơn hàng')]");

    public boolean isPageLoaded() {
        return isDisplayed(headingTitle);
    }

    public boolean areTableColumnsDisplayed() {
        try {
            return !findAll(tableHeaders).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public void searchOrder(String keyword) {
        type(inputSearch, keyword, "Tìm kiếm đơn hàng: " + keyword);
        WaitUtil.waitUntil(driver -> driver.findElements(tableRows).stream()
                .filter(WebElement::isDisplayed)
                .allMatch(row -> row.getText().toLowerCase().contains(keyword.toLowerCase())), 5);
    }

    public void filterByStatus(String status) {
        By target = switch (status.toLowerCase()) {
            case "pending" -> tabPending;
            case "confirmed" -> tabConfirmed;
            case "shipping" -> tabShipping;
            case "delivered" -> tabDelivered;
            case "cancelled" -> tabCancelled;
            default -> tabAll;
        };
        click(target, "Tab trạng thái: " + status);
        WaitUtil.waitForClickability(target, 5);
    }

    public int getOrderRowCount() {
        try {
            List<WebElement> rows = findAll(tableRows);
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isEmptyStateDisplayed() {
        return isDisplayed(emptyState);
    }

    public void clickFirstOrderDetail() {
        if (isDisplayed(linkFirstOrderDetail)) {
            click(linkFirstOrderDetail, "Chi tiết đơn hàng đầu tiên");
        }
    }
}
