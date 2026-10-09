package com.ecommerce.utils;

import com.ecommerce.constants.FrameworkConstants;
import com.ecommerce.driver.DriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class ScreenshotUtil {

    private ScreenshotUtil() {}

    /**
     * Captures screenshot as Base64 string for direct embedding in ExtentReports.
     */
    public static String captureBase64() {
        if (DriverManager.getDriver() == null) {
            return "";
        }
        return ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BASE64);
    }

    /**
     * Captures screenshot and saves to file system.
     *
     * @param testName name of the failing test
     * @return absolute path of the saved screenshot
     */
    public static String captureToFile(String testName) {
        if (DriverManager.getDriver() == null) {
            return "";
        }
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = testName + "_" + timestamp + ".png";
        String destinationPath = FrameworkConstants.SCREENSHOT_FOLDER_PATH + fileName;

        try {
            File source = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);
            File destination = new File(destinationPath);
            FileUtils.copyFile(source, destination);
            LogUtil.info("Screenshot saved at: {}", destinationPath);
            return destinationPath;
        } catch (IOException e) {
            LogUtil.error("Failed to save screenshot: {}", e.getMessage());
            return "";
        }
    }
}
