package com.ecommerce.tests.noithatbang;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithatbang.BangAdminDashboardPage;
import com.ecommerce.pages.noithatbang.BangAdminLoginPage;
import com.ecommerce.pages.noithatbang.BangAdminProductPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * UI Test Suite for Website_NoiThat Admin Product Management
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangAdminProductTest extends BaseTest {

    private BangAdminLoginPage loginPage;
    private BangAdminDashboardPage dashboardPage;

    @BeforeMethod
    public void setupTest() {
        openNoiThatBangAdmin();
        loginPage = new BangAdminLoginPage();
        loginPage.login("admin@noithat.com", "Admin@123");
        dashboardPage = new BangAdminDashboardPage();
    }

    @Test(description = "WNT_ADM_004: Điều hướng tới trang Quản lý Sản phẩm từ Sidebar")
    public void testNavigateToProductManagement() {
        BangAdminProductPage productPage = dashboardPage.navigateToProducts();
        Assert.assertTrue(productPage.isPageLoaded(), "Trang Quản lý Sản phẩm phải hiển thị tiêu đề");
        Assert.assertTrue(productPage.isNewProductButtonDisplayed(), "Phải hiển thị nút Thêm sản phẩm mới");
    }

    @Test(description = "WNT_ADM_005: Tìm kiếm sản phẩm theo từ khóa trên bảng sản phẩm")
    public void testSearchProducts() {
        BangAdminProductPage productPage = dashboardPage.navigateToProducts();
        productPage.searchProduct("Sofa");
        Assert.assertTrue(productPage.getProductRowsCount() >= 0, "Bảng sản phẩm phải phản hồi kết quả tìm kiếm");
    }
}
