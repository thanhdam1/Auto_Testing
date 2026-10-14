package com.ecommerce.dataprovider;

import org.testng.annotations.DataProvider;

public final class RepairBoundaryDataProviders {

    private RepairBoundaryDataProviders() {
    }

    @DataProvider(name = "repairBookingInputBoundaries")
    public static Object[][] repairBookingInputBoundaries() {
        return new Object[][]{
                {"min-readable", "A", "0900000000", "A"},
                {"unicode-and-spacing", "  Nguyễn Văn Á  ", "0912345678", "Lỗi pin 🔋 và ký tự / - _ ."},
                {"long-description", "Khách Boundary", "0987654321", "x".repeat(500)}
        };
    }

    @DataProvider(name = "repairRequiredFields")
    public static Object[][] repairRequiredFields() {
        return new Object[][]{
                {"missing-name", "", "0912345678", "Mô tả hợp lệ"},
                {"missing-phone", "Khách Boundary", "", "Mô tả hợp lệ"},
                {"missing-description", "Khách Boundary", "0912345678", ""}
        };
    }

    @DataProvider(name = "repairAdminTicketSearchBoundaries")
    public static Object[][] repairAdminTicketSearchBoundaries() {
        return new Object[][]{
                {"empty-query", "", 7},
                {"known-ticket", "RP-512806", 1},
                {"unknown-ticket", "NOT-FOUND-TEST", 0},
                {"whitespace-query", "   ", 7},
                {"overlong-query", "X".repeat(256), 0}
        };
    }

    @DataProvider(name = "repairTechnicianTicketSearchBoundaries")
    public static Object[][] repairTechnicianTicketSearchBoundaries() {
        return new Object[][]{
                {"empty-query", "", "all"},
                {"known-ticket", "RP-512806", "one"},
                {"known-customer", "Nguyen Van An", "one-or-more"},
                {"phone-fragment", "090900", "one-or-more"},
                {"unknown-ticket", "NOT-FOUND-TEST", "none"},
                {"whitespace-query", "   ", "all"},
                {"overlong-query", "X".repeat(256), "none"}
        };
    }

    @DataProvider(name = "repairInventoryThresholdBoundaries")
    public static Object[][] repairInventoryThresholdBoundaries() {
        return new Object[][]{
                {"minimum-valid", "1", "1", true},
                {"maximum-valid", "9999", "9999", true},
                {"below-minimum", "0", "5", true},
                {"above-maximum", "10000", "10000", false}
        };
    }
}
