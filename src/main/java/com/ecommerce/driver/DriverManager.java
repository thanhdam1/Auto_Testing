package com.ecommerce.driver;

import com.ecommerce.utils.LogUtil;
import org.openqa.selenium.WebDriver;

public final class DriverManager {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverManager() {}

    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    public static void setDriver(WebDriver driver) {
        driverThreadLocal.set(driver);
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                LogUtil.info("Browser session closed successfully for current thread.");
            } catch (Exception e) {
                LogUtil.error("Error closing browser session: {}", e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }
}
