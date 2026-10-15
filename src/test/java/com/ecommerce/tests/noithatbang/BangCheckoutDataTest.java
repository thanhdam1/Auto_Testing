package com.ecommerce.tests.noithatbang;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithatbang.BangCheckoutPage;
import com.ecommerce.pages.noithatbang.BangStorefrontPage;
import com.ecommerce.pages.noithatbang.BangStorefrontProductDetailPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Data-Driven Test Class for Checkout View on Port 4321 Storefront (Astro)
 * Tests Coupon codes, Form Validation, and End-to-End Order Placement
 */
public class BangCheckoutDataTest extends BaseTest {

    private BangStorefrontPage storefrontPage;
    private BangCheckoutPage checkoutPage;

    @BeforeMethod
    public void setUp() {
        openNoiThatBangApp();
        storefrontPage = new BangStorefrontPage();
    }

    @Test(description = "Kiểm tra trạng thái Giỏ hàng trống trên trang thanh toán")
    public void testEmptyCartOnCheckout() {
        openUrl("http://localhost:4321/checkout");
        checkoutPage = new BangCheckoutPage();
        Assert.assertTrue(checkoutPage.isPageLoaded(), "Trang checkout phải tải thành công");
        Assert.assertTrue(checkoutPage.isCartEmpty(), "Giỏ hàng phải hiển thị trạng thái trống khi chưa chọn sản phẩm");
    }

    @Test(dataProvider = "couponValidationData", dataProviderClass = BangStorefrontTestData.class,
          description = "Kiểm tra Nhập mã giảm giá với nhiều kiểu dữ liệu (Negative Coupons)")
    public void testCouponCodeNegativeData(String couponCode, String expectedErrorSubstr) {
        BangStorefrontProductDetailPage detailPage = storefrontPage.clickFirstProductCard();
        detailPage.clickAddToCart();

        openUrl("http://localhost:4321/checkout");
        checkoutPage = new BangCheckoutPage();
        Assert.assertTrue(checkoutPage.isPageLoaded(), "Trang checkout phải tải thành công");

        ExtentReportManager.logInfo("Thử áp dụng mã giảm giá không hợp lệ: " + couponCode);
        checkoutPage.applyCoupon(couponCode);

        boolean hasError = checkoutPage.isCouponErrorDisplayed();
        String errorText = checkoutPage.getCouponErrorText();
        ExtentReportManager.logInfo("Kết quả kiểm tra mã - Hiển thị lỗi: " + hasError + " - " + errorText);
        Assert.assertTrue(hasError, "Hệ thống phải hiển thị cảnh báo khi mã giảm giá không hợp lệ: " + couponCode);
    }

    @Test(description = "Kiểm tra Kiểm định Form Thanh toán (Validation) khi thiếu thông tin bắt buộc")
    public void testCheckoutFormMissingRequiredFields() {
        BangStorefrontProductDetailPage detailPage = storefrontPage.clickFirstProductCard();
        detailPage.clickAddToCart();

        openUrl("http://localhost:4321/checkout");
        checkoutPage = new BangCheckoutPage();
        Assert.assertTrue(checkoutPage.isPageLoaded(), "Trang checkout phải tải thành công");

        checkoutPage.submitOrder();
        ExtentReportManager.logInfo("Đã kiểm tra validation form checkout khi thiếu thông tin");
        Assert.assertFalse(checkoutPage.isOrderSuccessDisplayed(), "Không thể đặt hàng thành công khi chưa điền thông tin");
    }

    @Test(description = "Kiểm tra Đặt hàng thành công trọn gói End-to-End (COD) với Test Data Traceable")
    public void testCompleteCheckoutOrderWithCOD() {
        BangStorefrontProductDetailPage detailPage = storefrontPage.clickFirstProductCard();
        detailPage.clickAddToCart();

        openUrl("http://localhost:4321/checkout");
        checkoutPage = new BangCheckoutPage();
        Assert.assertTrue(checkoutPage.isPageLoaded(), "Trang checkout phải tải thành công");

        String uniqueEmail = BangStorefrontTestData.generateUniqueEmail("auto_order_cod");
        String customerName = "Khách Hàng Auto " + System.currentTimeMillis();
        String phone = "0931883988";
        String street = "615 Âu Cơ, Phường Phú Trung";
        String note = "Giao hàng giờ hành chính [E2E Test]";

        ExtentReportManager.logInfo("Điền thông tin đặt hàng: " + customerName + " | " + uniqueEmail);
        checkoutPage.fillRecipientInfo(customerName, phone, uniqueEmail, "MatKhau@123456");
        checkoutPage.fillAddress("Thành phố Hồ Chí Minh", "Quận Tân Phú", "Phường Phú Trung", street, note);

        checkoutPage.selectPaymentCOD();
        checkoutPage.submitOrder();

        Assert.assertTrue(checkoutPage.isOrderSuccessDisplayed(), "Màn hình ĐẶT HÀNG THÀNH CÔNG phải hiển thị");
        String orderCode = checkoutPage.getSuccessOrderCode();
        ExtentReportManager.logInfo("Mã đơn hàng được tạo thành công: " + orderCode);
        Assert.assertNotNull(orderCode, "Mã đơn hàng không được null");
        Assert.assertFalse(orderCode.trim().isEmpty(), "Mã đơn hàng không được rỗng");
    }
}
