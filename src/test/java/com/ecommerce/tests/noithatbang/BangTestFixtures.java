package com.ecommerce.tests.noithatbang;

import com.ecommerce.api.ApiClient;
import com.ecommerce.api.ApiResponse;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Fixture & REST API Helper for Website_NoiThat (Port 8080)
 * Prepares deterministic, traceable test data for UI & Business Flow verification.
 */
public final class BangTestFixtures {

    private static final String API_URL = ConfigReader.get("noithatbang.api.url", "http://localhost:8080/api/v1");
    private static final ApiClient API = new ApiClient(API_URL);
    private static String adminToken = null;

    private BangTestFixtures() {}

    public static synchronized String getAdminToken() {
        if (adminToken != null) {
            return adminToken;
        }
        try {
            ApiResponse res = API.post("/auth/login", Map.of(
                    "email", "admin@noithat.com",
                    "password", "Admin@123"
            ));
            if (res.isSuccessfulStatus()) {
                String token = res.stringValue("data.access_token");
                if (token == null || token.isBlank()) {
                    token = res.stringValue("data.token");
                }
                adminToken = token;
                return adminToken;
            }
        } catch (Exception e) {
            LogUtil.warn("Không thể đăng nhập admin API để lấy token: {}", e.getMessage());
        }
        return null;
    }

    public static ApiResponse createContact(String fullName, String email, String phone, String subject, String message) {
        return API.post("/contacts", Map.of(
                "full_name", fullName,
                "email", email,
                "phone", phone,
                "subject", subject,
                "message", message
        ));
    }

    public static ApiResponse deleteContact(long id) {
        String token = getAdminToken();
        return API.delete("/admin/contacts/" + id, token);
    }

    public static ApiResponse createCoupon(String code, String type, long value, long minOrder, Long maxDiscount, String endDate) {
        String token = getAdminToken();
        Map<String, Object> payload = new HashMap<>();
        payload.put("code", code);
        payload.put("discount_type", type);
        payload.put("discount_value", value);
        payload.put("min_order_amount", minOrder);
        payload.put("usage_limit", 100);
        payload.put("is_active", true);
        if (maxDiscount != null) {
            payload.put("max_discount", maxDiscount);
        }
        if (endDate != null) {
            payload.put("end_date", endDate);
        }
        return API.post("/admin/coupons", payload, token);
    }

    public static ApiResponse deleteCoupon(long id) {
        String token = getAdminToken();
        return API.delete("/admin/coupons/" + id, token);
    }

    public static String ensureExpiredCoupon() {
        String code = "AUTO_EXPIRED_" + (System.currentTimeMillis() % 100000);
        createCoupon(code, "percent", 10, 10000, 500000L, "2020-01-01T00:00:00Z");
        return code;
    }

    public static String ensureMinOrderCoupon() {
        String code = "AUTO_MIN50M_" + (System.currentTimeMillis() % 100000);
        createCoupon(code, "fixed", 500000, 50000000, null, null);
        return code;
    }

    public static String ensureValidCoupon() {
        String code = "AUTO_VALID10_" + (System.currentTimeMillis() % 100000);
        createCoupon(code, "percent", 10, 10000, 500000L, "2030-01-01T00:00:00Z");
        return code;
    }

    public static ApiResponse getOrders(String status, String query) {
        String token = getAdminToken();
        String path = "/admin/orders?limit=20";
        if (status != null && !status.isBlank()) path += "&status=" + status;
        if (query != null && !query.isBlank()) path += "&q=" + query;
        return API.get(path, token);
    }

    public static ApiResponse getOrderDetail(long orderId) {
        String token = getAdminToken();
        return API.get("/admin/orders/" + orderId, token);
    }

    public static ApiResponse getProductList() {
        return API.get("/products?limit=20");
    }
}
