package com.ecommerce.pages.saucedemo;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;

public class CheckoutInformationPage extends BasePage {

    // Locators
    private final By titleHeader = By.cssSelector(".header_secondary_container .title");
    private final By firstNameInput = By.id("first-name");
    private final By lastNameInput = By.id("last-name");
    private final By postalCodeInput = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By cancelButton = By.id("cancel");
    private final By errorMessageContainer = By.cssSelector("[data-test='error']");

    public boolean isLoaded() {
        return isDisplayed(firstNameInput) && isDisplayed(continueButton);
    }

    public CheckoutInformationPage enterFirstName(String firstName) {
        type(firstNameInput, firstName, "First Name");
        return this;
    }

    public CheckoutInformationPage enterLastName(String lastName) {
        type(lastNameInput, lastName, "Last Name");
        return this;
    }

    public CheckoutInformationPage enterPostalCode(String postalCode) {
        type(postalCodeInput, postalCode, "Postal Code");
        return this;
    }

    public CheckoutInformationPage fillCustomerInfo(String first, String last, String zip) {
        enterFirstName(first);
        enterLastName(last);
        enterPostalCode(zip);
        return this;
    }

    public CheckoutOverviewPage clickContinue() {
        click(continueButton, "Continue to Overview Button");
        return new CheckoutOverviewPage();
    }

    public CartPage clickCancel() {
        click(cancelButton, "Cancel Checkout Button");
        return new CartPage();
    }

    public String getErrorMessage() {
        return getText(errorMessageContainer);
    }

    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessageContainer);
    }
}
