package com.ecommerce.tests.noithatbang;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithatbang.BangStorefrontPage;
import com.ecommerce.reports.ExtentReportManager;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Data-Driven Test Class for Auth Modal on Port 4321 Storefront (Astro)
 * Tests Login, Register, Boundary Conditions, Password Masking & Reset
 */
public class BangStorefrontAuthDataTest extends BaseTest {

    private BangStorefrontPage storefrontPage;

    @BeforeMethod
    public void setUpAuthTest() {
        openNoiThatBangApp();
        storefrontPage = new BangStorefrontPage();
        storefrontPage.openAuthModal();
        Assert.assertTrue(storefrontPage.isAuthModalOpen(), "Auth Modal phải hiển thị sau khi click nút tài khoản");
    }

    @Test(dataProvider = "loginNegativeData", dataProviderClass = BangStorefrontTestData.class,
          description = "Kiểm tra Đăng nhập với các bộ dữ liệu không hợp lệ (Negative Test Data)")
    public void testLoginNegativeData(String email, String password, String scenario) {
        ExtentReportManager.logInfo("Thực hiện kịch bản kiểm thử: " + scenario);
        storefrontPage.switchAuthTab("login");
        storefrontPage.fillLoginForm(email, password);
        storefrontPage.submitLogin();

        boolean toastAppeared = storefrontPage.isToastDisplayed();
        ExtentReportManager.logInfo("Toast hiển thị: " + toastAppeared + " - Nội dung: " + storefrontPage.getToastText());
        Assert.assertTrue(storefrontPage.isAuthModalOpen(), "Modal đăng nhập vẫn mở khi dữ liệu không hợp lệ: " + scenario);
    }

    @Test(dataProvider = "registerPasswordBoundaryData", dataProviderClass = BangStorefrontTestData.class,
          description = "Kiểm tra Boundary Value Analysis cho độ dài mật khẩu đăng ký (Min 6 ký tự)")
    public void testRegisterPasswordBoundary(String password, boolean isValidBoundary, String scenario) {
        ExtentReportManager.logInfo("Kiểm tra Boundary độ dài mật khẩu: " + scenario);
        storefrontPage.switchAuthTab("register");

        String testEmail = BangStorefrontTestData.generateUniqueEmail("auto_boundary");
        storefrontPage.fillRegisterForm("Khách Hàng Boundary", testEmail, "0931883988", password);
        storefrontPage.submitRegister();

        if (!isValidBoundary) {
            boolean toastShown = storefrontPage.isToastDisplayed();
            String toastText = storefrontPage.getToastText();
            ExtentReportManager.logInfo("Boundary < 6: Toast: " + toastText);
            Assert.assertTrue(storefrontPage.isAuthModalOpen(), "Modal đăng ký phải giữ nguyên khi mật khẩu < 6 ký tự");
        } else {
            ExtentReportManager.logInfo("Boundary = 6: Gửi yêu cầu đăng ký hợp lệ");
        }
    }

    @Test(description = "Kiểm tra Đăng ký tài khoản mới thành công với dữ liệu động Unique & Traceable")
    public void testRegisterValidUniqueUser() {
        storefrontPage.switchAuthTab("register");

        String uniqueEmail = BangStorefrontTestData.generateUniqueEmail("auto_reg_valid");
        String uniqueName = "Test User " + System.currentTimeMillis();
        ExtentReportManager.logInfo("Đăng ký tài khoản với email unique: " + uniqueEmail);

        storefrontPage.fillRegisterForm(uniqueName, uniqueEmail, "0931883988", "MatKhau@123456");
        storefrontPage.submitRegister();

        boolean toastShown = storefrontPage.isToastDisplayed();
        ExtentReportManager.logInfo("Kết quả đăng ký - Toast hiển thị: " + toastShown + " - " + storefrontPage.getToastText());
    }

    @Test(description = "Kiểm tra nút ẩn/hiển thị mật khẩu (Password Masking Toggle)")
    public void testPasswordVisibilityToggle() {
        storefrontPage.switchAuthTab("login");
        storefrontPage.fillLoginForm("test@example.com", "SecretPass123");

        By passInput = By.xpath("//form//input[@placeholder='Nhập mật khẩu của bạn']");
        String initialType = getDriver().findElement(passInput).getAttribute("type");
        Assert.assertEquals(initialType, "password", "Ban đầu mật khẩu phải ở dạng ẩn (type=password)");

        storefrontPage.togglePasswordVisibility();
        String toggledType = getDriver().findElement(passInput).getAttribute("type");
        Assert.assertEquals(toggledType, "text", "Sau khi bấm icon mắt, mật khẩu phải hiển thị (type=text)");

        storefrontPage.togglePasswordVisibility();
        String restoredType = getDriver().findElement(passInput).getAttribute("type");
        Assert.assertEquals(restoredType, "password", "Bấm icon mắt lần 2 phải ẩn mật khẩu trở lại (type=password)");
    }

    @Test(description = "Kiểm tra tính năng Quên mật khẩu")
    public void testForgotPasswordPrompt() {
        storefrontPage.switchAuthTab("login");
        storefrontPage.clickForgotPassword();

        boolean toastShown = storefrontPage.isToastDisplayed();
        String toastText = storefrontPage.getToastText();
        ExtentReportManager.logInfo("Quên mật khẩu - Toast hiển thị: " + toastShown + " - " + toastText);
        Assert.assertTrue(toastShown || toastText.contains("0931.883.988") || toastText.contains("hotline"),
                "Hệ thống phải hiển thị hướng dẫn hotline cấp lại mật khẩu");
    }
}
