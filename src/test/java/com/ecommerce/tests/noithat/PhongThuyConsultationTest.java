package com.ecommerce.tests.noithat;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.noithat.PhongThuyPage;
import com.ecommerce.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests for the storefront feng shui consultation flow at /phong-thuy.
 */
public class PhongThuyConsultationTest extends BaseTest {

    private PhongThuyPage phongThuyPage;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        openStorefrontPath("/phong-thuy");
        phongThuyPage = new PhongThuyPage();
    }

    @Test(groups = {"smoke", "regression", "storefront", "phongthuy"}, priority = 1,
            description = "NT-FS01: Feng shui consultation page loads with required form and empty result placeholder")
    public void testPhongThuyPageLoadsWithForm() {
        ExtentReportManager.logInfo("Verify /phong-thuy loads the consultation form.");

        Assert.assertTrue(phongThuyPage.isPageLoaded(),
                "Phong thuy page should show the consultation form and initial placeholder.");
        Assert.assertFalse(phongThuyPage.isOutputDisplayed(),
                "Result output should stay hidden before a valid submission.");
    }

    @Test(groups = {"negative", "regression", "storefront", "phongthuy"}, priority = 2,
            description = "NT-FS02: Empty consultation form shows validation for all required fields")
    public void testEmptyFormShowsRequiredFieldValidation() {
        phongThuyPage.submitEmptyForm();

        Assert.assertEquals(phongThuyPage.getVisibleValidationErrorCount(), 3,
                "Empty form should show validation errors for year, gender, and room type.");
        Assert.assertFalse(phongThuyPage.isOutputDisplayed(),
                "Invalid submission must not render the consultation result.");
    }

    @Test(groups = {"negative", "regression", "storefront", "phongthuy"}, priority = 3,
            description = "NT-FS03: Birth year outside supported range is rejected")
    public void testInvalidBirthYearIsRejected() {
        phongThuyPage.fillConsultationForm("1929", "nam", "phong-khach");
        phongThuyPage.submitForm();

        String yearError = phongThuyPage.getYearValidationError();
        ExtentReportManager.logInfo("Year validation error: " + yearError);
        Assert.assertTrue(yearError.contains("1930"),
                "Year validation should mention the lower supported boundary 1930. Actual: " + yearError);
        Assert.assertFalse(phongThuyPage.isOutputDisplayed(),
                "Invalid year must not render the consultation result.");
    }

    @Test(groups = {"smoke", "regression", "storefront", "phongthuy"}, priority = 4,
            description = "NT-FS04: Valid consultation renders palace, directions, colors, and layout suggestions")
    public void testValidConsultationRendersExpectedSections() {
        phongThuyPage.fillConsultationForm("1990", "nam", "phong-khach");
        phongThuyPage.submitForm();
        phongThuyPage.waitForOutput();

        Assert.assertTrue(phongThuyPage.isOutputDisplayed(), "Valid input should render the result panel.");
        Assert.assertTrue(phongThuyPage.getCungText().contains("9"),
                "Male birth year 1990 should calculate quai number 9. Actual: " + phongThuyPage.getCungText());
        Assert.assertFalse(phongThuyPage.getSummaryText().isBlank(), "Result summary should not be blank.");
        Assert.assertEquals(phongThuyPage.getDirectionCount(), 4,
                "Result should render four favorable directions.");
        Assert.assertFalse(phongThuyPage.getColorsText().isBlank(), "Compatible colors should not be blank.");
        Assert.assertTrue(phongThuyPage.getSuggestions().size() >= 4,
                "Result should include room layout suggestions.");
    }
}
