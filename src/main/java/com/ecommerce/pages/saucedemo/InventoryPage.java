package com.ecommerce.pages.saucedemo;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class InventoryPage extends BasePage {

    // Locators
    private final By titleHeader = By.cssSelector(".header_secondary_container .title");
    private final By sortDropdown = By.className("product_sort_container");
    private final By inventoryItem = By.className("inventory_item");
    private final By inventoryItemName = By.className("inventory_item_name");
    private final By inventoryItemPrice = By.className("inventory_item_price");
    private final By shoppingCartLink = By.className("shopping_cart_link");
    private final By shoppingCartBadge = By.className("shopping_cart_badge");
    private final By menuButton = By.id("react-burger-menu-btn");
    private final By logoutLink = By.id("logout_sidebar_link");

    public boolean isLoaded() {
        return isDisplayed(titleHeader) && getText(titleHeader).equalsIgnoreCase("Products");
    }

    public String getPageHeaderTitle() {
        return getText(titleHeader);
    }

    public int getProductCount() {
        return findAll(inventoryItem).size();
    }

    public List<String> getAllProductTitles() {
        List<WebElement> elements = findAll(inventoryItemName);
        List<String> titles = new ArrayList<>();
        for (WebElement el : elements) {
            titles.add(el.getText().trim());
        }
        return titles;
    }

    public List<Double> getAllProductPrices() {
        List<WebElement> elements = findAll(inventoryItemPrice);
        List<Double> prices = new ArrayList<>();
        for (WebElement el : elements) {
            String rawPrice = el.getText().replace("$", "").trim();
            prices.add(Double.parseDouble(rawPrice));
        }
        return prices;
    }

    public InventoryPage selectSortOption(String visibleText) {
        selectDropdownByVisibleText(sortDropdown, visibleText, "Product Sort Dropdown");
        return this;
    }

    /**
     * Adds an item to cart by its product name (e.g. "Sauce Labs Backpack")
     */
    public InventoryPage addProductToCart(String productName) {
        String slug = productName.toLowerCase().replace(" ", "-");
        By addToCartBtn = By.id("add-to-cart-" + slug);
        click(addToCartBtn, "Add to Cart [" + productName + "]");
        return this;
    }

    /**
     * Removes an item from cart directly from catalog
     */
    public InventoryPage removeProductFromCart(String productName) {
        String slug = productName.toLowerCase().replace(" ", "-");
        By removeBtn = By.id("remove-" + slug);
        click(removeBtn, "Remove [" + productName + "]");
        return this;
    }

    public int getCartBadgeCount() {
        if (!isDisplayed(shoppingCartBadge)) {
            return 0;
        }
        try {
            return Integer.parseInt(getText(shoppingCartBadge));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public CartPage goToCart() {
        click(shoppingCartLink, "Shopping Cart Icon");
        return new CartPage();
    }

    public LoginPage logout() {
        click(menuButton, "Burger Menu Button");
        click(logoutLink, "Logout Link");
        return new LoginPage();
    }
}
