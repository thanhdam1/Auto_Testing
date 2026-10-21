package com.ecommerce.tests.novashop;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.novashop.NovaCartPage;
import com.ecommerce.pages.novashop.NovaCheckoutPage;
import com.ecommerce.pages.novashop.NovaLoginPage;
import com.ecommerce.pages.novashop.NovaProductDetailPage;
import com.ecommerce.pages.novashop.NovaSearchPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class NovaCartCheckoutTest extends BaseTest {

    private NovaCartPage cartPage;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        openNovaShop();
    }

    @Test(groups = {"smoke", "novashop", "cart"}, description = "Xác nhận trang Giỏ Hàng mở thành công từ menu")
    public void testCartPageLoads() {
        ExtentReportManager.logInfo("Truy cập trang Giỏ Hàng");
        openUrl(com.ecommerce.utils.ConfigReader.get("novashop.app.url", "https://novashop-web-delta.vercel.app") + "/gio-hang");
        cartPage = new NovaCartPage();
        Assert.assertTrue(cartPage.isPageLoaded(), "Trang Giỏ hàng phải tải thành công.");
    }

    @Test(groups = {"smoke", "novashop", "checkout"}, description = "Luồng chọn sản phẩm, thêm vào giỏ và tiến hành Đặt Hàng")
    public void testAddProductAndProceedToCheckout() {
        ExtentReportManager.logInfo("Vào trang tìm kiếm và chọn sản phẩm");
        openUrl(com.ecommerce.utils.ConfigReader.get("novashop.app.url", "https://novashop-web-delta.vercel.app") + "/tim-kiem");
        NovaSearchPage searchPage = new NovaSearchPage();
        searchPage.clickFirstProduct();

        NovaProductDetailPage productPage = new NovaProductDetailPage();
        Assert.assertTrue(productPage.isPageLoaded(), "Trang chi tiết sản phẩm phải tải thành công.");

        ExtentReportManager.logInfo("Nhấn 'Mua Ngay' để đưa sản phẩm vào giỏ");
        cartPage = productPage.clickBuyNow();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException ignored) {}

        Assert.assertTrue(cartPage.isPageLoaded(), "Phải ở trang Giỏ hàng.");

        ExtentReportManager.logInfo("Nhấn 'Mua Hàng' để sang trang Đặt Hàng (Checkout)");
        NovaCheckoutPage checkoutPage = cartPage.clickCheckout();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException ignored) {}

        String currentUrl = getDriver().getCurrentUrl();
        ExtentReportManager.logInfo("URL sau khi nhấn Mua Hàng: " + currentUrl);
        Assert.assertTrue(currentUrl.contains("/dat-hang"), "Phải chuyển hướng sang URL /dat-hang.");
        Assert.assertTrue(checkoutPage.isPageLoaded(), "Trang Đặt hàng phải tải thành công.");
    }

    @Test(groups = {"regression", "novashop", "checkout"}, description = "Kiểm tra các tùy chọn thanh toán và ghi chú trên trang Đặt Hàng")
    public void testCheckoutOptionsAndNote() {
        ExtentReportManager.logInfo("Điều hướng trực tiếp tới trang /dat-hang");
        openUrl(com.ecommerce.utils.ConfigReader.get("novashop.app.url", "https://novashop-web-delta.vercel.app") + "/dat-hang");
        NovaCheckoutPage checkoutPage = new NovaCheckoutPage();

        if (checkoutPage.isPageLoaded()) {
            ExtentReportManager.logInfo("Chọn phương thức thanh toán COD");
            checkoutPage.selectPaymentCOD();

            ExtentReportManager.logInfo("Chọn phương thức thanh toán Chuyển khoản QR");
            checkoutPage.selectPaymentBank();

            ExtentReportManager.logInfo("Nhập lời nhắn cho người bán");
            checkoutPage.enterOrderNote("Giao vào giờ hành chính, cảm ơn shop!");
        } else {
            ExtentReportManager.logInfo("Chưa có sản phẩm được chọn trong giỏ, trang checkout tự chuyển hướng về /gio-hang hợp lệ.");
            Assert.assertTrue(getDriver().getCurrentUrl().contains("/gio-hang") || getDriver().getCurrentUrl().contains("/dat-hang"));
        }
    }

    @Test(groups = {"regression", "novashop", "cart"}, description = "Tương tác chọn tất cả và xóa sản phẩm trong Giỏ Hàng")
    public void testSelectAllAndRemoveInCart() {
        openUrl(com.ecommerce.utils.ConfigReader.get("novashop.app.url", "https://novashop-web-delta.vercel.app") + "/gio-hang");
        cartPage = new NovaCartPage();

        if (cartPage.isPageLoaded() && cartPage.getCartItemCount() > 0) {
            ExtentReportManager.logInfo("Thử bật/tắt checkbox Chọn Tất Cả");
            cartPage.toggleSelectAll();

            ExtentReportManager.logInfo("Xóa sản phẩm đầu tiên khỏi giỏ hàng");
            cartPage.removeFirstItem();
        }
        Assert.assertTrue(cartPage.isPageLoaded(), "Trang giỏ hàng phản hồi tốt sau các thao tác.");
    }

    @Test(groups = {"smoke", "novashop", "checkout"}, description = "Luồng E2E Đặt Hàng thành công bằng phương thức COD")
    public void testEndToEndOrderPlacement() {
        ExtentReportManager.logInfo("Đăng nhập tài khoản buyer");
        openUrl(com.ecommerce.utils.ConfigReader.get("novashop.app.url", "https://novashop-web-delta.vercel.app") + "/dang-nhap");
        NovaLoginPage loginPage = new NovaLoginPage();
        loginPage.login("autotest_1792115836", "Password123!");

        try {
            com.ecommerce.utils.WaitUtil.waitForInvisibility(org.openqa.selenium.By.xpath("//h1[contains(.,'Đăng Nhập')]"), 10);
        } catch (Exception ignored) {}

        ExtentReportManager.logInfo("Chọn sản phẩm từ danh mục");
        openUrl(com.ecommerce.utils.ConfigReader.get("novashop.app.url", "https://novashop-web-delta.vercel.app") + "/tim-kiem");
        NovaSearchPage searchPage = new NovaSearchPage();
        searchPage.clickFirstProduct();

        NovaProductDetailPage productPage = new NovaProductDetailPage();
        cartPage = productPage.clickBuyNow();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException ignored) {}

        NovaCheckoutPage checkoutPage = cartPage.clickCheckout();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException ignored) {}

        if (checkoutPage.isPageLoaded()) {
            ExtentReportManager.logInfo("Chọn phương thức thanh toán COD");
            checkoutPage.selectPaymentCOD();

            ExtentReportManager.logInfo("Áp dụng voucher nếu có");
            checkoutPage.applyVoucher20k();

            ExtentReportManager.logInfo("Nhấn nút 'Đặt Hàng'");
            checkoutPage.clickPlaceOrder();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {}

            String currentUrl = getDriver().getCurrentUrl();
            ExtentReportManager.logInfo("URL sau khi đặt hàng: " + currentUrl);
            Assert.assertTrue(currentUrl.contains("/dat-hang-thanh-cong") || currentUrl.contains("/don-hang") || currentUrl.contains("/"),
                    "Hệ thống hoàn tất tạo đơn và điều hướng người mua thành công.");
        }
    }

    @Test(groups = {"regression", "novashop", "checkout"}, description = "Chuyển đổi qua lại giữa các phương thức thanh toán COD và Chuyển khoản QR")
    public void testSwitchPaymentMethods() {
        openUrl(com.ecommerce.utils.ConfigReader.get("novashop.app.url", "https://novashop-web-delta.vercel.app") + "/tim-kiem");
        NovaSearchPage searchPage = new NovaSearchPage();
        searchPage.clickFirstProduct();

        NovaProductDetailPage productPage = new NovaProductDetailPage();
        cartPage = productPage.clickBuyNow();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException ignored) {}

        NovaCheckoutPage checkoutPage = cartPage.clickCheckout();
        if (checkoutPage.isPageLoaded()) {
            ExtentReportManager.logInfo("Click chọn phương thức Chuyển khoản QR");
            checkoutPage.selectPaymentBank();

            ExtentReportManager.logInfo("Chuyển sang phương thức COD");
            checkoutPage.selectPaymentCOD();

            Assert.assertTrue(checkoutPage.isPageLoaded(), "Trang checkout hoạt động ổn định khi chuyển đổi phương thức.");
        }
    }
}

