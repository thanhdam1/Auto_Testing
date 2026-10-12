package com.ecommerce.pages.repair;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;

/**
 * Page Object for HungHau IT Support QR Repair Ticket System Homepage (Project 3)
 */
public class RepairHomePage extends BasePage {

    // Header & Navigation
    private final By headerLogo = By.cssSelector("header img[alt*='Logo']");
    private final By linkHome = By.xpath("//nav//a[contains(text(),'Trang chủ')]");
    private final By linkTracking = By.xpath("//nav//a[contains(text(),'Tra Cứu Tiến Độ')]");
    private final By linkAdminPortal = By.xpath("//header//a[contains(text(),'Cổng KTV')]");

    // Quick Actions on Landing Page
    private final By btnBookNow = By.cssSelector("a[href='/dat-lich']");
    private final By btnTrackNow = By.cssSelector("a[href='/tra-cuu']");
    private final By btnOnlineIntake = By.cssSelector("a[href='/online-intake']");

    // Contact & Floating Widgets
    private final By floatingZalo = By.cssSelector("a[href*='zalo.me']");
    private final By floatingHotline = By.cssSelector("a[href*='tel:']");

    public boolean isPageLoaded() {
        return isDisplayed(headerLogo);
    }

    public RepairTrackingPage navigateToTracking() {
        click(linkTracking, "Menu Tra Cứu Tiến Độ");
        return new RepairTrackingPage();
    }

    public RepairLoginPage navigateToAdminPortal() {
        click(linkAdminPortal, "Nút Cổng KTV");
        return new RepairLoginPage();
    }

    public RepairBookingPage clickBookingService() {
        click(btnBookNow, "Nút Đặt Lịch Hẹn");
        return new RepairBookingPage();
    }

    public boolean isFloatingWidgetsDisplayed() {
        return isDisplayed(floatingZalo) || isDisplayed(floatingHotline);
    }
}
