package com.ecommerce.pages.repair;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for IT Support Staff / Admin Dashboard (/admin)
 */
public class RepairAdminDashboardPage extends BasePage {

    // Header & Actions
    private final By headingDashboard = By.xpath("//h2[contains(text(),'Bảng điều khiển')]");
    private final By btnLogout = By.xpath("//button[contains(text(),'Đăng Xuất')]");

    // Tabs
    private final By tabTickets = By.xpath("//button[contains(text(),'Phiếu Yêu Cầu')]");
    private final By tabBookings = By.xpath("//button[contains(text(),'Lịch Hẹn Khách Hàng')]");

    // Tables & Rows
    private final By tableRows = By.cssSelector("table tbody tr");

    public boolean isPageLoaded() {
        return isDisplayed(headingDashboard);
    }

    public void selectTicketsTab() {
        click(tabTickets, "Tab Phiếu Yêu Cầu");
    }

    public void selectBookingsTab() {
        click(tabBookings, "Tab Lịch Hẹn Khách Hàng");
    }

    public int getVisibleTableRowCount() {
        try {
            List<WebElement> rows = getDriver().findElements(tableRows);
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void clickLogout() {
        click(btnLogout, "Nút Đăng Xuất");
    }
}
