package com.ecommerce.tests.noithatbang;

import org.testng.annotations.DataProvider;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Structured Test Data Provider for Website_NoiThat Storefront (Port 4321)
 * Follows 'test_data_generator' skill:
 * - Positive, Negative, Boundary, Edge Cases
 * - Unique, Deterministic, Traceable: auto_<testName>_<timestamp>
 */
public final class BangStorefrontTestData {

    private BangStorefrontTestData() {}

    public static String generateUniqueEmail(String prefix) {
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        return prefix + "_" + ts + "@test.com";
    }

    public static String generateUniqueUsername(String prefix) {
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return prefix + "_" + ts;
    }

    /**
     * Search Keywords: Positive, Negative (No results), Edge Cases (XSS, Special Chars)
     */
    @DataProvider(name = "searchQueriesData")
    public static Object[][] getSearchQueriesData() {
        return new Object[][] {
            // Positive happy path
            { "Sofa", "Positive keyword: Sofa" },
            { "Bàn", "Positive keyword: Bàn" },
            { "Gỗ", "Positive keyword: Gỗ" },
            // Negative / No matching results
            { "NonExistentFurniture9999XYZ", "Negative query: No items expected" },
            // Edge Cases
            { "<script>alert('test')</script>", "Edge case: XSS pattern input" },
            { "@#$%^&*", "Edge case: Special symbols" }
        };
    }

    /**
     * Auth Login Data: Negative & Boundary cases
     */
    @DataProvider(name = "loginNegativeData")
    public static Object[][] getLoginNegativeData() {
        return new Object[][] {
            { "", "Test@12345", "Empty email validation" },
            { "test@example.com", "", "Empty password validation" },
            { "nonexistent_user_99999@test.com", "WrongPassword@2026", "Invalid credentials" }
        };
    }

    /**
     * Auth Registration Password Boundary Data:
     * Min password length is 6.
     * Length 5 should trigger validation error "Mật khẩu phải có tối thiểu 6 ký tự!"
     */
    @DataProvider(name = "registerPasswordBoundaryData")
    public static Object[][] getRegisterPasswordBoundaryData() {
        return new Object[][] {
            { "12345", false, "Boundary Below: 5 characters (Min - 1) -> Must Fail" },
            { "123456", true, "Boundary Exact: 6 characters (Min) -> Valid length" }
        };
    }

    /**
     * Coupon Code Data: Positive & Negative
     */
    @DataProvider(name = "couponValidationData")
    public static Object[][] getCouponValidationData() {
        return new Object[][] {
            { "INVALID999", "Mã giảm giá không hợp lệ hoặc đã hết hạn" },
            { "EXPIRED2024", "Mã giảm giá không hợp lệ hoặc đã hết hạn" },
            { "SALE999XYZ", "Mã giảm giá không hợp lệ hoặc đã hết hạn" }
        };
    }

    /**
     * Contact Form Subjects Data:
     */
    @DataProvider(name = "contactSubjectsData")
    public static Object[][] getContactSubjectsData() {
        return new Object[][] {
            { "Tư vấn thiết kế nội thất phòng khách" },
            { "Đặt Sofa / Bàn ăn theo kích thước riêng" },
            { "Hỏi về bảo hành & bảo trì sản phẩm" }
        };
    }
}
