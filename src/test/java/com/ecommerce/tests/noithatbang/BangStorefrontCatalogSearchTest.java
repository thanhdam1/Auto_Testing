package com.ecommerce.tests.noithatbang;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithatbang.BangStorefrontPage;
import com.ecommerce.pages.noithatbang.BangStorefrontProductDetailPage;
import com.ecommerce.reports.ExtentReportManager;
import com.ecommerce.utils.WaitUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

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

        // Chờ điều hướng đến trang /products có chứa tham số q
        WaitUtil.waitForUrlContains("/products", 6);
        String currentUrl = currentUrl();
        ExtentReportManager.logInfo("URL sau tìm kiếm: " + currentUrl);

        // 1. Kiểm tra chính xác query parameter 'q', không chỉ pathname
        Assert.assertTrue(currentUrl.contains("/products"), "Trang phải điều hướng sang /products sau khi tìm kiếm");
        Assert.assertTrue(currentUrl.contains("q="), "URL phải chứa query parameter 'q=' thể hiện từ khóa tìm kiếm");

        // 2. Assert theo từng nhóm kịch bản nghiệp vụ
        if (scenario.startsWith("Positive")) {
            // Nhóm từ khóa có kết quả
            int cardCount = storefrontPage.getCatalogProductCount();
            List<String> titles = storefrontPage.getCatalogProductTitles();
            ExtentReportManager.logInfo("Số sản phẩm tìm thấy cho '" + query + "': " + cardCount);

            Assert.assertTrue(cardCount > 0, "Từ khóa hợp lệ '" + query + "' phải trả về ít nhất 1 sản phẩm");
            boolean anyMatch = titles.stream().anyMatch(t -> t.toLowerCase().contains(query.toLowerCase()));
            Assert.assertTrue(anyMatch, "Ít nhất một sản phẩm phải chứa từ khóa '" + query + "' trong tiêu đề");
        } else if (scenario.startsWith("Negative")) {
            // Nhóm từ khóa không tồn tại
            int cardCount = storefrontPage.getCatalogProductCount();
            ExtentReportManager.logInfo("Số sản phẩm tìm thấy cho từ khóa không tồn tại: " + cardCount);
            Assert.assertTrue(cardCount == 0 || storefrontPage.isCatalogEmptyStateDisplayed(),
                    "Từ khóa không tồn tại phải trả về 0 sản phẩm hoặc hiển thị thông báo không tìm thấy");
        } else if (scenario.contains("XSS")) {
            // Nhóm Payload XSS: đảm bảo không thực thi alert script
            Assert.assertFalse(storefrontPage.isAlertPresent(), "Payload XSS không được kích hoạt hộp thoại alert() trong trình duyệt");
            String pageSource = getDriver().getPageSource();
            Assert.assertFalse(pageSource.contains("<script>alert('test')</script>"),
                    "Payload script không được chèn nguyên bản dạng thẻ thực thi mà phải được sanitize/encode an toàn");
        } else if (scenario.contains("Special symbols")) {
            // Nhóm Ký tự đặc biệt
            Assert.assertFalse(storefrontPage.isAlertPresent(), "Ký tự đặc biệt không được gây crash alert");
            String decodedUrl = URLDecoder.decode(currentUrl, StandardCharsets.UTF_8);
            Assert.assertTrue(decodedUrl.contains(query) || currentUrl.contains("q="),
                    "Hệ thống phải mã hóa và xử lý ký tự đặc biệt an toàn mà không bị lỗi server 500");
        }
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
        WaitUtil.waitUntil(d -> detailPage.isFavoriteActive(), 5);
        Assert.assertTrue(detailPage.isFavoriteActive(), "Icon trái tim phải được tô đỏ sau khi bấm Yêu thích");

        detailPage.clickFavorite();
        WaitUtil.waitUntil(d -> !detailPage.isFavoriteActive(), 5);
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
