package com.ecommerce.tests.noithatbang;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithatbang.BangStorefrontPage;
import com.ecommerce.pages.noithatbang.BangStorefrontProductDetailPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Data-Driven Test Class for Storefront Search, Product Detail, Modals & Boundaries (Port 4321)
 */
public class BangStorefrontCatalogSearchTest extends BaseTest {

    private BangStorefrontPage storefrontPage;

    @BeforeMethod
    public void setUp() {
        openNoiThatBangApp();
        storefrontPage = new BangStorefrontPage();
        Assert.assertTrue(storefrontPage.isPageLoaded(), "Trang chủ Storefront phải tải thành công");
    }

    @Test(dataProvider = "searchQueriesData", dataProviderClass = BangStorefrontTestData.class,
          description = "Kiểm tra Tìm kiếm sản phẩm với nhiều dạng dữ liệu (Positive, Negative, Edge Cases, XSS)")
    public void testSearchWithDiverseData(String query, String scenario) {
        ExtentReportManager.logInfo("Thực hiện tìm kiếm với từ khóa: '" + query + "' - Kịch bản: " + scenario);
        storefrontPage.searchProduct(query);

        String currentUrl = currentUrl();
        Assert.assertTrue(currentUrl.contains("/products"), "Trang phải điều hướng sang /products sau khi tìm kiếm");
        ExtentReportManager.logInfo("URL sau tìm kiếm: " + currentUrl);
    }

    @Test(description = "Kiểm tra Quantity Stepper Boundary: Không thể giảm số lượng dưới 1")
    public void testQuantityStepperBoundaryMin() {
        BangStorefrontProductDetailPage detailPage = storefrontPage.clickFirstProductCard();
        Assert.assertTrue(detailPage.isPageLoaded(), "Trang chi tiết sản phẩm phải tải thành công");

        String initialQty = detailPage.getCurrentQuantity();
        Assert.assertEquals(initialQty, "1", "Số lượng mặc định ban đầu phải là 1");

        detailPage.decreaseQuantity(2);
        String qtyAfterDecrease = detailPage.getCurrentQuantity();
        Assert.assertEquals(qtyAfterDecrease, "1", "Số lượng không được giảm dưới 1 khi bấm nút trừ");

        detailPage.increaseQuantity(2);
        String qtyAfterIncrease = detailPage.getCurrentQuantity();
        Assert.assertEquals(qtyAfterIncrease, "3", "Số lượng phải tăng lên 3 sau 2 lần bấm cộng");
    }

    @Test(description = "Kiểm tra Wishlist Heart Toggle và hiển thị trạng thái yêu thích")
    public void testWishlistHeartToggle() {
        BangStorefrontProductDetailPage detailPage = storefrontPage.clickFirstProductCard();
        Assert.assertTrue(detailPage.isPageLoaded(), "Trang chi tiết sản phẩm phải tải thành công");

        detailPage.clickFavorite();
        Assert.assertTrue(detailPage.isFavoriteActive(), "Icon trái tim phải được tô đỏ sau khi bấm Yêu thích");

        detailPage.clickFavorite();
        Assert.assertFalse(detailPage.isFavoriteActive(), "Icon trái tim không được tô đỏ sau khi hủy Yêu thích");
    }

    @Test(description = "Kiểm tra đóng mở các Accordions (Chi tiết, Kích thước, Bảo hành, Giao hàng)")
    public void testAccordionsInteraction() {
        BangStorefrontProductDetailPage detailPage = storefrontPage.clickFirstProductCard();
        Assert.assertTrue(detailPage.isPageLoaded(), "Trang chi tiết sản phẩm phải tải thành công");

        detailPage.toggleAccordionSize();
        Assert.assertTrue(detailPage.isAccordionSizeContentVisible(), "Nội dung Kích thước phải hiển thị sau khi mở");

        detailPage.toggleAccordionWarranty();
        Assert.assertTrue(detailPage.isAccordionWarrantyContentVisible(), "Nội dung Bảo hành phải hiển thị sau khi mở");

        detailPage.toggleAccordionShipping();
        Assert.assertTrue(detailPage.isAccordionShippingContentVisible(), "Nội dung Giao hàng phải hiển thị sau khi mở");
    }

    @Test(description = "Kiểm tra Modal Xem Showroom cửa hàng trưng bày")
    public void testShowroomModal() {
        BangStorefrontProductDetailPage detailPage = storefrontPage.clickFirstProductCard();
        Assert.assertTrue(detailPage.isPageLoaded(), "Trang chi tiết sản phẩm phải tải thành công");

        detailPage.openShowroomModal();
        Assert.assertTrue(detailPage.isShowroomModalOpen(), "Showroom Modal phải hiển thị với danh sách địa chỉ");

        detailPage.closeShowroomModal();
        Assert.assertFalse(detailPage.isShowroomModalOpen(), "Showroom Modal phải đóng lại sau khi click icon X");
    }
}
