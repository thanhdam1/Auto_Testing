package com.ecommerce.pages.repair;

import com.ecommerce.pages.base.BasePage;
import com.ecommerce.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for IT Support Staff / Admin Dashboard (/admin)
 */
public class RepairAdminDashboardPage extends BasePage {

    // Header & Actions
    private final By headingDashboard = By.cssSelector("main h2");
    private final By btnLogout = By.xpath("//button[normalize-space()='Đăng Xuất']");
    private final By inputTicketSearch = By.cssSelector("input[type='search']");
    private final By notificationCloseButton = By.cssSelector("button[aria-label='Đóng thông báo']");

    // Tabs
    private final By managementTabs = By.cssSelector("button[class*='rounded-t-lg']");
    private final By notificationCenter = By.cssSelector("main h3");
    private final By inventoryHeading = By.cssSelector("main h3");
    private final By softwareHeading = By.cssSelector("main h3");
    private final By reportsHeading = By.cssSelector("main h3");

    // Tables & Rows
    private final By ticketTable = By.cssSelector("main table");
    private final By tableRows = By.cssSelector("table tbody tr");
    private final By noTicketResults = By.xpath(
            "//main//td[normalize-space()='Không tìm thấy phiếu phù hợp.']");
    private final By noTicketResultCell = By.xpath(
            ".//td[normalize-space()='Không tìm thấy phiếu phù hợp.']");
    private final By printTicketButtons = By.xpath("//main//button[normalize-space()='In phiếu 80mm']");

    // Inventory controls
    private final By inventorySearch = By.cssSelector("main input[type='search']");
    private final By inventoryThreshold = By.cssSelector("input[type='number']");
    private final By inventoryStatusFilter = By.cssSelector("main select");

    // Software and report controls
    private final By softwareTicketSelect = By.xpath(
            "//label[contains(normalize-space(.),'Phiếu sửa chữa')]/select");
    private final By softwareGroupSelect = By.xpath(
            "//label[contains(normalize-space(.),'Nhóm phần mềm')]/select");
    private final By softwarePackageCheckboxes = By.cssSelector("input[type='checkbox']");
    private final By saveSoftwareButton = By.xpath("//main//button[normalize-space()='Lưu gói cho phiếu']");
    private final By reportPeriodSelect = By.xpath(
            "//label[contains(normalize-space(.),'Khoảng thời gian')]/select");

    public boolean isPageLoaded() {
        return isDisplayed(headingDashboard);
    }

    public void waitUntilLoaded() {
        WaitUtil.waitForVisibility(headingDashboard, 30);
    }

    public void selectTicketsTab() {
        clickManagementTab(0, "Tab Phiếu Yêu Cầu");
    }

    public void selectBookingsTab() {
        clickManagementTab(1, "Tab Lịch Hẹn Khách Hàng");
    }

    public void selectInventoryTab() {
        clickManagementTab(2, "Tab Kho Linh Kiện");
        WaitUtil.waitForVisibility(inventorySearch, 30);
    }

    public void selectSoftwareTab() {
        clickManagementTab(3, "Tab Gói Phần Mềm");
    }

    public void selectReportsTab() {
        clickManagementTab(4, "Tab Báo Cáo");
    }

    public void selectNotificationsTab() {
        clickManagementTab(5, "Tab Thông Báo");
    }

    private void clickManagementTab(int index, String tabName) {
        List<WebElement> tabs = findAll(managementTabs);
        if (tabs.size() <= index) {
            throw new IllegalStateException("Không tìm thấy tab quản lý thứ " + (index + 1)
                    + ", số tab thực tế: " + tabs.size());
        }
        WebElement tab = tabs.get(index);
        scrollToElement(managementTabs);
        tab.click();
    }

    public boolean isTabActive(int index) {
        try {
            List<WebElement> tabs = findAll(managementTabs);
            if (tabs.size() > index) {
                String classes = tabs.get(index).getAttribute("class");
                String ariaSelected = tabs.get(index).getAttribute("aria-selected");
                return "true".equalsIgnoreCase(ariaSelected)
                        || (classes != null && (classes.contains("border-") || classes.contains("active") || classes.contains("bg-")));
            }
        } catch (Exception ignored) {}
        return false;
    }

    public boolean isBookingsContentDisplayed() {
        return isTicketTableDisplayed() || isTicketNoResultsDisplayed()
                || getDriver().findElements(By.xpath("//*[contains(text(),'lịch hẹn') or contains(text(),'Lịch hẹn')]")).size() > 0;
    }

    public String getFirstVisibleTicketCode() {
        try {
            List<WebElement> rows = getDriver().findElements(tableRows);
            for (WebElement row : rows) {
                if (row.findElements(noTicketResultCell).isEmpty()) {
                    List<WebElement> cells = row.findElements(By.cssSelector("td"));
                    if (!cells.isEmpty()) {
                        String text = cells.get(0).getText().trim();
                        if (!text.isEmpty()) {
                            return text;
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
        return null;
    }

    public boolean allVisibleRowsContain(String query) {
        try {
            List<WebElement> rows = getDriver().findElements(tableRows);
            List<WebElement> dataRows = rows.stream()
                    .filter(row -> row.findElements(noTicketResultCell).isEmpty())
                    .toList();
            if (dataRows.isEmpty()) {
                return false;
            }
            for (WebElement row : dataRows) {
                if (!row.getText().toLowerCase().contains(query.toLowerCase())) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isNotificationCenterDisplayed() {
        return isDisplayed(notificationCenter);
    }

    public boolean isInventoryDisplayed() {
        return isDisplayed(inventoryHeading);
    }

    public boolean isSoftwareDisplayed() {
        return isDisplayed(softwareHeading);
    }

    public boolean isReportsDisplayed() {
        return isDisplayed(reportsHeading);
    }

    public void searchTickets(String query) {
        type(inputTicketSearch, query, "Ô tìm kiếm phiếu sửa chữa");
    }

    public int getVisibleTableRowCount() {
        try {
            List<WebElement> rows = getDriver().findElements(tableRows);
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public int getTicketDataRowCount() {
        return (int) getDriver().findElements(tableRows).stream()
                .filter(row -> row.findElements(noTicketResultCell).isEmpty())
                .count();
    }

    public boolean isTicketNoResultsDisplayed() {
        return isDisplayed(noTicketResults);
    }

    public String getTicketSearchValue() {
        return find(inputTicketSearch).getAttribute("value");
    }

    public int getPrintTicketButtonCount() {
        return getDriver().findElements(printTicketButtons).size();
    }

    public boolean isTicketTableDisplayed() {
        return isDisplayed(ticketTable);
    }

    public void searchInventory(String query) {
        type(inventorySearch, query, "Ô tìm kiếm linh kiện");
    }

    public void setInventoryThreshold(String threshold) {
        type(inventoryThreshold, threshold, "Ngưỡng cảnh báo tồn kho");
    }

    public String getInventoryThresholdValue() {
        return find(inventoryThreshold).getAttribute("value");
    }

    public String getInventoryThresholdValidationMessage() {
        return find(inventoryThreshold).getAttribute("validationMessage");
    }

    public void filterInventoryByStatus(String status) {
        selectDropdownByVisibleText(inventoryStatusFilter, status, "Bộ lọc trạng thái linh kiện");
    }

    public int getInventoryRowCount() {
        return (int) getDriver().findElements(tableRows).stream()
                .filter(row -> row.findElements(By.cssSelector("td")).size() > 1)
                .count();
    }

    public void selectSoftwareTicket(String ticketText) {
        selectDropdownByVisibleText(softwareTicketSelect, ticketText, "Phiếu sửa chữa phần mềm");
    }

    public void selectFirstAvailableSoftwareTicket() {
        try {
            org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(find(softwareTicketSelect));
            if (select.getOptions().size() > 1) {
                select.selectByIndex(1);
            }
        } catch (Exception ignored) {}
    }

    public void selectSoftwareGroup(String groupText) {
        selectDropdownByVisibleText(softwareGroupSelect, groupText, "Nhóm phần mềm");
    }

    public int getSoftwarePackageCount() {
        return getDriver().findElements(softwarePackageCheckboxes).size();
    }

    public int getEnabledSoftwarePackageCount() {
        return (int) getDriver().findElements(softwarePackageCheckboxes).stream()
                .filter(element -> element.isEnabled())
                .count();
    }

    public boolean isSaveSoftwareButtonDisabled() {
        return find(saveSoftwareButton).isDisplayed()
                && !find(saveSoftwareButton).isEnabled();
    }

    public void selectReportPeriod(String period) {
        selectDropdownByVisibleText(reportPeriodSelect, period, "Khoảng thời gian báo cáo");
    }

    public String getSelectedReportPeriod() {
        return find(reportPeriodSelect).getAttribute("value");
    }

    public boolean isNotificationDisplayed() {
        return isDisplayed(notificationCenter);
    }

    public void closeNotification() {
        click(notificationCloseButton, "Nút đóng thông báo");
    }

    public void clickLogout() {
        click(btnLogout, "Nút Đăng Xuất");
    }
}
