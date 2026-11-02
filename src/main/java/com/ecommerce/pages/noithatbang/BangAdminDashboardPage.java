package com.ecommerce.pages.noithatbang;

import com.ecommerce.pages.base.BasePage;
import com.ecommerce.utils.ConfigReader;
import org.openqa.selenium.By;

/**
 * Page Object for Website_NoiThat Admin Dashboard Page (/)
 * Port: 5173
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangAdminDashboardPage extends BasePage {

    // Sidebar navigation locators
    private final By brandTitle = By.xpath("//aside//span[contains(text(),'GÓC NHÀ ĐẸP') or contains(text(),'Quản Trị Cửa Hàng') or contains(text(),'NỘI THẤT ADMIN')]");
    private final By navDashboard = By.cssSelector("aside nav a[href='/']");
    private final By navProducts = By.cssSelector("aside nav a[href='/products']");
    private final By navCategories = By.cssSelector("aside nav a[href='/categories']");
    private final By navBrands = By.cssSelector("aside nav a[href='/brands']");
    private final By navOrders = By.cssSelector("aside nav a[href='/orders']");
    private final By navCustomers = By.cssSelector("aside nav a[href='/customers']");
    private final By navCoupons = By.cssSelector("aside nav a[href='/coupons']");
    private final By navReviews = By.cssSelector("aside nav a[href='/reviews']");
    private final By navContacts = By.cssSelector("aside nav a[href='/contacts']");
    private final By navBanners = By.cssSelector("aside nav a[href='/banners']");
    private final By navSettings = By.cssSelector("aside nav a[href='/settings']");

    // Header & Sidebar toggle
    private final By btnToggleSidebar = By.xpath("//header//button[.//*[name()='svg']]");

    // Dashboard content locators
    private final By headingDashboard = By.xpath("//h1[contains(text(),'Bảng Điều Khiển Tổng Quan')]");
    private final By statCards = By.cssSelector(".grid > div.bg-white");
    private final By cardRevenue = By.xpath("//span[contains(text(),'Doanh thu')]");
    private final By cardOrders = By.xpath("//span[contains(text(),'đơn hàng') or contains(text(),'Đơn hàng')]");
    private final By cardCustomers = By.xpath("//span[contains(text(),'Khách hàng')]");
    private final By cardProducts = By.xpath("//span[contains(text(),'Sản phẩm')]");

    // Chart & Filters
    private final By revenueChartContainer = By.cssSelector(".recharts-responsive-container, .h-72");
    private final By btnRevenue7Days = By.xpath("//button[contains(.,'7 ngày')]");
    private final By btnRevenue14Days = By.xpath("//button[contains(.,'14 ngày')]");
    private final By btnRevenue30Days = By.xpath("//button[contains(.,'30 ngày')]");

    // Sections
    private final By topProductsSection = By.xpath("//h2[contains(text(),'Top Sản phẩm') or contains(text(),'bán chạy')]");
    private final By recentOrdersSection = By.xpath("//h2[contains(text(),'Đơn hàng mới') or contains(text(),'mới nhất')]");
    private final By linkViewAllOrders = By.xpath("//a[contains(@href,'/orders')]");
    private final By lowStockSection = By.xpath("//h2[contains(text(),'Cảnh báo') or contains(text(),'tồn kho')]");

    public boolean isPageLoaded() {
        return isDisplayed(headingDashboard) || isDisplayed(brandTitle);
    }

    public boolean isHeadingDisplayed() {
        return isDisplayed(headingDashboard);
    }

    public boolean isRevenueCardDisplayed() {
        return isDisplayed(cardRevenue);
    }

    public boolean isOrdersCardDisplayed() {
        return isDisplayed(cardOrders);
    }

    public boolean isCustomersCardDisplayed() {
        return isDisplayed(cardCustomers);
    }

    public boolean isProductsCardDisplayed() {
        return isDisplayed(cardProducts);
    }

    public int getStatCardsCount() {
        try {
            return findAll(statCards).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isRevenueChartDisplayed() {
        return isDisplayed(revenueChartContainer);
    }

    public void filterRevenueDays(int days) {
        By target = switch (days) {
            case 7 -> btnRevenue7Days;
            case 30 -> btnRevenue30Days;
            default -> btnRevenue14Days;
        };
        click(target, "Lọc doanh thu " + days + " ngày");
    }

    public boolean isTopProductsDisplayed() {
        return isDisplayed(topProductsSection);
    }

    public boolean isRecentOrdersDisplayed() {
        return isDisplayed(recentOrdersSection);
    }

    public boolean isLowStockSectionDisplayed() {
        return isDisplayed(lowStockSection);
    }

    private String getAdminBaseUrl() {
        return ConfigReader.get("noithatbang.admin.url", "http://localhost:5173");
    }

    public BangAdminProductPage navigateToProducts() {
        try {
            click(navProducts, "Menu Quản lý Sản phẩm");
        } catch (Exception e) {
            getDriver().navigate().to(getAdminBaseUrl() + "/products");
        }
        return new BangAdminProductPage();
    }

    public BangAdminCategoryPage navigateToCategories() {
        try {
            click(navCategories, "Menu Quản lý Danh mục");
        } catch (Exception e) {
            getDriver().navigate().to(getAdminBaseUrl() + "/categories");
        }
        return new BangAdminCategoryPage();
    }

    public BangAdminBrandPage navigateToBrands() {
        try {
            click(navBrands, "Menu Quản lý Thương hiệu");
        } catch (Exception e) {
            getDriver().navigate().to(getAdminBaseUrl() + "/brands");
        }
        return new BangAdminBrandPage();
    }

    public BangAdminOrdersPage navigateToOrders() {
        try {
            click(navOrders, "Menu Quản lý Đơn hàng");
        } catch (Exception e) {
            getDriver().navigate().to(getAdminBaseUrl() + "/orders");
        }
        return new BangAdminOrdersPage();
    }

    public BangAdminCustomerPage navigateToCustomers() {
        try {
            click(navCustomers, "Menu Quản lý Khách hàng");
        } catch (Exception e) {
            getDriver().navigate().to(getAdminBaseUrl() + "/customers");
        }
        return new BangAdminCustomerPage();
    }

    public BangAdminCouponPage navigateToCoupons() {
        try {
            click(navCoupons, "Menu Quản lý Mã giảm giá");
        } catch (Exception e) {
            getDriver().navigate().to(getAdminBaseUrl() + "/coupons");
        }
        return new BangAdminCouponPage();
    }

    public BangAdminReviewsPage navigateToReviews() {
        try {
            click(navReviews, "Menu Quản lý Đánh giá");
        } catch (Exception e) {
            getDriver().navigate().to(getAdminBaseUrl() + "/reviews");
        }
        return new BangAdminReviewsPage();
    }

    public BangAdminContactPage navigateToContacts() {
        try {
            click(navContacts, "Menu Quản lý Liên hệ");
        } catch (Exception e) {
            getDriver().navigate().to(getAdminBaseUrl() + "/contacts");
        }
        return new BangAdminContactPage();
    }

    public BangAdminBannerPage navigateToBanners() {
        try {
            click(navBanners, "Menu Quản lý Banner");
        } catch (Exception e) {
            getDriver().navigate().to(getAdminBaseUrl() + "/banners");
        }
        return new BangAdminBannerPage();
    }

    public BangAdminSettingPage navigateToSettings() {
        try {
            click(navSettings, "Menu Quản lý Cài đặt");
        } catch (Exception e) {
            getDriver().navigate().to(getAdminBaseUrl() + "/settings");
        }
        return new BangAdminSettingPage();
    }

    public void clickToggleSidebar() {
        if (isDisplayed(btnToggleSidebar)) {
            click(btnToggleSidebar, "Nút ẩn/hiện Sidebar");
        }
    }
}
