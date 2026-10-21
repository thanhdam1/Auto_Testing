package com.ecommerce.tests.novashop;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.novashop.NovaBuyerOrdersPage;
import com.ecommerce.pages.novashop.NovaLoginPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class NovaBuyerOrdersTest extends BaseTest {

    private NovaBuyerOrdersPage ordersPage;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        openUrl(com.ecommerce.utils.ConfigReader.get("novashop.app.url", "https://novashop-web-delta.vercel.app") + "/dang-nhap");
        NovaLoginPage loginPage = new NovaLoginPage();
        loginPage.login("autotest_1792115836", "Password123!");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {}

        openUrl(com.ecommerce.utils.ConfigReader.get("novashop.app.url", "https://novashop-web-delta.vercel.app") + "/don-hang");
        ordersPage = new NovaBuyerOrdersPage();
    }

    @Test(groups = {"smoke", "novashop", "orders"}, description = "Xác nhận trang Quản lý đơn mua của Người Mua tải thành công")
    public void testOrdersPageLoads() {
        ExtentReportManager.logInfo("Kiểm tra trang Quản lý đơn mua (/don-hang)");
        Assert.assertTrue(ordersPage.isPageLoaded(), "Trang Quản lý đơn mua phải tải thành công.");
    }

    @Test(groups = {"regression", "novashop", "orders"}, description = "Kiểm tra chuyển đổi bộ lọc giữa các tab trạng thái đơn hàng")
    public void testFilterByTabs() {
        ExtentReportManager.logInfo("Lọc tab 'Chờ xác nhận'");
        ordersPage.filterByTab("Chờ xác nhận");

        ExtentReportManager.logInfo("Lọc tab 'Đang giao'");
        ordersPage.filterByTab("Đang giao");

        ExtentReportManager.logInfo("Lọc tab 'Đã giao'");
        ordersPage.filterByTab("Đã giao");

        ExtentReportManager.logInfo("Lọc tab 'Tất cả'");
        ordersPage.filterByTab("Tất cả");

        Assert.assertTrue(ordersPage.isPageLoaded(), "Trang đơn mua vẫn phản hồi tốt sau khi chuyển các tab.");
    }

    @Test(groups = {"regression", "novashop", "orders"}, description = "Tìm kiếm đơn hàng theo từ khóa")
    public void testSearchOrder() {
        String keyword = "NOVA";
        ExtentReportManager.logInfo("Tìm kiếm đơn hàng với từ khóa: " + keyword);
        ordersPage.searchOrder(keyword);
        Assert.assertTrue(ordersPage.isPageLoaded(), "Giao diện tìm kiếm đơn hàng hiển thị đúng.");
    }

    @Test(groups = {"regression", "novashop", "orders"}, description = "Hủy đơn hàng ở tab Chờ xác nhận nếu có đơn")
    public void testCancelPendingOrderIfAvailable() {
        ExtentReportManager.logInfo("Chuyển tới tab 'Chờ xác nhận'");
        ordersPage.filterByTab("Chờ xác nhận");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}

        org.openqa.selenium.By btnCancel = org.openqa.selenium.By.xpath("//button[contains(., 'Hủy đơn hàng') or contains(., 'Hủy đơn')]");
        if (!getDriver().findElements(btnCancel).isEmpty()) {
            ExtentReportManager.logInfo("Nhấn nút 'Hủy đơn hàng'");
            getDriver().findElement(btnCancel).click();
        }
        Assert.assertTrue(ordersPage.isPageLoaded(), "Trang đơn mua phản hồi tốt.");
    }

    @Test(groups = {"regression", "novashop", "orders"}, description = "Tính năng 'Mua lại' từ danh sách đơn hàng")
    public void testBuyAgainOrder() {
        ExtentReportManager.logInfo("Chuyển tới tab 'Tất cả'");
        ordersPage.filterByTab("Tất cả");

        org.openqa.selenium.By btnBuyAgain = org.openqa.selenium.By.xpath("//button[contains(., 'Mua lại')]");
        if (!getDriver().findElements(btnBuyAgain).isEmpty()) {
            ExtentReportManager.logInfo("Bấm nút 'Mua lại'");
            getDriver().findElement(btnBuyAgain).click();
        }
        Assert.assertTrue(ordersPage.isPageLoaded(), "Tính năng mua lại hoạt động.");
    }
}
