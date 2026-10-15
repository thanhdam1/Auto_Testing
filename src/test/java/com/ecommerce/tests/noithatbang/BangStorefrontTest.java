package com.ecommerce.tests.noithatbang;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithatbang.BangStorefrontPage;
import com.ecommerce.pages.noithatbang.BangStorefrontProductDetailPage;
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

    @Test(description = "WNT_STF_004: Chuyển đổi qua lại giữa tab Đăng nhập và Đăng ký trên Auth Modal")
    public void testAuthModalTabSwitching() {
        storefrontPage.openAuthModal();
        Assert.assertTrue(storefrontPage.isAuthModalOpen(), "Auth Modal phải mở ra");

        storefrontPage.switchAuthTab("register");
        // Kiểm tra chuyển sang tab Đăng ký
        storefrontPage.switchAuthTab("login");
        // Kiểm tra chuyển lại tab Đăng nhập
        storefrontPage.closeAuthModal();
    }

    @Test(description = "WNT_STF_005: Điều hướng từ trang chủ vào trang chi tiết sản phẩm")
    public void testNavigateToProductDetail() {
        BangStorefrontProductDetailPage detailPage = storefrontPage.clickFirstProductCard();
        Assert.assertTrue(detailPage.isPageLoaded(), "Trang chi tiết sản phẩm phải hiển thị tên sản phẩm và nút đặt hàng");
        Assert.assertFalse(detailPage.getProductTitle().isEmpty(), "Tên sản phẩm không được rỗng");
    }
}
