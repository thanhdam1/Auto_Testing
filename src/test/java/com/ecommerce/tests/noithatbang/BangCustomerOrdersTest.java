package com.ecommerce.tests.noithatbang;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithatbang.BangCustomerOrdersPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * UI Test Suite for Customer Orders Page (/orders) on Port 4321
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangCustomerOrdersTest extends BaseTest {

    private BangCustomerOrdersPage ordersPage;

    @BeforeMethod
    public void setupTest() {
        openUrl("http://localhost:4321/orders");
        ordersPage = new BangCustomerOrdersPage();
    }

    @Test(description = "WNT_ORD_001: Màn hình Đơn hàng tải thành công khi chưa đăng nhập")
    public void testCustomerOrdersPageLoads() {
        Assert.assertTrue(ordersPage.isPageLoaded(), "Trang đơn hàng phải tải thành công");
    }

    @Test(description = "WNT_ORD_002: Hiển thị lời nhắc đăng nhập khi truy cập chưa có session")
    public void testNotLoggedInPromptDisplayed() {
        Assert.assertTrue(ordersPage.isNotLoggedInPromptDisplayed(), 
                "Phải hiển thị lời nhắc Đăng Nhập Tài Khoản khi chưa đăng nhập");
    }
}
