package com.ecommerce.pages.automationexercise;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class AECartPage extends BasePage {

    // Locators
    private final By cartRows = By.cssSelector("#cart_info_table tbody tr");
    private final By cartItemDescriptions = By.cssSelector(".cart_description h4 a");
    private final By proceedToCheckoutBtn = By.cssSelector(".check_out");

    public boolean isLoaded() {
        return isDisplayed(By.id("cart_info"));
    }

    public int getCartItemCount() {
        if (!isDisplayed(cartRows)) {
            return 0;
        }
        return findAll(cartRows).size();
    }

    public List<String> getCartItemNames() {
        List<WebElement> elements = findAll(cartItemDescriptions);
        List<String> names = new ArrayList<>();
        for (WebElement el : elements) {
            names.add(el.getText().trim());
        }
        return names;
    }

    public void clickProceedToCheckout() {
        click(proceedToCheckoutBtn, "Proceed To Checkout Button");
    }
}
