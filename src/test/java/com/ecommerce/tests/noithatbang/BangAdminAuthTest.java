package com.ecommerce.tests.noithatbang;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithatbang.BangAdminDashboardPage;
import com.ecommerce.pages.noithatbang.BangAdminLoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * UI Test Suite for Website_NoiThat Admin Authentication
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangAdminAuthTest extends BaseTest {

    private BangAdminLoginPage loginPage;

    @BeforeMethod
    public void setupTest() {
        openNoiThatBangAdmin();
        loginPage = new BangAdminLoginPage();
    }

    @Test(description = "WNT_ADM_001: Màn hình Đăng nhập Admin hiển thị đầy đủ các phần tử")
    public void testAdminLoginPageLoads() {
        Assert.assertTrue(loginPage.isPageLoaded(), "Trang đăng nhập Admin phải hiển thị tiêu đề và nút đăng nhập");
    }

    @Test(description = "WNT_ADM_002: Đăng nhập Admin thành công với tài khoản quản trị hợp lệ")
    public void testAdminLoginSuccess() {
        loginPage.login("admin@noithat.com", "Admin@123");
        BangAdminDashboardPage dashboardPage = new BangAdminDashboardPage();
        Assert.assertTrue(dashboardPage.isPageLoaded(), "Sau khi đăng nhập thành công phải chuyển về Dashboard quản trị");
    }

    @Test(description = "WNT_ADM_003: Đăng nhập Admin thất bại khi nhập sai mật khẩu")
    public void testAdminLoginWrongPassword() {
        loginPage.login("admin@noithat.com", "MatKhauSai@999");
        Assert.assertTrue(loginPage.isServerErrorDisplayed(), "Phải hiển thị thông báo lỗi khi sai mật khẩu");
    }
}
