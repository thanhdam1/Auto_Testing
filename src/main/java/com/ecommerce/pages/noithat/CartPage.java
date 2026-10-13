package com.ecommerce.pages.noithat;

import com.ecommerce.pages.base.BasePage;
import com.ecommerce.reports.ExtentReportManager;
import com.ecommerce.utils.LogUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Page Object for Cart Page (/gio-hang).
 *
 * <p>Locators được đối chiếu với DOM contract của ứng dụng Nội Thất:
 * {@code data-cart-root}, {@code data-cart-items}, {@code data-row}, {@code data-row-qty},
 * {@code data-row-up}, {@code data-row-down}, {@code data-row-remove}, {@code data-cart-subtotal},
 * {@code data-cart-total}, {@code data-cart-clear}, {@code data-cart-empty}, {@code data-cart-content},
 * {@code data-cart-count-label}.</p>
 *
 * <p>Nghiệp vụ quan trọng (từ {@code scripts/cart.ts}): số lượng bị kẹp trong khoảng 1..99,
 * sản phẩm trùng slug thì gộp dòng, tổng tiền = Σ(đơn giá × số lượng), phí ship KHÔNG cộng ở trang giỏ.</p>
 */
public class CartPage extends BasePage {

    // Cart State
    private final By cartRoot = By.cssSelector("[data-cart-root]");
    private final By cartEmpty = By.cssSelector("[data-cart-empty]");
    private final By cartContent = By.cssSelector("[data-cart-content]");
    private final By cartCountLabel = By.cssSelector("[data-cart-count-label]");
    private final By headerCartCount = By.cssSelector("[data-cart-count]");

    // Cart Items
    private final By cartItemRows = By.cssSelector("[data-cart-items] [data-row]");

    // Summary
    private final By cartSubtotal = By.cssSelector("[data-cart-subtotal]");
    private final By cartTotal = By.cssSelector("[data-cart-total]");

    // Actions
    private final By btnClearAll = By.cssSelector("[data-cart-clear]");
    private final By linkCheckout = By.cssSelector("a[href='/thanh-toan']");
    private final By linkContinueShopping = By.cssSelector("a[href='/san-pham']");

    public boolean isPageLoaded() {
        return isDisplayed(cartRoot);
    }

    public boolean isCartEmpty() {
        try {
            WebElement emptyEl = getDriver().findElement(cartEmpty);
            return !emptyEl.getAttribute("class").contains("hidden");
        } catch (Exception e) {
            return true;
        }
    }

    public boolean isCartContentDisplayed() {
        try {
            WebElement contentEl = getDriver().findElement(cartContent);
            return !contentEl.getAttribute("class").contains("hidden");
        } catch (Exception e) {
            return false;
        }
    }

    public int getCartItemCount() {
        try {
            List<WebElement> rows = getDriver().findElements(cartItemRows);
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public int waitForCartItemCount(int expectedCount) {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(5))
                .until(driver -> getCartItemCount() == expectedCount
                        ? expectedCount
                        : null);
    }

    public boolean waitForEmptyState() {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(5))
                .until(driver -> isCartEmpty() && !isCartContentDisplayed());
    }

    /**
     * Danh sách slug các sản phẩm đang có trong giỏ (thuộc tính {@code data-row}).
     */
    public List<String> getCartItemSlugs() {
        List<String> slugs = new ArrayList<>();
        try {
            for (WebElement row : getDriver().findElements(cartItemRows)) {
                slugs.add(row.getAttribute("data-row"));
            }
        } catch (Exception e) {
            LogUtil.warn("Không đọc được danh sách slug trong giỏ: {}", e.getMessage());
        }
        return slugs;
    }

    public boolean isItemInCart(String slug) {
        return getCartItemSlugs().contains(slug);
    }

    public String getCartCountLabelText() {
        return getText(cartCountLabel);
    }

    /**
     * Số lượng hiển thị trên badge giỏ hàng ở header.
     */
    public int getHeaderCartCount() {
        try {
            String text = getText(headerCartCount);
            return text.isEmpty() ? 0 : Integer.parseInt(text.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    public String getSubtotalText() {
        return getText(cartSubtotal);
    }

    public String getTotalText() {
        return getText(cartTotal);
    }

    public long getSubtotalValue() {
        return parseVnd(getSubtotalText());
    }

    public long getTotalValue() {
        return parseVnd(getTotalText());
    }

    public String getItemName(String slug) {
        return getText(By.cssSelector("[data-row='" + slug + "'] a[href*='/san-pham/']:not([aria-label])"));
    }

    public String getItemLineTotalText(String slug) {
        return getText(By.cssSelector("[data-row='" + slug + "'] p.w-28"));
    }

    public void increaseItemQuantity(String slug) {
        By btnUp = By.cssSelector("[data-row='" + slug + "'] [data-row-up]");
        click(btnUp, "Tăng số lượng sản phẩm: " + slug);
    }

    public void decreaseItemQuantity(String slug) {
        By btnDown = By.cssSelector("[data-row='" + slug + "'] [data-row-down]");
        click(btnDown, "Giảm số lượng sản phẩm: " + slug);
    }

    public String getItemQuantity(String slug) {
        By qtyInput = By.cssSelector("[data-row='" + slug + "'] [data-row-qty]");
        return find(qtyInput).getAttribute("value");
    }

    /**
     * Tăng số lượng {@code times} lần liên tiếp.
     */
    public void increaseItemQuantity(String slug, int times) {
        for (int i = 0; i < times; i++) {
            increaseItemQuantity(slug);
        }
    }

    /**
     * Giảm số lượng {@code times} lần liên tiếp.
     */
    public void decreaseItemQuantity(String slug, int times) {
        for (int i = 0; i < times; i++) {
            decreaseItemQuantity(slug);
        }
    }

    public void removeItem(String slug) {
        By btnRemove = By.cssSelector("[data-row='" + slug + "'] [data-row-remove]");
        click(btnRemove, "Xóa sản phẩm: " + slug);
    }

    /**
     * Xoá toàn bộ giỏ hàng và chấp nhận hộp thoại confirm của trình duyệt.
     *
     * <p>Trang giỏ dùng {@code confirm()} nên phải chờ alert xuất hiện thay vì switchTo ngay
     * (cách làm cũ dễ flaky).</p>
     */
    public void clearCart() {
        click(btnClearAll, "Nút Xóa toàn bộ giỏ hàng");
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(5))
                    .until(ExpectedConditions.alertIsPresent());
            getDriver().switchTo().alert().accept();
            LogUtil.info("Đã chấp nhận hộp thoại xác nhận xoá giỏ hàng.");
            ExtentReportManager.logInfo("Đã xác nhận xoá toàn bộ giỏ hàng");
        } catch (Exception e) {
            LogUtil.warn("Không thấy hộp thoại confirm khi xoá giỏ hàng: {}", e.getMessage());
        }
    }

    public void proceedToCheckout() {
        click(linkCheckout, "Liên kết Tiến hành đặt hàng");
    }

    public boolean isCheckoutLinkDisplayed() {
        return isDisplayed(linkCheckout);
    }

    public boolean isContinueShoppingLinkDisplayed() {
        return isDisplayed(linkContinueShopping);
    }

    /**
     * Chuyển chuỗi tiền tệ kiểu {@code 1.234.567 đ} thành số nguyên VND.
     */
    public static long parseVnd(String text) {
        if (text == null) {
            return -1L;
        }
        String digits = text.replaceAll("[^0-9]", "");
        return digits.isEmpty() ? -1L : Long.parseLong(digits);
    }
}
