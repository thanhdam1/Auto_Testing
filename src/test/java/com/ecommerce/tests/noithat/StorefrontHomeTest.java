package com.ecommerce.tests.noithat;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithat.PhongThuyPage;
import com.ecommerce.pages.noithat.StorefrontCatalogPage;
import com.ecommerce.pages.noithat.StorefrontHomePage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class StorefrontHomeTest extends BaseTest {

    private StorefrontHomePage homePage;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        openStorefront();
        homePage = new StorefrontHomePage();
    }

    @Test(groups = {"smoke", "storefront", "home"}, description = "Trang chủ tải thành công với khối Mua theo không gian")
    public void testHomePageLoadsSuccessfully() {
        ExtentReportManager.logInfo("Kiểm tra trang chủ tải hoàn tất");
        Assert.assertTrue(homePage.isPageLoaded(), "Trang chủ phải tải thành công.");
    }

    @Test(groups = {"regression", "storefront", "home"}, description = "Khối Mua theo không gian hiển thị danh sách các không gian nội thất")
    public void testSpaceCategoriesDisplay() {
        int spaceCount = homePage.getSpaceCardCount();
        ExtentReportManager.logInfo("Số không gian nội thất hiển thị: " + spaceCount);
        Assert.assertTrue(spaceCount >= 1, "Phải có ít nhất 1 thẻ không gian nội thất hiển thị từ Banner API. Thực tế: " + spaceCount);
    }

    @Test(groups = {"regression", "storefront", "home"}, description = "Nút Chatbot AI Phong Thủy nổi hiển thị trên trang chủ")
    public void testFengShuiChatbotButtonPresent() {
        Assert.assertTrue(homePage.isFengShuiChatButtonDisplayed(), "Nút AI Phong Thủy nổi phải hiển thị trên giao diện.");
    }

    @Test(groups = {"regression", "storefront", "home"}, description = "Điều hướng từ menu Trang chủ sang trang Danh mục Sản phẩm")
    public void testNavigateToCatalogFromMenu() {
        StorefrontCatalogPage catalogPage = homePage.navigateToCatalog();
        Assert.assertTrue(catalogPage.isPageLoaded(), "Phải điều hướng thành công sang trang Danh mục sản phẩm.");
    }
}
