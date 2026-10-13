package com.ecommerce.tests.api;

import com.ecommerce.api.ApiClient;
import com.ecommerce.api.ApiResponse;
import com.ecommerce.reports.ExtentReportManager;
import com.ecommerce.utils.LogUtil;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * F-03: Bộ test tầng API cho backend Nội Thất (Express + Prisma) tại {@code api.base.url}.
 *
 * <p>Phủ 12 endpoint: health, auth (login/register/customers), products (list/detail/categories),
 * orders (create/list/detail). Test KHÔNG cần browser. Nếu backend chưa bật, cả class được SKIP
 * với thông điệp rõ ràng thay vì fail ồn ào.</p>
 */
public class NoiThatApiTest {

    private ApiClient api;
    private String runSuffix;
    private boolean apiAvailable;
    private String skipMessage;

    @BeforeClass(alwaysRun = true)
    public void setupClass() {
        api = new ApiClient();
        runSuffix = String.valueOf(System.currentTimeMillis());
        // Chỉ gọi /health MỘT lần cho cả class để khi backend tắt không phải chờ 10s x 14 test
        apiAvailable = api.isAvailable();
        skipMessage = "Backend API không phản hồi tại " + api.baseUrl()
                + " - bỏ qua bộ test API. Hãy chạy backend Nội Thất (npm run dev) rồi thử lại.";

        if (apiAvailable) {
            LogUtil.info("[API] Backend sẵn sàng tại {}", api.baseUrl());
        } else {
            LogUtil.warn(skipMessage);
        }
    }

    /**
     * Bỏ qua từng test khi backend chưa bật.
     *
     * <p>Lưu ý: ném {@code SkipException} trong {@code @BeforeClass} sẽ khiến TestNG đánh dấu
     * các test là <b>FAILED</b>; ném trong {@code @BeforeMethod} mới cho ra trạng thái SKIPPED
     * đúng như mong muốn.</p>
     */
    @BeforeMethod(alwaysRun = true)
    public void checkApiAvailable() {
        if (!apiAvailable) {
            ExtentReportManager.logSkip(skipMessage);
            throw new SkipException(skipMessage);
        }
    }

    // ============================== HEALTH ==============================

    @Test(groups = {"api", "smoke"}, priority = 1,
            description = "GET /health trả về status ok")
    public void testHealthEndpointReturnsOk() {
        ApiResponse response = api.get("/health");

        Assert.assertEquals(response.status(), 200, "Endpoint /health phải trả HTTP 200. " + response);
        Assert.assertEquals(response.stringValue("status"), "ok", "Body phải chứa status=ok. " + response);
    }

    // ============================== PRODUCTS ==============================

    @Test(groups = {"api", "regression"}, priority = 2,
            description = "GET /products tuân thủ contract phân trang và trường dữ liệu")
    public void testGetProductsReturnsPaginationContract() {
        ApiResponse response = api.get("/products?limit=4&page=1");

        Assert.assertEquals(response.status(), 200, "GET /products phải trả HTTP 200. " + response);
        Assert.assertTrue(response.booleanValue("success"), "Body phải có success=true. " + response);

        List<Object> products = response.listValue("data");
        Assert.assertFalse(products.isEmpty(), "Danh sách sản phẩm không được rỗng (cần seed dữ liệu).");

        Assert.assertEquals(response.longValue("pagination.limit"), 4L,
                "Pagination limit phải phản ánh tham số limit=4. " + response);

        Map<String, Object> first = asMap(products.get(0));
        for (String field : new String[]{"id", "name", "slug", "price", "stock"}) {
            Assert.assertTrue(first.containsKey(field),
                    "Mỗi sản phẩm phải có trường '" + field + "'. Thiếu trong: " + first);
        }
        Assert.assertFalse(String.valueOf(first.get("slug")).isBlank(), "slug sản phẩm không được rỗng.");
    }

    @Test(groups = {"api", "regression"}, priority = 3,
            description = "GET /products?category=sofa chỉ trả sản phẩm thuộc danh mục sofa")
    public void testGetProductsFilteredByCategory() {
        ApiResponse response = api.get("/products?category=sofa&limit=12");

        Assert.assertEquals(response.status(), 200, "GET /products?category=sofa phải trả HTTP 200. " + response);

        List<Object> products = response.listValue("data");
        Assert.assertFalse(products.isEmpty(), "Danh mục sofa phải có ít nhất 1 sản phẩm.");

        for (Object item : products) {
            Map<String, Object> product = asMap(item);
            Assert.assertEquals(String.valueOf(product.get("category")), "sofa",
                    "Bộ lọc danh mục trả sai sản phẩm: " + product);
        }
    }

    @Test(groups = {"api", "regression"}, priority = 4,
            description = "GET /products/{slug} trả chi tiết đúng slug lấy từ danh sách")
    public void testGetProductDetailBySlugMatchesListData() {
        ApiResponse listResponse = api.get("/products?limit=1");
        List<Object> products = listResponse.listValue("data");
        Assert.assertFalse(products.isEmpty(), "Cần ít nhất 1 sản phẩm để kiểm tra chi tiết.");

        Map<String, Object> expected = asMap(products.get(0));
        String slug = String.valueOf(expected.get("slug"));

        ApiResponse detail = api.get("/products/" + slug);
        Assert.assertEquals(detail.status(), 200, "GET /products/" + slug + " phải trả HTTP 200. " + detail);
        Assert.assertEquals(detail.stringValue("data.slug"), slug, "Chi tiết trả sai slug. " + detail);
        Assert.assertEquals(detail.stringValue("data.name"), String.valueOf(expected.get("name")),
                "Tên sản phẩm giữa list và detail phải khớp. " + detail);
    }

    @Test(groups = {"api", "negative"}, priority = 5,
            description = "GET /products/{slug} với slug không tồn tại trả 404")
    public void testGetProductByUnknownSlugReturnsNotFound() {
        ApiResponse response = api.get("/products/slug-khong-ton-tai-" + runSuffix);

        Assert.assertEquals(response.status(), 404,
                "Slug không tồn tại phải trả HTTP 404. " + response);
        Assert.assertFalse(response.booleanValue("success"), "success phải là false khi không tìm thấy.");
    }

    @Test(groups = {"api", "regression"}, priority = 6,
            description = "GET /products/categories/all trả danh sách danh mục")
    public void testGetAllCategoriesReturnsList() {
        ApiResponse response = api.get("/products/categories/all");

        Assert.assertEquals(response.status(), 200, "GET /products/categories/all phải trả HTTP 200. " + response);
        Assert.assertTrue(response.booleanValue("success"), "Body phải có success=true. " + response);
        Assert.assertFalse(response.listValue("data").isEmpty(), "Danh sách danh mục không được rỗng.");
    }

    // ============================== ORDERS ==============================

    @Test(groups = {"api", "regression"}, priority = 7,
            description = "POST /orders tính đúng phí ship 50.000đ cho đơn dưới 2 triệu và tra cứu lại bằng mã đơn")
    public void testCreateOrderUnderTwoMillionAddsShippingFeeAndIsRetrievable() {
        String customerName = "API Test " + runSuffix;

        ApiResponse created = api.post("/orders", orderPayload(customerName, "0912" + runSuffix.substring(3), 500000L, 2));

        Assert.assertEquals(created.status(), 201, "Tạo đơn hàng phải trả HTTP 201. " + created);
        Assert.assertTrue(created.booleanValue("success"), "success phải là true. " + created);
        Assert.assertEquals(created.longValue("data.subtotalAmount"), 1000000L,
                "Subtotal = 500.000 x 2 phải là 1.000.000. " + created);
        Assert.assertEquals(created.longValue("data.shippingFee"), 50000L,
                "Đơn dưới 2.000.000đ phải cộng phí ship 50.000đ. " + created);
        Assert.assertEquals(created.longValue("data.totalAmount"), 1050000L,
                "Tổng tiền = 1.000.000 + 50.000. " + created);

        String orderCode = created.stringValue("data.orderCode");
        Assert.assertNotNull(orderCode, "Phải trả về orderCode. " + created);
        Assert.assertTrue(orderCode.matches("NT-\\d{4}-\\d{4}"),
                "Mã đơn phải theo định dạng NT-YYMM-XXXX nhưng nhận được: " + orderCode);

        ApiResponse detail = api.get("/orders/" + orderCode);
        Assert.assertEquals(detail.status(), 200, "Tra cứu đơn vừa tạo phải trả HTTP 200. " + detail);
        Assert.assertEquals(detail.stringValue("data.orderCode"), orderCode, "Mã đơn tra cứu không khớp.");
        Assert.assertEquals(detail.stringValue("data.customerName"), customerName, "Tên khách hàng không khớp.");
    }

    @Test(groups = {"api", "boundary"}, priority = 8,
            description = "Biên phí vận chuyển: subtotal >= 2.000.000đ thì miễn phí ship")
    public void testCreateOrderFromTwoMillionGetsFreeShipping() {
        ApiResponse created = api.post("/orders",
                orderPayload("API Ship Free " + runSuffix, "0987" + runSuffix.substring(3), 2000000L, 1));

        Assert.assertEquals(created.status(), 201, "Tạo đơn hàng phải trả HTTP 201. " + created);
        Assert.assertEquals(created.longValue("data.subtotalAmount"), 2000000L, "Subtotal phải đúng 2.000.000.");
        Assert.assertEquals(created.longValue("data.shippingFee"), 0L,
                "Đơn từ 2.000.000đ phải được miễn phí vận chuyển. " + created);
        Assert.assertEquals(created.longValue("data.totalAmount"), 2000000L,
                "Tổng tiền phải bằng subtotal khi ship = 0. " + created);
    }

    @Test(groups = {"api", "regression"}, priority = 9,
            description = "POST /orders thiếu số điện thoại hoặc danh sách sản phẩm trả 400")
    public void testCreateOrderWithMissingFieldsReturnsBadRequest() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("fullname", "Thiếu dữ liệu " + runSuffix);
        payload.put("items", new ArrayList<>());

        ApiResponse response = api.post("/orders", payload);

        Assert.assertEquals(response.status(), 400, "Thiếu phone/items phải trả HTTP 400. " + response);
        Assert.assertFalse(response.booleanValue("success"), "success phải là false. " + response);
    }

    @Test(groups = {"api", "negative"}, priority = 10,
            description = "GET /orders/{code} với mã không tồn tại trả 404")
    public void testGetOrderByUnknownCodeReturnsNotFound() {
        ApiResponse response = api.get("/orders/KHONG-TON-TAI-" + runSuffix);

        Assert.assertEquals(response.status(), 404, "Mã đơn không tồn tại phải trả HTTP 404. " + response);
    }

    @Test(groups = {"api", "regression"}, priority = 11,
            description = "GET /orders trả danh sách đơn hàng có đủ trường tổng tiền và chi tiết sản phẩm")
    public void testGetOrdersListContract() {
        ApiResponse response = api.get("/orders");

        Assert.assertEquals(response.status(), 200, "GET /orders phải trả HTTP 200. " + response);
        List<Object> orders = response.listValue("data");
        Assert.assertFalse(orders.isEmpty(), "Danh sách đơn hàng không được rỗng sau khi đã tạo đơn ở test trước.");

        Map<String, Object> order = asMap(orders.get(0));
        for (String field : new String[]{"orderCode", "customerName", "subtotal", "shippingFee", "total", "items"}) {
            Assert.assertTrue(order.containsKey(field), "Đơn hàng phải có trường '" + field + "'. Thiếu: " + order);
        }

        long subtotal = response.longValue("data.0.subtotal");
        long shipping = response.longValue("data.0.shippingFee");
        long total = response.longValue("data.0.total");
        Assert.assertEquals(total, subtotal + shipping,
                "Tổng tiền API trả về phải bằng subtotal + phí ship. " + order);
    }

    // ============================== AUTH ==============================

    @Test(groups = {"api", "negative"}, priority = 12,
            description = "POST /auth/login sai mật khẩu trả 401 và thiếu dữ liệu trả 400")
    public void testLoginValidationResponses() {
        Map<String, Object> wrongCredentials = new LinkedHashMap<>();
        wrongCredentials.put("email", "khong-ton-tai-" + runSuffix + "@noithat.local");
        wrongCredentials.put("password", "sai_mat_khau");

        ApiResponse unauthorized = api.post("/auth/login", wrongCredentials);
        Assert.assertEquals(unauthorized.status(), 401,
                "Sai thông tin đăng nhập phải trả HTTP 401. " + unauthorized);
        Assert.assertFalse(unauthorized.booleanValue("success"), "success phải là false. " + unauthorized);

        Map<String, Object> missingFields = new LinkedHashMap<>();
        missingFields.put("email", "");

        ApiResponse badRequest = api.post("/auth/login", missingFields);
        Assert.assertEquals(badRequest.status(), 400,
                "Thiếu email/mật khẩu phải trả HTTP 400. " + badRequest);
    }

    @Test(groups = {"api", "negative"}, priority = 13,
            description = "POST /auth/register với email đã tồn tại (sinh ra từ đơn hàng khách) trả 409")
    public void testRegisterDuplicateEmailReturnsConflict() {
        String email = "dup_" + runSuffix + "@noithat.local";

        Map<String, Object> order = orderPayload("Khách trùng email " + runSuffix, "0905" + runSuffix.substring(3), 500000L, 1);
        order.put("email", email);
        ApiResponse createdOrder = api.post("/orders", order);
        Assert.assertEquals(createdOrder.status(), 201,
                "Bước chuẩn bị: tạo đơn để sinh user với email " + email + " phải thành công. " + createdOrder);

        Map<String, Object> registerPayload = new LinkedHashMap<>();
        registerPayload.put("fullname", "Đăng ký trùng " + runSuffix);
        registerPayload.put("email", email);
        registerPayload.put("phone", "0905" + runSuffix.substring(3));
        registerPayload.put("password", "123456");

        ApiResponse response = api.post("/auth/register", registerPayload);

        Assert.assertEquals(response.status(), 409,
                "Email đã tồn tại phải trả HTTP 409. " + response);
        Assert.assertFalse(response.booleanValue("success"), "success phải là false. " + response);
    }

    @Test(groups = {"api", "regression"}, priority = 14,
            description = "GET /auth/customers trả danh sách khách hàng đúng contract cho trang Admin")
    public void testCustomersEndpointContract() {
        ApiResponse response = api.get("/auth/customers");

        Assert.assertEquals(response.status(), 200, "GET /auth/customers phải trả HTTP 200. " + response);
        Assert.assertTrue(response.booleanValue("success"), "Body phải có success=true. " + response);

        List<Object> customers = response.listValue("data");
        Assert.assertFalse(customers.isEmpty(), "Danh sách khách hàng không được rỗng.");

        Map<String, Object> first = asMap(customers.get(0));
        for (String field : new String[]{"id", "name", "email", "ordersCount", "totalSpent"}) {
            Assert.assertTrue(first.containsKey(field), "Khách hàng phải có trường '" + field + "'. Thiếu: " + first);
        }
        Assert.assertTrue(String.valueOf(first.get("id")).startsWith("KH-"),
                "Mã khách hàng phải có tiền tố KH-. Nhận được: " + first.get("id"));
    }

    // ============================== HELPERS ==============================

    private Map<String, Object> orderPayload(String fullname, String phone, long unitPrice, int qty) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("slug", "den-ban-gom");
        item.put("name", "Đèn bàn gốm");
        item.put("price", unitPrice);
        item.put("qty", qty);

        List<Map<String, Object>> items = new ArrayList<>();
        items.add(item);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("fullname", fullname);
        payload.put("phone", phone);
        payload.put("province", "Hà Nội");
        payload.put("district", "Quận Ba Đình");
        payload.put("address", "Số 1 Đường Test");
        payload.put("payment_method", "cod");
        payload.put("items", items);
        return payload;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> asMap(Object value) {
        return value instanceof Map<?, ?> map ? (Map<String, Object>) map : new LinkedHashMap<>();
    }
}
