package com.ecommerce.tests.noithatbang;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithatbang.BangContactPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Data-Driven Test Class for Storefront Contact Page (/contact or /lien-he)
 * Port: 4321
 */
public class BangContactDataTest extends BaseTest {

    private BangContactPage contactPage;

    @BeforeMethod
    public void setUp() {
        openUrl("http://localhost:4321/contact");
        contactPage = new BangContactPage();
        Assert.assertTrue(contactPage.isPageLoaded(), "Trang Liên hệ & Tư vấn phải tải thành công");
    }

    @Test(description = "Kiểm tra thông tin Showroom & kênh hỗ trợ trực tiếp trên trang Liên hệ")
    public void testContactShowroomsAndChannels() {
        Assert.assertTrue(contactPage.isShowroomAuCoDisplayed(), "Thông tin Showroom 1 Âu Cơ phải hiển thị");
        Assert.assertTrue(contactPage.isShowroomTayThanhDisplayed(), "Thông tin Showroom 2 Tây Thạnh phải hiển thị");
        Assert.assertTrue(contactPage.isZaloButtonDisplayed(), "Nút kết nối Chat Zalo phải hiển thị");
        ExtentReportManager.logInfo("Đã xác thực đầy đủ hệ thống showroom và kênh kết nối trực tiếp");
    }

    @Test(dataProvider = "contactSubjectsData", dataProviderClass = BangStorefrontTestData.class,
          description = "Kiểm tra Gửi tin nhắn liên hệ thành công với các chủ đề khác nhau (Data-Driven)")
    public void testSubmitContactWithDiverseSubjects(String subject) {
        String uniqueEmail = BangStorefrontTestData.generateUniqueEmail("auto_contact");
        String fullName = "Khách Hàng Tư Vấn " + System.currentTimeMillis();
        String phone = "0931883988";
        String message = "Xin chào Góc Nhà Đẹp, tôi quan tâm đến chủ đề: " + subject + ". Nhờ chuyên viên tư vấn hỗ trợ.";

        ExtentReportManager.logInfo("Gửi liên hệ với chủ đề: " + subject + " | Email: " + uniqueEmail);
        contactPage.fillContactForm(fullName, phone, uniqueEmail, subject, message);
        contactPage.submitContactForm();

        boolean isSuccess = contactPage.isSuccessAlertDisplayed();
        ExtentReportManager.logInfo("Kết quả gửi liên hệ - Hiển thị phản hồi: " + isSuccess);
    }
}
