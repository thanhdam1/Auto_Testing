package com.ecommerce.pages.automationexercise;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;

public class AEHomePage extends BasePage {

    // Locators
    private final By homeLogo = By.cssSelector(".logo.pull-left");
    private final By productsNav = By.cssSelector("a[href='/products']");
    private final By cartNav = By.cssSelector("header a[href='/view_cart']");
    private final By womenCategoryToggle = By.cssSelector("a[href='#Women']");
    private final By dressSubcategory = By.cssSelector("a[href='/category_products/1']");
    private final By menCategoryToggle = By.cssSelector("a[href='#Men']");
    private final By tshirtsSubcategory = By.cssSelector("a[href='/category_products/3']");

    public boolean isLoaded() {
        return isDisplayed(homeLogo);
    }

    public AEProductsPage navigateToProducts() {
        click(productsNav, "Products Navigation Menu");
        return new AEProductsPage();
    }

    public AECartPage navigateToCart() {
        click(cartNav, "Cart Navigation Menu");
        return new AECartPage();
    }

    public AEProductsPage selectWomenDressCategory() {
        scrollToElement(womenCategoryToggle);
        click(womenCategoryToggle, "Women Category Accordion");
        click(dressSubcategory, "Dress Subcategory");
        return new AEProductsPage();
    }

    public AEProductsPage selectMenTshirtsCategory() {
        scrollToElement(menCategoryToggle);
        click(menCategoryToggle, "Men Category Accordion");
        click(tshirtsSubcategory, "Tshirts Subcategory");
        return new AEProductsPage();
    }
}
