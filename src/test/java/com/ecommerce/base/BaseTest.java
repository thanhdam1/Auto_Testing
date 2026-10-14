package com.ecommerce.base;

import com.ecommerce.driver.DriverFactory;
import com.ecommerce.driver.DriverManager;
import com.ecommerce.reports.ExtentReportManager;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.EnvironmentGuard;
import com.ecommerce.utils.LogUtil;
import com.ecommerce.utils.ScreenshotUtil;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseTest {

    /** localStorage key chứa giỏ hàng của storefront Nội Thất (xem frontend/src/scripts/cart.ts). */
    protected static final String CART_STORAGE_KEY = "noithat_cart";

    /**
     * Cache kết quả probe môi trường theo URL: mỗi URL chỉ được kiểm tra 1 lần cho cả JVM.
     */
    private static final Map<String, Boolean> REACHABILITY_CACHE = new ConcurrentHashMap<>();

    @BeforeSuite(alwaysRun = true)
    public void setupSuite() {
        LogUtil.info("=== SUITE EXECUTION STARTED ===");
        ExtentReportManager.initReports();
        String suiteFile = System.getProperty("suiteFile", "testng.xml");
        EnvironmentGuard.requiredTargetsForSuite(suiteFile)
                .forEach(target -> LogUtil.info("Suite '{}' sẽ test: {} -> {}", suiteFile, target.name(), target.url()));
    }

    /**
     * F-01: Fail-fast nếu môi trường của test sắp chạy không sống.
     *
     * <p>Cách làm: chặn NGAY TRONG {@code @BeforeMethod}, TRƯỚC khi khởi tạo browser. Nhờ vậy khi
     * devtunnel/localhost chết, suite không mở hàng chục browser rồi chờ 30–240 giây mỗi test
     * (đã từng mất hơn 10 phút cho 15 test - xem log ngày 10/10); thay vào đó mỗi test SKIP tức thì
     * với thông điệp rõ ràng, và mỗi URL chỉ được probe 1 lần cho cả lượt chạy.</p>
     *
     * <p>Ghi chú: KHÔNG ném exception ở {@code @BeforeSuite} vì TestNG vẫn chạy tiếp toàn bộ test
     * (đã kiểm chứng thực tế cả với SkipException và IllegalStateException).</p>
     */
    private void verifyTestEnvironment(String testClassName) {
        if (!ConfigReader.getBoolean("env.check.enabled", true)) {
            return;
        }

        String targetUrl = EnvironmentGuard.targetUrlForTestClass(testClassName);
        if (targetUrl == null || targetUrl.isBlank()) {
            return;
        }

        boolean reachable = REACHABILITY_CACHE.computeIfAbsent(targetUrl, EnvironmentGuard::isReachable);
        if (!reachable) {
            String message = EnvironmentGuard.buildFailureMessage(
                    List.of(new EnvironmentGuard.Target(testClassName, targetUrl)));
            LogUtil.error(message);
            ExtentReportManager.logSkip(message);
            throw new SkipException(message);
        }
    }

    @AfterSuite(alwaysRun = true)
    public void teardownSuite() {
        ExtentReportManager.flushReports();
        LogUtil.info("=== SUITE EXECUTION COMPLETED ===");
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser"})
    public void setupMethod(@Optional String browser, Method method) {
        verifyTestEnvironment(method.getDeclaringClass().getName());
        String targetBrowser = (browser != null && !browser.isEmpty()) ? browser : ConfigReader.get("browser", "chrome");
        LogUtil.info("Setting up driver for test on browser: {}", targetBrowser);

        WebDriver driver = DriverFactory.createDriver(targetBrowser);
        DriverManager.setDriver(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void teardownMethod(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                String testName = result.getMethod().getMethodName();
                LogUtil.error("Test FAILED: {}", testName, result.getThrowable());
                String base64Screenshot = ScreenshotUtil.captureBase64();
                ExtentReportManager.logFail("Test Failed: " + result.getThrowable().getMessage(), base64Screenshot);
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                ExtentReportManager.logPass("Test passed successfully.");
            } else if (result.getStatus() == ITestResult.SKIP) {
                ExtentReportManager.logSkip("Test was skipped.");
            }
        } finally {
            DriverManager.quitDriver();
        }
    }

    /**
     * Navigates current browser session to the Storefront application
     */
    protected void openStorefront() {
        String url = ConfigReader.get("app.url", "http://localhost:4321");
        openUrl(url);
    }

    /**
     * Navigates current browser session to the Admin Portal
     */
    protected void openAdmin() {
        String url = ConfigReader.get("admin.url", "http://localhost:4321/admin");
        openUrl(url);
    }

    /**
     * Navigates current browser session to ShopNova Marketplace (Project 2)
     */
    protected void openNovaShop() {
        String url = ConfigReader.get("novashop.app.url", "http://localhost:5173");
        openUrl(url);
    }

    /**
     * Navigates current browser session to ShopNova Seller Portal (Project 2)
     */
    protected void openNovaShopSeller() {
        String url = ConfigReader.get("novashop.seller.url", "http://localhost:5173/nguoi-ban");
        openUrl(url);
    }

    /**
     * Navigates current browser session to QR Repair Ticket System (Project 3)
     */
    protected void openRepairApp() {
        String url = ConfigReader.get("repair.app.url", "http://localhost:5173");
        openUrl(url);
    }

    /**
     * Navigates current browser session to QR Repair Admin/Staff Portal (Project 3)
     */
    protected void openRepairAdmin() {
        String url = ConfigReader.get("repair.admin.url", "http://localhost:5173/login");
        openUrl(url);
    }

    /**
     * Navigates to an arbitrary URL or path
     */
    protected void openUrl(String url) {
        requireDriverOrSkip();
        LogUtil.info("Navigating to URL: {}", url);
        DriverManager.getDriver().get(url);
    }

    /**
     * Bảo đảm có WebDriver trước khi thao tác.
     *
     * <p>Khi môi trường test chết, {@link #setupMethod} đã SKIP nên driver là null; nếu không chặn ở đây,
     * các {@code @BeforeMethod} của lớp con (ví dụ {@code setupTest()} gọi {@code openAdmin()}) sẽ ném
     * NullPointerException và bị surefire tính là FAILURE dù test chỉ đáng SKIP.</p>
     */
    protected void requireDriverOrSkip() {
        if (DriverManager.getDriver() == null) {
            throw new SkipException("Không có WebDriver cho test này: môi trường test không sẵn sàng"
                    + " (đã được kiểm tra tự động ở @BeforeMethod của BaseTest).");
        }
    }

    /**
     * Mở một đường dẫn tương đối của storefront Nội Thất, ví dụ {@code /gio-hang}.
     */
    protected void openStorefrontPath(String path) {
        String baseUrl = ConfigReader.get("app.url", "http://localhost:4321");
        openUrl(baseUrl + (path.startsWith("/") ? path : "/" + path));
    }

    /**
     * WebDriver của phiên hiện tại (đã gắn ThreadLocal) để test dùng trực tiếp khi cần
     * (ví dụ: refresh trang, đọc URL hiện tại).
     */
    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    /**
     * URL hiện tại của trình duyệt.
     */
    protected String currentUrl() {
        return DriverManager.getDriver().getCurrentUrl();
    }

    // ==================================================================
    // TIỆN ÍCH DỮ LIỆU / MÔI TRƯỜNG
    // ==================================================================

    /**
     * Sinh hậu tố duy nhất cho dữ liệu test (theo TEST_STRATEGY: dữ liệu phải traceable).
     */
    protected String uniqueSuffix() {
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * Bỏ qua test khi backend API chưa bật (dùng cho test cần API như đặt hàng, thêm sản phẩm).
     */
    protected void assumeApiAvailable() {
        String apiUrl = ConfigReader.get("api.base.url", "http://localhost:4000/api");
        if (!EnvironmentGuard.isReachable(apiUrl + "/health")) {
            throw new SkipException("Backend API không chạy tại " + apiUrl
                    + " - test này cần API để tạo dữ liệu/đặt hàng. Hãy khởi động backend rồi chạy lại.");
        }
    }

    // ==================================================================
    // TIỆN ÍCH localStorage (giỏ hàng Nội Thất lưu ở localStorage)
    // ==================================================================

    protected void setLocalStorage(String key, String value) {
        ((JavascriptExecutor) DriverManager.getDriver())
                .executeScript("window.localStorage.setItem(arguments[0], arguments[1]);", key, value);
        LogUtil.info("localStorage['{}'] = {}", key, value);
    }

    protected String getLocalStorage(String key) {
        Object value = ((JavascriptExecutor) DriverManager.getDriver())
                .executeScript("return window.localStorage.getItem(arguments[0]);", key);
        return value == null ? null : String.valueOf(value);
    }

    protected void clearLocalStorage() {
        ((JavascriptExecutor) DriverManager.getDriver()).executeScript("window.localStorage.clear();");
        LogUtil.info("Đã xoá toàn bộ localStorage của phiên hiện tại.");
    }

    /**
     * Nạp sẵn giỏ hàng với 1 sản phẩm (phải gọi SAU khi đã mở 1 trang của storefront để đúng origin).
     *
     * @param slug  slug sản phẩm
     * @param name  tên sản phẩm
     * @param price đơn giá (VND)
     * @param qty   số lượng
     */
    protected void seedCartWithSingleItem(String slug, String name, long price, int qty) {
        String json = String.format(
                "[{\"slug\":\"%s\",\"name\":\"%s\",\"price\":%d,\"qty\":%d}]", slug, name, price, qty);
        setLocalStorage(CART_STORAGE_KEY, json);
    }

    /**
     * Nạp sẵn giỏ hàng với nhiều sản phẩm.
     *
     * @param items mảng 4 phần tử theo thứ tự: slug, name, price, qty
     */
    protected void seedCart(String[][] items) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < items.length; i++) {
            if (i > 0) {
                json.append(',');
            }
            json.append(String.format("{\"slug\":\"%s\",\"name\":\"%s\",\"price\":%d,\"qty\":%d}",
                    items[i][0], items[i][1], Long.parseLong(items[i][2]), Integer.parseInt(items[i][3])));
        }
        json.append(']');
        setLocalStorage(CART_STORAGE_KEY, json.toString());
    }

    /**
     * Nạp thẳng chuỗi JSON bất kỳ vào giỏ hàng (dùng để test dữ liệu hỏng / giá trị biên).
     */
    protected void seedRawCart(String rawJson) {
        setLocalStorage(CART_STORAGE_KEY, rawJson);
    }

    protected String readStoredCart() {
        return getLocalStorage(CART_STORAGE_KEY);
    }
}
