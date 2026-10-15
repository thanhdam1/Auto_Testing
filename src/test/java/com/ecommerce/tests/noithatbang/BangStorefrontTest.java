package com.ecommerce.tests.noithatbang;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithatbang.BangStorefrontPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * UI Test Suite for Website_NoiThat Storefront (Homepage, Cart Drawer, Auth Modal)
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
}
