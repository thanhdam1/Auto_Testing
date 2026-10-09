package com.ecommerce.base;

import com.ecommerce.driver.DriverFactory;
import com.ecommerce.driver.DriverManager;
import com.ecommerce.reports.ExtentReportManager;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.LogUtil;
import com.ecommerce.utils.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public abstract class BaseTest {

    @BeforeSuite(alwaysRun = true)
    public void setupSuite() {
        LogUtil.info("=== SUITE EXECUTION STARTED ===");
        ExtentReportManager.initReports();
    }

    @AfterSuite(alwaysRun = true)
    public void teardownSuite() {
        ExtentReportManager.flushReports();
        LogUtil.info("=== SUITE EXECUTION COMPLETED ===");
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser"})
    public void setupMethod(@Optional String browser) {
        String targetBrowser = (browser != null && !browser.isEmpty()) ? browser : ConfigReader.get("browser", "chrome");
        LogUtil.info("Setting up driver for test on browser: {}", targetBrowser);

        WebDriver driver = DriverFactory.createDriver(targetBrowser);
        DriverManager.setDriver(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void teardownMethod(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                String testName = result.getMethod().getMethodName();
                LogUtil.error("Test FAILED: {}", testName, result.getThrowable());
                String base64Screenshot = ScreenshotUtil.captureBase64();
                ExtentReportManager.logFail("Test Failed: " + result.getThrowable().getMessage(), base64Screenshot);
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                ExtentReportManager.logPass("Test passed successfully.");
            } else if (result.getStatus() == ITestResult.SKIP) {
                ExtentReportManager.logSkip("Test was skipped.");
            }
        } finally {
            DriverManager.quitDriver();
        }
    }

    /**
     * Navigates current browser session to the Storefront application
     */
    protected void openStorefront() {
        String url = ConfigReader.get("app.url", "http://localhost:4321");
        openUrl(url);
    }

    /**
     * Navigates current browser session to the Admin Portal
     */
    protected void openAdmin() {
        String url = ConfigReader.get("admin.url", "http://localhost:4321/admin");
        openUrl(url);
    }

    /**
     * Navigates to an arbitrary URL or path
     */
    protected void openUrl(String url) {
        LogUtil.info("Navigating to URL: {}", url);
        DriverManager.getDriver().get(url);
    }
}
