package com.ecommerce.tests.verification;

import com.ecommerce.listeners.TestListener;
import com.ecommerce.pages.base.BasePage;
import com.ecommerce.reports.ExtentReportManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

@Listeners(TestListener.class)
public class Phase2VerificationTest {

    // Dummy page extending BasePage for testing password masking logic
    static class DummyPage extends BasePage {
        public boolean checkIsSensitiveKeyword(String text) {
            try {
                Method method = BasePage.class.getDeclaredMethod("isSensitiveKeyword", String.class);
                method.setAccessible(true);
                return (boolean) method.invoke(this, text);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test(description = "Verify sensitive keyword detection for password masking in BasePage")
    public void testSensitiveKeywordDetection() {
        DummyPage dummy = new DummyPage();

        // Positive sensitive matches
        Assert.assertTrue(dummy.checkIsSensitiveKeyword("Password"), "Should detect 'Password'");
        Assert.assertTrue(dummy.checkIsSensitiveKeyword("Ô nhập mật khẩu"), "Should detect 'mật khẩu'");
        Assert.assertTrue(dummy.checkIsSensitiveKeyword("user_matkhau"), "Should detect 'matkhau'");
        Assert.assertTrue(dummy.checkIsSensitiveKeyword("SecretToken"), "Should detect 'secret'");
        Assert.assertTrue(dummy.checkIsSensitiveKeyword("api_token"), "Should detect 'token'");
        Assert.assertTrue(dummy.checkIsSensitiveKeyword("Mã PIN giao dịch"), "Should detect 'PIN'");

        // Negative non-sensitive matches
        Assert.assertFalse(dummy.checkIsSensitiveKeyword("Username"), "Should not detect 'Username'");
        Assert.assertFalse(dummy.checkIsSensitiveKeyword("Email người dùng"), "Should not detect 'Email'");
        Assert.assertFalse(dummy.checkIsSensitiveKeyword("Địa chỉ giao hàng"), "Should not detect 'Địa chỉ'");
    }

    @Test(description = "Verify ExtentReportManager receives test start and pass via TestListener")
    public void testListenerPassReporting() {
        // TestListener automatically creates test and logs pass on method completion
        ExtentReportManager.logInfo("Verifying step progress inside test method");
        Assert.assertNotNull(ExtentReportManager.getTest(), "ExtentTest instance must be active");
    }

    @Test(description = "Verify TestListener message sanitization for sensitive credentials")
    public void testListenerSanitization() throws Exception {
        TestListener listener = new TestListener();
        Method sanitizeMethod = TestListener.class.getDeclaredMethod("sanitizeMessage", String.class);
        sanitizeMethod.setAccessible(true);

        String rawError = "Expected status 200 but failed with password='SecretPassword123' and token='xyzabc'";
        String sanitized = (String) sanitizeMethod.invoke(listener, rawError);

        Assert.assertFalse(sanitized.contains("SecretPassword123"), "Raw password must not be present");
        Assert.assertTrue(sanitized.contains("password=********"), "Password should be masked as ********");
    }
}
