package com.ecommerce.tests.noithatbang;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithatbang.BangAdminCategoryPage;
import com.ecommerce.pages.noithatbang.BangAdminDashboardPage;
import com.ecommerce.pages.noithatbang.BangAdminLoginPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * UI Test Suite for Website_NoiThat Admin Category Management (/categories)
 * Port: 5173
 */
public class BangAdminCategoryTest extends BaseTest {

    private BangAdminLoginPage loginPage;
    private BangAdminDashboardPage dashboardPage;
    private BangAdminCategoryPage categoryPage;

    @BeforeMethod
    public void setupTest() {
        openNoiThatBangAdmin();
        loginPage = new BangAdminLoginPage();
        loginPage.login("admin@noithat.com", "Admin@123");
        dashboardPage = new BangAdminDashboardPage();
        categoryPage = dashboardPage.navigateToCategories();
        Assert.assertTrue(categoryPage.isPageLoaded(), "Trang Quản lý Danh mục phải tải thành công");
    }

    @Test(description = "WNT_CAT_001: Trang Danh mục tải đúng, hiển thị tiêu đề và nút Thêm mới")
    public void testCategoryPageLoads() {
        Assert.assertTrue(categoryPage.isPageLoaded(), "Trang Danh mục phải hiển thị tiêu đề và nút Thêm");
        ExtentReportManager.logInfo("Trang Danh mục tải thành công với " + categoryPage.getCategoryCount() + " mục");
    }

    @Test(description = "WNT_CAT_002: Mở modal form Thêm danh mục mới")
    public void testOpenCreateCategoryModal() {
        categoryPage.openCreateModal();
        Assert.assertTrue(categoryPage.isModalOpen(), "Modal thêm danh mục phải mở ra");
        categoryPage.closeModal();
        ExtentReportManager.logInfo("Đã mở và đóng modal Thêm danh mục thành công");
    }

    @Test(description = "WNT_CAT_003: Thêm danh mục hợp lệ thành công với dữ liệu động")
    public void testCreateValidCategory() {
        categoryPage.openCreateModal();
        String catName = "Danh Mục Auto " + System.currentTimeMillis();
        String catSlug = "danh-muc-auto-" + System.currentTimeMillis();

        ExtentReportManager.logInfo("Tạo danh mục mới: " + catName);
        categoryPage.fillCategoryForm(catName, catSlug);
        categoryPage.submitSave();
        Assert.assertFalse(categoryPage.isModalOpen(), "Modal phải đóng lại sau khi lưu thành công");
    }

    @Test(description = "WNT_CAT_004: Đóng modal danh mục an toàn khi bấm nút Hủy")
    public void testCancelCategoryModal() {
        categoryPage.openCreateModal();
        Assert.assertTrue(categoryPage.isModalOpen(), "Modal phải mở");
        categoryPage.closeModal();
        Assert.assertFalse(categoryPage.isModalOpen(), "Modal phải đóng khi bấm Hủy");
    }

    @Test(description = "WNT_CAT_005: Kiểm tra danh sách danh mục trong bảng")
    public void testCategoryListCount() {
        int count = categoryPage.getCategoryCount();
        ExtentReportManager.logInfo("Số lượng danh mục hiện tại: " + count);
        Assert.assertTrue(categoryPage.isPageLoaded() && (count > 0 || categoryPage.isEmptyStateDisplayed()),
                "Bảng danh mục phải hiển thị danh sách dòng hoặc trạng thái rỗng hợp lệ");
    }

    @Test(description = "WNT_CAT_006: Xóa danh mục -> Hiển thị Dialog xác nhận -> Bấm Hủy")
    public void testCancelDeleteCategoryDialog() {
        if (categoryPage.getCategoryCount() > 0) {
            categoryPage.clickDeleteFirstCategory();
            Assert.assertTrue(categoryPage.isConfirmDeleteDialogDisplayed(), "Dialog Xác nhận xóa danh mục phải hiển thị");
            categoryPage.cancelDeleteDialog();
            ExtentReportManager.logInfo("Đã hủy thao tác xóa danh mục an toàn");
        } else {
            ExtentReportManager.logInfo("Không có danh mục nào để test xóa");
        }
    }
}
