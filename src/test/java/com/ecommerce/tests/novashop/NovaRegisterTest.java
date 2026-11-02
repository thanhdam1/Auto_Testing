package com.ecommerce.tests.novashop;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.novashop.NovaRegisterPage;
import com.ecommerce.reports.ExtentReportManager;
import com.ecommerce.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class NovaRegisterTest extends BaseTest {

    private NovaRegisterPage registerPage;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        String baseUrl = ConfigReader.get("novashop.app.url", "https://novashop-web-delta.vercel.app");
        openUrl(baseUrl + "/dang-ky");
        registerPage = new NovaRegisterPage();
    }

    @Test(groups = {"smoke", "novashop", "register"}, description = "Xác nhận trang Đăng ký ShopNova tải thành công")
    public void testRegisterPageLoadsSuccessfully() {
        ExtentReportManager.logInfo("Kiểm tra giao diện trang Đăng Ký ShopNova");
        Assert.assertTrue(registerPage.isPageLoaded(), "Trang Đăng ký ShopNova phải hiển thị tiêu đề Đăng Ký.");
        Assert.assertTrue(registerPage.isLoginLinkDisplayed(), "Đường dẫn 'Đăng nhập' phải hiển thị.");
    }

    @Test(groups = {"regression", "novashop", "register"}, description = "Kiểm tra báo lỗi khi gửi form để trống")
    public void testEmptyFieldsValidation() {
        ExtentReportManager.logInfo("Nhấn Đăng ký khi không điền thông tin");
        registerPage.clickSubmit();

        Assert.assertTrue(registerPage.isErrorMessageDisplayed(), "Thông báo lỗi phải hiển thị khi để trống thông tin.");
        String errText = registerPage.getErrorMessageText();
        ExtentReportManager.logInfo("Nội dung báo lỗi: " + errText);
        Assert.assertFalse(errText.isEmpty(), "Nội dung lỗi không được rỗng.");
    }

    @Test(groups = {"regression", "novashop", "register"}, description = "Kiểm tra báo lỗi khi mật khẩu nhập lại không khớp")
    public void testPasswordMismatchValidation() {
        ExtentReportManager.logInfo("Điền form với mật khẩu xác nhận không khớp");
        registerPage.fillRegistrationForm(
                "Nguyễn Văn Test",
                "testuser_" + System.currentTimeMillis(),
                "0988776655",
                "test_" + System.currentTimeMillis() + "@example.com",
                "Password123!",
                "MismatchPassword999!"
        );
        registerPage.clickSubmit();

        Assert.assertTrue(registerPage.isErrorMessageDisplayed(), "Thông báo lỗi phải hiển thị khi mật khẩu không khớp.");
        ExtentReportManager.logInfo("Nội dung lỗi: " + registerPage.getErrorMessageText());
    }

    @Test(groups = {"smoke", "novashop", "register"}, description = "Đăng ký tài khoản người mua (Buyer) thành công với dữ liệu động")
    public void testRegisterBuyerSuccessfully() {
        long timestamp = System.currentTimeMillis();
        String username = "buyer_" + timestamp;
        String email = "buyer_" + timestamp + "@example.com";

        ExtentReportManager.logInfo("Đăng ký tài khoản mới: " + username);
        registerPage.registerBuyer(
                "Khách Hàng Auto " + (timestamp % 1000),
                username,
                "09" + String.valueOf(timestamp).substring(4),
                email,
                "Password123!"
        );

        String currentUrl = getDriver().getCurrentUrl();
        ExtentReportManager.logInfo("URL sau khi đăng ký: " + currentUrl);
        Assert.assertFalse(registerPage.isErrorMessageDisplayed(), "Đăng ký hợp lệ không được hiển thị lỗi.");
    }

    @Test(groups = {"smoke", "novashop", "register"}, description = "Chuyển hướng từ trang Đăng ký sang trang Đăng nhập")
    public void testNavigateToLoginFromRegister() {
        ExtentReportManager.logInfo("Bấm chuyển sang trang Đăng nhập");
        registerPage.clickLoginLink();

        String currentUrl = getDriver().getCurrentUrl();
        ExtentReportManager.logInfo("URL hiện tại: " + currentUrl);
        Assert.assertTrue(currentUrl.contains("/dang-nhap"), "Phải chuyển hướng đến trang Đăng nhập (/dang-nhap)");
    }

    @Test(groups = {"smoke", "novashop", "register"}, description = "Đăng ký tài khoản người bán (Seller) thành công với dữ liệu động")
    public void testRegisterSellerSuccessfully() {
        long timestamp = System.currentTimeMillis();
        String username = "seller_" + timestamp;
        String email = "seller_" + timestamp + "@example.com";

        ExtentReportManager.logInfo("Đăng ký tài khoản người bán mới: " + username);
        registerPage.registerSeller(
                "Chủ Shop Auto " + (timestamp % 1000),
                username,
                "09" + String.valueOf(timestamp).substring(4),
                email,
                "Password123!"
        );

        String currentUrl = getDriver().getCurrentUrl();
        ExtentReportManager.logInfo("URL sau khi đăng ký người bán: " + currentUrl);
        Assert.assertFalse(registerPage.isErrorMessageDisplayed(), "Đăng ký seller hợp lệ không được hiển thị lỗi.");
    }
}

