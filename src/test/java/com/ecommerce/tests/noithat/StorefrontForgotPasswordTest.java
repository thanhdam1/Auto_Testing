package com.ecommerce.tests.noithat;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithat.StorefrontForgotPasswordPage;
import com.ecommerce.reports.ExtentReportManager;
import com.ecommerce.utils.WaitUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class StorefrontForgotPasswordTest extends BaseTest {

    private StorefrontForgotPasswordPage forgotPage;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        openStorefrontPath("/quen-mat-khau");
        forgotPage = new StorefrontForgotPasswordPage();
    }

    @Test(groups = {"smoke", "storefront", "auth", "otp"}, priority = 1,
            description = "TC_FP_01: Xác nhận trang Quên mật khẩu tải thành công và hiển thị Bước 1")
    public void testForgotPasswordPageLoadsSuccessfully() {
        ExtentReportManager.logInfo("Kiểm tra trang Quên mật khẩu tải thành công");
        Assert.assertTrue(forgotPage.isPageLoaded(), "Trang Quên mật khẩu phải hiển thị tiêu đề.");
        Assert.assertTrue(forgotPage.isStep1Active(), "Bước 1 (nhập email) phải hiển thị mặc định.");
    }

    @Test(groups = {"negative", "regression", "storefront", "auth", "otp"}, priority = 2,
            description = "TC_FP_02: Bỏ trống email khi gửi yêu cầu OTP thì hiển thị thông báo lỗi")
    public void testEmptyEmailValidation() {
        ExtentReportManager.logInfo("Nhấn nút gửi OTP khi chưa nhập email");
        forgotPage.enterEmail("");
        forgotPage.clickSendOtp();

        Assert.assertTrue(forgotPage.isEmailErrorDisplayed(), "Phải hiển thị lỗi trường email.");
        String errorMsg = forgotPage.getEmailErrorMessage();
        ExtentReportManager.logInfo("Thông báo lỗi nhận được: " + errorMsg);
        Assert.assertTrue(errorMsg.contains("Vui lòng nhập địa chỉ email"),
                "Nội dung lỗi phải yêu cầu nhập email. Thực tế: " + errorMsg);
    }

    @Test(groups = {"negative", "regression", "storefront", "auth", "otp"}, priority = 3,
            description = "TC_FP_03: Nhập email sai định dạng thì hiển thị thông báo lỗi định dạng")
    public void testInvalidEmailFormatValidation() {
        ExtentReportManager.logInfo("Nhập email sai định dạng 'test-khong-hop-le'");
        forgotPage.enterEmail("test-khong-hop-le");
        forgotPage.clickSendOtp();

        Assert.assertTrue(forgotPage.isEmailErrorDisplayed(), "Phải hiển thị lỗi định dạng email.");
        String errorMsg = forgotPage.getEmailErrorMessage();
        ExtentReportManager.logInfo("Thông báo lỗi nhận được: " + errorMsg);
        Assert.assertTrue(errorMsg.contains("định dạng"),
                "Nội dung lỗi phải nhắc đến định dạng email. Thực tế: " + errorMsg);
    }

    @Test(groups = {"regression", "storefront", "auth", "otp"}, priority = 4,
            description = "TC_FP_04: Bấm liên kết 'Đăng nhập ngay' điều hướng về trang /dang-nhap")
    public void testNavigateBackToLoginPage() {
        ExtentReportManager.logInfo("Bấm liên kết 'Đăng nhập ngay'");
        forgotPage.clickBackToLogin();

        WaitUtil.waitUntil(driver -> driver.getCurrentUrl().contains("/dang-nhap"), 5);
        Assert.assertTrue(getDriver().getCurrentUrl().contains("/dang-nhap"),
                "Phải điều hướng về trang Đăng nhập.");
    }

    @Test(groups = {"smoke", "regression", "storefront", "auth", "otp"}, priority = 5,
            description = "TC_FP_05: Nhập email hợp lệ gửi mã OTP chuyển sang Bước 2 (Xác thực OTP)")
    public void testSendOtpWithValidEmailMovesToStepTwo() {
        String testEmail = "otp_user_" + System.currentTimeMillis() + "@gmail.com";
        ExtentReportManager.logInfo("Nhập email hợp lệ: " + testEmail);
        forgotPage.enterEmail(testEmail);
        forgotPage.clickSendOtp();

        WaitUtil.waitUntil(driver -> forgotPage.isStep2Active() || forgotPage.isToastDisplayed(), 10);
        Assert.assertTrue(forgotPage.isStep2Active(),
                "Sau khi gửi OTP thành công, giao diện phải chuyển sang Bước 2.");
        Assert.assertEquals(forgotPage.getTargetEmailDisplay(), testEmail,
                "Email hiển thị ở Bước 2 phải trùng khớp với email đã gửi.");
    }

    @Test(groups = {"negative", "regression", "storefront", "auth", "otp"}, priority = 6,
            description = "TC_FP_06: Bước 2 - Nhập thiếu chữ số OTP báo lỗi yêu cầu nhập đủ 6 số")
    public void testIncompleteOtpValidationOnStepTwo() {
        String testEmail = "step2_test_" + System.currentTimeMillis() + "@example.com";
        ExtentReportManager.logInfo("Gửi mã OTP đến email " + testEmail + " để vào Bước 2");
        forgotPage.enterEmail(testEmail);
        forgotPage.clickSendOtp();

        WaitUtil.waitUntil(driver -> forgotPage.isStep2Active(), 10);
        Assert.assertTrue(forgotPage.isStep2Active(), "Phải ở Bước 2.");

        ExtentReportManager.logInfo("Nhập mã OTP chỉ có 3 số '123'");
        forgotPage.enterOtp("123");
        forgotPage.clickVerifyOtp();

        Assert.assertTrue(forgotPage.isOtpErrorDisplayed(), "Phải hiển thị lỗi OTP chưa đủ 6 số.");
        String errText = forgotPage.getOtpErrorMessage();
        ExtentReportManager.logInfo("Thông điệp lỗi OTP: " + errText);
        Assert.assertTrue(errText.contains("6 chữ số"),
                "Lỗi phải nhắc nhập đủ 6 chữ số. Thực tế: " + errText);
    }
}
