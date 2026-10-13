package com.ecommerce.tests.noithat;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithat.AdminDashboardPage;
import com.ecommerce.pages.noithat.AdminOrderPage;
import com.ecommerce.pages.noithat.CartPage;
import com.ecommerce.pages.noithat.CheckoutPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Bộ test Thanh toán 3 bước của Nội Thất ({@code /thanh-toan}) - Phase 1.
 *
 * <p>Đây là module rủi ro cao nhất còn thiếu test (xem TEST_PLAN_BO_SUNG_3_DU_AN.md - mục 4.3):
 * trạng thái giỏ rỗng, validate bước 1, điều hướng 3 bước, phương thức thanh toán, màn hình xác nhận,
 * toán học phí ship 50.000đ / miễn phí từ 2.000.000đ, đặt hàng sinh mã đơn và hiển thị ở Admin.</p>
 *
 * <p>Các test đặt hàng thật (NT-P10, NT-P11) cần backend {@code http://localhost:4000/api} nên sẽ tự
 * SKIP nếu backend chưa bật.</p>
 */
public class CheckoutFlowTest extends BaseTest {

    private static final String SLUG_DEN = "den-ban-gom";
    private static final String NAME_DEN = "Đèn bàn gốm";
    private static final long PRICE_DEN = 1_900_000L;

    private static final String SLUG_SOFA = "sofa-da-y-milano";
    private static final String NAME_SOFA = "Sofa da Ý Milano";
    private static final long PRICE_SOFA = 58_900_000L;

    private static final String VALID_NAME = "Trần Văn Bình";
    private static final String VALID_PHONE = "0987654321";
    private static final String VALID_EMAIL = "binh.tran@vidu.com";
    private static final String VALID_DISTRICT = "Quận Ba Đình";
    private static final String VALID_ADDRESS = "Số 1 Đường Test";

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        openStorefrontPath("/san-pham");
        clearLocalStorage();
    }

    /**
     * Nạp giỏ hàng với 1 sản phẩm rồi mở trang thanh toán.
     */
    private CheckoutPage openCheckoutWithItem(String slug, String name, long price, int qty) {
        seedCartWithSingleItem(slug, name, price, qty);
        openStorefrontPath("/thanh-toan");
        CheckoutPage checkoutPage = new CheckoutPage();
        Assert.assertTrue(checkoutPage.isPageLoaded(), "Trang thanh toán phải tải được.");
        return checkoutPage;
    }

    private CheckoutPage openCheckoutWithCheapItem() {
        return openCheckoutWithItem(SLUG_DEN, NAME_DEN, PRICE_DEN, 1);
    }

    private void fillValidAddress(CheckoutPage checkoutPage) {
        checkoutPage.fillAddressForm(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                "Hà Nội", VALID_DISTRICT, VALID_ADDRESS, null);
    }

    // ============================ NT-P01 ============================

    @Test(groups = {"negative", "smoke", "storefront", "checkout"}, priority = 1,
            description = "NT-P01: Vào trang thanh toán khi giỏ trống thì hiện thông báo chưa có sản phẩm")
    public void testCheckoutWithEmptyCartShowsEmptyState() {
        openStorefrontPath("/thanh-toan");
        CheckoutPage checkoutPage = new CheckoutPage();

        Assert.assertTrue(checkoutPage.isPageLoaded(), "Trang thanh toán vẫn phải tải được.");
        Assert.assertTrue(checkoutPage.isCartEmptyMessageDisplayed(),
                "Phải hiển thị khối [data-checkout-empty] khi giỏ trống.");
        Assert.assertFalse(checkoutPage.isCheckoutContentDisplayed(),
                "Khối nội dung đặt hàng phải bị ẩn khi giỏ trống.");
    }

    // ============================ NT-P02 ============================

    @Test(groups = {"negative", "regression", "storefront", "checkout"}, priority = 2,
            description = "NT-P02: Bỏ trống bước 1 thì cả 5 trường bắt buộc đều báo lỗi và không sang bước 2")
    public void testSubmitEmptyAddressFormShowsAllRequiredErrors() {
        CheckoutPage checkoutPage = openCheckoutWithCheapItem();

        checkoutPage.submitAddressForm();

        for (String field : new String[]{"fullname", "phone", "province", "district", "address"}) {
            Assert.assertTrue(checkoutPage.isFieldErrorDisplayed(field),
                    "Trường bắt buộc '" + field + "' phải hiển thị lỗi khi bỏ trống.");
            Assert.assertFalse(checkoutPage.getFieldErrorText(field).isBlank(),
                    "Lỗi của trường '" + field + "' phải có nội dung.");
        }

        ExtentReportManager.logInfo("Số trường báo lỗi: " + checkoutPage.countDisplayedFieldErrors());
        Assert.assertFalse(checkoutPage.isFieldErrorDisplayed("email"),
                "Email là trường KHÔNG bắt buộc (không có rule validate) nên không được báo lỗi.");
        Assert.assertTrue(checkoutPage.isStepPanelVisible(0), "Phải vẫn đang ở bước 1.");
        Assert.assertFalse(checkoutPage.isStepPanelVisible(1), "Không được chuyển sang bước thanh toán.");
    }

    // ============================ NT-P03 ============================

    @Test(groups = {"negative", "regression", "storefront", "checkout"}, priority = 3,
            description = "NT-P03: Số điện thoại sai định dạng bị chặn, các trường hợp lệ không báo lỗi")
    public void testInvalidPhoneFormatBlocksStepOne() {
        CheckoutPage checkoutPage = openCheckoutWithCheapItem();

        checkoutPage.fillAddressForm(VALID_NAME, "123456", VALID_EMAIL, "Hà Nội", VALID_DISTRICT, VALID_ADDRESS, null);
        checkoutPage.submitAddressForm();

        Assert.assertTrue(checkoutPage.isFieldErrorDisplayed("phone"),
                "Số điện thoại '123456' phải bị báo lỗi (regex ^(0|\\+84)[0-9]{9}$).");
        Assert.assertFalse(checkoutPage.isFieldErrorDisplayed("fullname"),
                "Họ tên hợp lệ không được báo lỗi.");
        Assert.assertFalse(checkoutPage.isFieldErrorDisplayed("address"),
                "Địa chỉ hợp lệ không được báo lỗi.");
        Assert.assertFalse(checkoutPage.isStepPanelVisible(1), "Không được qua bước 2 khi còn lỗi.");
    }

    // ============================ NT-P04 ============================

    @Test(groups = {"smoke", "regression", "storefront", "checkout"}, priority = 4,
            description = "NT-P04: Điền hợp lệ bước 1 thì chuyển sang bước chọn phương thức thanh toán")
    public void testValidAddressFormMovesToPaymentStep() {
        CheckoutPage checkoutPage = openCheckoutWithCheapItem();

        fillValidAddress(checkoutPage);
        checkoutPage.submitAddressForm();

        Assert.assertTrue(checkoutPage.isStepPanelVisible(1), "Phải chuyển sang bước 2 (thanh toán).");
        Assert.assertFalse(checkoutPage.isStepPanelVisible(0), "Bước 1 phải bị ẩn.");
        Assert.assertTrue(checkoutPage.isStepLabelActive(1), "Nhãn bước 2 phải được active.");
        Assert.assertEquals(checkoutPage.countDisplayedFieldErrors(), 0, "Không được còn lỗi validate.");
    }

    // ============================ NT-P05 ============================

    @Test(groups = {"regression", "storefront", "checkout"}, priority = 5,
            description = "NT-P05: Đi tới bước xác nhận rồi quay lại thì dữ liệu đã nhập vẫn được giữ")
    public void testNavigationForwardAndBackPreservesEnteredData() {
        CheckoutPage checkoutPage = openCheckoutWithCheapItem();
        fillValidAddress(checkoutPage);
        checkoutPage.submitAddressForm();

        checkoutPage.goToReviewStep();
        Assert.assertTrue(checkoutPage.isStepPanelVisible(2), "Phải tới được bước 3 (xác nhận).");
        Assert.assertTrue(checkoutPage.isStepLabelActive(2), "Nhãn bước 3 phải được active.");

        checkoutPage.backToPaymentStep();
        Assert.assertTrue(checkoutPage.isStepPanelVisible(1), "Nút quay lại phải đưa về bước 2.");

        checkoutPage.backToAddressStep();
        Assert.assertTrue(checkoutPage.isStepPanelVisible(0), "Nút quay lại phải đưa về bước 1.");
        Assert.assertEquals(checkoutPage.getEnteredFullName(), VALID_NAME,
                "Họ tên đã nhập phải được giữ nguyên sau khi quay lại.");
        Assert.assertEquals(checkoutPage.getEnteredPhone(), VALID_PHONE,
                "Số điện thoại đã nhập phải được giữ nguyên sau khi quay lại.");
    }

    // ============================ NT-P06 ============================

    @Test(groups = {"data", "regression", "storefront", "checkout"}, priority = 6,
            description = "NT-P06: Phương thức thanh toán được chọn phải hiển thị đúng ở bước xác nhận")
    public void testPaymentMethodIsReflectedInReviewStep() {
        CheckoutPage checkoutPage = openCheckoutWithCheapItem();
        fillValidAddress(checkoutPage);
        checkoutPage.submitAddressForm();

        checkoutPage.selectPaymentMethod("bank");
        Assert.assertTrue(checkoutPage.isPaymentMethodSelected("bank"), "Radio 'bank' phải được chọn.");
        checkoutPage.goToReviewStep();
        String bankReview = checkoutPage.getReviewPaymentText();
        ExtentReportManager.logInfo("Phương thức hiển thị khi chọn bank: " + bankReview);
        Assert.assertTrue(bankReview.contains("Chuyển khoản"),
                "Chọn chuyển khoản ngân hàng thì bước xác nhận phải hiển thị 'Chuyển khoản'. Thực tế: " + bankReview);

        checkoutPage.backToPaymentStep();
        checkoutPage.selectPaymentMethod("cod");
        checkoutPage.goToReviewStep();
        String codReview = checkoutPage.getReviewPaymentText();
        ExtentReportManager.logInfo("Phương thức hiển thị khi chọn COD: " + codReview);
        Assert.assertTrue(codReview.contains("COD"),
                "Chọn COD thì bước xác nhận phải hiển thị 'COD'. Thực tế: " + codReview);
    }

    // ============================ NT-P07 ============================

    @Test(groups = {"regression", "storefront", "checkout"}, priority = 7,
            description = "NT-P07: Bước xác nhận hiển thị đúng địa chỉ người nhận và danh sách sản phẩm")
    public void testReviewStepShowsAddressAndItems() {
        CheckoutPage checkoutPage = openCheckoutWithCheapItem();
        fillValidAddress(checkoutPage);
        checkoutPage.submitAddressForm();
        checkoutPage.goToReviewStep();

        String reviewAddress = checkoutPage.getReviewAddressText();
        ExtentReportManager.logInfo("Địa chỉ hiển thị ở bước xác nhận: " + reviewAddress.replace("\n", " | "));

        Assert.assertTrue(reviewAddress.contains(VALID_NAME), "Phải hiển thị tên người nhận.");
        Assert.assertTrue(reviewAddress.contains(VALID_PHONE), "Phải hiển thị số điện thoại người nhận.");
        Assert.assertTrue(reviewAddress.contains(VALID_ADDRESS), "Phải hiển thị địa chỉ chi tiết.");
        Assert.assertTrue(reviewAddress.contains("Hà Nội"), "Phải hiển thị tỉnh/thành.");

        String reviewItems = checkoutPage.getReviewItemsText();
        Assert.assertTrue(reviewItems.contains(NAME_DEN),
                "Bước xác nhận phải liệt kê sản phẩm trong giỏ. Thực tế: " + reviewItems);
    }

    // ============================ NT-P08 ============================

    @Test(groups = {"data", "regression", "storefront", "checkout"}, priority = 8,
            description = "NT-P08: Đơn dưới 2.000.000đ phải cộng phí ship 50.000đ và tổng = tạm tính + phí ship")
    public void testShippingFeeAndTotalMathBelowTwoMillion() {
        CheckoutPage checkoutPage = openCheckoutWithCheapItem();

        long subtotal = checkoutPage.getSubtotalValue();
        long shipping = checkoutPage.getShippingFeeValue();
        long total = checkoutPage.getTotalValue();
        ExtentReportManager.logInfo(String.format("Tạm tính=%d, phí ship=%d, tổng=%d", subtotal, shipping, total));

        Assert.assertEquals(subtotal, PRICE_DEN, "Tạm tính phải bằng đơn giá sản phẩm.");
        Assert.assertEquals(shipping, CheckoutPage.SHIPPING_FEE,
                "Đơn dưới 2.000.000đ phải có phí ship 50.000đ.");
        Assert.assertEquals(total, subtotal + CheckoutPage.SHIPPING_FEE,
                "Tổng tiền phải bằng tạm tính + phí ship.");
    }

    // ============================ NT-P09 ============================

    @Test(groups = {"boundary", "regression", "storefront", "checkout"}, priority = 9,
            description = "NT-P09: Đơn từ 2.000.000đ được miễn phí vận chuyển nên tổng bằng tạm tính")
    public void testFreeShippingFromTwoMillion() {
        CheckoutPage checkoutPage = openCheckoutWithItem(SLUG_SOFA, NAME_SOFA, PRICE_SOFA, 1);

        long subtotal = checkoutPage.getSubtotalValue();
        long total = checkoutPage.getTotalValue();
        ExtentReportManager.logInfo(String.format("Tạm tính=%d, tổng=%d, text phí ship='%s'",
                subtotal, total, checkoutPage.getShippingText()));

        Assert.assertTrue(subtotal >= CheckoutPage.FREE_SHIPPING_THRESHOLD,
                "Điều kiện đầu: tạm tính phải từ 2.000.000đ.");
        Assert.assertEquals(checkoutPage.getShippingFeeValue(), 0L, "Phải được miễn phí vận chuyển.");
        Assert.assertTrue(checkoutPage.getShippingText().contains("Miễn phí"),
                "Nhãn phí ship phải hiển thị 'Miễn phí'. Thực tế: " + checkoutPage.getShippingText());
        Assert.assertEquals(total, subtotal, "Tổng tiền phải bằng tạm tính khi miễn phí ship.");
    }

    // ============================ NT-P10 ============================

    @Test(groups = {"e2e", "regression", "storefront", "checkout"}, priority = 10,
            description = "NT-P10: Đặt hàng thành công sinh mã đơn đúng định dạng và xoá giỏ hàng")
    public void testPlaceOrderGeneratesOrderCodeAndClearsCart() {
        assumeApiAvailable();

        CheckoutPage checkoutPage = openCheckoutWithCheapItem();
        fillValidAddress(checkoutPage);
        checkoutPage.submitAddressForm();
        checkoutPage.goToReviewStep();
        checkoutPage.placeOrder();

        Assert.assertTrue(checkoutPage.waitForOrderSuccess(25),
                "Phải hiển thị màn hình đặt hàng thành công (lỗi API: " + checkoutPage.getOrderErrorMessage() + ")");

        String orderCode = checkoutPage.getOrderCode();
        ExtentReportManager.logInfo("Mã đơn hàng nhận được: " + orderCode);
        Assert.assertTrue(orderCode.matches("NT-[0-9]{4}(-[0-9]{4})?"),
                "Mã đơn phải theo định dạng NT-YYMM-XXXX (hoặc NT-###### khi API trả mã dự phòng). Thực tế: " + orderCode);

        openStorefrontPath("/gio-hang");
        CartPage cartPage = new CartPage();
        Assert.assertTrue(cartPage.isCartEmpty(), "Sau khi đặt hàng thành công, giỏ hàng phải được xoá.");
        Assert.assertEquals(cartPage.getCartItemCount(), 0, "Giỏ hàng phải không còn sản phẩm.");
    }

    // ============================ NT-P11 ============================

    @Test(groups = {"e2e", "regression", "admin", "checkout"}, priority = 11,
            description = "NT-P11 (E2E): Đơn hàng đặt từ storefront phải xuất hiện trong Admin > Quản lý đơn hàng")
    public void testPlacedOrderAppearsInAdminOrderList() {
        assumeApiAvailable();

        String customerName = "E2E Checkout " + uniqueSuffix();
        CheckoutPage checkoutPage = openCheckoutWithCheapItem();
        checkoutPage.fillAddressForm(customerName, VALID_PHONE, "e2e" + uniqueSuffix() + "@vidu.com",
                "Hà Nội", VALID_DISTRICT, VALID_ADDRESS, "Đơn tạo bởi automation test");
        checkoutPage.submitAddressForm();
        checkoutPage.goToReviewStep();
        checkoutPage.placeOrder();

        Assert.assertTrue(checkoutPage.waitForOrderSuccess(25),
                "Đặt hàng phải thành công trước khi kiểm tra Admin. Lỗi: " + checkoutPage.getOrderErrorMessage());
        String orderCode = checkoutPage.getOrderCode();

        ExtentReportManager.logInfo("Kiểm tra đơn " + orderCode + " trong Admin");
        openAdmin();
        AdminOrderPage orderPage = new AdminDashboardPage().navigateToOrders();
        Assert.assertTrue(orderPage.isPageLoaded(), "Trang quản lý đơn hàng của Admin phải tải được.");
        Assert.assertTrue(orderPage.getVisibleOrderRowCount() > 0,
                "Danh sách đơn hàng của Admin không được rỗng (kiểm tra backend API).");

        orderPage.searchOrder(orderCode);
        Assert.assertTrue(orderPage.hasOrderByCode(orderCode),
                "Đơn vừa đặt (" + orderCode + ") phải tìm thấy trong Admin > Quản lý đơn hàng.");
    }
}
