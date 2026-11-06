package com.ecommerce.tests.noithatbang;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithatbang.BangStorefrontPage;
import com.ecommerce.pages.noithatbang.BangStorefrontProductDetailPage;
import com.ecommerce.reports.ExtentReportManager;
import com.ecommerce.utils.WaitUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * UI Test Suite for Website_NoiThat Storefront (Homepage, Cart Drawer, Auth Modal, Catalog)
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangStorefrontTest extends BaseTest {

    private BangStorefrontPage storefrontPage;

    @BeforeMethod
    public void setupTest() {
        openNoiThatBangApp();
        storefrontPage = new BangStorefrontPage();
    }

    @Test(description = "WNT_STF_001: Trang chủ Storefront Góc Nhà Đẹp tải thành công")
    public void testStorefrontHomepageLoads() {
        Assert.assertTrue(storefrontPage.isPageLoaded(), "Trang chủ phải hiển thị thương hiệu GÓC NHÀ ĐẸP");
    }

    @Test(description = "WNT_STF_002: Đóng mở Cart Drawer trượt từ bên phải")
    public void testCartDrawerOpenAndClose() {
        storefrontPage.openCartDrawer();
        Assert.assertTrue(storefrontPage.isCartDrawerOpen(), "Cart Drawer phải mở ra khi bấm icon giỏ hàng");

        storefrontPage.closeCartDrawer();
        Assert.assertFalse(storefrontPage.isCartDrawerOpen(), "Cart Drawer phải đóng lại khi bấm nút X");
    }

    @Test(description = "WNT_STF_003: Đóng mở Auth Modal Đăng nhập / Đăng ký")
    public void testAuthModalOpenAndClose() {
        storefrontPage.openAuthModal();
        Assert.assertTrue(storefrontPage.isAuthModalOpen(), "Auth Modal phải hiển thị khi bấm Đăng nhập");

        storefrontPage.closeAuthModal();
        Assert.assertFalse(storefrontPage.isAuthModalOpen(), "Auth Modal phải đóng lại khi bấm nút X");
    }

    @Test(description = "WNT_STF_004: Chuyển đổi qua lại giữa tab Đăng nhập và Đăng ký trên Auth Modal và xác minh form")
    public void testAuthModalTabSwitching() {
        storefrontPage.openAuthModal();
        Assert.assertTrue(storefrontPage.isAuthModalOpen(), "Auth Modal phải mở ra");

        // Chuyển sang tab Đăng ký và chứng minh form Đăng ký thực sự xuất hiện
        storefrontPage.switchAuthTab("register");
        Assert.assertTrue(storefrontPage.isRegisterFormVisible(), "Form Đăng ký phải xuất hiện khi bấm tab Đăng ký");

        // Chuyển lại tab Đăng nhập và chứng minh form Đăng nhập hiển thị
        storefrontPage.switchAuthTab("login");
        Assert.assertTrue(storefrontPage.isLoginFormVisible(), "Form Đăng nhập phải hiển thị khi bấm tab Đăng nhập");

        storefrontPage.closeAuthModal();
    }

    @Test(description = "WNT_STF_005: Điều hướng từ trang chủ vào trang chi tiết sản phẩm")
    public void testNavigateToProductDetail() {
        BangStorefrontProductDetailPage detailPage = storefrontPage.clickFirstProductCard();
        Assert.assertTrue(detailPage.isPageLoaded(), "Trang chi tiết sản phẩm phải hiển thị tên sản phẩm và nút đặt hàng");
        Assert.assertFalse(detailPage.getProductTitle().isEmpty(), "Tên sản phẩm không được rỗng");
    }

    @Test(description = "WNT_STF_006: Thao tác Giỏ hàng trong Cart Drawer (Tăng số lượng, xóa item và cập nhật rỗng)")
    public void testCartDrawerQuantityAndItemRemoval() {
        openUrl("http://localhost:4321/products/tu-victoria");
        BangStorefrontProductDetailPage detailPage = new BangStorefrontProductDetailPage();
        Assert.assertTrue(detailPage.isPageLoaded(), "Trang chi tiết sản phẩm phải tải thành công");
        detailPage.clickBuyNow();

        Assert.assertTrue(storefrontPage.isCartDrawerOpen(), "Cart Drawer phải mở ra sau khi bấm MUA NGAY");
        WaitUtil.waitUntil(d -> storefrontPage.getCartDrawerItemsCount() > 0, 5);
        Assert.assertTrue(storefrontPage.getCartDrawerItemsCount() > 0, "Cart Drawer phải có ít nhất 1 sản phẩm");

        // Tăng số lượng trong Cart Drawer
        storefrontPage.increaseFirstCartDrawerItem();
        ExtentReportManager.logInfo("Đã bấm tăng số lượng sản phẩm trong Cart Drawer");

        // Xóa sản phẩm khỏi Cart Drawer
        storefrontPage.removeFirstCartDrawerItem();
        ExtentReportManager.logInfo("Đã bấm xóa sản phẩm khỏi Cart Drawer");

        Assert.assertTrue(storefrontPage.isCartDrawerEmpty(), "Sau khi xóa toàn bộ item, giỏ hàng phải hiển thị trạng thái trống");
        storefrontPage.closeCartDrawer();
    }
}
