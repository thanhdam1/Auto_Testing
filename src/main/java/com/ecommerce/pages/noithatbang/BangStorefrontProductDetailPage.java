package com.ecommerce.pages.noithatbang;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;

/**
 * Page Object for Website_NoiThat Storefront Product Detail Page (/products/:slug)
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangStorefrontProductDetailPage extends BasePage {

    // Product Header
    private final By headingTitle = By.cssSelector("h1.text-2xl, h1.text-3xl");
    private final By productPrice = By.xpath("//div[contains(@class,'text-xl') and contains(text(),'VND')]");

    // Quantity Stepper
    private final By btnQuantityMinus = By.xpath("//button[contains(@class,'cursor-pointer') and .//*[name()='svg' and contains(@class,'lucide-minus')]]");
    private final By btnQuantityPlus = By.xpath("//button[contains(@class,'cursor-pointer') and .//*[name()='svg' and contains(@class,'lucide-plus')]]");
    private final By quantityDisplay = By.xpath("//span[contains(@class,'w-8') and contains(@class,'text-center')]");

    // CTA Buttons
    private final By btnAddToCart = By.xpath("//button[contains(text(),'THÊM VÀO GIỎ')]");
    private final By btnBuyNow = By.xpath("//button[contains(text(),'MUA NGAY')]");
    private final By btnFavorite = By.xpath("//button[@title='Yêu thích']");

    public boolean isPageLoaded() {
        return isDisplayed(headingTitle) && isDisplayed(btnAddToCart);
    }

    public String getProductTitle() {
        return getText(headingTitle);
    }

    public String getProductPriceText() {
        return getText(productPrice);
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
}
