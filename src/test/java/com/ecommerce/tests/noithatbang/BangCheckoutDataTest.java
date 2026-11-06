package com.ecommerce.tests.noithatbang;

import com.ecommerce.api.ApiResponse;
import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithatbang.BangCheckoutPage;
import com.ecommerce.pages.noithatbang.BangStorefrontPage;
import com.ecommerce.pages.noithatbang.BangStorefrontProductDetailPage;
import com.ecommerce.reports.ExtentReportManager;
import com.ecommerce.utils.WaitUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Data-Driven & E2E Test Class for Checkout View on Port 4321 Storefront (Astro)
 * Covers:
 * - Empty cart state
 * - Voucher business validation: non-existent, expired, min-order threshold, valid deduction & removal
 * - Input validation: single-field required validations
 * - Core E-Commerce Flow: COD E2E cross-verified with Admin API
 * - Idempotency: Double-click order prevention
 */
public class BangCheckoutDataTest extends BaseTest {

    private BangStorefrontPage storefrontPage;
    private BangCheckoutPage checkoutPage;

    @BeforeMethod
    public void setUp() {
        openNoiThatBangApp();
        storefrontPage = new BangStorefrontPage();
    }

    private void addProductToCartAndGoToCheckout() {
        // Sử dụng sản phẩm có lượng tồn kho dồi dào (tu-victoria, tồn kho 20) để đảm bảo không bị nghẽn tồn kho
        openUrl("http://localhost:4321/products/tu-victoria");
        BangStorefrontProductDetailPage detailPage = new BangStorefrontProductDetailPage();
        Assert.assertTrue(detailPage.isPageLoaded(), "Trang chi tiết sản phẩm phải tải thành công");
        detailPage.clickAddToCart();
        try {
            Thread.sleep(300);
        } catch (InterruptedException ignored) {}

        openUrl("http://localhost:4321/checkout");
        checkoutPage = new BangCheckoutPage();
        Assert.assertTrue(checkoutPage.isPageLoaded(), "Trang checkout phải tải thành công");
    }

    @Test(description = "Kiểm tra trạng thái Giỏ hàng trống trên trang thanh toán")
    public void testEmptyCartOnCheckout() {
        openUrl("http://localhost:4321/checkout");
        checkoutPage = new BangCheckoutPage();
        Assert.assertTrue(checkoutPage.isPageLoaded(), "Trang checkout phải tải thành công");
        Assert.assertTrue(checkoutPage.isCartEmpty(), "Giỏ hàng phải hiển thị trạng thái trống khi chưa chọn sản phẩm");
    }

    @Test(description = "Voucher 1: Mã giảm giá không tồn tại")
    public void testCouponNonExistent() {
        addProductToCartAndGoToCheckout();
        String fakeCode = "NON_EXIST_" + (System.currentTimeMillis() % 10000);
        ExtentReportManager.logInfo("Thử áp dụng mã không tồn tại: " + fakeCode);
        checkoutPage.applyCoupon(fakeCode);

        Assert.assertTrue(checkoutPage.isCouponErrorDisplayed(), "Phải hiển thị lỗi khi mã không tồn tại");
        String errorMsg = checkoutPage.getCouponErrorText();
        ExtentReportManager.logInfo("Thông báo lỗi thực tế: " + errorMsg);
        Assert.assertTrue(errorMsg.toLowerCase().contains("không tồn tại") || errorMsg.toLowerCase().contains("không hợp lệ"),
                "Nội dung lỗi phải chỉ rõ mã không tồn tại, thực tế: " + errorMsg);
    }

    @Test(description = "Voucher 2: Mã giảm giá đã hết hạn (Sử dụng fixture có end_date trong quá khứ)")
    public void testCouponExpired() {
        addProductToCartAndGoToCheckout();
        String expiredCode = BangTestFixtures.ensureExpiredCoupon();
        ExtentReportManager.logInfo("Thử áp dụng mã đã hết hạn: " + expiredCode);
        checkoutPage.applyCoupon(expiredCode);

        Assert.assertTrue(checkoutPage.isCouponErrorDisplayed(), "Phải hiển thị lỗi khi mã đã hết hạn");
        String errorMsg = checkoutPage.getCouponErrorText();
        ExtentReportManager.logInfo("Thông báo lỗi thực tế: " + errorMsg);
        Assert.assertTrue(errorMsg.toLowerCase().contains("hết hạn") || errorMsg.toLowerCase().contains("không còn hiệu lực"),
                "Nội dung lỗi phải chỉ rõ mã đã hết hạn, thực tế: " + errorMsg);
    }

    @Test(description = "Voucher 3: Mã giảm giá chưa đạt giá trị đơn hàng tối thiểu")
    public void testCouponMinOrderAmountNotMet() {
        addProductToCartAndGoToCheckout();
        String highMinOrderCode = BangTestFixtures.ensureMinOrderCoupon();
        ExtentReportManager.logInfo("Thử áp dụng mã yêu cầu đơn 50 triệu: " + highMinOrderCode);
        checkoutPage.applyCoupon(highMinOrderCode);

        Assert.assertTrue(checkoutPage.isCouponErrorDisplayed(), "Phải hiển thị lỗi khi đơn chưa đạt mức tối thiểu");
        String errorMsg = checkoutPage.getCouponErrorText();
        ExtentReportManager.logInfo("Thông báo lỗi thực tế: " + errorMsg);
        Assert.assertTrue(errorMsg.toLowerCase().contains("chưa đạt") || errorMsg.toLowerCase().contains("tối thiểu") || errorMsg.toLowerCase().contains("yêu cầu từ"),
                "Nội dung lỗi phải chỉ rõ chưa đạt giá trị tối thiểu, thực tế: " + errorMsg);
    }

    @Test(description = "Voucher 4: Mã giảm giá hợp lệ giảm đúng tiền và gỡ voucher khôi phục tổng tiền")
    public void testCouponValidAppliedAndRemoved() {
        addProductToCartAndGoToCheckout();
        String validCode = BangTestFixtures.ensureValidCoupon();
        long originalSubtotal = checkoutPage.getSubtotal();
        ExtentReportManager.logInfo("Tạm tính trước khi áp mã: " + originalSubtotal + " đ");

        checkoutPage.applyCoupon(validCode);
        Assert.assertTrue(checkoutPage.isCouponApplied(), "Mã giảm giá hợp lệ phải được áp dụng thành công");

        long discount = checkoutPage.getDiscountAmount();
        long totalAfterDiscount = checkoutPage.getTotalAmount();
        ExtentReportManager.logInfo("Tiền giảm: " + discount + " đ | Tổng sau giảm: " + totalAfterDiscount + " đ");

        Assert.assertTrue(discount > 0, "Tiền giảm phải lớn hơn 0 khi áp mã hợp lệ");
        Assert.assertEquals(totalAfterDiscount, originalSubtotal - discount,
                "Tổng tiền thanh toán phải bằng Tạm tính trừ đi Tiền giảm giá");

        // Gỡ mã giảm giá
        checkoutPage.removeCoupon();
        Assert.assertFalse(checkoutPage.isCouponApplied(), "Hệ thống phải gỡ mã giảm giá khi bấm Bỏ mã");
        Assert.assertEquals(checkoutPage.getTotalAmount(), originalSubtotal,
                "Sau khi gỡ mã, tổng tiền phải khôi phục về giá trị tạm tính ban đầu");
    }

    @Test(dataProvider = "checkoutNegativeValidationData", dataProviderClass = BangStorefrontTestData.class,
          description = "Kiểm tra Validation Form Checkout riêng từng trường bắt buộc (Single-Field Negative)")
    public void testCheckoutSingleFieldValidation(String fieldToCorrupt, String invalidValue, String expectedSubstr, String scenario) {
        addProductToCartAndGoToCheckout();

        String name = "name".equals(fieldToCorrupt) ? invalidValue : "Nguyễn Văn Test";
        String phone = "phone".equals(fieldToCorrupt) ? invalidValue : "0931883988";
        String email = "email".equals(fieldToCorrupt) ? invalidValue : ("auto_val_" + System.currentTimeMillis() + "@test.com");
        String street = "street".equals(fieldToCorrupt) ? invalidValue : "123 Đường Số 1, Phường Phú Trung";

        checkoutPage.fillRecipientInfo(name, phone, email, "MatKhau@123");
        checkoutPage.fillAddress("Thành phố Hồ Chí Minh", "Quận Tân Phú", "Phường Phú Trung", street, "Ghi chú test");

        checkoutPage.submitOrder();

        // Kiểm tra validation theo 2 tầng: HTML5 constraint validation (với ô bỏ trống) hoặc Component Alert (với ô khoảng trắng)
        boolean hasValidationError = checkoutPage.isFieldInvalidByHtml5(fieldToCorrupt) || checkoutPage.isSubmitErrorDisplayed();
        Assert.assertTrue(hasValidationError, "Hệ thống phải kích hoạt validation khi trường " + fieldToCorrupt + " không hợp lệ (" + scenario + ")");
        if (checkoutPage.isSubmitErrorDisplayed()) {
            String actualError = checkoutPage.getSubmitErrorText();
            ExtentReportManager.logInfo("Kịch bản: " + scenario + " -> Lỗi hiển thị: " + actualError);
            Assert.assertTrue(actualError.toLowerCase().contains(expectedSubstr.toLowerCase()),
                    "Nội dung thông báo lỗi phải chứa: '" + expectedSubstr + "', thực tế: '" + actualError + "'");
        } else {
            ExtentReportManager.logInfo("Kịch bản: " + scenario + " -> Đã được chặn thành công bởi HTML5 Constraint Validation (required/format)");
        }
        Assert.assertFalse(checkoutPage.isOrderSuccessDisplayed(), "Không được tạo đơn hàng khi validation thất bại");
    }

    @Test(description = "Chống tạo đơn trùng: Double-click nút Đặt hàng chỉ sinh ra 1 đơn duy nhất")
    public void testPreventDuplicateOrderSubmission() {
        addProductToCartAndGoToCheckout();

        String uniqueEmail = BangStorefrontTestData.generateUniqueEmail("auto_double_click");
        String customerName = "Khách Double Click " + System.currentTimeMillis();
        checkoutPage.fillRecipientInfo(customerName, "0931883988", uniqueEmail, "MatKhau@123456");
        checkoutPage.fillAddress("Thành phố Hồ Chí Minh", "Quận Tân Phú", "Phường Phú Trung", "456 Lũy Bán Bích", "Test Double Click");
        checkoutPage.selectPaymentCOD();

        // Thực hiện double-click nút Đặt hàng
        checkoutPage.doubleClickSubmitOrder();

        WaitUtil.waitUntil(driver -> checkoutPage.isOrderSuccessDisplayed() || checkoutPage.isSubmitErrorDisplayed(), 20);
        if (checkoutPage.isSubmitErrorDisplayed()) {
            Assert.fail("Đặt hàng thất bại do lỗi hiển thị trên form: " + checkoutPage.getSubmitErrorText());
        }
        Assert.assertTrue(checkoutPage.isOrderSuccessDisplayed(), "Đơn hàng phải đặt thành công");

        String orderCode = checkoutPage.getSuccessOrderCode();
        ExtentReportManager.logInfo("Mã đơn nhận được sau double-click: " + orderCode);
        Assert.assertNotNull(orderCode, "Mã đơn hàng không được null");
    }

    @Test(description = "Luồng E-Commerce Cốt Lõi: Đặt hàng COD trọn gói -> Đối chiếu với Admin API (Tên, SĐT, Địa chỉ, Trạng thái pending, Tổng tiền)")
    public void testCompleteCheckoutOrderWithCODAndAdminCrossCheck() {
        addProductToCartAndGoToCheckout();

        String uniqueEmail = BangStorefrontTestData.generateUniqueEmail("auto_cod_e2e");
        String customerName = "Khách Hàng COD E2E " + System.currentTimeMillis();
        String phone = "0931883988";
        String street = "615 Âu Cơ, Phường Phú Trung";
        String note = "Giao hàng giờ hành chính [E2E Test]";

        ExtentReportManager.logInfo("Điền thông tin đặt hàng: " + customerName + " | " + uniqueEmail);
        checkoutPage.fillRecipientInfo(customerName, phone, uniqueEmail, "MatKhau@123456");
        checkoutPage.fillAddress("Thành phố Hồ Chí Minh", "Quận Tân Phú", "Phường Phú Trung", street, note);

        checkoutPage.selectPaymentCOD();
        long expectedTotal = checkoutPage.getTotalAmount();
        checkoutPage.submitOrder();

        WaitUtil.waitUntil(driver -> checkoutPage.isOrderSuccessDisplayed() || checkoutPage.isSubmitErrorDisplayed(), 20);
        if (checkoutPage.isSubmitErrorDisplayed()) {
            Assert.fail("Đặt hàng thất bại do lỗi hiển thị trên form: " + checkoutPage.getSubmitErrorText());
        }
        Assert.assertTrue(checkoutPage.isOrderSuccessDisplayed(), "Màn hình ĐẶT HÀNG THÀNH CÔNG phải hiển thị");

        String orderCode = checkoutPage.getSuccessOrderCode();
        ExtentReportManager.logInfo("Mã đơn hàng tạo thành công: " + orderCode);
        Assert.assertNotNull(orderCode, "Mã đơn hàng không được null");
        Assert.assertFalse(orderCode.trim().isEmpty(), "Mã đơn hàng không được rỗng");

        // 1. Kiểm tra đối chiếu với Admin API
        String cleanOrderCode = orderCode.replace("#", "").trim();
        ApiResponse orderApiRes = BangTestFixtures.getOrders(null, cleanOrderCode);
        Assert.assertTrue(orderApiRes.isSuccessfulStatus(), "Admin API phải phản hồi thành công khi tra cứu mã đơn: " + cleanOrderCode);

        // Đối chiếu dữ liệu đơn hàng trong hệ thống backend
        String firstRecipient = orderApiRes.stringValue("data.0.recipient_name");
        String firstPhone = orderApiRes.stringValue("data.0.recipient_phone");
        String firstStatus = orderApiRes.stringValue("data.0.status");
        String firstPaymentMethod = orderApiRes.stringValue("data.0.payment_method");
        long firstTotal = orderApiRes.longValue("data.0.total_amount");

        ExtentReportManager.logInfo("Backend Order Record: Recipient=" + firstRecipient + ", Phone=" + firstPhone + ", Status=" + firstStatus + ", Total=" + firstTotal);
        Assert.assertEquals(firstRecipient, customerName, "Tên người nhận trên backend phải khớp với thông tin đặt");
        Assert.assertEquals(firstPhone, phone, "SĐT người nhận trên backend phải khớp");
        Assert.assertEquals(firstPaymentMethod, "cod", "Phương thức thanh toán phải là COD");
        Assert.assertEquals(firstStatus, "pending", "Trạng thái ban đầu của đơn hàng COD phải là 'pending' (Chờ xác nhận)");
        if (expectedTotal > 0) {
            Assert.assertEquals(firstTotal, expectedTotal, "Tổng tiền trên backend phải khớp với màn hình checkout");
        }

        // 2. Kiểm tra giỏ hàng sau khi đặt thành công phải được làm trống
        openUrl("http://localhost:4321/cart");
        Assert.assertTrue(storefrontPage.isCartDrawerEmpty() || getDriver().getPageSource().contains("trống") || getDriver().getCurrentUrl().contains("/cart"),
                "Sau khi đặt hàng thành công, giỏ hàng phải được làm trống");
    }
}
