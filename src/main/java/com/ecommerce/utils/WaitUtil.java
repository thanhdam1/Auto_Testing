package com.ecommerce.utils;

import com.ecommerce.constants.FrameworkConstants;
import com.ecommerce.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public final class WaitUtil {

    private WaitUtil() {}

    private static WebDriverWait getWait(int timeoutSeconds) {
        WebDriver driver = DriverManager.getDriver();
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    public static WebElement waitForVisibility(By locator, int timeoutSeconds) {
        return getWait(timeoutSeconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForVisibility(By locator) {
        return waitForVisibility(locator, FrameworkConstants.EXPLICIT_WAIT_SECONDS);
    }

    public static WebElement waitForClickability(By locator, int timeoutSeconds) {
        return getWait(timeoutSeconds).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForClickability(By locator) {
        return waitForClickability(locator, FrameworkConstants.EXPLICIT_WAIT_SECONDS);
    }

    public static WebElement waitForPresence(By locator, int timeoutSeconds) {
        return getWait(timeoutSeconds).until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static WebElement waitForPresence(By locator) {
        return waitForPresence(locator, FrameworkConstants.EXPLICIT_WAIT_SECONDS);
    }

    public static List<WebElement> waitForAllVisible(By locator, int timeoutSeconds) {
        return getWait(timeoutSeconds).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public static List<WebElement> waitForAllVisible(By locator) {
        return waitForAllVisible(locator, FrameworkConstants.EXPLICIT_WAIT_SECONDS);
    }

    public static boolean waitForInvisibility(By locator, int timeoutSeconds) {
        return getWait(timeoutSeconds).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static boolean waitForUrlContains(String fraction, int timeoutSeconds) {
        return getWait(timeoutSeconds).until(ExpectedConditions.urlContains(fraction));
    }
}
