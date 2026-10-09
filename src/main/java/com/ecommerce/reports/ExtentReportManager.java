package com.ecommerce.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.ecommerce.constants.FrameworkConstants;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.LogUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class ExtentReportManager {

    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> testThreadLocal = new ThreadLocal<>();
    private static String reportFilePath;

    private ExtentReportManager() {}

    public static synchronized void initReports() {
        if (extentReports == null) {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            reportFilePath = FrameworkConstants.EXTENT_REPORT_FOLDER_PATH + "ExtentReport_" + timestamp + ".html";

            File reportDir = new File(FrameworkConstants.EXTENT_REPORT_FOLDER_PATH);
            if (!reportDir.exists()) {
                reportDir.mkdirs();
            }

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFilePath);
            sparkReporter.config().setDocumentTitle(ConfigReader.get("report.title", "Automation Test Report"));
            sparkReporter.config().setReportName(ConfigReader.get("report.name", "E-Commerce Execution Report"));

            String themeStr = ConfigReader.get("report.theme", "DARK");
            sparkReporter.config().setTheme("DARK".equalsIgnoreCase(themeStr) ? Theme.DARK : Theme.STANDARD);
            sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");

            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);

            // System information
            extentReports.setSystemInfo("Framework", "Java + Selenium + TestNG + Maven");
            extentReports.setSystemInfo("Target Sites", "SauceDemo & Automation Exercise");
            extentReports.setSystemInfo("Environment", "QA / Staging");
            extentReports.setSystemInfo("User", System.getProperty("user.name"));
            extentReports.setSystemInfo("OS", System.getProperty("os.name"));
            extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
            extentReports.setSystemInfo("Browser", ConfigReader.get("browser", "chrome"));

            LogUtil.info("ExtentReports initialized at: {}", reportFilePath);
        }
    }

    public static synchronized void flushReports() {
        if (extentReports != null) {
            extentReports.flush();
            LogUtil.info("ExtentReports flushed. Report available at: {}", reportFilePath);
        }
    }

    public static ExtentTest createTest(String testName, String description) {
        ExtentTest test = extentReports.createTest(testName, description);
        testThreadLocal.set(test);
        return test;
    }

    public static ExtentTest getTest() {
        return testThreadLocal.get();
    }

    public static void logInfo(String message) {
        if (getTest() != null) {
            getTest().log(Status.INFO, message);
        }
        LogUtil.info("[REPORT] " + message);
    }

    public static void logPass(String message) {
        if (getTest() != null) {
            getTest().log(Status.PASS, message);
        }
        LogUtil.info("[REPORT PASS] " + message);
    }

    public static void logFail(String message, String base64Screenshot) {
        if (getTest() != null) {
            if (base64Screenshot != null && !base64Screenshot.isEmpty()) {
                getTest().log(Status.FAIL, message,
                        MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
            } else {
                getTest().log(Status.FAIL, message);
            }
        }
        LogUtil.error("[REPORT FAIL] " + message);
    }

    public static void logSkip(String message) {
        if (getTest() != null) {
            getTest().log(Status.SKIP, message);
        }
        LogUtil.warn("[REPORT SKIP] " + message);
    }

    public static String getReportFilePath() {
        return reportFilePath;
    }
}
