package com.ecommerce.pages.noithat;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;

/**
 * Page Object for Storefront Customer Account Page (/tai-khoan).
 */
public class StorefrontAccountPage extends BasePage {

    private final By userName = By.id("page-user-name");
    private final By userEmail = By.id("page-user-email");
    private final By userTier = By.id("page-user-tier");
    private final By btnLogout = By.cssSelector("button[data-logout]");
    private final By tabProfile = By.cssSelector("button[data-page-tab='profile']");
    private final By tabOrders = By.cssSelector("button[data-page-tab='orders']");
    private final By tabWishlist = By.cssSelector("button[data-page-tab='wishlist']");
    private final By tabAddresses = By.cssSelector("button[data-page-tab='addresses']");

    private final By paneProfile = By.id("page-pane-profile");
    private final By paneOrders = By.id("page-pane-orders");

    public boolean isPageLoaded() {
        return isDisplayed(userName) && isDisplayed(tabProfile);
    }

    public String getUserName() {
        return getText(userName);
    }

    public String getUserEmail() {
        return getText(userEmail);
    }

    public String getUserTier() {
        return getText(userTier);
    }

    public void selectOrdersTab() {
        click(tabOrders, "Tab Đơn hàng của tôi");
    }

    public void selectProfileTab() {
        click(tabProfile, "Tab Thông tin cá nhân");
    }

    public void selectWishlistTab() {
        click(tabWishlist, "Tab Yêu thích");
    }

    public void selectAddressesTab() {
        click(tabAddresses, "Tab Sổ địa chỉ");
    }

    public boolean isProfilePaneDisplayed() {
        return isDisplayed(paneProfile);
    }

    public boolean isOrdersPaneDisplayed() {
        return isDisplayed(paneOrders);
    }

    public void clickLogout() {
        click(btnLogout, "Nút Đăng xuất");
    }
}
