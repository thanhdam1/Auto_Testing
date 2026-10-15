package com.ecommerce.pages.noithatbang;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for Website_NoiThat Admin Customer Management Page (/customers)
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangAdminCustomerPage extends BasePage {

    // Header & Search Locators
    private final By pageHeading = By.xpath("//h1[contains(.,'Quản lý Khách hàng')]");
    private final By inputSearch = By.cssSelector("input[placeholder*='Tìm theo tên, email, SĐT']");

    // Table Locators
    private final By customerTable = By.cssSelector("table");
    private final By tableRows = By.cssSelector("table tbody tr");
    private final By emptyStateTitle = By.xpath("//div[contains(@class,'text-center')]//h3 | //td[contains(.,'Không tìm thấy khách hàng')] | //div[contains(.,'Không tìm thấy khách hàng')]");

    // Toggle Action Locators
    private final By btnToggleStatusFirstRow = By.cssSelector("table tbody tr:first-child button");
    private final By badgeStatusFirstRow = By.cssSelector("table tbody tr:first-child span");

    // Confirm Dialog Locators
    private final By confirmDialog = By.xpath("//div[@role='dialog'] | //div[contains(@class,'fixed') and contains(.,'tài khoản')]");
    private final By btnConfirmAction = By.xpath("//button[contains(.,'Khóa tài khoản') or contains(.,'Mở khóa')]");
    private final By btnCancelAction = By.xpath("//button[contains(.,'Hủy')]");

    public boolean isPageLoaded() {
        return isDisplayed(pageHeading) && isDisplayed(inputSearch);
    }

    public String getPageHeadingText() {
        return getText(pageHeading);
    }

    public void searchCustomer(String keyword) {
        type(inputSearch, keyword, "Ô tìm kiếm khách hàng: " + keyword);
        try {
            Thread.sleep(400); // UI client filter debounce/render
        } catch (InterruptedException ignored) {}
    }

    public int getCustomerCount() {
        try {
            List<WebElement> rows = findAll(tableRows);
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isEmptyStateDisplayed() {
        return isDisplayed(emptyStateTitle);
    }

    public String getFirstCustomerStatus() {
        return getText(badgeStatusFirstRow);
    }

    public void clickToggleStatusFirstCustomer() {
        click(btnToggleStatusFirstRow, "Nút Khóa/Mở khóa khách hàng dòng đầu tiên");
    }

    public boolean isConfirmDialogDisplayed() {
        return isDisplayed(confirmDialog);
    }

    public void confirmToggleStatus() {
        click(btnConfirmAction, "Nút Xác nhận trong Modal");
    }

    public void cancelToggleStatus() {
        click(btnCancelAction, "Nút Hủy bỏ trong Modal");
    }
}
