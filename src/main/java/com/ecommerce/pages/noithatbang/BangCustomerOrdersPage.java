package com.ecommerce.pages.noithatbang;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for Website_NoiThat Customer Orders Page (/orders)
 * Port: 4321
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangCustomerOrdersPage extends BasePage {

    // When NOT logged in
    private final By notLoggedInHeading = By.xpath("//h1[contains(text(),'Theo Dõi & Quản Lý Đơn Hàng')]");
    private final By btnLoginPrompt = By.xpath("//button[contains(.,'Đăng Nhập Tài Khoản')]");

    // When LOGGED IN - Profile Banner
    private final By profileName = By.cssSelector(".bg-white.rounded-3xl h1");
    private final By badgeMember = By.xpath("//span[contains(text(),'Thành viên')]");

    // Status Filter Tabs
    private final By tabAll = By.xpath("//button[contains(.,'Tất cả')]");
    private final By tabPending = By.xpath("//button[contains(.,'Chờ xác nhận')]");
    private final By tabConfirmed = By.xpath("//button[contains(.,'Đã xác nhận')]");
    private final By tabShipping = By.xpath("//button[contains(.,'Đang giao hàng')]");
    private final By tabDelivered = By.xpath("//button[contains(.,'Đã giao thành công')]");
    private final By tabCancelled = By.xpath("//button[contains(.,'Đã hủy')]");

    // Order List & Empty State
    private final By orderCards = By.cssSelector(".space-y-4 > div.rounded-2xl");
    private final By emptyOrdersMsg = By.xpath("//p[contains(text(),'Không có đơn hàng nào') or contains(text(),'Chưa có đơn hàng')]");

    public boolean isPageLoaded() {
        return isDisplayed(notLoggedInHeading) || isDisplayed(profileName);
    }

    public boolean isNotLoggedInPromptDisplayed() {
        return isDisplayed(btnLoginPrompt);
    }

    public void clickLoginPrompt() {
        click(btnLoginPrompt, "Nút Đăng Nhập Tài Khoản trên trang Đơn hàng");
    }

    public boolean isProfileBannerDisplayed() {
        return isDisplayed(profileName);
    }

    public String getProfileName() {
        return getText(profileName);
    }

    public void selectStatusTab(String status) {
        By target = switch (status.toLowerCase()) {
            case "pending" -> tabPending;
            case "confirmed" -> tabConfirmed;
            case "shipping" -> tabShipping;
            case "delivered" -> tabDelivered;
            case "cancelled" -> tabCancelled;
            default -> tabAll;
        };
        click(target, "Tab trạng thái đơn: " + status);
    }

    public int getOrderCardsCount() {
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
