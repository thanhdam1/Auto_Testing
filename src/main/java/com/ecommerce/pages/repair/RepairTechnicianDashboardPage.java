package com.ecommerce.pages.repair;

import com.ecommerce.pages.base.BasePage;
import com.ecommerce.utils.WaitUtil;
import org.openqa.selenium.By;

/**
 * Page Object for the technician view of the internal repair portal.
 */
public class RepairTechnicianDashboardPage extends BasePage {

    private final By dashboardHeading = By.xpath("//main//h2[contains(normalize-space(.),'Bảng điều khiển')]");
    private final By ticketSearch = By.cssSelector("main input[type='search']");
    private final By ticketRows = By.cssSelector("main table tbody tr");
    private final By noTicketResult = By.xpath(
            "//main//td[contains(normalize-space(.),'Không tìm thấy')]");
    private final By notificationButton = By.cssSelector("button[aria-label^='Thông báo']");
    private final By notificationCenter = By.xpath("//main//h3[normalize-space()='Trung tâm thông báo']");
    private final By logoutButton = By.xpath("//main//button[normalize-space()='Đăng Xuất']");
    private final By printTicketButtons = By.xpath(
            "//main//button[normalize-space()='In phiếu 80mm']");

    public void waitUntilLoaded() {
        WaitUtil.waitForVisibility(dashboardHeading, 30);
    }

    public boolean isLoaded() {
        return isDisplayed(dashboardHeading);
    }

    public String getHeading() {
        return getText(dashboardHeading);
    }

    public void searchTickets(String query) {
        type(ticketSearch, query, "Ô tìm kiếm phiếu KTV");
    }

    public String getSearchValue() {
        return find(ticketSearch).getAttribute("value");
    }

    public int getTicketRowCount() {
        return (int) getDriver().findElements(ticketRows).stream()
                .filter(row -> row.findElements(By.cssSelector("td")).size() > 1)
                .count();
    }

    public boolean isNoTicketResultDisplayed() {
        return isDisplayed(noTicketResult);
    }

    public int getPrintTicketButtonCount() {
        return getDriver().findElements(printTicketButtons).size();
    }

    public void openNotifications() {
        click(notificationButton, "Nút thông báo KTV");
    }

    public boolean isNotificationCenterDisplayed() {
        return isDisplayed(notificationCenter);
    }

    public void logout() {
        click(logoutButton, "Nút đăng xuất KTV");
    }
}
