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

    @Test(groups = {"regression", "repair", "tracking"}, description = "Tra cứu tiến độ với số điện thoại mẫu")
    public void testSearchBySamplePhone() {
        String samplePhone = "0901234567";
        ExtentReportManager.logInfo("Thực hiện tra cứu theo số điện thoại: " + samplePhone);
        trackingPage.searchByPhone(samplePhone);

        ExtentReportManager.logInfo("Đã thực hiện tra cứu thành công.");
    }
}
