package com.ecommerce.tests.noithat;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithat.StorefrontLoginPage;
import com.ecommerce.reports.ExtentReportManager;
import com.ecommerce.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class StorefrontAuthTest extends BaseTest {

    private StorefrontLoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        String baseUrl = ConfigReader.get("app.url", "https://86h0mht3-4321.asse.devtunnels.ms");
        openUrl(baseUrl + "/dang-nhap");
        loginPage = new StorefrontLoginPage();
    }

    @Test(groups = {"smoke", "auth"}, description = "Xác nhận trang Đăng nhập tải thành công và đầy đủ thành phần giao diện")
    public void testLoginPageUIElements() {
        ExtentReportManager.logInfo("Kiểm tra giao diện trang Đăng nhập");
        Assert.assertTrue(loginPage.isPageLoaded(), "Trang Đăng nhập phải hiển thị tiêu đề hợp lệ.");
        Assert.assertTrue(loginPage.isRegisterLinkDisplayed(), "Đường dẫn 'Đăng ký ngay' phải hiển thị.");
    }

    @Test(groups = {"regression", "auth"}, description = "Xác thực báo lỗi khi để trống tài khoản và mật khẩu")
    public void testEmptyCredentialsValidation() {
        ExtentReportManager.logInfo("Nhấn Đăng nhập khi không nhập email/mật khẩu");
        loginPage.clickSubmit();

        Assert.assertTrue(loginPage.isToastDisplayed(), "Thông báo toast cảnh báo phải hiển thị.");
        String toastText = loginPage.getToastMessage();
        ExtentReportManager.logInfo("Nội dung thông báo lỗi: " + toastText);
        Assert.assertTrue(toastText.contains("đầy đủ"), "Nội dung toast phải nhắc nhở nhập đầy đủ thông tin.");
    }

    @Test(groups = {"smoke", "auth"}, description = "Kiểm tra tính năng đăng nhập nhanh 1-chạm bằng tài khoản Gmail")
    public void testGoogleFastLogin() {
        ExtentReportManager.logInfo("Nhấp nút Đăng nhập nhanh bằng Gmail");
        loginPage.clickGoogleLogin();

        Assert.assertTrue(loginPage.isToastDisplayed(), "Toast thành công phải hiển thị sau khi nhấp Google login.");
        String toastText = loginPage.getToastMessage();
        ExtentReportManager.logInfo("Nội dung thông báo: " + toastText);
        Assert.assertTrue(toastText.contains("Gmail") || toastText.contains("thành công"), 
                "Toast phải thông báo thành công.");
    }
}
