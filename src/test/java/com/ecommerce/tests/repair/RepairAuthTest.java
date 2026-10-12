package com.ecommerce.tests.repair;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.repair.RepairLoginPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RepairAuthTest extends BaseTest {

    private RepairLoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        openRepairAdmin();
        loginPage = new RepairLoginPage();
    }

    @Test(groups = {"smoke", "repair", "auth"}, description = "Xác nhận trang Đăng nhập nội bộ KTV tải thành công")
    public void testLoginPageLoadsSuccessfully() {
        ExtentReportManager.logInfo("Kiểm tra trang Đăng nhập Cổng Nội Bộ KTV");
        Assert.assertTrue(loginPage.isPageLoaded(), "Trang Đăng nhập nội bộ phải hiển thị tiêu đề 'Cổng Nội Bộ'.");
    }

    @Test(groups = {"regression", "repair", "auth"}, description = "Kiểm tra đăng nhập với thông tin không chính xác")
    public void testInvalidLoginDisplaysError() {
        ExtentReportManager.logInfo("Thử đăng nhập với tài khoản sai: 'wrong_ktv' / '123456'");
        loginPage.login("wrong_ktv", "123456");

        Assert.assertTrue(loginPage.isErrorAlertDisplayed(), "Phải hiển thị thông báo lỗi khi đăng nhập không thành công.");
        ExtentReportManager.logInfo("Thông báo lỗi hiển thị: " + loginPage.getErrorAlertText());
    }
}
