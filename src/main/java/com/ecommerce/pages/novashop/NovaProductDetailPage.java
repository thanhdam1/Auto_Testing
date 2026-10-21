package com.ecommerce.pages.novashop;

import com.ecommerce.pages.base.BasePage;
import com.ecommerce.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for ShopNova Product Detail Page (/san-pham/:slug)
 * Conforms to LOCATORS.md Module 4 specification.
 */
public class NovaProductDetailPage extends BasePage {

    // Locators
    private final By productName = By.cssSelector("h1.text-ink, main h1");
    private final By currentPrice = By.cssSelector("div.bg-page span.text-brand, span.text-brand");
    private final By originalPrice = By.cssSelector("span.line-through");
    private final By btnQuantityMinus = By.cssSelector("button[aria-label='Giảm số lượng']");
    private final By btnQuantityPlus = By.cssSelector("button[aria-label='Tăng số lượng']");
    private final By inputQuantity = By.cssSelector("input.border-x, input[inputmode='numeric']");
    private final By btnAddToCart = By.xpath("//button[contains(., 'Thêm Vào Giỏ Hàng')]");
    private final By btnBuyNow = By.xpath("//button[contains(., 'Mua Ngay')]");
    private final By btnFollowShop = By.xpath("//button[contains(., 'Theo Dõi') or contains(., 'Đang Theo Dõi')]");
    private final By linkViewShop = By.xpath("//a[contains(., 'Xem Shop') or contains(@href, '/shop/')]");
    private final By toastNotification = By.cssSelector("div.animate-slide-in, div.pointer-events-auto");

    public boolean isPageLoaded() {
        return isDisplayed(productName);
    }

    public String getProductNameText() {
        return getText(productName);
    }

    public String getCurrentPriceText() {
        return getText(currentPrice);
    }

    public void increaseQuantity() {
        click(btnQuantityPlus, "Nút tăng số lượng (+)");
    }

    public void decreaseQuantity() {
        click(btnQuantityMinus, "Nút giảm số lượng (-)");
    }

    public String getQuantityValue() {
        try {
            WebElement input = WaitUtil.waitForVisibility(inputQuantity, 5);
            return input.getAttribute("value");
        } catch (Exception e) {
            return "1";
        }
    }

    public void clickAddToCart() {
        click(btnAddToCart, "Nút Thêm Vào Giỏ Hàng");
    }

    public NovaCartPage clickBuyNow() {
        click(btnBuyNow, "Nút Mua Ngay");
        return new NovaCartPage();
    }

    public void toggleFollowShop() {
        click(btnFollowShop, "Nút Theo Dõi Shop");
    }

    public boolean isToastDisplayed() {
        return isDisplayed(toastNotification);
    }

    public void selectFirstVariant() {
        By variantBtn = By.cssSelector("button.rounded-\\[2px\\], div.flex.flex-wrap button");
        if (isDisplayed(variantBtn)) {
            click(variantBtn, "Chọn biến thể sản phẩm đầu tiên");
        }
    }

    public String getFollowButtonText() {
        return getText(btnFollowShop);
    }

    public void clickShopLink() {
        click(linkViewShop, "Đường dẫn Xem Shop");
    }
}
