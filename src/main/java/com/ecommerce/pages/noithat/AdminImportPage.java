package com.ecommerce.pages.noithat;

import com.ecommerce.pages.base.BasePage;
import com.ecommerce.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for Admin Import Management (/admin/nhap-hang)
 * Phân hệ Quản lý Nhập hàng & Nhà cung cấp
 */
public class AdminImportPage extends BasePage {

    // Page Header & KPI Cards
    private final By pageHeader = By.xpath("//h1[contains(text(),'Quản Lý Nhập Hàng') or contains(text(),'Nhập Hàng')]");
    private final By kpiTotalImport = By.id("kpi-total-import");
    private final By kpiTotalPaid = By.id("kpi-total-paid");
    private final By kpiTotalDebt = By.id("kpi-total-debt");
    private final By kpiTotalItems = By.id("kpi-total-items");

    // Action Buttons
    private final By btnOpenCreateSlip = By.id("btn-open-create-slip");
    private final By btnOpenAddSupplier = By.id("btn-open-add-supplier");

    // Tabs
    private final By tabBtnSlips = By.id("tab-btn-slips");
    private final By tabBtnSuppliers = By.id("tab-btn-suppliers");
    private final By tabContentSlips = By.id("tab-content-slips");
    private final By tabContentSuppliers = By.id("tab-content-suppliers");

    // Slips Table & Filter
    private final By searchSlipsInput = By.id("search-slips-input");
    private final By filterPaymentStatus = By.id("filter-payment-status");
    private final By slipsTableBody = By.id("slips-table-body");
    private final By slipRows = By.cssSelector(".slip-row");
    private final By btnViewSlipList = By.cssSelector(".btn-view-slip");

    // Modal Create Slip
    private final By modalCreateSlip = By.id("modal-create-slip");
    private final By selectSupplier = By.id("slip-supplier-id");
    private final By inputImportDate = By.id("slip-import-date");
    private final By inputReferenceCode = By.id("slip-reference-code");
    private final By btnAddItemRow = By.id("btn-add-item-row");
    private final By inputPaidAmount = By.id("slip-paid-amount");
    private final By selectPaymentMethod = By.id("slip-payment-method");
    private final By inputSlipNote = By.id("slip-note");
    private final By btnSubmitCreateSlip = By.id("btn-submit-create-slip");
    private final By btnCloseCreateSlipModal = By.cssSelector("#modal-create-slip .btn-close-modal");

    // Modal Add Supplier
    private final By modalAddSupplier = By.id("modal-add-supplier");
    private final By btnSubmitAddSupplier = By.id("btn-submit-add-supp");
    private final By btnCloseAddSupplierModal = By.cssSelector("#modal-add-supplier .btn-close-modal");

    // Modal View Slip Detail
    private final By modalViewSlip = By.id("modal-view-slip");
    private final By btnPrintSlip = By.id("btn-print-slip");
    private final By btnCloseViewSlipModal = By.cssSelector("#modal-view-slip .btn-close-modal");

    public boolean isPageLoaded() {
        return isDisplayed(pageHeader);
    }

    public String getTotalImportText() {
        return getText(kpiTotalImport);
    }

    public String getTotalPaidText() {
        return getText(kpiTotalPaid);
    }

    public String getTotalDebtText() {
        return getText(kpiTotalDebt);
    }

    public String getTotalItemsText() {
        return getText(kpiTotalItems);
    }

    public void switchToSuppliersTab() {
        click(tabBtnSuppliers, "Tab Danh sách Nhà cung cấp");
        WaitUtil.waitForVisibility(tabContentSuppliers);
    }

    public void switchToSlipsTab() {
        click(tabBtnSlips, "Tab Lịch sử Phiếu nhập");
        WaitUtil.waitForVisibility(tabContentSlips);
    }

    public boolean isSuppliersTabActive() {
        try {
            List<WebElement> els = getDriver().findElements(tabContentSuppliers);
            if (els.isEmpty()) return false;
            WebElement content = els.get(0);
            return content.isDisplayed() && !content.getAttribute("class").contains("hidden");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSlipsTabActive() {
        try {
            List<WebElement> els = getDriver().findElements(tabContentSlips);
            if (els.isEmpty()) return false;
            WebElement content = els.get(0);
            return content.isDisplayed() && !content.getAttribute("class").contains("hidden");
        } catch (Exception e) {
            return false;
        }
    }

    public void searchSlip(String keyword) {
        WebElement input = find(searchSlipsInput);
        input.clear();
        input.sendKeys(keyword);
        ((JavascriptExecutor) getDriver()).executeScript(
            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", input);
        try { Thread.sleep(300); } catch (InterruptedException ignored) {}
    }

    public void filterByPaymentStatus(String statusValue) {
        selectDropdownByValue(filterPaymentStatus, statusValue, "Lọc trạng thái thanh toán phiếu nhập");
        try { Thread.sleep(300); } catch (InterruptedException ignored) {}
    }

    public int getSlipRowsCount() {
        try {
            List<WebElement> rows = getDriver().findElements(slipRows);
            return (int) rows.stream().filter(WebElement::isDisplayed).count();
        } catch (Exception e) {
            return 0;
        }
    }

    public String getFirstSlipCode() {
        try {
            List<WebElement> rows = getDriver().findElements(slipRows);
            for (WebElement row : rows) {
                if (row.isDisplayed()) {
                    String code = row.getAttribute("data-code");
                    if (code != null && !code.isBlank()) return code;
                }
            }
        } catch (Exception e) {}
        return null;
    }

    public void openCreateSlipModal() {
        click(btnOpenCreateSlip, "Nút Tạo Phiếu Nhập Mới");
        WaitUtil.waitForVisibility(modalCreateSlip);
    }

    public void closeCreateSlipModal() {
        click(btnCloseCreateSlipModal, "Nút Đóng modal Tạo phiếu nhập");
        try { Thread.sleep(200); } catch (InterruptedException ignored) {}
    }

    public boolean isCreateSlipModalOpen() {
        try {
            List<WebElement> els = getDriver().findElements(modalCreateSlip);
            if (els.isEmpty()) return false;
            WebElement modal = els.get(0);
            return modal.isDisplayed() && !modal.getAttribute("class").contains("hidden");
        } catch (Exception e) {
            return false;
        }
    }

    public void openAddSupplierModal() {
        click(btnOpenAddSupplier, "Nút Thêm Nhà Cung Cấp");
        WaitUtil.waitForVisibility(modalAddSupplier);
    }

    public void closeAddSupplierModal() {
        click(btnCloseAddSupplierModal, "Nút Đóng modal Thêm nhà cung cấp");
        try { Thread.sleep(200); } catch (InterruptedException ignored) {}
    }

    public boolean isAddSupplierModalOpen() {
        try {
            List<WebElement> els = getDriver().findElements(modalAddSupplier);
            if (els.isEmpty()) return false;
            WebElement modal = els.get(0);
            return modal.isDisplayed() && !modal.getAttribute("class").contains("hidden");
        } catch (Exception e) {
            return false;
        }
    }

    public void openFirstSlipDetail() {
        List<WebElement> viewBtns = getDriver().findElements(btnViewSlipList);
        for (WebElement btn : viewBtns) {
            if (btn.isDisplayed()) {
                btn.click();
                WaitUtil.waitForVisibility(modalViewSlip);
                return;
            }
        }
    }

    public boolean isSlipDetailModalOpen() {
        try {
            List<WebElement> els = getDriver().findElements(modalViewSlip);
            if (els.isEmpty()) return false;
            WebElement modal = els.get(0);
            return modal.isDisplayed() && !modal.getAttribute("class").contains("hidden");
        } catch (Exception e) {
            return false;
        }
    }

    public void closeSlipDetailModal() {
        click(btnCloseViewSlipModal, "Nút Đóng modal Xem chi tiết phiếu nhập");
        try { Thread.sleep(200); } catch (InterruptedException ignored) {}
    }
}
