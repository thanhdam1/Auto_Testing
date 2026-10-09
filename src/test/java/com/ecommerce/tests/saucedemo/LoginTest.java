package com.ecommerce.tests.saucedemo;

import com.ecommerce.base.BaseTest;
import com.ecommerce.constants.FrameworkConstants;
import com.ecommerce.dataprovider.TestDataProviders;
import com.ecommerce.pages.saucedemo.InventoryPage;
import com.ecommerce.pages.saucedemo.LoginPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(description = "Verify successful login with standard user", groups = {"smoke", "regression"})
    public void testValidLogin() {
        openSauceDemo();
        LoginPage loginPage = new LoginPage();
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page should be displayed");

        InventoryPage inventoryPage = loginPage.loginAs(FrameworkConstants.SAUCE_STANDARD_USER, FrameworkConstants.SAUCE_PASSWORD);

        Assert.assertTrue(inventoryPage.isLoaded(), "Inventory page should be loaded");
        Assert.assertEquals(inventoryPage.getPageHeaderTitle(), "Products", "Header title should be 'Products'");
        Assert.assertTrue(inventoryPage.getCurrentUrl().contains("/inventory.html"), "URL should contain /inventory.html");

        ExtentReportManager.logPass("Verified valid login successfully for: " + FrameworkConstants.SAUCE_STANDARD_USER);
    }

    @Test(dataProvider = "invalidLoginData", dataProviderClass = TestDataProviders.class,
            description = "Verify login error messages with invalid credentials", groups = {"regression"})
    public void testInvalidLoginScenarios(String username, String password, String expectedErrorMessage) {
        openSauceDemo();
        LoginPage loginPage = new LoginPage();

        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
        Assert.assertEquals(loginPage.getErrorMessage(), expectedErrorMessage, "Error message text mismatch");

        ExtentReportManager.logPass("Correct error message displayed for username: '" + username + "'");
    }

    @Test(description = "Verify logout redirects to login page", groups = {"regression"})
    public void testLogoutFlow() {
        openSauceDemo();
        LoginPage loginPage = new LoginPage();
        InventoryPage inventoryPage = loginPage.loginAs(FrameworkConstants.SAUCE_STANDARD_USER, FrameworkConstants.SAUCE_PASSWORD);

        loginPage = inventoryPage.logout();
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Should be redirected back to Login page");

        ExtentReportManager.logPass("Verified logout flow successfully.");
    }
}
