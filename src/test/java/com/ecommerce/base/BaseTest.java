package com.ecommerce.base;

import com.ecommerce.api.ApiClient;
import com.ecommerce.api.ApiResponse;
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
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import com.ecommerce.listeners.TestListener;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Listeners(TestListener.class)
public abstract class BaseTest {

    /** localStorage key chứa giỏ hàng của storefront Nội Thất (xem frontend/src/scripts/cart.ts: getCartKey()). */
    protected static final String CART_STORAGE_KEY = "noithat_cart_guest";

    private static String cachedAuthToken = null;
    private static String cachedUserJson = null;
    private static Integer cachedUserId = null;

    protected static synchronized void ensureRealAuthToken() {
        if (cachedAuthToken != null) return;
        try {
            ApiClient api = new ApiClient();
            if (api.isAvailable()) {
                Map<String, String> creds = new ConcurrentHashMap<>();
                creds.put("email", "admin@noithat.vn");
                creds.put("password", "Admin@123456");
                ApiResponse res = api.post("/auth/login", creds);
                if (res.isSuccessfulStatus()) {
                    cachedAuthToken = (String) res.asMap().get("token");
                    Map<String, Object> user = (Map<String, Object>) res.asMap().get("user");
                    if (user != null) {
                        cachedUserId = ((Number) user.get("id")).intValue();
                        String phone = user.get("phone") != null ? String.valueOf(user.get("phone")) : "0359180209";
                        cachedUserJson = String.format(
                            "{\"id\":%d,\"email\":\"%s\",\"name\":\"%s\",\"role\":\"%s\",\"phone\":\"%s\",\"tier\":\"Vàng\"}",
                            cachedUserId, user.get("email"), user.get("full_name"), user.get("role"), phone
                        );
                        LogUtil.info("Đã cache JWT token và user xác thực từ backend: {}", user.get("email"));
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.warn("Không thể lấy token xác thực thật từ API: {}", e.getMessage());
        }
    }

    public void injectCustomerSession() {
        ensureRealAuthToken();
        String token = cachedAuthToken != null ? cachedAuthToken : "admin_token";
        String userJson = cachedUserJson != null ? cachedUserJson :
            "{\"id\":6,\"email\":\"admin@noithat.vn\",\"name\":\"Quản Trị Viên\",\"role\":\"admin\",\"phone\":\"0359180209\",\"tier\":\"Vàng\"}";
        
        ((JavascriptExecutor) DriverManager.getDriver()).executeScript(
            "window.localStorage.setItem('auth_token', arguments[0]);" +
            "window.localStorage.setItem('current_user', arguments[1]);" +
            "window.localStorage.setItem('admin_token', arguments[0]);" +
            "window.localStorage.setItem('admin_user', arguments[1]);",
            token, userJson
        );
    }

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
                try {
                    ScreenshotUtil.captureToFile(testName);
                } catch (Exception ignored) {}
                String base64Screenshot = ScreenshotUtil.captureBase64();
                if (base64Screenshot != null && !base64Screenshot.isEmpty()) {
                    result.setAttribute("screenshot_base64", base64Screenshot);
                }
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
     * Navigates current browser session to the Admin Portal (gắn sẵn admin auth session vào localStorage để vượt Auth Guard).
     */
    protected void openAdmin() {
        String baseUrl = ConfigReader.get("app.url", "http://localhost:4321");
        openUrl(baseUrl + "/san-pham");
        injectCustomerSession();
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
     * Navigates current browser session to Website_NoiThat Storefront (Project 4 - NguyenBang01)
     */
    protected void openNoiThatBangApp() {
        String url = ConfigReader.get("noithatbang.app.url", "http://localhost:4321");
        openUrl(url);
    }

    /**
     * Navigates current browser session to Website_NoiThat Admin Portal (Project 4 - NguyenBang01)
     */
    protected void openNoiThatBangAdmin() {
        String url = ConfigReader.get("noithatbang.admin.url", "http://localhost:5173");
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
     * Bỏ qua test khi backend API chưa bật hoặc cơ sở dữ liệu chưa sẵn sàng (dùng cho test cần API như đặt hàng, thêm sản phẩm).
     */
    protected void assumeApiAvailable() {
        String apiUrl = ConfigReader.get("api.base.url", "http://localhost:4000/api");
        if (!EnvironmentGuard.isReachable(apiUrl + "/health") || isDatabaseDisconnected(apiUrl + "/orders")) {
            throw new SkipException("Backend API / Cơ sở dữ liệu MySQL chưa sẵn sàng tại " + apiUrl
                    + " - test này cần database thực tế để tạo/lưu đơn hàng.");
        }
    }

    private boolean isDatabaseDisconnected(String testEndpoint) {
        try {
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) new java.net.URL(testEndpoint).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(2000);
            return conn.getResponseCode() >= 500;
        } catch (Exception e) {
            return true;
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
        seedRawCart(json);
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
        seedRawCart(json.toString());
    }

    /**
     * Nạp thẳng chuỗi JSON bất kỳ vào giỏ hàng trên tất cả các khóa giỏ hàng (khách và tài khoản đăng nhập).
     */
    protected void seedRawCart(String rawJson) {
        setLocalStorage(CART_STORAGE_KEY, rawJson);
        setLocalStorage("noithat_cart", rawJson);
        setLocalStorage("noithat_cart_u_6", rawJson);
        setLocalStorage("noithat_cart_u_1", rawJson);
        setLocalStorage("noithat_cart_u_15", rawJson);
        if (cachedUserId != null) {
            setLocalStorage("noithat_cart_u_" + cachedUserId, rawJson);
        }
    }

    protected String readStoredCart() {
        String val = getLocalStorage(CART_STORAGE_KEY);
        return (val == null || val.isBlank() || "null".equals(val)) ? "[]" : val;
    }
}
