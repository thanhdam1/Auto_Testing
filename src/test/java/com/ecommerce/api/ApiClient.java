package com.ecommerce.api;

import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.LogUtil;
import org.openqa.selenium.json.Json;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * F-03: HTTP client tối giản cho tầng API test của dự án Nội Thất.
 *
 * <p>Ưu điểm: dùng {@link HttpClient} của JDK 17 + {@link Json} của selenium-json nên
 * <b>không cần thêm dependency</b> (REST Assured không resolve được trong môi trường build hiện tại —
 * xem TEST_PLAN_BO_SUNG_3_DU_AN.md mục F-03). Khi CI cho phép tải thư viện, có thể thay lớp này
 * bằng REST Assured mà không phải sửa test nhờ lớp bọc {@link ApiResponse}.</p>
 */
public class ApiClient {

    private static final Json JSON = new Json();
    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(25);

    private final String baseUrl;
    private final HttpClient httpClient;

    public ApiClient() {
        this(ConfigReader.get("api.base.url", "http://localhost:4000/api"));
    }

    public ApiClient(String baseUrl) {
        String normalized = baseUrl == null ? "" : baseUrl.trim();
        this.baseUrl = normalized.endsWith("/") ? normalized.substring(0, normalized.length() - 1) : normalized;
        this.httpClient = HttpClient.newBuilder().connectTimeout(CONNECT_TIMEOUT).build();
    }

    public String baseUrl() {
        return baseUrl;
    }

    public ApiResponse get(String path) {
        return send("GET", path, null);
    }

    public ApiResponse post(String path, Object payload) {
        return send("POST", path, serialize(payload));
    }

    public ApiResponse put(String path, Object payload) {
        return send("PUT", path, serialize(payload));
    }

    public ApiResponse delete(String path) {
        return send("DELETE", path, null);
    }

    /**
     * Kiểm tra API có sống không (dùng cho cơ chế skip thay vì fail khi backend chưa bật).
     */
    public boolean isAvailable() {
        try {
            return get("/health").status() == 200;
        } catch (Exception e) {
            LogUtil.warn("[API] Backend không phản hồi tại {}: {}", baseUrl, e.getMessage());
            return false;
        }
    }

    private String serialize(Object payload) {
        if (payload == null) {
            return "{}";
        }
        return payload instanceof String text ? text : JSON.toJson(payload);
    }

    private ApiResponse send(String method, String path, String body) {
        String url = baseUrl + (path.startsWith("/") ? path : "/" + path);
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url))
                .timeout(REQUEST_TIMEOUT)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");

        HttpRequest request = switch (method) {
            case "POST" -> builder.POST(HttpRequest.BodyPublishers.ofString(body == null ? "{}" : body)).build();
            case "PUT" -> builder.PUT(HttpRequest.BodyPublishers.ofString(body == null ? "{}" : body)).build();
            case "DELETE" -> builder.DELETE().build();
            default -> builder.GET().build();
        };

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            LogUtil.info("[API] {} {} -> HTTP {}", method, url, response.statusCode());
            return new ApiResponse(response.statusCode(), response.body());
        } catch (Exception e) {
            throw new IllegalStateException("Không gọi được API " + method + " " + url + ": " + e.getMessage(), e);
        }
    }
}
