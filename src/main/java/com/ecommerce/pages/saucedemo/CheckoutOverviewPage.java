package com.ecommerce.pages.saucedemo;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;

public class CheckoutOverviewPage extends BasePage {

    // Locators
    private final By titleHeader = By.cssSelector(".header_secondary_container .title");
    private final By itemTotalLabel = By.className("summary_subtotal_label");
    private final By taxLabel = By.className("summary_tax_label");
    private final By totalLabel = By.className("summary_total_label");
    private final By paymentInfoLabel = By.xpath("//div[contains(@class,'summary_value_label')][1]");
    private final By shippingInfoLabel = By.xpath("//div[contains(@class,'summary_value_label')][2]");
    private final By finishButton = By.id("finish");
    private final By cancelButton = By.id("cancel");

    public boolean isLoaded() {
        return isDisplayed(finishButton) && isDisplayed(itemTotalLabel);
    }

    public double getSubtotal() {
        String text = getText(itemTotalLabel); // "Item total: $29.99"
        return parseAmount(text);
    }

    public double getTax() {
        String text = getText(taxLabel); // "Tax: $2.40"
        return parseAmount(text);
    }

    public double getTotal() {
        String text = getText(totalLabel); // "Total: $32.39"
        return parseAmount(text);
    }

    public String getPaymentInfo() {
        return getText(paymentInfoLabel);
    }

    public String getShippingInfo() {
        return getText(shippingInfoLabel);
    }

    public CheckoutCompletePage clickFinish() {
        click(finishButton, "Finish Order Button");
        return new CheckoutCompletePage();
    }

    public InventoryPage clickCancel() {
        click(cancelButton, "Cancel Order Button");
        return new InventoryPage();
    }

    private double parseAmount(String text) {
        String numeric = text.replaceAll("[^0-9.]", "").trim();
        return Double.parseDouble(numeric);
    }
}
