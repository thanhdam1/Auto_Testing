package com.ecommerce.pages.noithatbang;

import com.ecommerce.pages.base.BasePage;
import com.ecommerce.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for Website_NoiThat Admin Contact Inbox Page (/contacts)
 * Port: 5173 / 1234
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangAdminContactPage extends BasePage {

    private final By headingContact = By.xpath("//h1[contains(.,'Hòm thư Liên hệ')]");
    private final By tableHeaders = By.cssSelector("table thead th");
    private final By tableRows = By.cssSelector("table tbody tr");
    private final By emptyState = By.xpath("//td[contains(.,'Hòm thư liên hệ đang rỗng')] | //div[contains(.,'Hòm thư liên hệ đang rỗng')]");
    private final By btnViewDetailFirst = By.cssSelector("table tbody tr button[title='Xem nội dung']");

    // Contact Detail Modal
    private final By modalDetail = By.xpath("//div[contains(@class,'fixed')]//div[contains(@class,'bg-white') and (contains(.,'Gửi từ') or .//h2)]");
    private final By modalDetailBody = By.xpath("//div[contains(@class,'fixed')]//div[contains(@class,'whitespace-pre-wrap') or contains(@class,'bg-stone-50')]");
    private final By btnCloseDetail = By.xpath("//div[contains(@class,'fixed')]//button[contains(.,'Đóng')]");

    // Delete
    private final By btnDeleteFirst = By.cssSelector("table tbody tr button[title='Xóa liên hệ']");
    private final By confirmDeleteDialog = By.xpath("//div[contains(@class,'fixed')]//h3[contains(.,'Xác nhận')] | //div[contains(@class,'fixed')]//button[contains(.,'Hủy')]");
    private final By btnCancelDelete = By.xpath("//div[contains(@class,'fixed')]//button[contains(.,'Hủy')]");

    public boolean isPageLoaded() {
        return isDisplayed(headingContact);
    }

    public void waitForDataLoaded() {
        try {
            WaitUtil.waitUntil(driver -> {
                boolean skeleton = driver.findElements(By.cssSelector(".animate-pulse, [data-skeleton]")).size() > 0;
                if (skeleton) return false;
                return isDisplayed(btnViewDetailFirst) || isDisplayed(emptyState) || !driver.findElements(tableRows).isEmpty();
            }, 8);
        } catch (Exception ignored) {}
    }

    public boolean areTableColumnsDisplayed() {
        try {
            return !findAll(tableHeaders).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasContacts() {
        waitForDataLoaded();
        return isDisplayed(btnViewDetailFirst);
    }

    public int getContactCount() {
        waitForDataLoaded();
        try {
            List<WebElement> rows = findAll(tableRows);
            return (int) rows.stream().filter(r -> r.findElements(By.xpath(".//td[@colspan]")).isEmpty()).count();
        } catch (Exception e) {
            return 0;
        }
    }

    public void openFirstContactDetail() {
        waitForDataLoaded();
        WebElement btn = WaitUtil.waitForClickability(btnViewDetailFirst, 5);
        btn.click();
    }

    public boolean isDetailModalOpen() {
        return isDisplayed(modalDetail);
    }

    public String getDetailModalContent() {
        try {
            return getText(modalDetail);
        } catch (Exception e) {
            return "";
        }
    }

    public void closeDetailModal() {
        if (isDisplayed(btnCloseDetail)) {
            click(btnCloseDetail, "Đóng modal chi tiết liên hệ");
        }
    }

    public void clickDeleteFirstContact() {
        waitForDataLoaded();
        WebElement btn = WaitUtil.waitForClickability(btnDeleteFirst, 5);
        btn.click();
    }

    public boolean isConfirmDeleteDialogDisplayed() {
        return isDisplayed(confirmDeleteDialog);
    }

    public void cancelDeleteDialog() {
        if (isDisplayed(btnCancelDelete)) {
            click(btnCancelDelete, "Hủy dialog xóa liên hệ");
        }
    }

    public boolean isContactPresent(String text) {
        waitForDataLoaded();
        try {
            return findAll(tableRows).stream()
                    .anyMatch(r -> r.getText().toLowerCase().contains(text.toLowerCase()));
        } catch (Exception e) {
            return false;
        }
    }
}
