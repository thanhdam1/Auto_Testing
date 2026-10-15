package com.ecommerce.pages.noithatbang;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

/**
 * Page Object for Website_NoiThat Storefront Contact Page (/contact or /lien-he)
 * Port: 4321
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangContactPage extends BasePage {

    // Page Heading & Breadcrumb
    private final By headingContact = By.xpath("//h1[contains(text(),'KẾT NỐI VỚI GÓC NHÀ ĐẸP')]");

    // Showrooms Info
    private final By showroomAuCo = By.xpath("//h3[contains(text(),'Góc Nhà Đẹp - Âu Cơ')]");
    private final By showroomTayThanh = By.xpath("//h3[contains(text(),'Góc Nhà Đẹp - Tây Thạnh')]");
    private final By btnChatZalo = By.xpath("//a[contains(.,'Chat Zalo')]");
    private final By hotlineLink = By.xpath("//a[contains(@href,'tel:0931883988')]");

    // Form Fields
    private final By inputFullName = By.xpath("//form//input[@placeholder='Ví dụ: Nguyễn Văn A']");
    private final By inputPhone = By.xpath("//form//input[@placeholder='Ví dụ: 0931883988']");
    private final By inputEmail = By.xpath("//form//input[@placeholder='vidu@gmail.com']");
    private final By selectSubject = By.xpath("//form//select");
    private final By textareaMessage = By.xpath("//form//textarea");
    private final By btnSubmit = By.xpath("//form//button[@type='submit']");

    // Feedback Alerts
    private final By alertSuccess = By.cssSelector(".bg-emerald-50");
    private final By alertError = By.cssSelector(".bg-rose-50");

    public boolean isPageLoaded() {
        return isDisplayed(headingContact) && isDisplayed(btnSubmit);
    }

    public boolean isShowroomAuCoDisplayed() {
        return isDisplayed(showroomAuCo);
    }

    public boolean isShowroomTayThanhDisplayed() {
        return isDisplayed(showroomTayThanh);
    }

    public boolean isZaloButtonDisplayed() {
        return isDisplayed(btnChatZalo);
    }

    public void fillContactForm(String fullName, String phone, String email, String subject, String message) {
        type(inputFullName, fullName, "Họ tên liên hệ");
        type(inputPhone, phone, "SĐT liên hệ");
        type(inputEmail, email, "Email liên hệ");
        if (subject != null && isDisplayed(selectSubject)) {
            Select select = new Select(getDriver().findElement(selectSubject));
            select.selectByVisibleText(subject);
        }
        type(textareaMessage, message, "Nội dung tin nhắn liên hệ");
    }

    public void submitContactForm() {
        click(btnSubmit, "Nút GỬI LIÊN HỆ ĐẾN GÓC NHÀ ĐẸP");
    }

    public boolean isSuccessAlertDisplayed() {
        return isDisplayed(alertSuccess);
    }

    public String getSuccessAlertText() {
        return getText(alertSuccess);
    }

    public boolean isErrorAlertDisplayed() {
        return isDisplayed(alertError);
    }

    public String getErrorAlertText() {
        return getText(alertError);
    }
}
