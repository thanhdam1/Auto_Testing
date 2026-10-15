package com.ecommerce.pages.noithatbang;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for Website_NoiThat Storefront Product Detail Page (/products/:slug)
 * Port: 4321
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangStorefrontProductDetailPage extends BasePage {

    // Breadcrumb
    private final By breadcrumbNav = By.cssSelector("nav[aria-label='Đường dẫn trang']");

    // Product Header & Pricing
    private final By headingTitle = By.cssSelector("h1.text-2xl, h1.text-3xl");
    private final By productPrice = By.xpath("//div[contains(@class,'text-xl') and contains(text(),'VND')]");
    private final By productSKU = By.xpath("//span[contains(@class,'font-mono')]");

    // Gallery
    private final By primaryImage = By.cssSelector(".lg\\:col-span-7 img");
    private final By secondaryImages = By.cssSelector(".grid.grid-cols-2 img");

    // Quantity Stepper
    private final By btnQuantityMinus = By.xpath("//button[contains(@class,'cursor-pointer') and .//*[name()='svg' and contains(@class,'lucide-minus')]]");
    private final By btnQuantityPlus = By.xpath("//button[contains(@class,'cursor-pointer') and .//*[name()='svg' and contains(@class,'lucide-plus')]]");
    private final By quantityDisplay = By.xpath("//span[contains(@class,'w-8') and contains(@class,'text-center')]");

    // CTA Buttons
    private final By btnAddToCart = By.xpath("//button[contains(text(),'THÊM VÀO GIỎ')]");
    private final By btnBuyNow = By.xpath("//button[contains(text(),'MUA NGAY')]");
    private final By btnFavorite = By.xpath("//button[@title='Yêu thích']");
    private final By heartFilled = By.cssSelector("button[title='Yêu thích'] svg.fill-red-600");

    // Member 5% Discount Banner & Modal
    private final By btnMemberBanner = By.xpath("//button[contains(text(),'ĐĂNG KÝ NGAY')]");
    private final By memberModal = By.xpath("//h3[contains(text(),'Đăng Ký Thành Viên Góc Nhà Đẹp')]");
    private final By inputMemberName = By.xpath("//form//input[@placeholder='Họ và tên của bạn']");
    private final By inputMemberPhone = By.xpath("//form//input[@placeholder='Số điện thoại nhận mã ưu đãi']");
    private final By btnMemberSubmit = By.xpath("//form//button[contains(text(),'Nhận Voucher Ngay')]");
    private final By btnCloseMemberModal = By.xpath("//div[contains(@class,'fixed')]//button[*[name()='svg' and contains(@class,'lucide-x')]]");

    // Accordions
    private final By accordionDetail = By.xpath("//button[span[text()='Chi tiết']]");
    private final By accordionDetailContent = By.xpath("//div[contains(@class,'leading-relaxed')]");
    private final By accordionSize = By.xpath("//button[span[text()='Kích thước']]");
    private final By accordionSizeContent = By.xpath("//p[strong[contains(text(),'Kích thước tổng thể')]]");
    private final By accordionWarranty = By.xpath("//button[span[text()='Bảo hành']]");
    private final By accordionWarrantyContent = By.xpath("//p[strong[contains(text(),'Thời gian bảo hành')]]");
    private final By accordionShipping = By.xpath("//button[span[text()='Giao hàng & lắp đặt']]");
    private final By accordionShippingContent = By.xpath("//p[strong[contains(text(),'Khu vực TP. Hồ Chí Minh')]]");

    // Showroom Modal
    private final By linkShowroom = By.xpath("//button[contains(text(),'Xem cửa hàng trưng bày')]");
    private final By showroomModal = By.xpath("//h3[contains(text(),'Hệ Thống Showroom')]");
    private final By btnCloseShowroomModal = By.xpath("//div[contains(@class,'fixed')]//button[*[name()='svg' and contains(@class,'lucide-x')]]");

    public boolean isPageLoaded() {
        return isDisplayed(headingTitle) && isDisplayed(btnAddToCart);
    }

    public String getProductTitle() {
        return getText(headingTitle);
    }

    public String getProductPriceText() {
        return getText(productPrice);
    }

    public String getProductSKU() {
        return getText(productSKU);
    }

    public boolean isBreadcrumbDisplayed() {
        return isDisplayed(breadcrumbNav);
    }

    public void increaseQuantity(int times) {
        for (int i = 0; i < times; i++) {
            click(btnQuantityPlus, "Tăng số lượng (+)");
        }
    }

    public void decreaseQuantity(int times) {
        for (int i = 0; i < times; i++) {
            click(btnQuantityMinus, "Giảm số lượng (-)");
        }
    }

    public String getCurrentQuantity() {
        return getText(quantityDisplay);
    }

    public void clickAddToCart() {
        click(btnAddToCart, "Nút THÊM VÀO GIỎ");
    }

    public void clickBuyNow() {
        click(btnBuyNow, "Nút MUA NGAY");
    }

    public void clickFavorite() {
        click(btnFavorite, "Icon Trái tim Yêu thích");
    }

    public boolean isFavoriteActive() {
        return isDisplayed(heartFilled);
    }

    public void selectSecondaryImage(int index) {
        List<WebElement> thumbs = findAll(secondaryImages);
        if (index >= 0 && index < thumbs.size()) {
            thumbs.get(index).click();
        }
    }

    public void toggleAccordionDetail() {
        click(accordionDetail, "Accordion Chi tiết");
    }

    public boolean isAccordionDetailContentVisible() {
        return isDisplayed(accordionDetailContent);
    }

    public void toggleAccordionSize() {
        click(accordionSize, "Accordion Kích thước");
    }

    public boolean isAccordionSizeContentVisible() {
        return isDisplayed(accordionSizeContent);
    }

    public void toggleAccordionWarranty() {
        click(accordionWarranty, "Accordion Bảo hành");
    }

    public boolean isAccordionWarrantyContentVisible() {
        return isDisplayed(accordionWarrantyContent);
    }

    public void toggleAccordionShipping() {
        click(accordionShipping, "Accordion Giao hàng & lắp đặt");
    }

    public boolean isAccordionShippingContentVisible() {
        return isDisplayed(accordionShippingContent);
    }

    public void openShowroomModal() {
        click(linkShowroom, "Link Xem cửa hàng trưng bày");
    }

    public boolean isShowroomModalOpen() {
        return isDisplayed(showroomModal);
    }

    public void closeShowroomModal() {
        click(btnCloseShowroomModal, "Đóng Showroom Modal");
    }

    public void openMemberModal() {
        click(btnMemberBanner, "Banner Đăng ký nhận 5% voucher thành viên");
    }

    public boolean isMemberModalOpen() {
        return isDisplayed(memberModal);
    }

    public void fillMemberModal(String name, String phone) {
        type(inputMemberName, name, "Họ tên nhận voucher");
        type(inputMemberPhone, phone, "SĐT nhận voucher");
    }

    public void submitMemberModal() {
        click(btnMemberSubmit, "Nút Nhận Voucher Ngay");
    }

    public void closeMemberModal() {
        click(btnCloseMemberModal, "Đóng Member Voucher Modal");
    }
}
