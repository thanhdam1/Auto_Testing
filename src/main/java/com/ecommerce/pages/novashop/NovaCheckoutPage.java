package com.ecommerce.pages.novashop;

import com.ecommerce.pages.base.BasePage;
import com.ecommerce.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Page Object for ShopNova Checkout Page (/dat-hang)
 * Conforms to LOCATORS.md Module 6 specification.
 */
public class NovaCheckoutPage extends BasePage {

    // Locators
    private final By headingTitle = By.xpath("//h1[contains(., 'Thanh Toán') or contains(., 'Đặt Hàng')] | //main//h1");
    private final By btnChangeAddress = By.xpath("//button[contains(., 'Thay Đổi')]");
    private final By inputRecipientName = By.cssSelector("input[placeholder*='Họ và tên người nhận']");
    private final By inputRecipientPhone = By.cssSelector("input[placeholder*='Số điện thoại']");
    private final By inputDetailAddress = By.cssSelector("input[placeholder*='Địa chỉ chi tiết']");
    private final By voucher20k = By.xpath("//button[contains(., 'NOVA20K')]");
    private final By voucher50k = By.xpath("//button[contains(., 'NOVA50K')]");
    private final By paymentCOD = By.xpath("//button[contains(., 'Thanh toán khi nhận hàng')]");
    private final By paymentBank = By.xpath("//button[contains(., 'Chuyển khoản ngân hàng')]");
    private final By paymentWallet = By.xpath("//button[contains(., 'Ví điện tử')]");
    private final By inputNote = By.cssSelector("textarea[placeholder*='giao giờ hành chính']");
    private final By btnPlaceOrder = By.xpath("//button[contains(., 'Đặt Hàng')]");

    public boolean isPageLoaded() {
        try {
            boolean urlOk = getDriver().getCurrentUrl().contains("/dat-hang");
            boolean hasElement = isDisplayed(btnPlaceOrder) || isDisplayed(headingTitle);
            return urlOk || hasElement;
        } catch (Exception e) {
            return false;
        }
    }

    public void selectPaymentCOD() {
        if (isDisplayed(paymentCOD)) {
            click(paymentCOD, "Phương thức COD");
        }
    }

    public void selectPaymentBank() {
        if (isDisplayed(paymentBank)) {
            click(paymentBank, "Phương thức Chuyển khoản QR");
        }
    }

    public void enterOrderNote(String note) {
        if (isDisplayed(inputNote)) {
            type(inputNote, note, "Ghi chú đơn hàng cho người bán");
        }
    }

    public void clickPlaceOrder() {
        click(btnPlaceOrder, "Nút Đặt Hàng Submit");
    }

    public boolean isVoucherAvailable() {
        try {
            return !getDriver().findElements(voucher20k).isEmpty() || !getDriver().findElements(voucher50k).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public void applyVoucher20k() {
        try {
            if (isVoucherAvailable() && !getDriver().findElements(voucher20k).isEmpty()) {
                WebElement el = getDriver().findElement(voucher20k);
                if (el.isDisplayed()) {
                    jsClick(voucher20k, "Voucher NOVA20K");
                }
            }
        } catch (Exception ignored) {}
    }

    public void applyVoucher50k() {
        try {
            if (isVoucherAvailable() && !getDriver().findElements(voucher50k).isEmpty()) {
                WebElement el = getDriver().findElement(voucher50k);
                if (el.isDisplayed()) {
                    jsClick(voucher50k, "Voucher NOVA50K");
                }
            }
        } catch (Exception ignored) {}
    }

    public void openChangeAddressModal() {
        if (isDisplayed(btnChangeAddress)) {
            click(btnChangeAddress, "Nút Thay Đổi địa chỉ");
        }
    }
}
