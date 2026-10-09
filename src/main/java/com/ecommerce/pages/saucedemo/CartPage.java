package com.ecommerce.pages.saucedemo;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class CartPage extends BasePage {

    // Locators
    private final By titleHeader = By.cssSelector(".header_secondary_container .title");
    private final By cartItems = By.className("cart_item");
    private final By cartItemNames = By.className("inventory_item_name");
    private final By cartItemPrices = By.className("inventory_item_price");
    private final By checkoutButton = By.id("checkout");
    private final By continueShoppingButton = By.id("continue-shopping");

    public boolean isLoaded() {
        return isDisplayed(titleHeader) && getText(titleHeader).equalsIgnoreCase("Your Cart");
    }

    public int getItemCount() {
        if (!isDisplayed(cartItems)) {
            return 0;
        }
        return findAll(cartItems).size();
    }

    public List<String> getItemNames() {
        List<String> names = new ArrayList<>();
        if (!isDisplayed(cartItems)) {
            return names;
        }
        List<WebElement> elements = findAll(cartItemNames);
        for (WebElement el : elements) {
            names.add(el.getText().trim());
        }
        return names;
    }

    public List<Double> getItemPrices() {
        List<Double> prices = new ArrayList<>();
        if (!isDisplayed(cartItems)) {
            return prices;
        }
        List<WebElement> elements = findAll(cartItemPrices);
        for (WebElement el : elements) {
            String rawPrice = el.getText().replace("$", "").trim();
            prices.add(Double.parseDouble(rawPrice));
        }
        return prices;
    }

    public CartPage removeItemByName(String productName) {
        String slug = productName.toLowerCase().replace(" ", "-");
        By removeBtn = By.id("remove-" + slug);
        click(removeBtn, "Remove Cart Item [" + productName + "]");
        return this;
    }

    public CheckoutInformationPage proceedToCheckout() {
        click(checkoutButton, "Checkout Button");
        return new CheckoutInformationPage();
    }

    public InventoryPage clickContinueShopping() {
        click(continueShoppingButton, "Continue Shopping Button");
        return new InventoryPage();
    }
}
