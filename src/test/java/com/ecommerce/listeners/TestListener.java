package com.ecommerce.listeners;

import com.ecommerce.driver.DriverManager;
import com.ecommerce.reports.ExtentReportManager;
import com.ecommerce.utils.LogUtil;
import com.ecommerce.utils.ScreenshotUtil;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        LogUtil.info("Starting Test Suite: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        LogUtil.info("Finished Test Suite: {}", context.getName());
        // Ghi báo cáo cho cả những suite không kế thừa BaseTest (ví dụ testng-api.xml)
        ExtentReportManager.flushReports();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ensureTestCreated(result);
        String methodName = result.getMethod().getMethodName();
        LogUtil.info("--> Running Test: {}.{} [{}]",
                result.getTestClass().getRealClass().getSimpleName(),
                methodName,
                result.getMethod().getDescription());
        ExtentReportManager.logInfo("Test execution started: <b>" + methodName + "</b>");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        LogUtil.info("<-- Test PASSED: {}", methodName);
        ensureTestCreated(result);
        ExtentReportManager.logPass("Test <b>" + methodName + "</b> passed successfully.");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        Throwable throwable = result.getThrowable();
        LogUtil.error("<-- Test FAILED: {}", methodName, throwable);

        ensureTestCreated(result);

        // Lấy screenshot đã chụp từ BaseTest (chụp trước khi đóng driver)
        String base64Screenshot = (String) result.getAttribute("screenshot_base64");
        if ((base64Screenshot == null || base64Screenshot.isEmpty()) && DriverManager.getDriver() != null) {
            try {
                base64Screenshot = ScreenshotUtil.captureBase64();
            } catch (Exception ignored) {}
        }

        String errorMessage = "Test Failed";
        if (throwable != null) {
            errorMessage = throwable.getMessage() != null ? throwable.getMessage() : throwable.toString();
        }

        ExtentReportManager.logFail("<b>Test Failed:</b> " + sanitizeMessage(errorMessage), base64Screenshot);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        Throwable throwable = result.getThrowable();
        LogUtil.warn("<-- Test SKIPPED: {}", methodName);

        ensureTestCreated(result);

        String skipReason = (throwable != null && throwable.getMessage() != null)
                ? throwable.getMessage()
                : "Test was skipped without an explicit message.";
        ExtentReportManager.logSkip("<b>Test Skipped:</b> " + sanitizeMessage(skipReason));
    }

    private void ensureTestCreated(ITestResult result) {
        if (ExtentReportManager.getTest() == null) {
            String className = result.getTestClass().getRealClass().getSimpleName();
            String methodName = result.getMethod().getMethodName();
            String description = result.getMethod().getDescription();
            if (description == null || description.isEmpty()) {
                description = methodName;
            }
            ExtentReportManager.createTest(className + " - " + methodName, description);
        }
    }

    private String sanitizeMessage(String message) {
        if (message == null) return "";
        // Che mật khẩu/token nếu xuất hiện trong error message/assertion
        return message.replaceAll("(?i)(password|pass|pwd|secret)\\s*=\\s*['\"]?[^\\s,'\"]+", "$1=********");
    }
}
