package com.ecommerce.tests.saucedemo;

import com.ecommerce.base.BaseTest;
import com.ecommerce.constants.FrameworkConstants;
import com.ecommerce.pages.saucedemo.InventoryPage;
import com.ecommerce.pages.saucedemo.LoginPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductCatalogTest extends BaseTest {

    private InventoryPage inventoryPage;

    @BeforeMethod(alwaysRun = true)
    public void loginBeforeTest() {
        openSauceDemo();
        LoginPage loginPage = new LoginPage();
        inventoryPage = loginPage.loginAs(FrameworkConstants.SAUCE_STANDARD_USER, FrameworkConstants.SAUCE_PASSWORD);
    }

    @Test(description = "Verify product catalog displays items", groups = {"smoke", "regression"})
    public void testCatalogDisplay() {
        int productCount = inventoryPage.getProductCount();
        Assert.assertTrue(productCount > 0, "Product count should be greater than zero");
        ExtentReportManager.logPass("Catalog displays " + productCount + " products.");
    }

    @Test(description = "Verify sorting products by Name: A to Z", groups = {"regression"})
    public void testSortByNameAscending() {
        inventoryPage.selectSortOption("Name (A to Z)");
        List<String> actualTitles = inventoryPage.getAllProductTitles();

        List<String> expectedTitles = new ArrayList<>(actualTitles);
        Collections.sort(expectedTitles);

        Assert.assertEquals(actualTitles, expectedTitles, "Products should be sorted alphabetically A to Z");
        ExtentReportManager.logPass("Verified sorting by Name A to Z.");
    }

    @Test(description = "Verify sorting products by Name: Z to A", groups = {"regression"})
    public void testSortByNameDescending() {
        inventoryPage.selectSortOption("Name (Z to A)");
        List<String> actualTitles = inventoryPage.getAllProductTitles();

        List<String> expectedTitles = new ArrayList<>(actualTitles);
        expectedTitles.sort(Collections.reverseOrder());

        Assert.assertEquals(actualTitles, expectedTitles, "Products should be sorted reverse alphabetically Z to A");
        ExtentReportManager.logPass("Verified sorting by Name Z to A.");
    }

    @Test(description = "Verify sorting products by Price: Low to High", groups = {"regression"})
    public void testSortByPriceLowToHigh() {
        inventoryPage.selectSortOption("Price (low to high)");
        List<Double> actualPrices = inventoryPage.getAllProductPrices();

        List<Double> expectedPrices = new ArrayList<>(actualPrices);
        Collections.sort(expectedPrices);

        Assert.assertEquals(actualPrices, expectedPrices, "Products should be sorted from lowest price to highest");
        ExtentReportManager.logPass("Verified sorting by Price Low to High: " + actualPrices);
    }

    @Test(description = "Verify sorting products by Price: High to Low", groups = {"regression"})
    public void testSortByPriceHighToLow() {
        inventoryPage.selectSortOption("Price (high to low)");
        List<Double> actualPrices = inventoryPage.getAllProductPrices();

        List<Double> expectedPrices = new ArrayList<>(actualPrices);
        expectedPrices.sort(Collections.reverseOrder());

        Assert.assertEquals(actualPrices, expectedPrices, "Products should be sorted from highest price to lowest");
        ExtentReportManager.logPass("Verified sorting by Price High to Low: " + actualPrices);
    }
}
