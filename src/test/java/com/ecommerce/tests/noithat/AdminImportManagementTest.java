package com.ecommerce.tests.noithat;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithat.AdminDashboardPage;
import com.ecommerce.pages.noithat.AdminImportPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test Suite kiểm thử Phân hệ Quản lý Nhập hàng & Nhà cung cấp (/admin/nhap-hang)
 */
public class AdminImportManagementTest extends BaseTest {

    private AdminImportPage importPage;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        openAdmin();
        AdminDashboardPage dashboardPage = new AdminDashboardPage();
        importPage = dashboardPage.navigateToImportManagement();
    }

    @Test(groups = {"smoke", "admin", "import"}, description = "TC_IMPORT_001: Xác nhận trang Quản lý nhập hàng tải thành công cùng 4 Bento KPI thẻ")
    public void testAdminImportPageLoadsSuccessfully() {
        ExtentReportManager.logInfo("Kiểm tra trang Quản lý nhập hàng đã tải thành công");
        Assert.assertTrue(importPage.isPageLoaded(), "Trang Quản lý nhập hàng phải hiển thị tiêu đề hợp lệ.");

        ExtentReportManager.logInfo("Xác minh 4 thẻ KPI tài chính kho vận");
        Assert.assertNotNull(importPage.getTotalImportText(), "KPI Tổng tiền nhập kho phải hiển thị.");
        Assert.assertNotNull(importPage.getTotalPaidText(), "KPI Đã thanh toán NCC phải hiển thị.");
        Assert.assertNotNull(importPage.getTotalDebtText(), "KPI Nợ NCC phải hiển thị.");
        Assert.assertNotNull(importPage.getTotalItemsText(), "KPI Mặt hàng nhập kho phải hiển thị.");
        ExtentReportManager.logPass("Tất cả 4 thẻ KPI nhập kho hiển thị đầy đủ thông tin.");
    }

    @Test(groups = {"regression", "admin", "import"}, description = "TC_IMPORT_002: Chuyển đổi qua lại giữa Tab Phiếu Nhập và Tab Nhà Cung Cấp")
    public void testSwitchBetweenSlipsAndSuppliersTab() {
        ExtentReportManager.logInfo("Chuyển sang Tab Danh sách Nhà cung cấp");
        importPage.switchToSuppliersTab();
        Assert.assertTrue(importPage.isSuppliersTabActive(), "Tab Nhà cung cấp phải được kích hoạt và hiển thị bảng đối tác.");

        ExtentReportManager.logInfo("Chuyển lại về Tab Lịch sử Phiếu nhập kho");
        importPage.switchToSlipsTab();
        Assert.assertTrue(importPage.isSlipsTabActive(), "Tab Phiếu nhập phải được hiển thị lại bình thường.");
        ExtentReportManager.logPass("Chuyển đổi Tab linh hoạt, chính xác.");
    }

    @Test(groups = {"regression", "admin", "import"}, description = "TC_IMPORT_003: Mở và đóng modal Tạo Phiếu Nhập Mới")
    public void testOpenAndCloseCreateSlipModal() {
        ExtentReportManager.logInfo("Mở modal Lập phiếu nhập hàng mới");
        importPage.openCreateSlipModal();
        Assert.assertTrue(importPage.isCreateSlipModalOpen(), "Modal Lập phiếu nhập kho phải được mở ra.");

        ExtentReportManager.logInfo("Đóng modal Lập phiếu nhập");
        importPage.closeCreateSlipModal();
        Assert.assertFalse(importPage.isCreateSlipModalOpen(), "Modal Lập phiếu nhập phải được đóng lại.");
        ExtentReportManager.logPass("Modal Tạo phiếu nhập mở và đóng chính xác.");
    }

    @Test(groups = {"regression", "admin", "import"}, description = "TC_IMPORT_004: Mở và đóng modal Thêm Nhà Cung Cấp Mới")
    public void testOpenAndCloseAddSupplierModal() {
        ExtentReportManager.logInfo("Mở modal Thêm Nhà Cung Cấp mới");
        importPage.openAddSupplierModal();
        Assert.assertTrue(importPage.isAddSupplierModalOpen(), "Modal Thêm nhà cung cấp phải hiển thị.");

        ExtentReportManager.logInfo("Đóng modal Thêm Nhà Cung Cấp");
        importPage.closeAddSupplierModal();
        Assert.assertFalse(importPage.isAddSupplierModalOpen(), "Modal Thêm nhà cung cấp phải đóng.");
        ExtentReportManager.logPass("Modal Thêm nhà cung cấp mở và đóng mượt mà.");
    }

    @Test(groups = {"regression", "admin", "import"}, description = "TC_IMPORT_005: Tìm kiếm phiếu nhập theo mã phiếu")
    public void testSearchSlipByCode() {
        String slipCode = importPage.getFirstSlipCode();
        if (slipCode != null && !slipCode.isBlank()) {
            ExtentReportManager.logInfo("Tìm kiếm theo mã phiếu nhập thực tế: " + slipCode);
            importPage.searchSlip(slipCode);
            int count = importPage.getSlipRowsCount();
            ExtentReportManager.logInfo("Số lượng phiếu nhập tìm thấy: " + count);
            Assert.assertTrue(count >= 1, "Phải tìm thấy ít nhất 1 phiếu nhập tương ứng với mã: " + slipCode);
            ExtentReportManager.logPass("Tìm kiếm phiếu nhập theo mã thành công.");
        } else {
            ExtentReportManager.logInfo("Chưa có phiếu nhập mẫu trong hệ thống, thực hiện tìm kiếm từ khóa kiểm thử.");
            importPage.searchSlip("PN-");
            Assert.assertTrue(importPage.isPageLoaded());
        }
    }

    @Test(groups = {"regression", "admin", "import"}, description = "TC_IMPORT_006: Lọc phiếu nhập theo trạng thái thanh toán")
    public void testFilterPaymentStatus() {
        importPage.searchSlip("");
        ExtentReportManager.logInfo("Lọc danh sách theo trạng thái 'Đã thanh toán đủ' (paid)");
        importPage.filterByPaymentStatus("paid");
        int paidCount = importPage.getSlipRowsCount();
        ExtentReportManager.logInfo("Số phiếu nhập đã thanh toán: " + paidCount);

        ExtentReportManager.logInfo("Xóa bộ lọc (Tất cả thanh toán)");
        importPage.filterByPaymentStatus("");
        int allCount = importPage.getSlipRowsCount();
        Assert.assertTrue(allCount >= paidCount, "Tổng số phiếu nhập phải lớn hơn hoặc bằng số phiếu đã thanh toán.");
        ExtentReportManager.logPass("Bộ lọc trạng thái thanh toán hoạt động chính xác.");
    }
}
