package com.ecommerce.tests.noithat;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithat.AdminCategoryPage;
import com.ecommerce.pages.noithat.AdminDashboardPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AdminCategoryManagementTest extends BaseTest {

    private AdminCategoryPage categoryPage;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        openAdmin();
        AdminDashboardPage dashboardPage = new AdminDashboardPage();
        categoryPage = dashboardPage.navigateToCategories();
    }

    @Test(groups = {"smoke", "admin", "categories"}, description = "Trang Quản lý danh mục tải thành công và hiển thị danh sách danh mục")
    public void testCategoryPageLoadsWithData() {
        ExtentReportManager.logInfo("Kiểm tra trang Quản lý danh mục");
        Assert.assertTrue(categoryPage.isPageLoaded(), "Trang quản lý danh mục phải hiển thị tiêu đề hợp lệ.");
        int count = categoryPage.getCategoryRowCount();
        ExtentReportManager.logInfo("Số lượng danh mục trong bảng: " + count);
        Assert.assertTrue(count > 0, "Bảng danh mục phải có ít nhất 1 dòng dữ liệu.");
    }

    @Test(groups = {"regression", "admin", "categories"}, description = "Mở modal Thêm danh mục mới và đóng modal thành công")
    public void testOpenAndCloseAddCategoryModal() {
        ExtentReportManager.logInfo("Mở modal Thêm Danh Mục");
        categoryPage.openAddCategoryModal();
        Assert.assertTrue(categoryPage.isModalDisplayed(), "Modal thêm danh mục phải hiển thị trên màn hình.");

        ExtentReportManager.logInfo("Đóng modal");
        categoryPage.closeModal();
    }
}
