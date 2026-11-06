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
 * Port: 5173 / 1234
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangAdminCouponTest extends BaseTest {

    private BangAdminLoginPage loginPage;
    private BangAdminDashboardPage dashboardPage;
    private BangAdminCouponPage couponPage;

    @BeforeMethod
    public void setupTest() {
        // Đảm bảo luôn có fixture coupon hợp lệ trước khi vào UI
        BangTestFixtures.ensureValidCoupon();

        openNoiThatBangAdmin();
        loginPage = new BangAdminLoginPage();
        loginPage.login("admin@noithat.com", "Admin@123");
        dashboardPage = new BangAdminDashboardPage();
        couponPage = dashboardPage.navigateToCoupons();
        couponPage.waitForDataLoaded();
        Assert.assertTrue(couponPage.isPageLoaded(), "Trang Quản lý Mã giảm giá phải tải thành công");
    }

    @Test(description = "WNT_CPN_001: Trang Mã giảm giá tải đúng, có bảng và nút Tạo mã mới")
    public void testCouponPageLoads() {
        Assert.assertTrue(couponPage.isPageLoaded(), "Trang Quản lý Mã giảm giá phải hiển thị tiêu đề và nút Tạo mới");
        int count = couponPage.getCouponCount();
        ExtentReportManager.logInfo("Trang Coupon tải thành công với " + count + " mã");
        Assert.assertTrue(count > 0, "Bảng mã giảm giá phải có ít nhất 1 mã từ dữ liệu fixture");
    }

    @Test(description = "WNT_CPN_002: Mở và đóng modal Tạo mã mới")
    public void testOpenAndCloseCouponModal() {
        couponPage.openCreateModal();
        Assert.assertTrue(couponPage.isModalOpen(), "Modal tạo mã mới phải mở ra");
        couponPage.closeModal();
        Assert.assertFalse(couponPage.isModalOpen(), "Modal phải đóng khi bấm Hủy");
    }

    @Test(description = "WNT_CPN_003: Thêm mã giảm giá % hợp lệ và xác minh bản ghi hiển thị trong bảng")
    public void testCreatePercentCoupon() {
        couponPage.openCreateModal();
        String code = "PCT" + (System.currentTimeMillis() % 100000);
        ExtentReportManager.logInfo("Tạo mã giảm giá phần trăm: " + code);
        couponPage.fillCouponForm(code, "percent", "15", "500000");
        couponPage.submitSave();
        Assert.assertFalse(couponPage.isModalOpen(), "Modal phải đóng lại sau khi lưu");

        // React Query tự động cập nhật bảng ngay sau khi mutation thành công
        couponPage.waitForDataLoaded();
        Assert.assertTrue(couponPage.isCouponCodePresent(code),
                "Mã giảm giá " + code + " phải tồn tại trong bảng sau khi tạo thành công");
    }

    @Test(description = "WNT_CPN_004: Thêm mã giảm giá số tiền cố định (fixed) và xác minh dữ liệu")
    public void testCreateFixedAmountCoupon() {
        couponPage.openCreateModal();
        String code = "FIX" + (System.currentTimeMillis() % 100000);
        ExtentReportManager.logInfo("Tạo mã giảm giá số tiền cố định: " + code);
        couponPage.fillCouponForm(code, "fixed", "100000", "1000000");
        couponPage.submitSave();
        Assert.assertFalse(couponPage.isModalOpen(), "Modal phải đóng lại sau khi lưu");

        // Tải lại qua sidebar điều hướng SPA (không dùng browser refresh để tránh rớt auth session)
        couponPage = dashboardPage.navigateToCoupons();
        couponPage.waitForDataLoaded();
        Assert.assertTrue(couponPage.isCouponCodePresent(code),
                "Mã giảm giá cố định " + code + " phải tồn tại trong bảng");
    }

    @Test(description = "WNT_CPN_005: Xóa mã giảm giá -> Xác nhận -> Hủy -> Bản ghi vẫn tồn tại")
    public void testCancelDeleteCouponDialog() {
        Assert.assertTrue(couponPage.hasCoupons(), "Bảng mã giảm giá phải có dữ liệu để test hủy xóa");
        int initialCount = couponPage.getCouponCount();

        couponPage.clickDeleteFirstCoupon();
        Assert.assertTrue(couponPage.isConfirmDeleteDialogDisplayed(), "Dialog Xác nhận xóa mã giảm giá phải hiển thị");

        couponPage.cancelDeleteDialog();
        ExtentReportManager.logInfo("Đã hủy thao tác xóa mã giảm giá");

        // Xác minh dialog đã đóng và số lượng bản ghi không đổi
        Assert.assertFalse(couponPage.isConfirmDeleteDialogDisplayed(), "Dialog xác nhận xóa phải biến mất sau khi hủy");
        Assert.assertTrue(couponPage.hasCoupons(), "Bảng mã giảm giá vẫn phải tồn tại");
        Assert.assertEquals(couponPage.getCouponCount(), initialCount, "Số lượng coupon không được giảm sau khi bấm Hủy");
    }
}
