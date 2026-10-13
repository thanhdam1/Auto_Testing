package com.ecommerce.pages.noithat;

import com.ecommerce.pages.base.BasePage;
import com.ecommerce.utils.WaitUtil;
import org.openqa.selenium.By;

/**
 * Page Object for Product Detail Page (/san-pham/:slug).
 *
 * <p>Locators được đối chiếu với DOM contract của ứng dụng Nội Thất:
 * {@code #btn-add-to-cart}, {@code #btn-buy-now}, {@code #btn-qty-minus}, {@code #btn-qty-plus},
 * {@code #input-qty}; các nút CTA mang sẵn {@code data-slug}, {@code data-name}, {@code data-price}.</p>
 *
 * <p>Nghiệp vụ (từ script trong trang): nút +/- giới hạn 1..99, "Mua ngay" thêm vào giỏ rồi
 * điều hướng sang {@code /thanh-toan}, toast có id {@code cart-toast-notification}.</p>
 */
public class ProductDetailPage extends BasePage {

    // Product Info
    private final By productName = By.cssSelector("h1.font-display");
    private final By productPrice = By.cssSelector("#btn-add-to-cart[data-price]");
    private final By stockStatus = By.cssSelector("span.text-emerald-700, span.text-red-600");

    // Quantity Selector
    private final By inputQty = By.id("input-qty");
    private final By btnQtyMinus = By.id("btn-qty-minus");
    private final By btnQtyPlus = By.id("btn-qty-plus");

    // CTA Buttons
    private final By btnAddToCart = By.id("btn-add-to-cart");
    private final By btnBuyNow = By.id("btn-buy-now");

    // Toast sau khi thêm vào giỏ (id do scripts/cart.ts tạo)
    private final By toastNotification = By.id("cart-toast-notification");

    public boolean isPageLoaded() {
        return isDisplayed(productName);
    }

    public String getProductName() {
        return getText(productName);
    }

    public String getProductPrice() {
        return find(productPrice).getAttribute("data-price");
    }

    /**
     * Slug sản phẩm đang xem (đọc từ thuộc tính {@code data-slug} của nút thêm giỏ).
     */
    public String getProductSlug() {
        return find(btnAddToCart).getAttribute("data-slug");
    }

    public long getProductPriceValue() {
        return CartPage.parseVnd(getProductPrice());
    }

    public String getStockStatusText() {
        return getText(stockStatus);
    }

    public boolean isInStock() {
        String text = getStockStatusText();
        return text != null && text.contains("Còn hàng");
    }

    public boolean isOutOfStock() {
        String text = getStockStatusText();
        return text != null && text.contains("Hết hàng");
    }

    public void increaseQuantity(int times) {
        for (int i = 0; i < times; i++) {
            click(btnQtyPlus, "Nút tăng số lượng (+)");
        }
    }

    public void decreaseQuantity(int times) {
        for (int i = 0; i < times; i++) {
            click(btnQtyMinus, "Nút giảm số lượng (-)");
        }
    }

    public String getCurrentQuantity() {
        return find(inputQty).getAttribute("value");
    }

    public int getCurrentQuantityValue() {
        try {
            return Integer.parseInt(getCurrentQuantity());
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Giá trị thuộc tính {@code max} của ô số lượng (mặc định bằng tồn kho hoặc 99).
     */
    public int getQuantityMaxAttribute() {
        try {
            return Integer.parseInt(find(inputQty).getAttribute("max"));
        } catch (Exception e) {
            return -1;
        }
    }

    public boolean isAddToCartEnabled() {
        return find(btnAddToCart).isEnabled();
    }

    public void clickAddToCart() {
        click(btnAddToCart, "Nút Thêm vào giỏ hàng");
    }

    public void clickBuyNow() {
        click(btnBuyNow, "Nút Mua ngay");
    }

    /**
     * Gán trực tiếp giá trị ô số lượng bằng JavaScript (kiểm tra giá trị biên nhanh, tránh
     * phải click hàng trăm lần).
     */
    public void setQuantityViaJs(int quantity) {
        org.openqa.selenium.WebElement input = find(inputQty);
        ((org.openqa.selenium.JavascriptExecutor) getDriver())
                .executeScript("arguments[0].value = arguments[1];", input, String.valueOf(quantity));
    }

    /**
     * Chờ toast "đã thêm vào giỏ hàng" xuất hiện rồi trả về nội dung.
     */
    public String waitForCartToast() {
        WaitUtil.waitForVisibility(toastNotification, 5);
        return getText(toastNotification);
    }

    public boolean isCartToastDisplayed() {
        return isDisplayed(toastNotification);
    }

    /**
     * Chờ điều hướng sang trang thanh toán (dùng cho nút "Mua ngay").
     */
    public boolean waitForCheckoutPage() {
        try {
            return WaitUtil.waitForUrlContains("/thanh-toan", 10);
        } catch (Exception e) {
            return false;
        }
    }
}
