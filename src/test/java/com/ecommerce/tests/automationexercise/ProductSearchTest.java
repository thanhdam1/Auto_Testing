package com.ecommerce.tests.automationexercise;

import com.ecommerce.base.BaseTest;
import com.ecommerce.dataprovider.TestDataProviders;
import com.ecommerce.pages.automationexercise.AECartPage;
import com.ecommerce.pages.automationexercise.AEHomePage;
import com.ecommerce.pages.automationexercise.AEProductsPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class ProductSearchTest extends BaseTest {

    @Test(dataProvider = "searchKeywords", dataProviderClass = TestDataProviders.class,
            description = "Verify product search functionality by keyword", groups = {"regression"})
    public void testSearchProduct(String keyword) {
        openAutomationExercise();
        AEHomePage homePage = new AEHomePage();

        AEProductsPage productsPage = homePage.navigateToProducts();
        Assert.assertTrue(productsPage.isLoaded(), "Products page should be loaded");

        productsPage.searchProduct(keyword);
        Assert.assertTrue(productsPage.getProductCount() > 0, "Search results should not be empty for keyword: " + keyword);

        List<String> titles = productsPage.getProductTitles();
        boolean matchedAny = titles.stream().anyMatch(t -> t.toLowerCase().contains(keyword.toLowerCase()));
        Assert.assertTrue(matchedAny, "At least one product title should contain keyword: " + keyword);

        ExtentReportManager.logPass("Verified search for keyword '" + keyword + "' returned " + titles.size() + " items.");
    }

    @Test(description = "Verify category filtering: Women > Dress", groups = {"regression"})
    public void testCategoryFilter() {
        openAutomationExercise();
        AEHomePage homePage = new AEHomePage();

        AEProductsPage productsPage = homePage.selectWomenDressCategory();
        Assert.assertTrue(productsPage.isLoaded(), "Category page should be loaded");
        Assert.assertTrue(productsPage.getPageHeaderTitle().toLowerCase().contains("women"),
                "Header should contain category name");

        ExtentReportManager.logPass("Verified category filtering for Women - Dress.");
    }

    @Test(description = "Verify adding product to cart and viewing cart in Automation Exercise", groups = {"regression"})
    public void testAddProductToCartAndVerify() {
        openAutomationExercise();
        AEHomePage homePage = new AEHomePage();

        AEProductsPage productsPage = homePage.navigateToProducts();
        productsPage.addFirstProductToCart();

        AECartPage cartPage = productsPage.clickViewCartOnModal();
        Assert.assertTrue(cartPage.isLoaded(), "Cart page should be loaded");
        Assert.assertTrue(cartPage.getCartItemCount() >= 1, "Cart should contain at least 1 item");

        ExtentReportManager.logPass("Verified item successfully added to cart and displayed in Cart page.");
    }
}
