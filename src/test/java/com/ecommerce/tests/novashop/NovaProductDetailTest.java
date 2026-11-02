package com.ecommerce.tests.novashop;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.novashop.NovaCartPage;
import com.ecommerce.pages.novashop.NovaProductDetailPage;
import com.ecommerce.pages.novashop.NovaSearchPage;
import com.ecommerce.reports.ExtentReportManager;
import com.ecommerce.utils.WaitUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class NovaProductDetailTest extends BaseTest {

    private NovaProductDetailPage productDetailPage;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        openNovaShop();
        openUrl(com.ecommerce.utils.ConfigReader.get("novashop.app.url", "https://novashop-web-delta.vercel.app") + "/tim-kiem");
        NovaSearchPage searchPage = new NovaSearchPage();
        searchPage.clickFirstProduct();
        productDetailPage = new NovaProductDetailPage();
    }

    @Test(groups = {"smoke", "novashop", "product"}, description = "Xác nhận trang chi tiết sản phẩm hiển thị đầy đủ thông tin")
    public void testProductDetailLoadsAndDisplaysInfo() {
        ExtentReportManager.logInfo("Kiểm tra trang chi tiết sản phẩm");
        Assert.assertTrue(productDetailPage.isPageLoaded(), "Trang chi tiết sản phẩm phải tải thành công.");

        String name = productDetailPage.getProductNameText();
        ExtentReportManager.logInfo("Tên sản phẩm: " + name);
        Assert.assertFalse(name.isEmpty(), "Tên sản phẩm không được rỗng.");

        String price = productDetailPage.getCurrentPriceText();
        ExtentReportManager.logInfo("Giá bán hiện tại: " + price);
        Assert.assertFalse(price.isEmpty(), "Giá bán phải hiển thị.");
    }

    @Test(groups = {"regression", "novashop", "product"}, description = "Kiểm tra tăng giảm số lượng mua sản phẩm")
    public void testQuantityAdjustment() {
        ExtentReportManager.logInfo("Kiểm tra số lượng mặc định ban đầu");
        Assert.assertEquals(productDetailPage.getQuantityValue(), "1", "Số lượng ban đầu phải là 1.");

        ExtentReportManager.logInfo("Nhấn nút tăng số lượng (+)");
        productDetailPage.increaseQuantity();
        Assert.assertEquals(productDetailPage.getQuantityValue(), "2", "Số lượng sau khi tăng phải là 2.");

        ExtentReportManager.logInfo("Nhấn nút giảm số lượng (-)");
        productDetailPage.decreaseQuantity();
        Assert.assertEquals(productDetailPage.getQuantityValue(), "1", "Số lượng sau khi giảm phải trở về 1.");
    }

    @Test(groups = {"smoke", "novashop", "product"}, description = "Thêm sản phẩm vào giỏ hàng và kiểm tra thông báo toast")
    public void testAddToCartDisplaysToastNotification() {
        ExtentReportManager.logInfo("Nhấn 'Thêm Vào Giỏ Hàng'");
        productDetailPage.clickAddToCart();

        Assert.assertTrue(productDetailPage.isToastDisplayed(), "Thông báo Toast phải xuất hiện khi thêm vào giỏ.");
    }

    @Test(groups = {"regression", "novashop", "product"}, description = "Nhấn 'Mua Ngay' chuyển hướng trực tiếp tới trang Giỏ Hàng")
    public void testBuyNowRedirectsToCart() {
        ExtentReportManager.logInfo("Nhấn 'Mua Ngay'");
        NovaCartPage cartPage = productDetailPage.clickBuyNow();
        WaitUtil.waitForUrlContains("/gio-hang", 10);

        String currentUrl = getDriver().getCurrentUrl();
        ExtentReportManager.logInfo("URL sau khi nhấn Mua Ngay: " + currentUrl);
        Assert.assertTrue(currentUrl.contains("/gio-hang"), "Phải chuyển hướng sang URL /gio-hang.");
        Assert.assertTrue(cartPage.isPageLoaded(), "Trang Giỏ hàng phải tải thành công.");
    }

    @Test(groups = {"regression", "novashop", "product"}, description = "Chọn biến thể kích cỡ / màu sắc của sản phẩm")
    public void testSelectProductVariant() {
        ExtentReportManager.logInfo("Chọn biến thể sản phẩm đầu tiên");
        productDetailPage.selectFirstVariant();
        Assert.assertTrue(productDetailPage.isPageLoaded(), "Trang sản phẩm vẫn phản hồi tốt sau khi chọn biến thể.");
    }

    @Test(groups = {"regression", "novashop", "product"}, description = "Bật/tắt theo dõi gian hàng người bán")
    public void testToggleFollowShop() {
        ExtentReportManager.logInfo("Nhấn nút Theo Dõi Shop");
        productDetailPage.toggleFollowShop();

        Assert.assertTrue(productDetailPage.isToastDisplayed() || !productDetailPage.getFollowButtonText().isEmpty(),
                "Phải hiển thị Toast thông báo hoặc cập nhật trạng thái Theo Dõi.");
    }

    @Test(groups = {"regression", "novashop", "product"}, description = "Xem trang thông tin gian hàng người bán từ chi tiết sản phẩm")
    public void testNavigateToShopProfile() {
        ExtentReportManager.logInfo("Nhấn link 'Xem Shop'");
        productDetailPage.clickShopLink();
        WaitUtil.waitForUrlContains("/shop/", 10);

        String currentUrl = getDriver().getCurrentUrl();
        ExtentReportManager.logInfo("URL gian hàng: " + currentUrl);
        Assert.assertTrue(currentUrl.contains("/shop/"), "Phải chuyển hướng đến trang thông tin Shop.");
    }
}
