package com.ecommerce.pages.noithat;

import com.ecommerce.pages.base.BasePage;
import com.ecommerce.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for the Noi That feng shui consultation page (/phong-thuy).
 */
public class PhongThuyPage extends BasePage {

    private final By form = By.cssSelector("[data-kt-form]");
    private final By inputYear = By.cssSelector("input[name='namSinh']");
    private final By selectGender = By.cssSelector("select[name='gioiTinh']");
    private final By selectRoom = By.cssSelector("select[name='loaiPhong']");
    private final By submitButton = By.cssSelector("[data-kt-form] button[type='submit']");
    private final By placeholder = By.cssSelector("[data-kt-placeholder]");
    private final By output = By.cssSelector("[data-kt-output]");
    private final By cung = By.cssSelector("[data-kt-cung]");
    private final By summary = By.cssSelector("[data-kt-tom-tat]");
    private final By directions = By.cssSelector("[data-kt-huong] div");
    private final By colors = By.cssSelector("[data-kt-mau]");
    private final By suggestions = By.cssSelector("[data-kt-goi-y] li");
    private final By visibleErrors = By.cssSelector("[data-error-for]:not(.hidden)");

    public boolean isPageLoaded() {
        return isDisplayed(form) && isDisplayed(placeholder);
    }

    public void submitEmptyForm() {
        click(submitButton, "Submit empty feng shui form");
    }

    public void fillConsultationForm(String year, String gender, String room) {
        type(inputYear, year, "Birth year");
        selectDropdownByValue(selectGender, gender, "Gender");
        selectDropdownByValue(selectRoom, room, "Room type");
    }

    public void submitForm() {
        click(submitButton, "Submit feng shui form");
    }

    public void waitForOutput() {
        WaitUtil.waitForVisibility(cung, 5);
    }

    public boolean isOutputDisplayed() {
        return isDisplayed(output) && !getCungText().isBlank();
    }

    public int getVisibleValidationErrorCount() {
        return getDriver().findElements(visibleErrors).size();
    }

    public String getYearValidationError() {
        return getText(By.cssSelector("[data-error-for='namSinh']"));
    }

    public String getCungText() {
        return getText(cung);
    }

    public String getSummaryText() {
        return getText(summary);
    }

    public int getDirectionCount() {
        return getDriver().findElements(directions).size();
    }

    public String getColorsText() {
        return getText(colors);
    }

    public List<WebElement> getSuggestions() {
        return getDriver().findElements(suggestions);
    }
}
