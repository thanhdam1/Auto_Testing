package com.ecommerce.listeners;

import com.ecommerce.reports.ExtentReportManager;
import com.ecommerce.utils.LogUtil;
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
        String className = result.getTestClass().getRealClass().getSimpleName();
        String methodName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        if (description == null || description.isEmpty()) {
            description = methodName;
        }

        LogUtil.info("--> Running Test: {}.{} [{}]", className, methodName, description);
        ExtentReportManager.createTest(className + " - " + methodName, description);
        ExtentReportManager.logInfo("Test execution started: <b>" + methodName + "</b>");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LogUtil.info("<-- Test PASSED: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LogUtil.error("<-- Test FAILED: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LogUtil.warn("<-- Test SKIPPED: {}", result.getMethod().getMethodName());
    }
}
