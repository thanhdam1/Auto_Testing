package com.ecommerce.pages.novashop;

import com.ecommerce.pages.base.BasePage;
import com.ecommerce.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for ShopNova Cart Page (/gio-hang)
 * Conforms to LOCATORS.md Module 5 specification.
 */
public class NovaCartPage extends BasePage {

    // Locators
    private final By headingTitle = By.cssSelector("h1");
    private final By btnCheckout = By.xpath("//button[contains(., 'Mua Hàng')]");
    private final By cartItems = By.cssSelector("div.pb-24 div.mt-3 > div, div.divide-y > div");
    private final By totalPrice = By.cssSelector("div.fixed span.text-brand, span.text-brand");
    private final By btnSelectAll = By.xpath("//span[contains(., 'Chọn Tất Cả')] | //button[@role='checkbox']");
    private final By btnDeleteItems = By.xpath("//button[contains(., 'Xoá')]");

    public boolean isPageLoaded() {
        try {
            boolean urlOk = getDriver().getCurrentUrl().contains("/gio-hang");
            boolean hasHeader = isDisplayed(headingTitle) || isDisplayed(btnCheckout);
            return urlOk || hasHeader;
        } catch (Exception e) {
            return false;
        }
    }

    public int getCartItemCount() {
        try {
            List<WebElement> items = getDriver().findElements(cartItems);
            return items.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public NovaCheckoutPage clickCheckout() {
        click(btnCheckout, "Nút Mua Hàng từ giỏ");
        return new NovaCheckoutPage();
    }

    public boolean isCheckoutButtonEnabled() {
        try {
            WebElement btn = WaitUtil.waitForVisibility(btnCheckout, 5);
            return btn.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public void toggleSelectAll() {
        if (isDisplayed(btnSelectAll)) {
            click(btnSelectAll, "Nút chọn tất cả giỏ hàng");
        }
    }

    public void removeFirstItem() {
        By btnDelete = By.xpath("(//button[contains(., 'Xoá') or contains(., 'Xóa')])[1]");
        if (isDisplayed(btnDelete)) {
            click(btnDelete, "Xoá sản phẩm khỏi giỏ");
        }
    }

    public void increaseFirstItemQty() {
        By btnPlus = By.cssSelector("button[aria-label='Tăng số lượng']");
        if (isDisplayed(btnPlus)) {
            click(btnPlus, "Nút tăng số lượng trong giỏ");
        }
    }

    public String getTotalPriceText() {
        try {
            return getText(totalPrice);
        } catch (Exception e) {
            return "";
        }
    }
}
