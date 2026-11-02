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
            scrollToElement(locator);
            WebElement element = WaitUtil.waitForClickability(locator);
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
        type(locator, text, elementName, false);
    }

    /**
     * Nhập dữ liệu nhạy cảm (mật khẩu, OTP, token, thẻ, v.v.), tự động che chuỗi nhập trong log và báo cáo.
     */
    protected void typeSecure(By locator, String text, String elementName) {
        type(locator, text, elementName, true);
    }

    /**
     * Nhập dữ liệu với cờ chỉ định trường nhạy cảm, đồng thời tự động phát hiện ô mật khẩu qua DOM/tên trường.
     */
    protected void type(By locator, String text, String elementName, boolean isSensitive) {
        try {
            scrollToElement(locator);
            WebElement element = WaitUtil.waitForVisibility(locator);
            highlight(element);
            element.clear();
            element.sendKeys(text);

            boolean sensitive = isSensitive || isSensitiveElement(element, elementName);
            String displayVal = sensitive ? "••••••••" : text;

            LogUtil.info("Typed '{}' into: {}", displayVal, elementName);
            ExtentReportManager.logInfo("Typed '<b>" + displayVal + "</b>' into: <b>" + elementName + "</b>");
        } catch (Exception e) {
            LogUtil.error("Failed to type into element: {}", elementName, e);
            throw e;
        }
    }

    private boolean isSensitiveElement(WebElement element, String elementName) {
        if (isSensitiveKeyword(elementName)) {
            return true;
        }
        try {
            String typeAttr = element.getAttribute("type");
            if ("password".equalsIgnoreCase(typeAttr)) {
                return true;
            }
            String nameAttr = element.getAttribute("name");
            if (isSensitiveKeyword(nameAttr)) {
                return true;
            }
            String idAttr = element.getAttribute("id");
            if (isSensitiveKeyword(idAttr)) {
                return true;
            }
            String autocompleteAttr = element.getAttribute("autocomplete");
            if (autocompleteAttr != null && autocompleteAttr.toLowerCase().contains("password")) {
                return true;
            }
        } catch (Exception ignored) {
            // Tránh vỡ luồng nếu phần tử DOM bị stale hoặc DOM không phản hồi thuộc tính
        }
        return false;
    }

    private boolean isSensitiveKeyword(String text) {
        if (text == null || text.isBlank()) {
            return false;
        }
        String lower = text.toLowerCase();
        return lower.contains("password")
                || lower.contains("passwd")
                || lower.contains("mật khẩu")
                || lower.contains("mat khau")
                || lower.contains("matkhau")
                || lower.contains("secret")
                || lower.contains("token")
                || lower.contains("credential")
                || lower.contains("cvv")
                || lower.contains("pin");
    }

    protected String getText(By locator) {
        scrollToElement(locator);
        WebElement element = WaitUtil.waitForVisibility(locator);
        highlight(element);
        return element.getText().trim();
    }

    protected boolean isDisplayed(By locator) {
        try {
            WebElement element = WaitUtil.waitForVisibility(locator, 5);
            try {
                ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
            } catch (Exception ignored) {}
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected void selectDropdownByVisibleText(By locator, String text, String dropdownName) {
        scrollToElement(locator);
        WebElement element = WaitUtil.waitForVisibility(locator);
        Select select = new Select(element);
        select.selectByVisibleText(text);
        LogUtil.info("Selected option '{}' from dropdown: {}", text, dropdownName);
        ExtentReportManager.logInfo("Selected '<b>" + text + "</b>' from dropdown <b>" + dropdownName + "</b>");
    }

    protected void selectDropdownByValue(By locator, String value, String dropdownName) {
        scrollToElement(locator);
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
        try {
            List<WebElement> elements = getDriver().findElements(locator);
            if (!elements.isEmpty()) {
                ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView({block: 'center'});", elements.get(0));
            }
        } catch (Exception ignored) {}
    }

    protected void highlight(WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) getDriver();
            js.executeScript("arguments[0].style.border='3px solid #ff0055'; arguments[0].style.backgroundColor='rgba(255,200,0,0.2)';", element);
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
