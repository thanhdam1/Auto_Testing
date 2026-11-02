package com.ecommerce.tests.noithat;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithat.StorefrontRegisterPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class StorefrontRegisterTest extends BaseTest {

    private StorefrontRegisterPage registerPage;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        openStorefrontPath("/dang-ky");
        registerPage = new StorefrontRegisterPage();
    }

    @Test(groups = {"smoke", "storefront", "auth"}, description = "Xác nhận trang Đăng ký thành viên tải thành công")
    public void testRegisterPageLoadsSuccessfully() {
        ExtentReportManager.logInfo("Kiểm tra trang Đăng ký tải hoàn tất");
        Assert.assertTrue(registerPage.isPageLoaded(), "Trang đăng ký phải hiển thị tiêu đề và nút đăng ký.");
    }

    @Test(groups = {"negative", "regression", "storefront", "auth"}, description = "Bỏ trống các trường thông tin bắt buộc thì hiển thị thông báo lỗi")
    public void testEmptyFormValidation() {
        ExtentReportManager.logInfo("Bấm tạo tài khoản khi form để trống");
        registerPage.submitForm();
        Assert.assertTrue(registerPage.isToastDisplayed(), "Phải hiển thị toast thông báo khi để trống thông tin.");
        String msg = registerPage.getToastMessage();
        Assert.assertTrue(msg.contains("đầy đủ") || msg.contains("họ và tên"), "Nội dung toast phải báo lỗi họ tên hoặc điền đầy đủ. Thực tế: " + msg);
    }

    @Test(groups = {"negative", "regression", "storefront", "auth"}, description = "Nhập mật khẩu xác nhận không khớp thì báo lỗi")
    public void testPasswordMismatchValidation() {
        ExtentReportManager.logInfo("Nhập thông tin với mật khẩu xác nhận không khớp");
        registerPage.fillRegistrationForm("Test User", "test" + uniqueSuffix() + "@example.com", "0909123456", "Pass123456", "Pass999999");
        registerPage.submitForm();
        Assert.assertTrue(registerPage.isToastDisplayed(), "Phải hiển thị toast thông báo lỗi mật khẩu không khớp.");
        String msg = registerPage.getToastMessage();
        Assert.assertTrue(msg.contains("không khớp"), "Toast phải nêu mật khẩu không khớp. Thực tế: " + msg);
    }

    @Test(groups = {"regression", "storefront", "auth"}, description = "Thanh đo độ mạnh mật khẩu cập nhật theo độ phức tạp của mật khẩu")
    public void testPasswordStrengthIndicator() {
        ExtentReportManager.logInfo("Nhập mật khẩu đơn giản");
        registerPage.fillRegistrationForm(null, null, null, "123", null);
        Assert.assertTrue(registerPage.isPasswordStrengthDisplayed(), "Thanh đo độ mạnh mật khẩu phải hiển thị.");
        String strengthWeak = registerPage.getPasswordStrengthText();
        ExtentReportManager.logInfo("Độ mạnh mật khẩu ngắn: " + strengthWeak);
        Assert.assertTrue(strengthWeak.contains("Yếu") || strengthWeak.contains("yếu"), "Mật khẩu ngắn phải báo yếu.");
    }
}
