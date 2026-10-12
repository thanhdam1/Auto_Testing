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
    private final By inputPhone = By.cssSelector("input[type='tel']");
    private final By btnSearch = By.cssSelector("form button[type='submit']");
    private final By ticketCards = By.cssSelector("div.rounded-2xl.shadow-md, div.border-slate-200");
    private final By linkBackHome = By.xpath("//a[contains(text(),'Về trang chủ')]");

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
}
