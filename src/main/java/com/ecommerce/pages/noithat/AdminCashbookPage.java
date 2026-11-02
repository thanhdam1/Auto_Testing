package com.ecommerce.pages.noithat;

import com.ecommerce.pages.base.BasePage;
import com.ecommerce.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for Admin Cashbook Management (/admin/so-quy)
 * Phân hệ Sổ quỹ Tiền mặt & Đối soát COD
 */
public class AdminCashbookPage extends BasePage {

    // Page Header
    private final By pageHeader = By.xpath("//h1[contains(text(),'Sổ Quỹ Tiền Mặt') or contains(text(),'Sổ Quỹ')]");

    // Action Buttons
    private final By btnOpenCreateExpense = By.id("btn-open-create-expense");
    private final By btnOpenCreateIncome = By.id("btn-open-create-income");

    // Navigation Tabs
    private final By tabBtnCod = By.id("tab-btn-cod");
    private final By tabBtnLedger = By.id("tab-btn-ledger");
    private final By tabContentCod = By.id("tab-content-cod");
    private final By tabContentLedger = By.id("tab-content-ledger");

    // COD Tab elements
    private final By btnReconcileCodList = By.cssSelector(".btn-reconcile-cod");

    // Ledger Tab elements
    private final By searchLedgerInput = By.id("search-ledger-input");
    private final By filterLedgerType = By.id("filter-ledger-type");
    private final By filterLedgerMethod = By.id("filter-ledger-method");
    private final By ledgerRows = By.cssSelector(".ledger-row");
    private final By btnPrintVoucherList = By.cssSelector(".btn-print-voucher");

    // Modal Create Voucher
    private final By modalCreateVoucher = By.id("modal-create-voucher");
    private final By voucherModalTitle = By.id("voucher-modal-title");
    private final By inputVoucherType = By.id("voucher-type");
    private final By inputVoucherAmount = By.id("voucher-amount");
    private final By selectVoucherMethod = By.id("voucher-method");
    private final By inputVoucherCategory = By.id("voucher-category");
    private final By inputVoucherRecipient = By.id("voucher-recipient");
    private final By inputVoucherRef = By.id("voucher-ref");
    private final By inputVoucherNote = By.id("voucher-note");
    private final By btnSubmitVoucher = By.id("btn-submit-voucher");
    private final By btnCloseVoucherModal = By.cssSelector("#modal-create-voucher .btn-close-modal");

    // Modal Print Voucher
    private final By modalPrintVoucher = By.id("modal-print-voucher");
    private final By btnClosePrintVoucherModal = By.cssSelector("#modal-print-voucher .btn-close-modal");

    public boolean isPageLoaded() {
        return isDisplayed(pageHeader);
    }

    public void switchToLedgerTab() {
        click(tabBtnLedger, "Tab Nhật ký Sổ Quỹ Thu - Chi");
        WaitUtil.waitForVisibility(tabContentLedger);
    }

    public void switchToCodTab() {
        click(tabBtnCod, "Tab Đối soát Tiền mặt COD");
        WaitUtil.waitForVisibility(tabContentCod);
    }

    public boolean isLedgerTabActive() {
        try {
            List<WebElement> els = getDriver().findElements(tabContentLedger);
            if (els.isEmpty()) return false;
            WebElement content = els.get(0);
            return content.isDisplayed() && !content.getAttribute("class").contains("hidden");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCodTabActive() {
        try {
            List<WebElement> els = getDriver().findElements(tabContentCod);
            if (els.isEmpty()) return false;
            WebElement content = els.get(0);
            return content.isDisplayed() && !content.getAttribute("class").contains("hidden");
        } catch (Exception e) {
            return false;
        }
    }

    public void openCreateIncomeModal() {
        click(btnOpenCreateIncome, "Nút Lập Phiếu Thu");
        WaitUtil.waitForVisibility(modalCreateVoucher);
    }

    public void openCreateExpenseModal() {
        click(btnOpenCreateExpense, "Nút Lập Phiếu Chi");
        WaitUtil.waitForVisibility(modalCreateVoucher);
    }

    public void closeCreateVoucherModal() {
        click(btnCloseVoucherModal, "Nút Đóng modal Tạo phiếu thu/chi");
        try { Thread.sleep(200); } catch (InterruptedException ignored) {}
    }

    public boolean isCreateVoucherModalOpen() {
        try {
            List<WebElement> els = getDriver().findElements(modalCreateVoucher);
            if (els.isEmpty()) return false;
            WebElement modal = els.get(0);
            return modal.isDisplayed() && !modal.getAttribute("class").contains("hidden");
        } catch (Exception e) {
            return false;
        }
    }

    public String getVoucherModalTitle() {
        return getText(voucherModalTitle);
    }

    public void searchLedger(String keyword) {
        WebElement input = find(searchLedgerInput);
        input.clear();
        input.sendKeys(keyword);
        ((JavascriptExecutor) getDriver()).executeScript(
            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", input);
        try { Thread.sleep(300); } catch (InterruptedException ignored) {}
    }

    public void filterLedgerByType(String typeValue) {
        selectDropdownByValue(filterLedgerType, typeValue, "Lọc loại phiếu (income/expense)");
        try { Thread.sleep(300); } catch (InterruptedException ignored) {}
    }

    public void filterLedgerByMethod(String methodValue) {
        selectDropdownByValue(filterLedgerMethod, methodValue, "Lọc hình thức thanh toán (cash/bank)");
        try { Thread.sleep(300); } catch (InterruptedException ignored) {}
    }

    public int getLedgerRowCount() {
        try {
            List<WebElement> rows = getDriver().findElements(ledgerRows);
            return (int) rows.stream().filter(WebElement::isDisplayed).count();
        } catch (Exception e) {
            return 0;
        }
    }
}
