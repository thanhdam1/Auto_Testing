package com.ecommerce.pages.automationexercise;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class AEProductsPage extends BasePage {

    // Locators
    private final By searchInput = By.id("search_product");
    private final By searchButton = By.id("submit_search");
    private final By allProductsTitle = By.cssSelector(".features_items .title");
    private final By productCards = By.cssSelector(".features_items .col-sm-4");
    private final By productTitles = By.cssSelector(".productinfo p");
    private final By addToCartButtons = By.cssSelector(".productinfo a.add-to-cart");
    private final By continueShoppingBtn = By.cssSelector("#cartModal .btn-success");
    private final By viewCartModalLink = By.cssSelector("#cartModal a[href='/view_cart']");

    public boolean isLoaded() {
        return isDisplayed(allProductsTitle);
    }

    public String getPageHeaderTitle() {
        return getText(allProductsTitle);
    }

    public AEProductsPage searchProduct(String keyword) {
        scrollToElement(searchInput);
        type(searchInput, keyword, "Search Input Field");
        click(searchButton, "Search Button");
        return this;
    }

    public int getProductCount() {
        if (!isDisplayed(productCards)) {
            return 0;
        }
        return findAll(productCards).size();
    }

    public List<String> getProductTitles() {
        List<WebElement> elements = findAll(productTitles);
        List<String> titles = new ArrayList<>();
        for (WebElement el : elements) {
            titles.add(el.getText().trim());
        }
        return titles;
    }

    public AEProductsPage addFirstProductToCart() {
        scrollToElement(addToCartButtons);
        jsClick(addToCartButtons, "First Add to Cart Button");
        return this;
    }

    public AEProductsPage clickContinueShoppingOnModal() {
        click(continueShoppingBtn, "Continue Shopping Modal Button");
        return this;
    }

    public AECartPage clickViewCartOnModal() {
        click(viewCartModalLink, "View Cart Modal Link");
        return new AECartPage();
    }
}
