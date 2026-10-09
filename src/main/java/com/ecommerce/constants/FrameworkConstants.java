package com.ecommerce.constants;

import java.io.File;

public final class FrameworkConstants {

    private FrameworkConstants() {
        // Prevent instantiation
    }

    public static final String CONFIG_FILE_PATH = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "config.properties";
    public static final String EXTENT_REPORT_FOLDER_PATH = System.getProperty("user.dir") + File.separator + "reports" + File.separator;
    public static final String SCREENSHOT_FOLDER_PATH = EXTENT_REPORT_FOLDER_PATH + "screenshots" + File.separator;

    public static final int EXPLICIT_WAIT_SECONDS = 10;
    public static final int PAGE_LOAD_TIMEOUT_SECONDS = 30;

    // SauceDemo Standard Users
    public static final String SAUCE_STANDARD_USER = "standard_user";
    public static final String SAUCE_LOCKED_USER = "locked_out_user";
    public static final String SAUCE_PASSWORD = "secret_sauce";

    // Automation Exercise Credentials / URL
    public static final String AE_DEFAULT_URL = "https://automationexercise.com";
}
