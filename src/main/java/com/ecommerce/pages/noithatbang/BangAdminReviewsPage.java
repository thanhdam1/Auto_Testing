package com.ecommerce.pages.noithatbang;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * Page Object for Website_NoiThat Admin Review Moderation Page (/reviews)
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangAdminReviewsPage extends BasePage {

    // Header & Filter
    private final By headingTitle = By.xpath("//h1[contains(.,'Quản lý Đánh giá')]");
    private final By selectStatus = By.xpath("//select[option[contains(text(),'Tất cả trạng thái')]]");

    // Table
    private final By tableRows = By.cssSelector("table tbody tr");
    private final By emptyState = By.xpath("//div[contains(.,'Chưa có đánh giá nào')]");

    public boolean isPageLoaded() {
        return isDisplayed(headingTitle);
    }

    public void filterByStatus(String statusValue) {
        WebElement el = find(selectStatus);
        new Select(el).selectByValue(statusValue);
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {}
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
