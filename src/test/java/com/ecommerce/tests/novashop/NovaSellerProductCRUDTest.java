package com.ecommerce.tests.novashop;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.novashop.NovaLoginPage;
import com.ecommerce.pages.novashop.NovaSellerAddProductPage;
import com.ecommerce.pages.novashop.NovaSellerProductsPage;
import com.ecommerce.reports.ExtentReportManager;
import com.ecommerce.utils.WaitUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class NovaSellerProductCRUDTest extends BaseTest {

    private NovaSellerAddProductPage addProductPage;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        openUrl(com.ecommerce.utils.ConfigReader.get("novashop.app.url", "https://novashop-web-delta.vercel.app") + "/dang-nhap");
        NovaLoginPage loginPage = new NovaLoginPage();
        loginPage.login("testseller", "Password123!");

        WaitUtil.waitForUrlContains("/nguoi-ban", 15);

        openUrl(com.ecommerce.utils.ConfigReader.get("novashop.app.url", "https://novashop-web-delta.vercel.app") + "/nguoi-ban/san-pham/them-moi");
        addProductPage = new NovaSellerAddProductPage();
    }

    @Test(groups = {"smoke", "novashop", "seller"}, description = "Xác nhận form Đăng sản phẩm mới của người bán tải thành công")
    public void testAddProductFormLoads() {
        ExtentReportManager.logInfo("Kiểm tra form đăng sản phẩm mới");
        Assert.assertTrue(addProductPage.isPageLoaded(), "Form đăng sản phẩm mới phải tải thành công.");
    }

    @Test(groups = {"regression", "novashop", "seller"}, description = "Thêm sản phẩm mới thành công từ kênh người bán")
    public void testCreateNewProductSuccess() {
        String testProductName = "Cáp Sạc Nhanh AutoTest " + System.currentTimeMillis();
        String price = "180000";
        String stock = "50";
        String description = "Sản phẩm thử nghiệm tự động bởi Selenium Automation Framework";

        ExtentReportManager.logInfo("Điền thông tin sản phẩm mới: " + testProductName);
        addProductPage.fillAndSubmitProduct(testProductName, price, stock, description);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {}

        NovaSellerProductsPage productsPage = new NovaSellerProductsPage();
        ExtentReportManager.logInfo("Kiểm tra điều hướng về trang quản lý sản phẩm");
        Assert.assertTrue(productsPage.isPageLoaded(), "Sau khi thêm sản phẩm phải chuyển về danh sách sản phẩm.");
    }

    @Test(groups = {"negative", "novashop", "seller"}, description = "Xác nhận validation khi để trống thông tin bắt buộc khi đăng sản phẩm")
    public void testAddProductFormValidationNegative() {
        ExtentReportManager.logInfo("Thực hiện submit form đăng sản phẩm khi để trống toàn bộ");
        addProductPage.clickSave();

        // Trang vẫn phải ở lại form đăng sản phẩm (không điều hướng vì HTML5 validation hoặc React validation chặn lại)
        Assert.assertTrue(addProductPage.isPageLoaded(), "Form đăng sản phẩm không được submit khi thiếu thông tin bắt buộc.");
    }

    @Test(groups = {"regression", "novashop", "seller"}, description = "Đăng sản phẩm đầy đủ thông số: Biến thể, Giá gốc, Mô tả chi tiết")
    public void testCreateProductWithCategoryAndVariants() {
        String testProductName = "Tai Nghe Không Dây Pro " + System.currentTimeMillis();
        ExtentReportManager.logInfo("Nhập tên sản phẩm: " + testProductName);
        addProductPage.enterProductName(testProductName);
        addProductPage.enterPrice("250000");
        addProductPage.enterOriginalPrice("350000");
        addProductPage.enterStock("100");
        addProductPage.enterVariants("Trắng, Đen, Xanh");
        addProductPage.enterDescription("Tai nghe chống ồn chủ động cao cấp, pin 24h.");
        
        ExtentReportManager.logInfo("Submit form đăng sản phẩm chi tiết");
        addProductPage.clickSave();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {}

        NovaSellerProductsPage productsPage = new NovaSellerProductsPage();
        Assert.assertTrue(productsPage.isPageLoaded(), "Sau khi đăng sản phẩm phải chuyển về danh sách sản phẩm.");
    }
}

