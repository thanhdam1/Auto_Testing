package com.ecommerce.pages.noithatbang;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for Website_NoiThat Admin Contact Inbox Page (/contacts)
 * Port: 5173
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangAdminContactPage extends BasePage {

    private final By headingContact = By.xpath("//h1[contains(.,'Hòm thư Liên hệ')]");
    private final By tableHeaders = By.cssSelector("table thead th");
    private final By tableRows = By.cssSelector("table tbody tr");
    private final By btnViewDetailFirst = By.xpath("(//table tbody tr//button[@title='Xem nội dung' or .//*[name()='svg' and contains(@class,'lucide-eye')]])[1]");

    // Contact Detail Modal
    private final By modalDetail = By.xpath("//div[contains(@class,'fixed')]//h2 | //div[contains(@class,'fixed')]//div[contains(@class,'rounded')]//p[contains(text(),'Gửi từ')]");
    private final By btnCloseDetail = By.xpath("//div[contains(@class,'fixed')]//button[contains(.,'Đóng')]");

    // Delete
    private final By btnDeleteFirst = By.cssSelector("table tbody tr:first-child button.text-rose-600, table tbody tr:first-child td:last-child button:last-child, table tbody tr:first-child button[title*='Xóa']");
    private final By confirmDeleteDialog = By.xpath("//div[contains(@class,'fixed')]//h3[contains(.,'Xác nhận')] | //div[contains(@class,'fixed')]//button[contains(.,'Hủy')]");
    private final By btnCancelDelete = By.xpath("//div[contains(@class,'fixed')]//button[contains(.,'Hủy')]");

    public boolean isPageLoaded() {
        return isDisplayed(headingContact);
    }

    public boolean areTableColumnsDisplayed() {
        try {
            return !findAll(tableHeaders).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasContacts() {
        return isDisplayed(btnViewDetailFirst);
    }

    public int getContactCount() {
        try {
            List<WebElement> rows = findAll(tableRows);
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void openFirstContactDetail() {
        if (isDisplayed(btnViewDetailFirst)) {
            click(btnViewDetailFirst, "Xem chi tiết tin nhắn liên hệ đầu tiên");
        }
    }

    public boolean isDetailModalOpen() {
        return isDisplayed(modalDetail);
    }

    public void closeDetailModal() {
        if (isDisplayed(btnCloseDetail)) {
            click(btnCloseDetail, "Đóng modal chi tiết liên hệ");
        }
    }

    public void clickDeleteFirstContact() {
        if (isDisplayed(btnDeleteFirst)) {
            click(btnDeleteFirst, "Icon Xóa tin nhắn liên hệ đầu tiên");
        }
    }

    public boolean isConfirmDeleteDialogDisplayed() {
        return isDisplayed(confirmDeleteDialog);
    }

    public void cancelDeleteDialog() {
        if (isDisplayed(btnCancelDelete)) {
            click(btnCancelDelete, "Hủy dialog xóa liên hệ");
        }
    }
}
