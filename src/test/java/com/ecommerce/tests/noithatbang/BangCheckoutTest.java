package com.ecommerce.tests.noithatbang;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithatbang.BangCheckoutPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * UI Test Suite for Website_NoiThat Checkout Flow
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangCheckoutTest extends BaseTest {

    private BangCheckoutPage checkoutPage;

    @BeforeMethod
    public void setupTest() {
        openUrl("http://localhost:4321/checkout");
        checkoutPage = new BangCheckoutPage();
    }

    @Test(description = "WNT_CHK_001: Màn hình Checkout tải thành công và hiển thị trạng thái hợp lệ")
    public void testCheckoutPageLoads() {
        Assert.assertTrue(checkoutPage.isPageLoaded(), "Trang thanh toán phải tải thành công");
    }

    @Test(description = "WNT_CHK_002: Kiểm tra trạng thái giỏ hàng trống trên trang Checkout")
    public void testEmptyCartOnCheckout() {
        // Khi chưa thêm sản phẩm vào giỏ hàng, trang hiển thị giỏ hàng trống
        Assert.assertTrue(checkoutPage.isCartEmpty(), "Phải hiển thị thông báo giỏ hàng trống khi chưa chọn sản phẩm");
    }
}
