package com.ecommerce.pages.noithat;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for Storefront Home Page (/).
 */
public class StorefrontHomePage extends BasePage {

    private final By heroSection = By.cssSelector("section#mua-theo-khong-gian, .hero-slider, main");
    private final By spaceSection = By.id("mua-theo-khong-gian");
    private final By spaceCards = By.cssSelector("#mua-theo-khong-gian a[href*='/san-pham']");
    private final By btnFengShuiAiChat = By.cssSelector("button[aria-label='Mở tư vấn phong thủy AI']");
    private final By navLinkProducts = By.cssSelector("header a[href='/san-pham']");
    private final By navLinkFengShui = By.cssSelector("header a[href='/phong-thuy']");
    private final By navLinkCart = By.cssSelector("header a[href='/gio-hang']");

    public boolean isPageLoaded() {
        return isDisplayed(spaceSection) || isDisplayed(heroSection);
    }

    public int getSpaceCardCount() {
        try {
            List<WebElement> cards = findAll(spaceCards);
            return cards.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isFengShuiChatButtonDisplayed() {
        return isDisplayed(btnFengShuiAiChat);
    }

    public StorefrontCatalogPage navigateToCatalog() {
        click(navLinkProducts, "Menu - Sản phẩm");
        return new StorefrontCatalogPage();
    }

    public PhongThuyPage navigateToFengShui() {
        click(navLinkFengShui, "Menu - Phong thủy");
        return new PhongThuyPage();
    }
}
