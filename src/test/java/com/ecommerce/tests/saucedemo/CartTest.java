package com.ecommerce.tests.saucedemo;

import com.ecommerce.base.BaseTest;
import com.ecommerce.constants.FrameworkConstants;
import com.ecommerce.pages.saucedemo.CartPage;
import com.ecommerce.pages.saucedemo.InventoryPage;
import com.ecommerce.pages.saucedemo.LoginPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class CartTest extends BaseTest {

    private InventoryPage inventoryPage;

    @BeforeMethod(alwaysRun = true)
    public void loginBeforeTest() {
        openSauceDemo();
        LoginPage loginPage = new LoginPage();
        inventoryPage = loginPage.loginAs(FrameworkConstants.SAUCE_STANDARD_USER, FrameworkConstants.SAUCE_PASSWORD);
    }

    @Test(description = "Verify adding single item updates cart badge count", groups = {"smoke", "regression"})
    public void testAddSingleItemToCart() {
        String item = "Sauce Labs Backpack";
        inventoryPage.addProductToCart(item);

        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 1, "Cart badge should display 1");
        ExtentReportManager.logPass("Verified single item added. Cart badge count: 1");
    }

    @Test(description = "Verify adding multiple items updates cart badge count correctly", groups = {"regression"})
    public void testAddMultipleItemsToCart() {
        inventoryPage.addProductToCart("Sauce Labs Backpack")
                     .addProductToCart("Sauce Labs Bike Light")
                     .addProductToCart("Sauce Labs Bolt T-Shirt");

        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 3, "Cart badge should display 3");

        CartPage cartPage = inventoryPage.goToCart();
        Assert.assertTrue(cartPage.isLoaded(), "Cart page should be loaded");
        Assert.assertEquals(cartPage.getItemCount(), 3, "Cart should contain 3 items");

        List<String> itemNames = cartPage.getItemNames();
        Assert.assertTrue(itemNames.contains("Sauce Labs Backpack"), "Cart should contain Backpack");
        Assert.assertTrue(itemNames.contains("Sauce Labs Bike Light"), "Cart should contain Bike Light");
        Assert.assertTrue(itemNames.contains("Sauce Labs Bolt T-Shirt"), "Cart should contain Bolt T-Shirt");

        ExtentReportManager.logPass("Verified multiple items present in Cart page.");
    }

    @Test(description = "Verify removing item from cart", groups = {"regression"})
    public void testRemoveItemFromCart() {
        inventoryPage.addProductToCart("Sauce Labs Backpack")
                     .addProductToCart("Sauce Labs Bike Light");

        CartPage cartPage = inventoryPage.goToCart();
        Assert.assertEquals(cartPage.getItemCount(), 2, "Cart should initially have 2 items");

        cartPage.removeItemByName("Sauce Labs Backpack");
        Assert.assertEquals(cartPage.getItemCount(), 1, "Cart should have 1 item after removal");

        List<String> remainingItems = cartPage.getItemNames();
        Assert.assertFalse(remainingItems.contains("Sauce Labs Backpack"), "Removed item should not be in cart");
        Assert.assertTrue(remainingItems.contains("Sauce Labs Bike Light"), "Non-removed item should remain");

        ExtentReportManager.logPass("Verified item removal from cart successfully.");
    }
}
