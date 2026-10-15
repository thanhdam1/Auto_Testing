package com.ecommerce.pages.noithatbang;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;

/**
 * Page Object for Website_NoiThat Checkout Page (/checkout)
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangCheckoutPage extends BasePage {

    // Empty Cart State
    private final By emptyCartHeading = By.xpath("//h1[contains(text(),'Giỏ hàng của bạn đang trống')]");
    private final By btnExploreProducts = By.xpath("//a[contains(@href,'/products') and contains(.,'Khám phá sản phẩm ngay')]");

    // Checkout Form
    private final By checkoutHeading = By.xpath("//h1[contains(text(),'THANH TOÁN ĐƠN HÀNG')]");
    private final By inputEmail = By.xpath("//input[@placeholder='vidu@gmail.com']");
    private final By inputPassword = By.xpath("//input[@placeholder='Ít nhất 6 ký tự']");
    private final By inputRecipientName = By.xpath("//input[@placeholder='Ví dụ: Nguyễn Văn A']");
    private final By inputRecipientPhone = By.xpath("//input[@placeholder='Ví dụ: 0931883988']");
    private final By inputDistrict = By.xpath("//input[@placeholder='Quận/Huyện']");
    private final By inputWard = By.xpath("//input[@placeholder='Phường/Xã']");
    private final By inputStreet = By.xpath("//input[contains(@placeholder,'Âu Cơ')]");
    private final By textareaNote = By.xpath("//textarea[contains(@placeholder,'Giao giờ hành chính')]");

    // Payment Radio
    private final By radioPaymentCOD = By.xpath("//label[contains(.,'COD')]//input[@type='radio']");
    private final By radioPaymentBank = By.xpath("//label[contains(.,'VietQR')]//input[@type='radio']");

    // Submit
    private final By btnSubmitOrder = By.xpath("//button[@type='submit' and contains(.,'ĐẶT HÀNG')]");
    private final By submitAlertError = By.cssSelector(".bg-rose-50.text-rose-700");

    // Success Screen
    private final By successHeading = By.xpath("//h1[contains(text(),'ĐẶT HÀNG THÀNH CÔNG!')]");
    private final By orderCode = By.xpath("//div[contains(text(),'Mã đơn hàng:')]//span");

    public boolean isPageLoaded() {
        return isDisplayed(checkoutHeading) || isDisplayed(emptyCartHeading);
    }

    public boolean isCartEmpty() {
        return isDisplayed(emptyCartHeading);
    }

    public void fillCheckoutForm(String name, String phone, String email, String password, String street) {
        if (isDisplayed(inputEmail)) {
            type(inputEmail, email, "Email đặt hàng");
        }
        if (isDisplayed(inputPassword)) {
            type(inputPassword, password, "Mật khẩu tạo tài khoản");
        }
        type(inputRecipientName, name, "Tên người nhận");
        type(inputRecipientPhone, phone, "Số điện thoại người nhận");
        type(inputStreet, street, "Địa chỉ chi tiết");
    }

    public void selectPaymentCOD() {
        click(radioPaymentCOD, "Chọn phương thức thanh toán COD");
    }

    public void selectPaymentBank() {
        click(radioPaymentBank, "Chọn phương thức thanh toán VietQR");
    }

    public void submitOrder() {
        click(btnSubmitOrder, "Nút Xác nhận Đặt hàng");
    }

    public boolean isOrderSuccessDisplayed() {
        return isDisplayed(successHeading);
    }

    public String getSuccessOrderCode() {
        return getText(orderCode);
    }

    public boolean isSubmitErrorDisplayed() {
        return isDisplayed(submitAlertError);
    }

    public String getSubmitErrorText() {
        return getText(submitAlertError);
    }
}
