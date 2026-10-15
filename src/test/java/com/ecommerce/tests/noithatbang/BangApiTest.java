package com.ecommerce.tests.noithatbang;

import com.ecommerce.api.ApiClient;
import com.ecommerce.api.ApiResponse;
import com.ecommerce.reports.ExtentReportManager;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.LogUtil;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * API Test Suite for Website_NoiThat Go/Gin Backend (Port 8080)
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangApiTest {

    private ApiClient healthClient;
    private ApiClient apiClient;
    private boolean apiAvailable;
    private String skipMessage;

    @BeforeClass(alwaysRun = true)
    public void setupClass() {
        String healthUrl = ConfigReader.get("noithatbang.health.url", "http://localhost:8080/health");
        String apiUrl = ConfigReader.get("noithatbang.api.url", "http://localhost:8080/api/v1");

        // Client cho /health tại root
        healthClient = new ApiClient("http://localhost:8080");
        apiClient = new ApiClient(apiUrl);

        try {
            ApiResponse res = healthClient.get("/health");
            apiAvailable = res.status() == 200;
        } catch (Exception e) {
            apiAvailable = false;
        }

        skipMessage = "Website_NoiThat Go Backend không phản hồi tại http://localhost:8080/health - Bỏ qua test.";
        if (apiAvailable) {
            LogUtil.info("[API] Website_NoiThat Go Backend sẵn sàng tại {}", apiUrl);
        } else {
            LogUtil.warn(skipMessage);
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void checkAvailable() {
        if (!apiAvailable) {
            ExtentReportManager.logSkip(skipMessage);
            throw new SkipException(skipMessage);
        }
    }

    @Test(description = "WNT_API_001: Kiểm tra endpoint /health của Go Backend trả về HTTP 200 OK")
    public void testBackendHealth() {
        ApiResponse res = healthClient.get("/health");
        Assert.assertEquals(res.status(), 200, "HTTP Status phải là 200 OK");
        Assert.assertTrue(res.body().contains("ok"), "Body phản hồi phải chứa status ok");
    }

    @Test(description = "WNT_API_002: Kiểm tra danh sách sản phẩm công khai /products")
    public void testGetPublicProducts() {
        ApiResponse res = apiClient.get("/products");
        Assert.assertEquals(res.status(), 200, "Lấy danh sách sản phẩm thành công");
        Assert.assertTrue(res.body().contains("data"), "Phản hồi phải chứa key data");
    }

    @Test(description = "WNT_API_003: Kiểm tra danh mục công khai /categories")
    public void testGetCategories() {
        ApiResponse res = apiClient.get("/categories");
        Assert.assertEquals(res.status(), 200, "Lấy danh mục thành công");
        Assert.assertTrue(res.body().contains("data"), "Phản hồi phải chứa key data");
    }

    @Test(description = "WNT_API_004: Đăng nhập Admin thành công qua API /auth/login")
    public void testAdminLoginApiSuccess() {
        Map<String, String> payload = Map.of(
                "email", "admin@noithat.com",
                "password", "Admin@123"
        );
        ApiResponse res = apiClient.post("/auth/login", payload);
        Assert.assertEquals(res.status(), 200, "Đăng nhập thành công trả về HTTP 200");
        Assert.assertTrue(res.body().contains("access_token"), "Phải trả về access_token");
        Assert.assertTrue(res.body().contains("admin"), "Vai trò người dùng phải là admin");
    }

    @Test(description = "WNT_API_005: Đăng nhập thất bại khi sai mật khẩu")
    public void testAdminLoginApiWrongPassword() {
        Map<String, String> payload = Map.of(
                "email", "admin@noithat.com",
                "password", "SaiMatKhau@999"
        );
        ApiResponse res = apiClient.post("/auth/login", payload);
        Assert.assertTrue(res.status() == 400 || res.status() == 401, "Phải trả về HTTP 400 hoặc 401 khi sai mật khẩu");
    }
}
