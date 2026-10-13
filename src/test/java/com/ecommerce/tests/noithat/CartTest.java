package com.ecommerce.tests.noithat;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithat.CartPage;
import com.ecommerce.pages.noithat.StorefrontCatalogPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Bộ test Giỏ hàng Nội Thất ({@code /gio-hang}) - Phase 1.
 *
 * <p>Trước đây {@link CartPage} đã được viết nhưng KHÔNG có test nào sử dụng (xem
 * TEST_PLAN_BO_SUNG_3_DU_AN.md - mục 4.1). Bộ test này phủ trạng thái rỗng, thêm/gộp sản phẩm,
 * tăng giảm số lượng kèm giá trị biên 1..99, xoá từng món, xoá toàn bộ, toán học tổng tiền,
 * độ bền vững qua reload và xử lý dữ liệu localStorage hỏng.</p>
 */
public class CartTest extends BaseTest {

    // Dữ liệu đối chiếu từ bộ fixture sản phẩm của ứng dụng Nội Thất (giá VND)
    private static final String SLUG_SOFA = "sofa-da-y-milano";
    private static final String NAME_SOFA = "Sofa da Ý Milano";
    private static final long PRICE_SOFA = 58_900_000L;

    private static final String SLUG_DEN = "den-ban-gom";
    private static final String NAME_DEN = "Đèn bàn gốm";
    private static final long PRICE_DEN = 1_900_000L;

    private static final String SLUG_GHE = "ghe-cong-thai-hoc-ergo";
    private static final String NAME_GHE = "Ghế công thái học Ergo";
    private static final long PRICE_GHE = 6_900_000L;

    private static final int MAX_QUANTITY = 99;

    private CartPage cartPage;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        // Mở storefront trước để có đúng origin cho localStorage, rồi dọn giỏ hàng cũ
        openStorefrontPath("/san-pham");
        clearLocalStorage();
        cartPage = new CartPage();
    }

    private void gotoCart() {
        openStorefrontPath("/gio-hang");
        cartPage = new CartPage();
        Assert.assertTrue(cartPage.isPageLoaded(), "Trang giỏ hàng phải tải được.");
    }

    private void seedSingle(String slug, String name, long price, int qty) {
        seedCartWithSingleItem(slug, name, price, qty);
    }

    // ============================ NT-C01 ============================

    @Test(groups = {"smoke", "regression", "storefront", "cart"}, priority = 1,
            description = "NT-C01: Giỏ hàng trống hiển thị đúng trạng thái rỗng và tổng tiền 0")
    public void testEmptyCartStateOnFirstVisit() {
        ExtentReportManager.logInfo("Kiểm tra trạng thái giỏ hàng khi chưa có sản phẩm");
        gotoCart();

        Assert.assertTrue(cartPage.isCartEmpty(), "Giỏ hàng mới phải ở trạng thái rỗng.");
        Assert.assertFalse(cartPage.isCartContentDisplayed(), "Khối nội dung giỏ hàng phải bị ẩn khi rỗng.");
        Assert.assertEquals(cartPage.getCartItemCount(), 0, "Không được có dòng sản phẩm nào.");
        Assert.assertEquals(readStoredCart(), "[]", "localStorage phải lưu mảng rỗng.");
    }

    // ============================ NT-C02 ============================

    @Test(groups = {"smoke", "regression", "storefront", "cart"}, priority = 2,
            description = "NT-C02: Nạp 1 sản phẩm vào giỏ thì hiển thị 1 dòng với đúng tên, số lượng và tạm tính")
    public void testSingleItemCartShowsRowNameQuantityAndSubtotal() {
        seedSingle(SLUG_SOFA, NAME_SOFA, PRICE_SOFA, 1);
        gotoCart();

        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Giỏ phải có đúng 1 dòng sản phẩm.");
        Assert.assertTrue(cartPage.isItemInCart(SLUG_SOFA), "Giỏ phải chứa sản phẩm " + SLUG_SOFA);
        Assert.assertTrue(cartPage.getItemName(SLUG_SOFA).contains(NAME_SOFA),
                "Tên sản phẩm trong giỏ không khớp. Thực tế: " + cartPage.getItemName(SLUG_SOFA));
        Assert.assertEquals(cartPage.getItemQuantity(SLUG_SOFA), "1", "Số lượng ban đầu phải là 1.");
        Assert.assertEquals(cartPage.getSubtotalValue(), PRICE_SOFA,
                "Tạm tính phải bằng đơn giá khi số lượng = 1.");
        Assert.assertFalse(cartPage.isCartEmpty(), "Giỏ hàng không còn rỗng sau khi thêm sản phẩm.");
        Assert.assertTrue(cartPage.isCartContentDisplayed(), "Khối nội dung giỏ hàng phải hiển thị.");
    }

    // ============================ NT-C03 ============================

    @Test(groups = {"regression", "storefront", "cart"}, priority = 3,
            description = "NT-C03: Thêm cùng một sản phẩm 2 lần từ danh mục thì gộp thành 1 dòng với số lượng 2")
    public void testAddingSameProductTwiceMergesIntoOneRow() {
        StorefrontCatalogPage catalogPage = new StorefrontCatalogPage();
        String slug = catalogPage.getFirstProductSlug();
        ExtentReportManager.logInfo("Sản phẩm dùng để kiểm tra gộp dòng: " + slug);

        catalogPage.quickAddFirstProduct();
        catalogPage.quickAddFirstProduct();

        gotoCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 1,
                "Thêm trùng slug phải gộp vào 1 dòng, không tạo dòng mới. Giỏ hiện có: " + cartPage.getCartItemSlugs());
        Assert.assertEquals(cartPage.getItemQuantity(slug), "2", "Số lượng phải được cộng dồn thành 2.");
        Assert.assertEquals(cartPage.getHeaderCartCount(), 2, "Badge giỏ hàng ở header phải hiển thị 2.");
    }

    // ============================ NT-C04 ============================

    @Test(groups = {"regression", "storefront", "cart"}, priority = 4,
            description = "NT-C04: Nút +/- trong giỏ cập nhật đúng số lượng và tạm tính theo từng bước")
    public void testIncreaseDecreaseQuantityUpdatesValueAndSubtotal() {
        seedSingle(SLUG_GHE, NAME_GHE, PRICE_GHE, 1);
        gotoCart();

        cartPage.increaseItemQuantity(SLUG_GHE, 2);
        Assert.assertEquals(cartPage.getItemQuantity(SLUG_GHE), "3", "Sau khi bấm + 2 lần, số lượng phải là 3.");
        Assert.assertEquals(cartPage.getSubtotalValue(), PRICE_GHE * 3,
                "Tạm tính phải bằng đơn giá x 3.");

        cartPage.decreaseItemQuantity(SLUG_GHE);
        Assert.assertEquals(cartPage.getItemQuantity(SLUG_GHE), "2", "Sau khi bấm - 1 lần, số lượng phải là 2.");
        Assert.assertEquals(cartPage.getSubtotalValue(), PRICE_GHE * 2,
                "Tạm tính phải cập nhật lại theo số lượng mới.");
    }

    // ============================ NT-C05 ============================

    @Test(groups = {"boundary", "regression", "storefront", "cart"}, priority = 5,
            description = "NT-C05: Số lượng không thể giảm xuống dưới 1 (biên dưới)")
    public void testQuantityCannotGoBelowOne() {
        seedSingle(SLUG_DEN, NAME_DEN, PRICE_DEN, 1);
        gotoCart();

        cartPage.decreaseItemQuantity(SLUG_DEN, 3);

        Assert.assertEquals(cartPage.getItemQuantity(SLUG_DEN), "1",
                "Số lượng tối thiểu phải là 1 (logic Math.max(1, qty-1) trong gio-hang/index.astro).");
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Dòng sản phẩm vẫn phải tồn tại.");
    }

    // ============================ NT-C06 ============================

    @Test(groups = {"boundary", "regression", "storefront", "cart"}, priority = 6,
            description = "NT-C06: Số lượng bị kẹp ở mức tối đa 99 (biên trên theo scripts/cart.ts)")
    public void testQuantityIsCappedAtMaxNinetyNine() {
        seedSingle(SLUG_DEN, NAME_DEN, PRICE_DEN, MAX_QUANTITY - 1);
        gotoCart();

        cartPage.increaseItemQuantity(SLUG_DEN, 5);

        Assert.assertEquals(cartPage.getItemQuantity(SLUG_DEN), String.valueOf(MAX_QUANTITY),
                "Số lượng phải bị kẹp ở 99 (Math.min(99, ...) trong scripts/cart.ts).");
        Assert.assertEquals(cartPage.getSubtotalValue(), PRICE_DEN * MAX_QUANTITY,
                "Tạm tính tại biên trên phải khớp đơn giá x 99.");
    }

    // ============================ NT-C07 ============================

    @Test(groups = {"regression", "storefront", "cart"}, priority = 7,
            description = "NT-C07: Xoá một sản phẩm khỏi giỏ thì giỏ trở về trạng thái rỗng")
    public void testRemoveItemEmptiesTheCart() {
        seedSingle(SLUG_DEN, NAME_DEN, PRICE_DEN, 2);
        gotoCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Điều kiện đầu: giỏ phải có 1 dòng.");

        cartPage.removeItem(SLUG_DEN);

        Assert.assertEquals(cartPage.waitForCartItemCount(0), 0, "Sau khi xoá, giỏ không còn dòng nào.");
        Assert.assertTrue(cartPage.waitForEmptyState(), "Giỏ phải hiển thị trạng thái rỗng.");
        Assert.assertEquals(readStoredCart(), "[]", "localStorage phải lưu mảng rỗng.");
    }

    // ============================ NT-C08 ============================

    @Test(groups = {"regression", "storefront", "cart"}, priority = 8,
            description = "NT-C08: Nút xoá toàn bộ (kèm hộp thoại xác nhận) làm rỗng giỏ hàng")
    public void testClearAllCartWithConfirmationDialog() {
        seedCart(new String[][]{
                {SLUG_SOFA, NAME_SOFA, String.valueOf(PRICE_SOFA), "1"},
                {SLUG_DEN, NAME_DEN, String.valueOf(PRICE_DEN), "2"}
        });
        gotoCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Điều kiện đầu: giỏ phải có 2 dòng.");

        cartPage.clearCart();

        Assert.assertEquals(cartPage.getCartItemCount(), 0, "Sau khi xác nhận xoá, giỏ phải rỗng.");
        Assert.assertTrue(cartPage.isCartEmpty(), "Giỏ phải hiển thị trạng thái rỗng.");
        Assert.assertEquals(readStoredCart(), "[]", "localStorage phải được ghi mảng rỗng.");
    }

    // ============================ NT-C09 ============================

    @Test(groups = {"data", "regression", "storefront", "cart"}, priority = 9,
            description = "NT-C09: Toán học giỏ hàng - tạm tính = tổng (đơn giá x số lượng) và tổng tiền bằng tạm tính")
    public void testSubtotalMathWithMultipleItems() {
        seedCart(new String[][]{
                {SLUG_SOFA, NAME_SOFA, String.valueOf(PRICE_SOFA), "2"},
                {SLUG_DEN, NAME_DEN, String.valueOf(PRICE_DEN), "1"},
                {SLUG_GHE, NAME_GHE, String.valueOf(PRICE_GHE), "3"}
        });
        gotoCart();

        long expectedSubtotal = PRICE_SOFA * 2 + PRICE_DEN + PRICE_GHE * 3;
        ExtentReportManager.logInfo("Tạm tính mong đợi: " + expectedSubtotal + " - thực tế: " + cartPage.getSubtotalValue());

        Assert.assertEquals(cartPage.getCartItemCount(), 3, "Giỏ phải có 3 dòng sản phẩm.");
        Assert.assertEquals(cartPage.getSubtotalValue(), expectedSubtotal,
                "Tạm tính phải bằng tổng (đơn giá x số lượng) của từng dòng.");
        Assert.assertEquals(cartPage.getTotalValue(), expectedSubtotal,
                "Trang giỏ hàng chưa cộng phí ship nên tổng tiền phải bằng tạm tính.");
    }

    // ============================ NT-C10 ============================

    @Test(groups = {"data", "regression", "storefront", "cart"}, priority = 10,
            description = "NT-C10: Nhãn đếm sản phẩm (data-cart-count-label) bằng tổng số lượng, không phải số dòng")
    public void testCartCountLabelEqualsTotalQuantity() {
        seedCart(new String[][]{
                {SLUG_SOFA, NAME_SOFA, String.valueOf(PRICE_SOFA), "2"},
                {SLUG_GHE, NAME_GHE, String.valueOf(PRICE_GHE), "3"}
        });
        gotoCart();

        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Có 2 dòng sản phẩm.");
        Assert.assertEquals(cartPage.getCartCountLabelText(), "5",
                "Nhãn đếm phải cộng dồn số lượng (2 + 3 = 5), không phải đếm số dòng.");
    }

    // ============================ NT-C11 ============================

    @Test(groups = {"regression", "storefront", "cart"}, priority = 11,
            description = "NT-C11: Giỏ hàng bền vững qua reload (lưu ở localStorage key noithat_cart)")
    public void testCartPersistsAfterPageReload() {
        seedSingle(SLUG_GHE, NAME_GHE, PRICE_GHE, 2);
        gotoCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Điều kiện đầu: giỏ có 1 dòng.");

        getDriver().navigate().refresh();
        cartPage = new CartPage();

        Assert.assertTrue(cartPage.isPageLoaded(), "Trang giỏ hàng phải tải lại được.");
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Giỏ hàng phải được giữ sau khi tải lại trang.");
        Assert.assertEquals(cartPage.getItemQuantity(SLUG_GHE), "2", "Số lượng phải được giữ nguyên.");
        Assert.assertEquals(cartPage.getSubtotalValue(), PRICE_GHE * 2, "Tạm tính phải được tính lại đúng.");
    }

    // ============================ NT-C12 ============================

    @Test(groups = {"negative", "regression", "storefront", "cart"}, priority = 12,
            description = "NT-C12: Dữ liệu localStorage hỏng không làm vỡ trang giỏ hàng")
    public void testCorruptedCartDataIsHandledGracefully() {
        seedRawCart("{day-khong-phai-json");
        gotoCart();

        Assert.assertTrue(cartPage.isPageLoaded(), "Trang giỏ hàng vẫn phải tải được với dữ liệu hỏng.");
        Assert.assertTrue(cartPage.isCartEmpty(), "Giỏ phải được coi như rỗng khi dữ liệu không đọc được.");
        Assert.assertEquals(cartPage.getCartItemCount(), 0, "Không được render dòng sản phẩm rác.");
    }

    // ============================ NT-C13 ============================

    @Test(groups = {"regression", "storefront", "cart"}, priority = 13,
            description = "NT-C13: Badge giỏ hàng trên header đồng bộ giữa trang danh mục và trang giỏ hàng")
    public void testHeaderCartBadgeSyncsAcrossPages() {
        seedCart(new String[][]{
                {SLUG_SOFA, NAME_SOFA, String.valueOf(PRICE_SOFA), "1"},
                {SLUG_DEN, NAME_DEN, String.valueOf(PRICE_DEN), "4"}
        });
        gotoCart();
        Assert.assertEquals(cartPage.getHeaderCartCount(), 5, "Badge trên trang giỏ hàng phải là 5.");

        openStorefrontPath("/san-pham");
        CartPage catalogView = new CartPage();

        Assert.assertEquals(catalogView.getHeaderCartCount(), 5,
                "Badge trên trang danh mục cũng phải là 5 (đồng bộ qua sự kiện cart:updated).");
    }
}
