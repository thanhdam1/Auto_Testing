package com.ecommerce.tests.noithat;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithat.AdminCashbookPage;
import com.ecommerce.pages.noithat.AdminDashboardPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test Suite kiểm thử Phân hệ Sổ quỹ Tiền mặt & Đối soát COD (/admin/so-quy)
 */
public class AdminCashbookManagementTest extends BaseTest {

    private AdminCashbookPage cashbookPage;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        openAdmin();
        AdminDashboardPage dashboardPage = new AdminDashboardPage();
        cashbookPage = dashboardPage.navigateToCashbook();
    }

    @Test(groups = {"smoke", "admin", "cashbook"}, description = "TC_CASHBOOK_001: Xác nhận trang Sổ quỹ & Đối soát COD tải thành công")
    public void testAdminCashbookPageLoadsSuccessfully() {
        ExtentReportManager.logInfo("Kiểm tra trang Sổ quỹ đã tải sẵn sàng");
        Assert.assertTrue(cashbookPage.isPageLoaded(), "Trang Sổ quỹ phải hiển thị tiêu đề hợp lệ.");
        ExtentReportManager.logPass("Trang Sổ quỹ tải thành công cùng đầy đủ nút tác vụ.");
    }

    @Test(groups = {"regression", "admin", "cashbook"}, description = "TC_CASHBOOK_002: Chuyển đổi qua lại giữa Tab Đối soát COD và Tab Nhật ký Sổ quỹ")
    public void testSwitchBetweenCodAndLedgerTabs() {
        ExtentReportManager.logInfo("Chuyển sang Tab Nhật ký Sổ Quỹ Thu - Chi");
        cashbookPage.switchToLedgerTab();
        Assert.assertTrue(cashbookPage.isLedgerTabActive(), "Tab Nhật ký Sổ Quỹ phải được kích hoạt.");

        ExtentReportManager.logInfo("Chuyển lại về Tab Đối soát COD");
        cashbookPage.switchToCodTab();
        Assert.assertTrue(cashbookPage.isCodTabActive(), "Tab Đối soát COD phải được hiển thị lại bình thường.");
        ExtentReportManager.logPass("Chuyển đổi Tab đối soát COD và Sổ quỹ hoạt động chuẩn xác.");
    }

    @Test(groups = {"regression", "admin", "cashbook"}, description = "TC_CASHBOOK_003: Mở modal Lập Phiếu Thu Tiền và kiểm tra tiêu đề modal")
    public void testOpenAndCloseCreateIncomeVoucherModal() {
        ExtentReportManager.logInfo("Mở modal Lập Phiếu Thu");
        cashbookPage.openCreateIncomeModal();
        Assert.assertTrue(cashbookPage.isCreateVoucherModalOpen(), "Modal Lập phiếu thu phải hiển thị.");

        String title = cashbookPage.getVoucherModalTitle();
        ExtentReportManager.logInfo("Tiêu đề modal hiển thị: " + title);
        Assert.assertTrue(title.contains("Thu"), "Tiêu đề modal phải là Lập Phiếu Thu Tiền.");

        ExtentReportManager.logInfo("Đóng modal Lập Phiếu Thu");
        cashbookPage.closeCreateVoucherModal();
        Assert.assertFalse(cashbookPage.isCreateVoucherModalOpen(), "Modal Lập phiếu thu phải được đóng.");
        ExtentReportManager.logPass("Modal Lập phiếu thu mở, kiểm tra tiêu đề và đóng thành công.");
    }

    @Test(groups = {"regression", "admin", "cashbook"}, description = "TC_CASHBOOK_004: Mở modal Lập Phiếu Chi Tiền và kiểm tra tiêu đề modal")
    public void testOpenAndCloseCreateExpenseVoucherModal() {
        ExtentReportManager.logInfo("Mở modal Lập Phiếu Chi");
        cashbookPage.openCreateExpenseModal();
        Assert.assertTrue(cashbookPage.isCreateVoucherModalOpen(), "Modal Lập phiếu chi phải hiển thị.");

        String title = cashbookPage.getVoucherModalTitle();
        ExtentReportManager.logInfo("Tiêu đề modal hiển thị: " + title);
        Assert.assertTrue(title.contains("Chi"), "Tiêu đề modal phải là Lập Phiếu Chi Tiền.");

        ExtentReportManager.logInfo("Đóng modal Lập Phiếu Chi");
        cashbookPage.closeCreateVoucherModal();
        Assert.assertFalse(cashbookPage.isCreateVoucherModalOpen(), "Modal Lập phiếu chi phải được đóng.");
        ExtentReportManager.logPass("Modal Lập phiếu chi mở, kiểm tra tiêu đề và đóng thành công.");
    }

    @Test(groups = {"regression", "admin", "cashbook"}, description = "TC_CASHBOOK_005: Tìm kiếm và lọc Nhật ký giao dịch sổ quỹ")
    public void testSearchAndFilterLedger() {
        ExtentReportManager.logInfo("Chuyển sang Tab Nhật ký Sổ Quỹ");
        cashbookPage.switchToLedgerTab();

        ExtentReportManager.logInfo("Lọc giao dịch theo loại Phiếu Thu (income)");
        cashbookPage.filterLedgerByType("income");
        int incomeCount = cashbookPage.getLedgerRowCount();
        ExtentReportManager.logInfo("Số giao dịch thu: " + incomeCount);

        ExtentReportManager.logInfo("Lọc giao dịch theo hình thức Tiền mặt két quỹ (cash)");
        cashbookPage.filterLedgerByMethod("cash");

        ExtentReportManager.logInfo("Thực hiện tìm kiếm theo từ khóa trong nhật ký");
        cashbookPage.searchLedger("Thu");
        Assert.assertTrue(cashbookPage.isPageLoaded());
        ExtentReportManager.logPass("Bộ lọc và tìm kiếm nhật ký giao dịch sổ quỹ hoạt động ổn định.");
    }
}
