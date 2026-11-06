package com.ecommerce.pages.noithatbang;

import com.ecommerce.pages.base.BasePage;
import com.ecommerce.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

/**
 * Page Object for Website_NoiThat Checkout Page (/checkout)
 * Port: 4321
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangCheckoutPage extends BasePage {

    // Empty Cart State
    private final By emptyCartHeading = By.xpath("//h1[contains(text(),'Giỏ hàng của bạn đang trống')]");
    private final By btnExploreProducts = By.xpath("//a[contains(@href,'/products') and contains(.,'Khám phá sản phẩm ngay')]");

    // Checkout Form Heading
    private final By checkoutHeading = By.xpath("//h1[contains(text(),'THANH TOÁN ĐƠN HÀNG')]");

    // User / Auth Info
    private final By loggedInBadge = By.xpath("//span[contains(text(),'Đã đăng nhập')]");
    private final By inputEmail = By.xpath("//input[@placeholder='vidu@gmail.com']");
    private final By inputPassword = By.xpath("//input[@placeholder='Ít nhất 6 ký tự']");
    private final By inputRecipientName = By.xpath("//input[@placeholder='Ví dụ: Nguyễn Văn A']");
    private final By inputRecipientPhone = By.xpath("//input[@placeholder='Ví dụ: 0931883988']");

    // Address
    private final By selectProvince = By.xpath("//select[contains(@class,'border-warm-200')]");
    private final By inputDistrict = By.xpath("//input[@placeholder='Quận/Huyện']");
    private final By inputWard = By.xpath("//input[@placeholder='Phường/Xã']");
    private final By inputStreet = By.xpath("//input[contains(@placeholder,'Âu Cơ') or contains(@placeholder,'Tây Thạnh')]");
    private final By textareaNote = By.xpath("//textarea[contains(@placeholder,'Giao giờ hành chính')]");

    // Coupon
    private final By inputCoupon = By.xpath("//input[contains(@placeholder,'Mã giảm giá')]");
    private final By btnApplyCoupon = By.xpath("//button[contains(text(),'Áp dụng')]");
    private final By appliedCouponBadge = By.cssSelector(".bg-emerald-50 .font-mono");
    private final By btnRemoveCoupon = By.xpath("//button[contains(text(),'Bỏ mã')]");
    private final By couponErrorMsg = By.cssSelector("p.text-rose-600");

    // Payment Radio
    private final By radioPaymentCOD = By.xpath("//label[contains(.,'COD')]//input[@type='radio']");
    private final By radioPaymentBank = By.xpath("//label[contains(.,'VietQR')]//input[@type='radio']");

    // Submit & Alerts
    private final By btnSubmitOrder = By.xpath("//button[@type='submit' and contains(.,'ĐẶT HÀNG')]");
    private final By submitAlertError = By.cssSelector(".bg-rose-50.text-rose-700");

    // Price Summary
    private final By textSubtotal = By.xpath("//div[span[contains(text(),'Tạm tính')]]/span[contains(@class,'font-serif')]");
    private final By textDiscount = By.xpath("//div[span[contains(text(),'Giảm giá')]]/span[contains(@class,'font-serif')]");
    private final By textTotal = By.xpath("//div[span[contains(text(),'Tổng thanh toán')]]//span[contains(@class,'font-serif')]");

    // Order Success Screen
    private final By successHeading = By.xpath("//h1[contains(.,'ĐẶT HÀNG THÀNH CÔNG') or contains(.,'HOÀN TẤT')]");
    private final By orderCode = By.xpath("//div[contains(.,'Mã đơn hàng:')]//span | //div[contains(.,'Mã thanh toán:')]//span");
    private final By btnViewMyOrders = By.xpath("//a[contains(@href,'/orders') and contains(.,'Xem đơn hàng')]");
    private final By vietQrImage = By.xpath("//img[contains(@src,'vietqr.io')]");

    public boolean isPageLoaded() {
        return isDisplayed(checkoutHeading) || isDisplayed(emptyCartHeading);
    }

    public boolean isCartEmpty() {
        return isDisplayed(emptyCartHeading);
    }

    public boolean isAuthenticatedUserDisplayed() {
        return isDisplayed(loggedInBadge);
    }

    public void fillRecipientInfo(String name, String phone, String email, String password) {
        if (isDisplayed(inputEmail) && email != null) {
            type(inputEmail, email, "Email đặt hàng");
        }
        if (isDisplayed(inputPassword) && password != null) {
            type(inputPassword, password, "Mật khẩu tài khoản mới");
        }
        type(inputRecipientName, name, "Tên người nhận");
        type(inputRecipientPhone, phone, "SĐT người nhận");
    }

    public void fillAddress(String province, String district, String ward, String street, String note) {
        if (isDisplayed(selectProvince) && province != null) {
            Select select = new Select(getDriver().findElement(selectProvince));
            try {
                select.selectByVisibleText(province);
            } catch (Exception e) {
                try {
                    select.selectByValue(province);
                } catch (Exception ex) {
                    select.selectByIndex(0);
                }
            }
        }
        if (district != null) {
            type(inputDistrict, district, "Quận/Huyện");
        }
        if (ward != null) {
            type(inputWard, ward, "Phường/Xã");
        }
        type(inputStreet, street, "Địa chỉ chi tiết");
        if (note != null && isDisplayed(textareaNote)) {
            type(textareaNote, note, "Ghi chú đơn hàng");
        }
    }

    public void applyCoupon(String code) {
        if (isDisplayed(inputCoupon)) {
            type(inputCoupon, code, "Nhập mã giảm giá: " + code);
            click(btnApplyCoupon, "Nút Áp dụng");
        }
    }

    public boolean isCouponApplied() {
        return isDisplayed(appliedCouponBadge);
    }

    public String getAppliedCouponCode() {
        return getText(appliedCouponBadge);
    }

    public void removeCoupon() {
        click(btnRemoveCoupon, "Nút Bỏ mã giảm giá");
    }

    public boolean isCouponErrorDisplayed() {
        return isDisplayed(couponErrorMsg);
    }

    public String getCouponErrorText() {
        return getText(couponErrorMsg);
    }

    public void selectPaymentCOD() {
        click(radioPaymentCOD, "Chọn thanh toán COD");
    }

    public void selectPaymentBank() {
        click(radioPaymentBank, "Chọn thanh toán VietQR");
    }

    public void submitOrder() {
        click(btnSubmitOrder, "Nút HOÀN TẤT ĐẶT HÀNG");
    }

    public boolean isSubmitErrorDisplayed() {
        return isDisplayed(submitAlertError);
    }

    public String getSubmitErrorText() {
        return getText(submitAlertError);
    }

    public boolean isOrderSuccessDisplayed() {
        return isDisplayed(successHeading);
    }

    public String getSuccessOrderCode() {
        return getText(orderCode);
    }

    public long getSubtotal() {
        return parseCurrency(getText(textSubtotal));
    }

    public long getDiscountAmount() {
        if (isDisplayed(textDiscount)) {
            return parseCurrency(getText(textDiscount));
        }
        return 0;
    }

    public long getTotalAmount() {
        return parseCurrency(getText(textTotal));
    }

    public static long parseCurrency(String text) {
        if (text == null) return 0;
        String clean = text.replaceAll("[^0-9]", "");
        return clean.isEmpty() ? 0 : Long.parseLong(clean);
    }

    public void clearRecipientName() {
        clearField(inputRecipientName);
    }

    public void clearRecipientPhone() {
        clearField(inputRecipientPhone);
    }

    public void clearRecipientEmail() {
        clearField(inputEmail);
    }

    public void clearStreet() {
        clearField(inputStreet);
    }

    private void clearField(By locator) {
        try {
            org.openqa.selenium.WebElement el = getDriver().findElement(locator);
            el.click();
            ((org.openqa.selenium.JavascriptExecutor) getDriver()).executeScript(
                    "arguments[0].value = '';" +
                    "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                    "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", el);
        } catch (Exception ignored) {}
    }

    public boolean isFieldInvalidByHtml5(String field) {
        By locator;
        if ("name".equalsIgnoreCase(field)) locator = inputRecipientName;
        else if ("phone".equalsIgnoreCase(field)) locator = inputRecipientPhone;
        else if ("email".equalsIgnoreCase(field)) locator = inputEmail;
        else if ("street".equalsIgnoreCase(field)) locator = inputStreet;
        else locator = inputRecipientName;

        try {
            org.openqa.selenium.WebElement el = getDriver().findElement(locator);
            String val = el.getAttribute("value");
            boolean isRequired = el.getAttribute("required") != null;
            if (isRequired && (val == null || val.trim().isEmpty())) {
                return true;
            }
            Boolean isValid = (Boolean) ((org.openqa.selenium.JavascriptExecutor) getDriver()).executeScript("return arguments[0].checkValidity();", el);
            return isValid != null && !isValid;
        } catch (Exception e) {
            return false;
        }
    }

    public void doubleClickSubmitOrder() {
        if (isDisplayed(btnSubmitOrder)) {
            org.openqa.selenium.WebElement btn = WaitUtil.waitForClickability(btnSubmitOrder);
            ((org.openqa.selenium.JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);
            btn.click();
            try {
                btn.click();
            } catch (Exception ignored) {}
        }
    }

    public boolean isVietQrImageDisplayed() {
        return isDisplayed(vietQrImage);
    }

    public BangCustomerOrdersPage clickViewMyOrdersAfterCheckout() {
        click(btnViewMyOrders, "Nút Xem đơn hàng của tôi");
        return new BangCustomerOrdersPage();
    }
}
