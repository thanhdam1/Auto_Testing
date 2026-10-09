package com.ecommerce.tests.saucedemo;

import com.ecommerce.base.BaseTest;
import com.ecommerce.constants.FrameworkConstants;
import com.ecommerce.pages.saucedemo.*;
import com.ecommerce.reports.ExtentReportManager;
import com.ecommerce.utils.DataFakerUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CheckoutE2ETest extends BaseTest {

    private LoginPage loginPage;
    private InventoryPage inventoryPage;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        openSauceDemo();
        loginPage = new LoginPage();
        inventoryPage = loginPage.loginAs(FrameworkConstants.SAUCE_STANDARD_USER, FrameworkConstants.SAUCE_PASSWORD);
    }

    @Test(description = "E2E: Complete order placement flow with price calculation validation", groups = {"smoke", "regression"})
    public void testEndToEndCheckoutOrderPlacement() {
        // Step 1: Add items to Cart
        inventoryPage.addProductToCart("Sauce Labs Backpack")
                     .addProductToCart("Sauce Labs Fleece Jacket");

        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 2, "Badge count should be 2");

        // Step 2: Navigate to Cart
        CartPage cartPage = inventoryPage.goToCart();
        Assert.assertTrue(cartPage.isLoaded(), "Cart page should be loaded");
        Assert.assertEquals(cartPage.getItemCount(), 2, "Should have 2 items in cart");

        // Step 3: Checkout Information with Dynamic Test Data
        CheckoutInformationPage infoPage = cartPage.proceedToCheckout();
        Assert.assertTrue(infoPage.isLoaded(), "Checkout Info page should be loaded");

        String fakeFirstName = DataFakerUtil.getFirstName();
        String fakeLastName = DataFakerUtil.getLastName();
        String fakeZipCode = DataFakerUtil.getPostalCode();

        infoPage.fillCustomerInfo(fakeFirstName, fakeLastName, fakeZipCode);
        ExtentReportManager.logInfo("Filled Customer Data: " + fakeFirstName + " " + fakeLastName + ", Zip: " + fakeZipCode);

        // Step 4: Checkout Overview & Price Verification
        CheckoutOverviewPage overviewPage = infoPage.clickContinue();
        Assert.assertTrue(overviewPage.isLoaded(), "Overview page should be loaded");

        double subtotal = overviewPage.getSubtotal();
        double tax = overviewPage.getTax();
        double total = overviewPage.getTotal();

        // Mathematical validation: Subtotal + Tax == Total (allowing delta 0.01 for rounding)
        double expectedTotal = Math.round((subtotal + tax) * 100.0) / 100.0;
        Assert.assertEquals(total, expectedTotal, 0.01, "Total amount should match Subtotal + Tax");
        ExtentReportManager.logPass(String.format("Price calculation verified: Subtotal ($%.2f) + Tax ($%.2f) = Total ($%.2f)",
                subtotal, tax, total));

        // Step 5: Complete Order
        CheckoutCompletePage completePage = overviewPage.clickFinish();
        Assert.assertTrue(completePage.isLoaded(), "Order Complete page should be loaded");
        Assert.assertEquals(completePage.getConfirmationHeader(), "Thank you for your order!", "Confirmation header mismatch");
        Assert.assertTrue(completePage.isPonyExpressImageDisplayed(), "Success pony express badge should be visible");

        // Step 6: Return back to Catalog
        InventoryPage catalogPage = completePage.clickBackHome();
        Assert.assertTrue(catalogPage.isLoaded(), "Should navigate back to Inventory catalog");
        Assert.assertEquals(catalogPage.getCartBadgeCount(), 0, "Cart badge should be reset to 0");

        ExtentReportManager.logPass("Full E2E Checkout Flow completed successfully.");
    }

    @Test(description = "Verify checkout error validation when customer info is missing", groups = {"regression"})
    public void testCheckoutMissingInformationValidation() {
        inventoryPage.addProductToCart("Sauce Labs Backpack");
        CartPage cartPage = inventoryPage.goToCart();
        CheckoutInformationPage infoPage = cartPage.proceedToCheckout();

        // Case 1: Submit with all fields empty
        infoPage.clickContinue();
        Assert.assertTrue(infoPage.isErrorMessageDisplayed(), "Error message should appear for empty fields");
        Assert.assertEquals(infoPage.getErrorMessage(), "Error: First Name is required");

        // Case 2: Submit with First Name only
        infoPage.enterFirstName(DataFakerUtil.getFirstName());
        infoPage.clickContinue();
        Assert.assertEquals(infoPage.getErrorMessage(), "Error: Last Name is required");

        // Case 3: Submit with First & Last Name, missing Zip
        infoPage.enterLastName(DataFakerUtil.getLastName());
        infoPage.clickContinue();
        Assert.assertEquals(infoPage.getErrorMessage(), "Error: Postal Code is required");

        ExtentReportManager.logPass("Verified all missing info validation error messages on checkout.");
    }
}
