package com.ecommerce.driver;

import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.LogUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

public final class DriverFactory {

    private DriverFactory() {}

    /**
     * Initializes and configures the WebDriver based on browser name and headless flag.
     *
     * @param browserName Name of browser (chrome, edge, firefox)
     * @return Initialized WebDriver instance
     */
    public static WebDriver createDriver(String browserName) {
        WebDriver driver;
        boolean headless = ConfigReader.getBoolean("headless", false);
        int pageLoadTimeout = ConfigReader.getInt("timeout.pageload", 30);

        TargetBrowser target;
        try {
            target = TargetBrowser.valueOf(browserName.toUpperCase().trim());
        } catch (IllegalArgumentException | NullPointerException e) {
            LogUtil.warn("Unknown browser '{}', defaulting to CHROME", browserName);
            target = TargetBrowser.CHROME;
        }

        LogUtil.info("Initializing browser: {} (headless={})", target, headless);

        switch (target) {
            case EDGE:
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless=new");
                }
                edgeOptions.addArguments("--window-size=1920,1080");
                edgeOptions.addArguments("--start-maximized");
                edgeOptions.addArguments("--disable-notifications");
                edgeOptions.addArguments("--force-prefers-reduced-motion");
                edgeOptions.addArguments("--remote-allow-origins=*");
                edgeOptions.addArguments("--ignore-certificate-errors");
                driver = new EdgeDriver(edgeOptions);
                break;

            case FIREFOX:
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("-headless");
                }
                firefoxOptions.addArguments("--window-size=1920,1080");
                firefoxOptions.addArguments("--start-maximized");
                firefoxOptions.addArguments("--accept-insecure-certs");
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case CHROME:
            default:
                if (System.getProperty("webdriver.chrome.driver") == null) {
                    String cachedDriver = System.getProperty("user.home") + "\\.cache\\selenium\\chromedriver\\win64\\155.0.8059.79\\chromedriver.exe";
                    if (new java.io.File(cachedDriver).exists()) {
                        System.setProperty("webdriver.chrome.driver", cachedDriver);
                    }
                }
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--window-size=1920,1080");
                } else {
                    chromeOptions.addArguments("--start-maximized");
                    chromeOptions.addArguments("--window-position=0,0");
                }
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--force-prefers-reduced-motion");
                chromeOptions.addArguments("--disable-popup-blocking");
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--ignore-certificate-errors");
                chromeOptions.addArguments("--allow-insecure-localhost");
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                driver = new ChromeDriver(chromeOptions);
                break;
        }

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));
        if (!headless) {
            try {
                driver.manage().window().maximize();
            } catch (Exception ignored) {}
        }

        return driver;
    }
}
