package com.ecommerce.pages.noithatbang;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for Website_NoiThat Customer Orders Page (/orders)
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangCustomerOrdersPage extends BasePage {

    // Header & State
    private final By headingTitle = By.xpath("//h1[contains(text(),'ĐƠN HÀNG CỦA TÔI') or contains(text(),'Bạn chưa đăng nhập')]");
    private final By btnLoginPrompt = By.xpath("//button[contains(text(),'Đăng nhập ngay')]");

    // Status Filter Tabs
    private final By tabAll = By.xpath("//button[contains(.,'Tất cả')]");
    private final By tabPending = By.xpath("//button[contains(.,'Chờ xử lý')]");
    private final By tabConfirmed = By.xpath("//button[contains(.,'Đã xác nhận')]");
    private final By tabShipping = By.xpath("//button[contains(.,'Đang giao')]");
    private final By tabDelivered = By.xpath("//button[contains(.,'Đã giao')]");
    private final By tabCancelled = By.xpath("//button[contains(.,'Đã hủy')]");

    // Order Cards
    private final By orderCards = By.cssSelector(".space-y-4 > div.bg-white, .space-y-4 > div.rounded-2xl");
    private final By emptyOrdersMsg = By.xpath("//p[contains(text(),'Không có đơn hàng nào')]");

    public boolean isPageLoaded() {
        return isDisplayed(headingTitle);
    }

    public boolean isNotLoggedInPromptDisplayed() {
        return isDisplayed(btnLoginPrompt);
    }

    public void clickLoginPrompt() {
        click(btnLoginPrompt, "Nút Đăng nhập ngay trên trang Đơn hàng");
    }

    public void selectTab(String status) {
        By targetTab = switch (status.toLowerCase()) {
            case "pending" -> tabPending;
            case "confirmed" -> tabConfirmed;
            case "shipping" -> tabShipping;
            case "delivered" -> tabDelivered;
            case "cancelled" -> tabCancelled;
            default -> tabAll;
        };
        click(targetTab, "Tab lọc trạng thái: " + status);
    }

    public int getOrdersCount() {
        try {
            List<WebElement> cards = findAll(orderCards);
            return cards.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isEmptyOrdersMessageDisplayed() {
        return isDisplayed(emptyOrdersMsg);
    }
}
