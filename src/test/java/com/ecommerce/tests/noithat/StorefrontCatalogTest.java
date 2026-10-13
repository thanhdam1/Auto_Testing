package com.ecommerce.tests.noithat;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithat.StorefrontCatalogPage;
import com.ecommerce.reports.ExtentReportManager;
import com.ecommerce.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class StorefrontCatalogTest extends BaseTest {

    private StorefrontCatalogPage catalogPage;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        String baseUrl = ConfigReader.get("app.url", "https://86h0mht3-4321.asse.devtunnels.ms");
        openUrl(baseUrl + "/san-pham");
        catalogPage = new StorefrontCatalogPage();
    }

    @Test(groups = {"smoke", "storefront", "catalog"}, description = "Xác nhận trang Danh mục sản phẩm hiển thị đầy đủ danh sách hàng hóa")
    public void testCatalogPageLoadsAndDisplaysProducts() {
        ExtentReportManager.logInfo("Kiểm tra trang Danh mục sản phẩm");
        Assert.assertTrue(catalogPage.isPageLoaded(), "Trang Danh mục sản phẩm phải hiển thị tiêu đề hợp lệ.");

        int count = catalogPage.getVisibleProductCount();
        ExtentReportManager.logInfo("Số sản phẩm hiển thị trên trang: " + count);
        Assert.assertTrue(count > 0, "Trang danh mục phải hiển thị danh sách sản phẩm.");
    }

    @Test(groups = {"regression", "storefront", "catalog"}, description = "Lọc sản phẩm theo danh mục và hoàn tác bộ lọc")
    public void testFilterCatalogByCategoryAndReset() {
        int initialCount = catalogPage.getVisibleProductCount();
        ExtentReportManager.logInfo("Số lượng ban đầu: " + initialCount);

        ExtentReportManager.logInfo("Áp dụng bộ lọc danh mục: 'sofa'");
        catalogPage.filterByCategory("sofa");

        int filteredCount = catalogPage.waitForVisibleProductsInCategory("sofa");
        ExtentReportManager.logInfo("Số lượng sản phẩm Sofa: " + filteredCount);
        Assert.assertTrue(filteredCount > 0, "Phải có ít nhất 1 sản phẩm thuộc danh mục sofa.");
        Assert.assertTrue(catalogPage.getVisibleProductCategories().stream()
                        .allMatch("sofa"::equals),
                "Mọi card hiển thị phải thuộc danh mục sofa.");

        ExtentReportManager.logInfo("Xóa bộ lọc để trở về danh sách ban đầu");
        catalogPage.resetAllFilters();
        int resetCount = catalogPage.waitForVisibleProductCount(initialCount);
        Assert.assertEquals(resetCount, initialCount, "Sau khi xóa lọc, số sản phẩm hiển thị phải trở về ban đầu.");
    }

    @Test(groups = {"regression", "storefront", "catalog"}, description = "Sắp xếp sản phẩm theo giá hoặc tên")
    public void testSortProducts() {
        ExtentReportManager.logInfo("Sắp xếp sản phẩm theo giá tăng dần: price-asc");
        catalogPage.selectSortOption("price-asc");

        int count = catalogPage.getVisibleProductCount();
        Assert.assertTrue(count > 0, "Danh sách vẫn phải hiển thị sau khi sắp xếp.");
    }
}
