package com.ecommerce.tests.noithat;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithat.CartPage;
import com.ecommerce.pages.noithat.CheckoutPage;
import com.ecommerce.pages.noithat.ProductDetailPage;
import com.ecommerce.pages.noithat.StorefrontCatalogPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Bộ test Trang chi tiết sản phẩm ({@code /san-pham/{slug}}) - Phase 1.
 *
 * <p>{@link ProductDetailPage} đã tồn tại nhưng chưa từng được dùng trong test nào. Bộ test phủ:
 * hiển thị thông tin, bộ chọn số lượng (kèm biên 1 và max), toast thêm vào giỏ, "Mua ngay" điều hướng
 * sang thanh toán, slug không tồn tại bị chuyển hướng về danh mục.</p>
 */
public class ProductDetailTest extends BaseTest {

    private static final String KNOWN_SLUG = "ghe-cong-thai-hoc-ergo";

    private StorefrontCatalogPage catalogPage;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        openStorefrontPath("/san-pham");
        clearLocalStorage();
        catalogPage = new StorefrontCatalogPage();
    }

    private ProductDetailPage openKnownProduct() {
        ProductDetailPage detailPage = catalogPage.openProductBySlug(KNOWN_SLUG);
        Assert.assertTrue(detailPage.isPageLoaded(), "Trang chi tiết sản phẩm phải tải được cho slug " + KNOWN_SLUG);
        return detailPage;
    }

    // ============================ NT-D01 ============================

    @Test(groups = {"smoke", "regression", "storefront", "product"}, priority = 1,
            description = "NT-D01: Trang chi tiết hiển thị tên, giá, trạng thái kho và slug đúng")
    public void testProductDetailDisplaysNamePriceStockAndSlug() {
        String expectedSlug = catalogPage.getFirstProductSlug();
        ExtentReportManager.logInfo("Mở chi tiết sản phẩm đầu tiên của danh mục: " + expectedSlug);

        ProductDetailPage detailPage = catalogPage.openFirstProduct();
        Assert.assertTrue(detailPage.isPageLoaded(), "Trang chi tiết sản phẩm phải hiển thị tên sản phẩm.");

        Assert.assertEquals(detailPage.getProductSlug(), expectedSlug,
                "Slug trên nút thêm giỏ phải khớp slug của card vừa bấm.");
        Assert.assertFalse(detailPage.getProductName().isBlank(), "Tên sản phẩm không được rỗng.");
        Assert.assertTrue(detailPage.getProductPriceValue() > 0,
                "Giá sản phẩm phải là số dương. Thực tế: " + detailPage.getProductPrice());
        Assert.assertFalse(detailPage.getStockStatusText().isBlank(), "Phải hiển thị trạng thái kho.");
        Assert.assertTrue(detailPage.isAddToCartEnabled() || detailPage.isOutOfStock(),
                "Nút thêm giỏ phải bật khi còn hàng (hoặc trang báo hết hàng).");
    }

    // ============================ NT-D02 ============================

    @Test(groups = {"boundary", "regression", "storefront", "product"}, priority = 2,
            description = "NT-D02: Nút giảm số lượng không cho xuống dưới 1")
    public void testQuantityDecreaseStopsAtOne() {
        ProductDetailPage detailPage = openKnownProduct();

        detailPage.decreaseQuantity(3);

        Assert.assertEquals(detailPage.getCurrentQuantityValue(), 1,
                "Số lượng tối thiểu trên trang chi tiết phải là 1 (Math.max(1, qty-1)).");
    }

    // ============================ NT-D03 ============================

    @Test(groups = {"regression", "storefront", "product"}, priority = 3,
            description = "NT-D03: Nút tăng số lượng cập nhật đúng giá trị ô số lượng")
    public void testQuantityIncreaseUpdatesInputValue() {
        ProductDetailPage detailPage = openKnownProduct();

        detailPage.increaseQuantity(2);

        Assert.assertEquals(detailPage.getCurrentQuantityValue(), 3,
                "Sau khi bấm + 2 lần từ 1, số lượng phải là 3.");
    }

    // ============================ NT-D04 ============================

    @Test(groups = {"smoke", "regression", "storefront", "product", "cart"}, priority = 4,
            description = "NT-D04: Thêm vào giỏ hàng hiển thị toast đúng số lượng và đồng bộ sang trang giỏ")
    public void testAddToCartShowsToastAndSyncsCartPage() {
        ProductDetailPage detailPage = openKnownProduct();
        detailPage.increaseQuantity(2);

        long unitPrice = detailPage.getProductPriceValue();
        String productName = detailPage.getProductName();
        String productSlug = detailPage.getProductSlug();
        ExtentReportManager.logInfo("Thêm 3 x '" + productName + "' vào giỏ hàng");

        detailPage.clickAddToCart();

        String toast = detailPage.waitForCartToast();
        ExtentReportManager.logInfo("Nội dung toast: " + toast);
        Assert.assertTrue(toast.contains("3"), "Toast phải nêu đúng số lượng 3. Thực tế: " + toast);

        openStorefrontPath("/gio-hang");
        CartPage cartPage = new CartPage();
        Assert.assertTrue(cartPage.isPageLoaded(), "Trang giỏ hàng phải tải được.");
        Assert.assertEquals(cartPage.waitForCartItemCount(1), 1, "Giỏ phải có 1 dòng sản phẩm.");
        Assert.assertEquals(cartPage.getItemQuantity(productSlug), "3",
                "Số lượng trong giỏ phải bằng số lượng đã chọn ở trang chi tiết.");
        Assert.assertEquals(cartPage.getSubtotalValue(), unitPrice * 3,
                "Tạm tính trong giỏ phải bằng đơn giá x 3.");
    }

    // ============================ NT-D05 ============================

    @Test(groups = {"regression", "storefront", "product", "checkout"}, priority = 5,
            description = "NT-D05: Nút 'Mua ngay' thêm sản phẩm vào giỏ và điều hướng sang trang thanh toán")
    public void testBuyNowAddsItemAndOpensCheckout() {
        ProductDetailPage detailPage = openKnownProduct();
        long unitPrice = detailPage.getProductPriceValue();

        detailPage.clickBuyNow();

        Assert.assertTrue(detailPage.waitForCheckoutPage(),
                "Nút 'Mua ngay' phải điều hướng sang /thanh-toan.");

        CheckoutPage checkoutPage = new CheckoutPage();
        Assert.assertTrue(checkoutPage.isPageLoaded(), "Trang thanh toán phải hiển thị.");
        Assert.assertEquals(checkoutPage.getCheckoutItemRowCount(), 1,
                "Sản phẩm vừa 'Mua ngay' phải xuất hiện trong tóm tắt đơn hàng.");
        Assert.assertEquals(checkoutPage.getSubtotalValue(), unitPrice,
                "Tạm tính tại trang thanh toán phải bằng đơn giá sản phẩm.");
    }

    // ============================ NT-D06 ============================

    @Test(groups = {"negative", "regression", "storefront", "product"}, priority = 6,
            description = "NT-D06: Slug không tồn tại bị chuyển hướng về trang danh mục sản phẩm")
    public void testUnknownSlugRedirectsToCatalog() {
        ExtentReportManager.logInfo("Mở slug không tồn tại để kiểm tra chuyển hướng");
        catalogPage.openProductBySlug("slug-khong-ton-tai-" + uniqueSuffix());

        Assert.assertTrue(getDriver().getCurrentUrl().endsWith("/san-pham"),
                "Slug không tồn tại phải redirect về /san-pham. URL thực tế: " + getDriver().getCurrentUrl());

        StorefrontCatalogPage redirected = new StorefrontCatalogPage();
        Assert.assertTrue(redirected.isPageLoaded(), "Trang danh mục phải hiển thị sau khi chuyển hướng.");
    }

    // ============================ NT-D07 ============================

    @Test(groups = {"regression", "storefront", "product", "defect"}, priority = 7,
            description = "[DEFECT DA BIET] NT-D07: Nút + vượt quá thuộc tính max (tồn kho) của ô số lượng - "
                    + "test nay du kien FAIL cho toi khi dev sua logic getQty()+1")
    public void testQuantityPlusRespectsMaxAttribute_KnownDefect() {
        ProductDetailPage detailPage = openKnownProduct();
        int maxQuantity = detailPage.getQuantityMaxAttribute();
        ExtentReportManager.logInfo("Thuộc tính max của ô số lượng (tồn kho) = " + maxQuantity);
        Assert.assertTrue(maxQuantity > 1, "Cần sản phẩm có tồn kho > 1 để kiểm tra giá trị biên.");

        // Đặt số lượng đúng bằng max rồi bấm thêm 1 lần
        detailPage.setQuantityViaJs(maxQuantity);
        detailPage.increaseQuantity(1);
        int actual = detailPage.getCurrentQuantityValue();

        Assert.assertTrue(actual <= maxQuantity,
                "[DEFECT] Số lượng vượt quá tồn kho/max: ô số lượng có max=" + maxQuantity
                        + " nhưng sau khi bấm + đã thành " + actual
                        + ". Nguyên nhân: san-pham/[slug].astro dùng inputQty.value = getQty() + 1 "
                        + "mà không kẹp theo thuộc tính max.");
    }
}
