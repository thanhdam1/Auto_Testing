package com.ecommerce.pages.saucedemo;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;

public class CheckoutCompletePage extends BasePage {

    // Locators
    private final By titleHeader = By.cssSelector(".header_secondary_container .title");
    private final By completeHeader = By.className("complete-header");
    private final By completeText = By.className("complete-text");
    private final By backHomeButton = By.id("back-to-products");
    private final By ponyExpressImage = By.className("pony_express");

    public boolean isLoaded() {
        return isDisplayed(completeHeader) && isDisplayed(backHomeButton);
    }

    public String getConfirmationHeader() {
        return getText(completeHeader);
    }

    public String getConfirmationText() {
        return getText(completeText);
    }

    public boolean isPonyExpressImageDisplayed() {
        return isDisplayed(ponyExpressImage);
    }

    public InventoryPage clickBackHome() {
        click(backHomeButton, "Back Home Button");
        return new InventoryPage();
    }
}
