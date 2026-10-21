package com.ecommerce.pages.noithatbang;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * Page Object for Website_NoiThat Admin Review Moderation Page (/reviews)
 * Port: 5173
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangAdminReviewsPage extends BasePage {

    // Header & Filter
    private final By headingTitle = By.xpath("//h1[contains(.,'Quản lý Đánh giá')]");
    private final By selectStatus = By.xpath("//select[option[contains(text(),'Tất cả trạng thái')]]");
    private final By inputSearch = By.cssSelector("input[placeholder*='Tìm'], input[placeholder*='tìm']");

    // Table
    private final By tableHeaders = By.cssSelector("table thead th");
    private final By tableRows = By.cssSelector("table tbody tr");
    private final By emptyState = By.xpath("//div[contains(.,'Chưa có đánh giá nào') or contains(.,'Không tìm thấy')]");

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

    public void filterByStatus(String statusValue) {
        if (isDisplayed(selectStatus)) {
            WebElement el = find(selectStatus);
            new Select(el).selectByValue(statusValue);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}
        }
    }

    public void searchReview(String keyword) {
        if (isDisplayed(inputSearch)) {
            type(inputSearch, keyword, "Tìm kiếm đánh giá: " + keyword);
            try {
                Thread.sleep(600);
            } catch (InterruptedException ignored) {}
        }
    }

    public int getReviewsCount() {
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
