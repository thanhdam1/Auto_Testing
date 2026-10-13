package com.ecommerce.pages.noithat;

import com.ecommerce.pages.base.BasePage;
import com.ecommerce.utils.LogUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object for Checkout Page (/thanh-toan) - luồng 3 bước của dự án Nội Thất.
 *
 * <p>Locators được đối chiếu với DOM contract của ứng dụng Nội Thất:
 * {@code [data-checkout-root|empty|content|steps|items|subtotal|shipping|total|success]},
 * {@code [data-address-form]}, {@code #ck-name|#ck-phone|#ck-email|#ck-province|#ck-district|#ck-address|#ck-note},
 * {@code [data-error-for='...']}, {@code [data-step-panel='0|1|2']}, {@code [data-step-next]},
 * {@code [data-step-back='0|1']}, {@code [data-payment]}, {@code [data-review-address|payment|items]},
 * {@code [data-place-order]}, {@code [data-order-code]}, {@code #checkout-error-msg}.</p>
 *
 * <p>Nghiệp vụ: validate theo {@code scripts/validate.ts} (fullname ≥2 ký tự, phone theo regex VN,
 * province/district/address bắt buộc; <b>email không bắt buộc và không được validate</b>);
 * phí ship = 0 nếu subtotal ≥ 2.000.000đ, ngược lại 50.000đ; đặt hàng gọi
 * {@code POST http://localhost:4000/api/orders}.</p>
 */
public class CheckoutPage extends BasePage {

    public static final long SHIPPING_FEE = 50000L;
    public static final long FREE_SHIPPING_THRESHOLD = 2000000L;

    // Trang & các khối trạng thái
    private final By headingCheckout = By.xpath("//h1[contains(text(),'Tiến hành đặt hàng')]");
    private final By checkoutRoot = By.cssSelector("[data-checkout-root]");
    private final By checkoutEmpty = By.cssSelector("[data-checkout-empty]");
    private final By checkoutContent = By.cssSelector("[data-checkout-content]");
    private final By checkoutSuccess = By.cssSelector("[data-checkout-success]");
    private final By orderCode = By.cssSelector("[data-order-code]");
    private final By orderErrorMessage = By.id("checkout-error-msg");

    // Bước 1 - địa chỉ
    private final By addressForm = By.cssSelector("[data-address-form]");
    private final By inputName = By.id("ck-name");
    private final By inputPhone = By.id("ck-phone");
    private final By inputEmail = By.id("ck-email");
    private final By selectProvince = By.id("ck-province");
    private final By inputDistrict = By.id("ck-district");
    private final By inputAddress = By.id("ck-address");
    private final By txtNote = By.id("ck-note");
    private final By btnSubmitAddress = By.cssSelector("[data-address-form] button[type='submit']");

    // Bước 2 - thanh toán
    private final By btnStepNext = By.cssSelector("[data-step-next]");
    private final By btnBackToAddress = By.cssSelector("[data-step-back='0']");
    private final By btnBackToPayment = By.cssSelector("[data-step-back='1']");

    // Bước 3 - xác nhận
    private final By reviewAddress = By.cssSelector("[data-review-address]");
    private final By reviewPayment = By.cssSelector("[data-review-payment]");
    private final By reviewItems = By.cssSelector("[data-review-items]");
    private final By btnPlaceOrder = By.cssSelector("[data-place-order]");

    // Tóm tắt đơn hàng (aside)
    private final By checkoutItems = By.cssSelector("[data-checkout-items]");
    private final By lblSubtotal = By.cssSelector("[data-checkout-subtotal]");
    private final By lblShipping = By.cssSelector("[data-checkout-shipping]");
    private final By lblTotal = By.cssSelector("[data-checkout-total]");

    public boolean isPageLoaded() {
        return isDisplayed(headingCheckout);
    }

    public boolean isCheckoutRootDisplayed() {
        return isVisibleNow(checkoutRoot);
    }

    // ======================= TRẠNG THÁI GIỎ HÀNG =======================

    public boolean isCartEmptyMessageDisplayed() {
        return isVisibleNow(checkoutEmpty);
    }

    public boolean isCheckoutContentDisplayed() {
        return isVisibleNow(checkoutContent);
    }

    public boolean isOrderSuccessDisplayed() {
        return isVisibleNow(checkoutSuccess);
    }

    // ======================= BƯỚC 1: ĐỊA CHỈ =======================

    public void fillAddressForm(String fullname, String phone, String email, String province,
                                String district, String address, String note) {
        type(inputName, fullname, "Họ và tên người nhận");
        type(inputPhone, phone, "Số điện thoại người nhận");
        if (email != null && !email.isEmpty()) {
            type(inputEmail, email, "Email nhận thông báo");
        }
        if (province != null && !province.isEmpty()) {
            selectDropdownByVisibleText(selectProvince, province, "Tỉnh / Thành phố");
        }
        type(inputDistrict, district, "Quận / Huyện");
        type(inputAddress, address, "Địa chỉ cụ thể");
        if (note != null && !note.isEmpty()) {
            type(txtNote, note, "Ghi chú đơn hàng");
        }
    }

    public void submitAddressForm() {
        click(btnSubmitAddress, "Nút 'Tiếp tục chọn thanh toán'");
    }

    /**
     * Điền đủ thông tin hợp lệ tối thiểu để qua bước 1.
     */
    public void fillValidAddressForm(String fullname, String phone, String email) {
        fillAddressForm(fullname, phone, email, "Hà Nội", "Quận Ba Đình", "Số 1 Đường Test", null);
    }

    /**
     * Thông điệp lỗi đang hiển thị dưới một trường (fullname, phone, province, district, address).
     */
    public String getFieldErrorText(String field) {
        return textNow(By.cssSelector("[data-error-for='" + field + "']"));
    }

    public boolean isFieldErrorDisplayed(String field) {
        return isVisibleNow(By.cssSelector("[data-error-for='" + field + "']"));
    }

    /**
     * Số trường đang báo lỗi (dùng để kiểm tra validate toàn form).
     */
    public int countDisplayedFieldErrors() {
        int count = 0;
        for (String field : new String[]{"fullname", "phone", "email", "province", "district", "address"}) {
            if (isFieldErrorDisplayed(field)) {
                count++;
            }
        }
        return count;
    }

    public String getEnteredFullName() {
        return find(inputName).getAttribute("value");
    }

    public String getEnteredPhone() {
        return find(inputPhone).getAttribute("value");
    }

    // ======================= ĐIỀU HƯỚNG 3 BƯỚC =======================

    public boolean isStepPanelVisible(int index) {
        return isVisibleNow(By.cssSelector("[data-step-panel='" + index + "']"));
    }

    public boolean isStepLabelActive(int index) {
        try {
            WebElement label = getDriver().findElement(By.cssSelector("[data-step-label='" + index + "']"));
            String classes = label.getAttribute("class");
            return classes != null && classes.contains("font-semibold");
        } catch (Exception e) {
            return false;
        }
    }

    public void goToPaymentStep() {
        click(btnStepNext, "Nút 'Kiểm tra đơn hàng'");
    }

    public void goToReviewStep() {
        click(btnStepNext, "Nút 'Kiểm tra đơn hàng' (sang bước xác nhận)");
    }

    public void backToAddressStep() {
        click(btnBackToAddress, "Nút 'Quay lại' về bước địa chỉ");
    }

    public void backToPaymentStep() {
        click(btnBackToPayment, "Nút 'Quay lại' về bước thanh toán");
    }

    // ======================= BƯỚC 2: THANH TOÁN =======================

    public void selectPaymentMethod(String value) {
        click(By.cssSelector("input[data-payment][value='" + value + "']"), "Phương thức thanh toán: " + value);
    }

    public boolean isPaymentMethodSelected(String value) {
        try {
            return getDriver().findElement(By.cssSelector("input[data-payment][value='" + value + "']")).isSelected();
        } catch (Exception e) {
            return false;
        }
    }

    // ======================= BƯỚC 3: XÁC NHẬN =======================

    public String getReviewAddressText() {
        return textNow(reviewAddress);
    }

    public String getReviewPaymentText() {
        return textNow(reviewPayment);
    }

    public String getReviewItemsText() {
        return textNow(reviewItems);
    }

    // ======================= TÓM TẮT & ĐẶT HÀNG =======================

    public int getCheckoutItemRowCount() {
        try {
            List<WebElement> rows = getDriver().findElements(
                    By.cssSelector("[data-checkout-items] > div"));
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public String getSubtotalText() {
        return textNow(lblSubtotal);
    }

    public String getShippingText() {
        return textNow(lblShipping);
    }

    public String getTotalText() {
        return textNow(lblTotal);
    }

    public long getSubtotalValue() {
        return CartPage.parseVnd(getSubtotalText());
    }

    /**
     * Phí vận chuyển dạng số; "Miễn phí" được quy về 0.
     */
    public long getShippingFeeValue() {
        String text = getShippingText();
        return text != null && text.contains("Miễn phí") ? 0L : CartPage.parseVnd(text);
    }

    public long getTotalValue() {
        return CartPage.parseVnd(getTotalText());
    }

    public void placeOrder() {
        click(btnPlaceOrder, "Nút 'Hoàn tất đặt hàng'");
    }

    public boolean isOrderErrorMessageDisplayed() {
        return isVisibleNow(orderErrorMessage);
    }

    public String getOrderErrorMessage() {
        return textNow(orderErrorMessage);
    }

    /**
     * Chờ tối đa {@code timeoutSeconds} cho tới khi màn hình đặt hàng thành công xuất hiện.
     */
    public boolean waitForOrderSuccess(int timeoutSeconds) {
        try {
            return new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutSeconds))
                    .until(driver -> isVisibleNow(checkoutSuccess));
        } catch (Exception e) {
            LogUtil.warn("Không thấy màn hình đặt hàng thành công sau {}s.", timeoutSeconds);
            return false;
        }
    }

    /**
     * Chờ tối đa {@code timeoutSeconds} cho tới khi hộp lỗi đặt hàng xuất hiện (API lỗi/offline).
     */
    public boolean waitForOrderError(int timeoutSeconds) {
        try {
            return new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutSeconds))
                    .until(driver -> isVisibleNow(orderErrorMessage));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Mã đơn hàng hiển thị trên màn hình thành công.
     */
    public String getOrderCode() {
        return textNow(orderCode);
    }

    // ======================= HELPERS =======================

    /**
     * Kiểm tra hiển thị "ngay lập tức" (không chờ) - dùng cho các phần tử bật/tắt bằng class
     * {@code hidden} để test không bị chậm 5-10 giây mỗi lần kiểm tra trạng thái ẩn.
     */
    private boolean isVisibleNow(By locator) {
        try {
            return getDriver().findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private String textNow(By locator) {
        try {
            return getDriver().findElement(locator).getText().trim();
        } catch (Exception e) {
            return "";
        }
    }
}
