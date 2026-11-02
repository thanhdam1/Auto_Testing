package com.ecommerce.tests.verification;

import com.ecommerce.dataprovider.RepairBoundaryDataProviders;
import com.ecommerce.listeners.TestListener;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(TestListener.class)
public class Phase3VerificationTest {

    @Test(description = "Xác minh DataProvider tìm kiếm admin không còn chứa giá trị hardcoded 7 hay RP-512806")
    public void testRepairAdminSearchBoundariesDynamic() {
        Object[][] data = RepairBoundaryDataProviders.repairAdminTicketSearchBoundaries();
        Assert.assertNotNull(data, "DataProvider phải trả về mảng dữ liệu");

        for (Object[] row : data) {
            String caseName = (String) row[0];
            String expected = (String) row[2];

            // Kỳ vọng phải là "all" hoặc "none", không được là số cứng 7 hay 1
            Assert.assertTrue("all".equals(expected) || "none".equals(expected),
                    "Case " + caseName + " phải sử dụng kỳ vọng linh hoạt ('all' hoặc 'none') thay vì số dòng cố định.");
        }
    }

    @Test(description = "Xác minh DataProvider tìm kiếm KTV không còn phụ thuộc mã cố định RP-512806")
    public void testRepairTechnicianSearchBoundariesDynamic() {
        Object[][] data = RepairBoundaryDataProviders.repairTechnicianTicketSearchBoundaries();
        Assert.assertNotNull(data, "DataProvider phải trả về mảng dữ liệu");

        for (Object[] row : data) {
            String query = (String) row[1];
            Assert.assertFalse("RP-512806".equals(query),
                    "DataProvider KTV không được chứa mã phiếu hardcoded 'RP-512806'.");
        }
    }
}
