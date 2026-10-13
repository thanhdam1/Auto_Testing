package com.ecommerce.api;

import org.openqa.selenium.json.Json;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * F-03: Bao bọc phản hồi HTTP của tầng API để test đọc dữ liệu dễ dàng.
 *
 * <p>Dùng {@link Json} có sẵn trong selenium-json nên KHÔNG cần thêm dependency mới vào pom.xml.</p>
 */
public class ApiResponse {

    private static final Json JSON = new Json();

    private final int status;
    private final String body;

    public ApiResponse(int status, String body) {
        this.status = status;
        this.body = body == null ? "" : body;
    }

    public int status() {
        return status;
    }

    public String body() {
        return body;
    }

    public boolean isSuccessfulStatus() {
        return status >= 200 && status < 300;
    }

    /**
     * Parse body thành Map phẳng (trả về Map rỗng nếu body không phải JSON object).
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> asMap() {
        try {
            Object parsed = JSON.toType(body, Map.class);
            if (parsed instanceof Map<?, ?> map) {
                return (Map<String, Object>) map;
            }
        } catch (Exception ignored) {
            // body không phải JSON hợp lệ
        }
        return Collections.emptyMap();
    }

    /**
     * Đọc giá trị theo đường dẫn kiểu {@code data.orderCode} hoặc {@code data.0.id}.
     *
     * @param path đường dẫn phân tách bằng dấu chấm
     * @return giá trị hoặc null nếu không tồn tại
     */
    public Object value(String path) {
        Object current = asMap();
        for (String segment : path.split("\\.")) {
            if (current instanceof Map<?, ?> map) {
                current = map.get(segment);
            } else if (current instanceof List<?> list) {
                try {
                    int index = Integer.parseInt(segment);
                    current = (index >= 0 && index < list.size()) ? list.get(index) : null;
                } catch (NumberFormatException e) {
                    return null;
                }
            } else {
                return null;
            }
        }
        return current;
    }

    public boolean hasValue(String path) {
        return value(path) != null;
    }

    public String stringValue(String path) {
        Object value = value(path);
        return value == null ? null : String.valueOf(value);
    }

    public long longValue(String path) {
        Object value = value(path);
        if (value instanceof Number number) {
            return number.longValue();
        }
        if (value == null) {
            return -1L;
        }
        try {
            return Long.parseLong(String.valueOf(value).replaceAll("[^0-9-]", ""));
        } catch (NumberFormatException e) {
            return -1L;
        }
    }

    public boolean booleanValue(String path) {
        Object value = value(path);
        if (value instanceof Boolean bool) {
            return bool;
        }
        return Boolean.parseBoolean(String.valueOf(value));
    }

    @SuppressWarnings("unchecked")
    public List<Object> listValue(String path) {
        Object value = value(path);
        return value instanceof List<?> list ? (List<Object>) list : Collections.emptyList();
    }

    @Override
    public String toString() {
        return "HTTP " + status + " | body: " + body;
    }
}
