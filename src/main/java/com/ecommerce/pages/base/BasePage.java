package com.ecommerce.pages.base;

import com.ecommerce.driver.DriverManager;
import com.ecommerce.reports.ExtentReportManager;
import com.ecommerce.utils.LogUtil;
import com.ecommerce.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public abstract class BasePage {

    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    protected WebElement find(By locator) {
        return WaitUtil.waitForVisibility(locator);
    }

    protected List<WebElement> findAll(By locator) {
        return WaitUtil.waitForAllVisible(locator);
    }

    protected void click(By locator, String elementName) {
        try {
            WebElement element = WaitUtil.waitForClickability(locator);
            scrollToElement(locator);
            highlight(element);
            try {
                element.click();
            } catch (Exception e) {
                LogUtil.warn("Standard click failed on '{}', executing JS click", elementName);
                ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", element);
            }
            LogUtil.info("Clicked on element: {}", elementName);
            ExtentReportManager.logInfo("Clicked on: <b>" + elementName + "</b>");
        } catch (Exception e) {
            LogUtil.error("Failed to click element: {}", elementName, e);
            throw e;
        }
    }

    protected void type(By locator, String text, String elementName) {
        try {
            WebElement element = WaitUtil.waitForVisibility(locator);
            scrollToElement(locator);
            highlight(element);
            element.clear();
            element.sendKeys(text);
            LogUtil.info("Typed '{}' into: {}", text, elementName);
            ExtentReportManager.logInfo("Typed '<b>" + text + "</b>' into: <b>" + elementName + "</b>");
        } catch (Exception e) {
            LogUtil.error("Failed to type into element: {}", elementName, e);
            throw e;
        }
    }

    protected String getText(By locator) {
        WebElement element = WaitUtil.waitForVisibility(locator);
        highlight(element);
        return element.getText().trim();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return WaitUtil.waitForVisibility(locator, 5).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected void selectDropdownByVisibleText(By locator, String text, String dropdownName) {
        WebElement element = WaitUtil.waitForVisibility(locator);
        Select select = new Select(element);
        select.selectByVisibleText(text);
        LogUtil.info("Selected option '{}' from dropdown: {}", text, dropdownName);
        ExtentReportManager.logInfo("Selected '<b>" + text + "</b>' from dropdown <b>" + dropdownName + "</b>");
    }

    protected void selectDropdownByValue(By locator, String value, String dropdownName) {
        WebElement element = WaitUtil.waitForVisibility(locator);
        Select select = new Select(element);
        select.selectByValue(value);
        LogUtil.info("Selected value '{}' from dropdown: {}", value, dropdownName);
        ExtentReportManager.logInfo("Selected value '<b>" + value + "</b>' from dropdown <b>" + dropdownName + "</b>");
    }

    protected void jsClick(By locator, String elementName) {
        try {
            WebElement element = WaitUtil.waitForPresence(locator);
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", element);
            LogUtil.info("JS-Clicked on element: {}", elementName);
            ExtentReportManager.logInfo("JS-Clicked on: <b>" + elementName + "</b>");
        } catch (Exception e) {
            LogUtil.error("Failed to JS-click element: {}", elementName, e);
            throw e;
        }
    }

    protected void scrollToElement(By locator) {
        WebElement element = WaitUtil.waitForPresence(locator);
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    protected void highlight(WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) getDriver();
            js.executeScript("arguments[0].style.border='2px solid #28a745'", element);
        } catch (Exception ignored) {
            // Highlighting is a visual helper; don't fail test if script fails
        }
    }

    public String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }

    public String getPageTitle() {
        return getDriver().getTitle();
    }
}
