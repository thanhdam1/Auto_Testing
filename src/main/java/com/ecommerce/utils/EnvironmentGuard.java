package com.ecommerce.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * F-01: Kiểm tra nhanh "môi trường có sống không" TRƯỚC khi chạy test.
 *
 * <p>Trước đây khi devtunnel/localhost chết, suite vẫn cố chạy và tiêu tốn 30–240 giây cho MỖI test
 * rồi mới báo SKIP (xem log ngày 10/10: 15 test SKIP mất hơn 10 phút). Lớp này giúp phát hiện
 * ngay từ {@code @BeforeSuite} và dừng suite với một thông điệp duy nhất, rõ ràng.</p>
 */
public final class EnvironmentGuard {

    private static final int CONNECT_TIMEOUT_MS = 5_000;
    private static final int READ_TIMEOUT_MS = 5_000;

    private EnvironmentGuard() {
    }

    /**
     * Một mục tiêu cần kiểm tra (tên hiển thị + URL).
     */
    public record Target(String name, String url) {
    }

    /**
     * Xác định các URL bắt buộc phải sống dựa trên suite đang chạy.
     *
     * @param suiteFile tên file suite TestNG (ví dụ {@code testng.xml}, {@code testng-novashop.xml})
     * @return danh sách target cần kiểm tra
     */
    public static List<Target> requiredTargetsForSuite(String suiteFile) {
        String suite = suiteFile == null ? "" : suiteFile.toLowerCase();
        boolean runAll = suite.contains("all");
        boolean runNova = runAll || suite.contains("novashop");
        boolean runRepair = runAll || suite.contains("repair");
        boolean runNoiThat = runAll || (!runNova && !runRepair);

        List<Target> targets = new ArrayList<>();

        if (runNoiThat) {
            targets.add(new Target("Project 1 - Noi That Storefront", ConfigReader.get("app.url", "http://localhost:4321")));
        }
        if (runNova) {
            targets.add(new Target("Project 2 - ShopNova Marketplace", ConfigReader.get("novashop.app.url", "http://localhost:5173")));
        }
        if (runRepair) {
            targets.add(new Target("Project 3 - QR Repair System", ConfigReader.get("repair.app.url", "http://localhost:5174")));
        }
        if (runAll || suite.contains("api")) {
            targets.add(new Target("Backend API (Noi That)", ConfigReader.get("api.base.url", "http://localhost:4000/api")));
        }

        return targets;
    }

    /**
     * Xác định URL môi trường mà một test class cần, dựa trên package của nó.
     *
     * <p>Nhờ vậy khi chạy testng-all.xml mà chỉ 1 trong 3 app chết, chỉ các test thuộc app đó
     * bị SKIP nhanh, các dự án còn lại vẫn chạy bình thường.</p>
     *
     * @param testClassName tên đầy đủ của test class, ví dụ com.ecommerce.tests.novashop.NovaAuthTest
     * @return URL cần kiểm tra, hoặc null nếu không xác định được
     */
    public static String targetUrlForTestClass(String testClassName) {
        if (testClassName == null) {
            return null;
        }
        if (testClassName.contains(".novashop.")) {
            return ConfigReader.get("novashop.app.url", "http://localhost:5173");
        }
        if (testClassName.contains(".repair.")) {
            return ConfigReader.get("repair.app.url", "http://localhost:5174");
        }
        if (testClassName.contains(".api.")) {
            return ConfigReader.get("api.base.url", "http://localhost:4000/api");
        }
        if (testClassName.contains(".noithatbang.")) {
            return ConfigReader.get("noithatbang.app.url", "http://localhost:4321");
        }
        if (testClassName.contains(".noithat.")) {
            return ConfigReader.get("app.url", "http://localhost:4321");
        }
        return null;
    }

    /**
     * Kiểm tra một URL có phản hồi HTTP hay không. Bất kỳ mã phản hồi nào (kể cả 404/500) đều được
     * coi là "sống" — chỉ lỗi kết nối/timeout mới là "chết".
     *
     * @param url URL cần kiểm tra
     * @return true nếu kết nối được tới server
     */
    public static boolean isReachable(String url) {
        if (url == null || url.isBlank()) {
            return false;
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(CONNECT_TIMEOUT_MS);
            connection.setReadTimeout(READ_TIMEOUT_MS);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("User-Agent", "AutomationFramework-EnvironmentGuard/1.0");
            int status = connection.getResponseCode();
            LogUtil.debug("EnvironmentGuard: {} responded with HTTP {}", url, status);
            return status > 0;
        } catch (IOException e) {
            LogUtil.warn("EnvironmentGuard: {} KHÔNG truy cập được ({})", url, e.getMessage());
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Lọc ra các target không truy cập được.
     *
     * @param targets danh sách target
     * @return danh sách target chết (rỗng nếu mọi thứ OK)
     */
    public static List<Target> unreachableTargets(List<Target> targets) {
        List<Target> down = new ArrayList<>();
        for (Target target : targets) {
            if (!isReachable(target.url())) {
                down.add(target);
            }
        }
        return down;
    }

    /**
     * Tạo thông điệp báo lỗi môi trường thống nhất.
     *
     * @param down danh sách target chết
     * @return thông điệp tiếng Việt
     */
    public static String buildFailureMessage(List<Target> down) {
        StringBuilder sb = new StringBuilder("MÔI TRƯỜNG TEST KHÔNG SẴN SÀNG - không truy cập được:\n");
        for (Target target : down) {
            sb.append("  - ").append(target.name()).append(" -> ").append(target.url()).append('\n');
        }
        sb.append("Hãy khởi động lại app / kiểm tra tunnel trước khi chạy lại suite. ")
                .append("(Có thể tắt kiểm tra này bằng -Denv.check.enabled=false)");
        return sb.toString();
    }
}
