package com.ecommerce.tests.novashop;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.novashop.NovaHomePage;
import com.ecommerce.pages.novashop.NovaLoginPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class NovaAuthTest extends BaseTest {

    private NovaLoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        openNovaShop();
        openUrl(getCurrentLoginUrl());
        loginPage = new NovaLoginPage();
    }

    private String getCurrentLoginUrl() {
        return com.ecommerce.utils.ConfigReader.get("novashop.app.url", "http://localhost:5173") + "/dang-nhap";
    }

    @Test(groups = {"smoke", "novashop", "auth"}, description = "Xác nhận trang Đăng nhập ShopNova tải thành công")
    public void testLoginPageLoadsSuccessfully() {
        ExtentReportManager.logInfo("Kiểm tra giao diện trang Đăng nhập ShopNova");
        Assert.assertTrue(loginPage.isPageLoaded(), "Trang Đăng nhập ShopNova phải hiển thị tiêu đề hợp lệ.");
        Assert.assertTrue(loginPage.isRegisterLinkDisplayed(), "Đường dẫn 'Đăng ký tài khoản mới' phải hiển thị.");
    }

    @Test(groups = {"regression", "novashop", "auth"}, description = "Kiểm tra báo lỗi khi gửi form để trống")
    public void testEmptyCredentialsValidation() {
        ExtentReportManager.logInfo("Nhấn Đăng nhập khi không điền thông tin");
        loginPage.clickSubmit();

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Thông báo lỗi phải hiển thị khi để trống tài khoản.");
        String errText = loginPage.getErrorMessageText();
        ExtentReportManager.logInfo("Nội dung báo lỗi: " + errText);
        Assert.assertTrue(errText.contains("đầy đủ"), "Nội dung lỗi phải yêu cầu nhập đầy đủ thông tin.");
    }

    @Test(groups = {"regression", "novashop", "auth"}, description = "Kiểm tra đăng nhập thất bại với mật khẩu sai")
    public void testInvalidCredentialsError() {
        ExtentReportManager.logInfo("Đăng nhập với mật khẩu không đúng");
        loginPage.login("minhanh", "wrong_password_999");

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Thông báo lỗi phải hiển thị khi nhập sai mật khẩu.");
    }

    @Test(groups = {"smoke", "novashop", "auth"}, description = "Kiểm tra đăng nhập thành công với tài khoản người mua (Buyer)")
    public void testBuyerLoginSuccess() {
        ExtentReportManager.logInfo("Đăng nhập với tài khoản Buyer: minhanh / 123456");
        loginPage.login("minhanh", "123456");

        // F-02: trước đây test này KHÔNG có assertion nào (luôn PASS dù đăng nhập hỏng)
        Assert.assertFalse(loginPage.isErrorMessageDisplayed(),
                "Đăng nhập buyer hợp lệ không được hiển thị thông báo lỗi.");
        String currentUrl = getDriver().getCurrentUrl();
        ExtentReportManager.logInfo("URL sau khi đăng nhập buyer: " + currentUrl);
        Assert.assertFalse(currentUrl.contains("/dang-nhap"),
                "Đăng nhập thành công phải rời khỏi trang đăng nhập. URL thực tế: " + currentUrl);
    }

    @Test(groups = {"smoke", "novashop", "auth"}, description = "Kiểm tra đăng nhập tài khoản người bán chuyển hướng đến kênh người bán")
    public void testSellerLoginRedirect() {
        ExtentReportManager.logInfo("Đăng nhập với tài khoản Seller: ankerseller / 123456");
        loginPage.login("ankerseller", "123456");

        // F-02: trước đây test này KHÔNG có assertion nào
        Assert.assertFalse(loginPage.isErrorMessageDisplayed(),
                "Đăng nhập seller hợp lệ không được hiển thị thông báo lỗi.");
        String currentUrl = getDriver().getCurrentUrl();
        ExtentReportManager.logInfo("URL sau khi đăng nhập seller: " + currentUrl);
        Assert.assertFalse(currentUrl.contains("/dang-nhap"),
                "Đăng nhập seller thành công phải rời khỏi trang đăng nhập. URL thực tế: " + currentUrl);
    }
}
