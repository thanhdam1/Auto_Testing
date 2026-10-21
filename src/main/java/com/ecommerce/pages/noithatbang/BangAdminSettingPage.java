package com.ecommerce.pages.noithatbang;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;

/**
 * Page Object for Website_NoiThat Admin Settings Page (/settings)
 * Port: 5173
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangAdminSettingPage extends BasePage {

    private final By headingSetting = By.xpath("//h1[contains(.,'Cài đặt Cửa hàng')]");

    // Inputs
    private final By inputShopName = By.xpath("//input[@name='shop_name']");
    private final By inputHotline = By.xpath("//input[@name='hotline']");
    private final By inputEmail = By.xpath("//input[@name='email']");
    private final By inputAddress = By.xpath("//input[@name='address']");
    private final By inputShippingFee = By.xpath("//input[@name='default_shipping_fee']");
    private final By inputFreeThreshold = By.xpath("//input[@name='free_shipping_threshold']");
    private final By btnSaveSettings = By.xpath("//button[@type='submit' and contains(.,'Lưu')]");

    public boolean isPageLoaded() {
        return isDisplayed(headingSetting) && isDisplayed(btnSaveSettings);
    }

    public String getShopName() {
        return getDriver().findElement(inputShopName).getAttribute("value");
    }

    public String getHotline() {
        return getDriver().findElement(inputHotline).getAttribute("value");
    }

    public void updateStoreInfo(String shopName, String hotline, String email) {
        if (shopName != null) {
            type(inputShopName, shopName, "Tên cửa hàng");
        }
        if (hotline != null) {
            type(inputHotline, hotline, "Hotline cửa hàng");
        }
        if (email != null) {
            type(inputEmail, email, "Email cửa hàng");
        }
    }

    public void submitSettings() {
        click(btnSaveSettings, "Nút Lưu cài đặt");
    }
}
