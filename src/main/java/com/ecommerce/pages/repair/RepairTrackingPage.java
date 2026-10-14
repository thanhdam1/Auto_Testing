package com.ecommerce.pages.repair;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for Phone Tracking Page (/tra-cuu)
 */
public class RepairTrackingPage extends BasePage {

    // Locators
    private final By headingTracking = By.xpath("//h1[contains(text(),'Tra Cứu Tiến Độ Sửa Chữa')]");
    private final By inputPhone = By.cssSelector("input[type='tel'][placeholder='Nhập số điện thoại (VD: 0901234567)']");
    private final By btnSearch = By.cssSelector("form button[type='submit']");
    private final By ticketCards = By.cssSelector("main a[href*='/track/']");
    private final By linkBackHome = By.xpath("//a[normalize-space()='Về trang chủ']");

    public boolean isPageLoaded() {
        return isDisplayed(headingTracking);
    }

    public void enterPhone(String phone) {
        type(inputPhone, phone, "Số điện thoại tra cứu");
    }

    public void clickSearch() {
        click(btnSearch, "Nút Tra cứu");
    }

    public void searchByPhone(String phone) {
        enterPhone(phone);
        clickSearch();
    }

    public int getTicketResultsCount() {
        try {
            List<WebElement> cards = getDriver().findElements(ticketCards);
            return cards.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void navigateBackHome() {
        click(linkBackHome, "Đường dẫn Về trang chủ");
    }

    // ==================================================================
    // BỔ SUNG PHỤC VỤ F-02: các test tra cứu cần assert được phản hồi của hệ thống
    // ==================================================================

    /**
     * Thông báo "không tìm thấy phiếu" (nhiều biến thể chữ hoa/thường).
     */
    private final By noResultMessage = By.xpath(
            "//*[contains(normalize-space(text()),'Không tìm thấy')"
                    + " or contains(normalize-space(text()),'không tìm thấy')"
                    + " or contains(normalize-space(text()),'Không có phiếu')"
                    + " or contains(normalize-space(text()),'không có phiếu')]");

    public boolean isNoResultMessageDisplayed() {
        try {
            return getDriver().findElement(noResultMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Sau khi tra cứu, hệ thống phải phản hồi: hoặc có phiếu, hoặc có thông báo không tìm thấy.
     * Dùng để assert thay cho việc chỉ "click rồi log" như trước.
     */
    public boolean hasSearchFeedback() {
        return getTicketResultsCount() > 0 || isNoResultMessageDisplayed();
    }
}
