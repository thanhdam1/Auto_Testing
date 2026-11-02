package com.ecommerce.tests.noithat;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithat.StorefrontAccountPage;
import com.ecommerce.reports.ExtentReportManager;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class StorefrontAccountTest extends BaseTest {

    private StorefrontAccountPage accountPage;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        openStorefrontPath("/san-pham");
        // Giả lập phiên đăng nhập của người dùng khách hàng
        ((JavascriptExecutor) getDriver()).executeScript(
            "window.localStorage.setItem('auth_token', 'test_user_jwt_token_12345');" +
            "window.localStorage.setItem('current_user', JSON.stringify({" +
            "  id: 101, email: 'khachhang.test@noithat.vn', name: 'Nguyễn Khách Hàng', role: 'customer', tier: 'Hội Viên VIP'" +
            "}));"
        );
        openStorefrontPath("/tai-khoan");
        accountPage = new StorefrontAccountPage();
    }

    @Test(groups = {"smoke", "storefront", "account"}, description = "Trang tài khoản hiển thị đúng thông tin người dùng từ phiên đăng nhập")
    public void testAccountPageLoadsWithUserSession() {
        ExtentReportManager.logInfo("Kiểm tra trang tài khoản cá nhân");
        Assert.assertTrue(accountPage.isPageLoaded(), "Trang tài khoản phải tải thành công.");
        Assert.assertFalse(accountPage.getUserName().isBlank(), "Tên người dùng không được rỗng.");
        Assert.assertFalse(accountPage.getUserEmail().isBlank(), "Email người dùng không được rỗng.");
    }

    @Test(groups = {"regression", "storefront", "account"}, description = "Chuyển đổi giữa các tab thông tin cá nhân và đơn hàng")
    public void testAccountTabsNavigation() {
        ExtentReportManager.logInfo("Mở tab Đơn hàng của tôi");
        accountPage.selectOrdersTab();
        Assert.assertTrue(accountPage.isOrdersPaneDisplayed(), "Khối đơn hàng phải hiển thị khi chọn tab Đơn hàng.");

        ExtentReportManager.logInfo("Quay lại tab Thông tin cá nhân");
        accountPage.selectProfileTab();
        Assert.assertTrue(accountPage.isProfilePaneDisplayed(), "Khối thông tin cá nhân phải hiển thị khi chọn tab Profile.");
    }
}
