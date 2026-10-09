package com.ecommerce.tests.novashop;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.novashop.NovaHomePage;
import com.ecommerce.pages.novashop.NovaSearchPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class NovaProductSearchTest extends BaseTest {

    private NovaHomePage homePage;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        openNovaShop();
        homePage = new NovaHomePage();
    }

    @Test(groups = {"smoke", "novashop", "search"}, description = "Xác nhận trang chủ ShopNova hiển thị và tìm kiếm sản phẩm")
    public void testHomePageLoadsAndSearch() {
        ExtentReportManager.logInfo("Kiểm tra trang chủ ShopNova");
        Assert.assertTrue(homePage.isPageLoaded(), "Trang chủ ShopNova phải tải thành công.");

        String keyword = "Anker";
        ExtentReportManager.logInfo("Tìm kiếm sản phẩm từ khóa: " + keyword);
        homePage.searchProduct(keyword);

        NovaSearchPage searchPage = new NovaSearchPage();
        Assert.assertTrue(searchPage.isPageLoaded(), "Trang kết quả tìm kiếm phải hiển thị.");
    }

    @Test(groups = {"regression", "novashop", "search"}, description = "Lọc sản phẩm theo ngành hàng trong trang tìm kiếm")
    public void testFilterProductsByCategory() {
        openUrl(com.ecommerce.utils.ConfigReader.get("novashop.app.url", "http://localhost:5173") + "/tim-kiem");
        NovaSearchPage searchPage = new NovaSearchPage();

        int initialCount = searchPage.getProductCount();
        ExtentReportManager.logInfo("Số lượng sản phẩm ban đầu: " + initialCount);

        searchPage.filterByCategory("Điện Thoại");
        ExtentReportManager.logInfo("Đã click lọc ngành hàng Điện Thoại");
    }

    @Test(groups = {"regression", "novashop", "search"}, description = "Sắp xếp sản phẩm theo giá bán trên thanh SortBar")
    public void testSortProducts() {
        openUrl(com.ecommerce.utils.ConfigReader.get("novashop.app.url", "http://localhost:5173") + "/tim-kiem");
        NovaSearchPage searchPage = new NovaSearchPage();

        ExtentReportManager.logInfo("Chọn sắp xếp theo Mới Nhất");
        searchPage.selectSort("Mới Nhất");
        Assert.assertTrue(searchPage.isPageLoaded(), "Danh sách sản phẩm vẫn hiển thị sau khi sắp xếp.");
    }
}
