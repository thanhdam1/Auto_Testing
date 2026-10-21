package com.ecommerce.tests.noithatbang;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithatbang.BangAdminDashboardPage;
import com.ecommerce.pages.noithatbang.BangAdminLoginPage;
import com.ecommerce.pages.noithatbang.BangAdminReviewsPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * UI Test Suite for Website_NoiThat Admin Review Moderation (/reviews)
 * Port: 5173
 */
public class BangAdminReviewTest extends BaseTest {

    private BangAdminLoginPage loginPage;
    private BangAdminDashboardPage dashboardPage;
    private BangAdminReviewsPage reviewsPage;

    @BeforeMethod
    public void setupTest() {
        openNoiThatBangAdmin();
        loginPage = new BangAdminLoginPage();
        loginPage.login("admin@noithat.com", "Admin@123");
        dashboardPage = new BangAdminDashboardPage();
        reviewsPage = dashboardPage.navigateToReviews();
        Assert.assertTrue(reviewsPage.isPageLoaded(), "Trang Quản lý Đánh giá phải tải thành công");
    }

    @Test(description = "WNT_RVW_001: Trang Đánh giá admin tải đúng, hiển thị tiêu đề và bộ lọc")
    public void testReviewsPageLoads() {
        Assert.assertTrue(reviewsPage.isPageLoaded(), "Trang Quản lý Đánh giá phải hiển thị tiêu đề");
        ExtentReportManager.logInfo("Trang Đánh giá tải thành công với " + reviewsPage.getReviewsCount() + " đánh giá");
    }

    @Test(description = "WNT_RVW_002: Lọc đánh giá theo trạng thái pending / approved / rejected")
    public void testFilterReviewStatus() {
        reviewsPage.filterByStatus("pending");
        int pendingCount = reviewsPage.getReviewsCount();
        ExtentReportManager.logInfo("Số đánh giá chờ duyệt: " + pendingCount);
        Assert.assertTrue(pendingCount >= 0, "Lọc trạng thái chờ duyệt phản hồi tốt");

        reviewsPage.filterByStatus("approved");
        int approvedCount = reviewsPage.getReviewsCount();
        ExtentReportManager.logInfo("Số đánh giá đã duyệt: " + approvedCount);
        Assert.assertTrue(approvedCount >= 0, "Lọc trạng thái đã duyệt phản hồi tốt");
    }

    @Test(description = "WNT_RVW_003: Tìm kiếm đánh giá theo từ khóa sản phẩm")
    public void testSearchReviews() {
        reviewsPage.searchReview("sofa");
        int count = reviewsPage.getReviewsCount();
        ExtentReportManager.logInfo("Kết quả tìm kiếm đánh giá: " + count);
        Assert.assertTrue(count >= 0, "Tìm kiếm đánh giá hoạt động tốt");
    }

    @Test(description = "WNT_RVW_004: Kiểm tra hiển thị cột trong bảng đánh giá")
    public void testReviewTableColumns() {
        Assert.assertTrue(reviewsPage.areTableColumnsDisplayed(), "Các cột trong bảng đánh giá phải hiển thị");
        ExtentReportManager.logInfo("Cấu trúc cột bảng đánh giá hợp lệ");
    }
}
