package com.ecommerce.pages.noithatbang;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for Website_NoiThat Admin Order Management Page (/orders)
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
    private final By tableRows = By.cssSelector("table tbody tr");
    private final By emptyState = By.xpath("//div[contains(.,'Không tìm thấy đơn hàng')]");

    public boolean isPageLoaded() {
        return isDisplayed(headingTitle);
    }

    public void searchOrder(String keyword) {
        type(inputSearch, keyword, "Tìm kiếm đơn hàng: " + keyword);
        try {
            Thread.sleep(500); // 400ms debounce
        } catch (InterruptedException ignored) {}
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
}
