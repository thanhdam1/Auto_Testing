package com.ecommerce.tests.repair;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.repair.RepairTrackingPage;
import com.ecommerce.reports.ExtentReportManager;
import com.ecommerce.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RepairTrackingTest extends BaseTest {

    private RepairTrackingPage trackingPage;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        String baseUrl = ConfigReader.get("repair.app.url", "http://localhost:5174");
        openUrl(baseUrl + "/tra-cuu");
        trackingPage = new RepairTrackingPage();
    }

    @Test(groups = {"smoke", "repair", "tracking"}, description = "Xác nhận trang Tra cứu tiến độ hiển thị đúng tiêu đề và ô nhập SĐT")
    public void testTrackingPageLoadsSuccessfully() {
        ExtentReportManager.logInfo("Kiểm tra giao diện trang Tra Cứu Tiến Độ");
        Assert.assertTrue(trackingPage.isPageLoaded(), "Trang Tra Cứu Tiến Độ phải hiển thị tiêu đề hợp lệ.");
    }

    @Test(groups = {"regression", "repair", "tracking"}, description = "Tra cứu tiến độ với số điện thoại mẫu phải trả về phiếu hoặc thông báo không tìm thấy")
    public void testSearchBySamplePhone() {
        String samplePhone = "0901234567";
        ExtentReportManager.logInfo("Thực hiện tra cứu theo số điện thoại: " + samplePhone);
        trackingPage.searchByPhone(samplePhone);

        // F-02: trước đây test này chỉ click rồi log, không hề assert kết quả
        int results = trackingPage.getTicketResultsCount();
        ExtentReportManager.logInfo("Số phiếu tra cứu được: " + results);
        Assert.assertTrue(trackingPage.hasSearchFeedback(),
                "Sau khi tra cứu, hệ thống phải hiển thị danh sách phiếu hoặc thông báo không tìm thấy.");
    }

    @Test(groups = {"regression", "repair", "tracking"}, description = "Tra cứu lặp lại 2 lần không làm nhân đôi kết quả hiển thị")
    public void testRepeatedSearchDoesNotDuplicateResults() {
        String samplePhone = "0901234567";
        trackingPage.searchByPhone(samplePhone);
        int firstResults = trackingPage.getTicketResultsCount();

        trackingPage.searchByPhone(samplePhone);
        int secondResults = trackingPage.getTicketResultsCount();
        ExtentReportManager.logInfo("Kết quả lần 1 = " + firstResults + ", lần 2 = " + secondResults);

        Assert.assertEquals(secondResults, firstResults,
                "Tra cứu lại cùng số điện thoại không được nhân đôi số phiếu hiển thị.");
    }
}
