package com.ecommerce.tests.noithatbang;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithatbang.BangAdminCouponPage;
import com.ecommerce.pages.noithatbang.BangAdminDashboardPage;
import com.ecommerce.pages.noithatbang.BangAdminLoginPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * UI Test Suite for Website_NoiThat Admin Coupon Management (/coupons)
 * Port: 5173
 */
public class BangAdminCouponTest extends BaseTest {

    private BangAdminLoginPage loginPage;
    private BangAdminDashboardPage dashboardPage;
    private BangAdminCouponPage couponPage;

    @BeforeMethod
    public void setupTest() {
        openNoiThatBangAdmin();
        loginPage = new BangAdminLoginPage();
        loginPage.login("admin@noithat.com", "Admin@123");
        dashboardPage = new BangAdminDashboardPage();
        couponPage = dashboardPage.navigateToCoupons();
        Assert.assertTrue(couponPage.isPageLoaded(), "Trang Quản lý Mã giảm giá phải tải thành công");
    }

    @Test(description = "WNT_CPN_001: Trang Mã giảm giá tải đúng, có bảng và nút Tạo mã mới")
    public void testCouponPageLoads() {
        Assert.assertTrue(couponPage.isPageLoaded(), "Trang Quản lý Mã giảm giá phải hiển thị tiêu đề và nút Tạo mới");
        ExtentReportManager.logInfo("Trang Coupon tải thành công với " + couponPage.getCouponCount() + " mã");
    }

    @Test(description = "WNT_CPN_002: Mở và đóng modal Tạo mã mới")
    public void testOpenAndCloseCouponModal() {
        couponPage.openCreateModal();
        Assert.assertTrue(couponPage.isModalOpen(), "Modal tạo mã mới phải mở ra");
        couponPage.closeModal();
        Assert.assertFalse(couponPage.isModalOpen(), "Modal phải đóng khi bấm Hủy");
    }

    @Test(description = "WNT_CPN_003: Thêm mã giảm giá % hợp lệ")
    public void testCreatePercentCoupon() {
        couponPage.openCreateModal();
        String code = "DISCOUNT" + (System.currentTimeMillis() % 10000);
        ExtentReportManager.logInfo("Tạo mã giảm giá phần trăm: " + code);
        couponPage.fillCouponForm(code, "percent", "15", "500000");
        couponPage.submitSave();
        Assert.assertFalse(couponPage.isModalOpen(), "Modal phải đóng lại sau khi lưu");
    }

    @Test(description = "WNT_CPN_004: Thêm mã giảm giá số tiền cố định (fixed)")
    public void testCreateFixedAmountCoupon() {
        couponPage.openCreateModal();
        String code = "FIXED" + (System.currentTimeMillis() % 10000);
        ExtentReportManager.logInfo("Tạo mã giảm giá số tiền cố định: " + code);
        couponPage.fillCouponForm(code, "fixed", "100000", "1000000");
        couponPage.submitSave();
        Assert.assertFalse(couponPage.isModalOpen(), "Modal phải đóng lại sau khi lưu");
    }

    @Test(description = "WNT_CPN_005: Xóa mã giảm giá -> Xác nhận -> Hủy")
    public void testCancelDeleteCouponDialog() {
        if (couponPage.hasCoupons()) {
            couponPage.clickDeleteFirstCoupon();
            Assert.assertTrue(couponPage.isConfirmDeleteDialogDisplayed(), "Dialog Xác nhận xóa mã giảm giá phải hiển thị");
            couponPage.cancelDeleteDialog();
            ExtentReportManager.logInfo("Đã hủy thao tác xóa mã giảm giá");
        } else {
            ExtentReportManager.logInfo("Không có mã giảm giá nào để test xóa");
        }
    }
}
