package com.ecommerce.pages.noithatbang;

import com.ecommerce.pages.base.BasePage;
import com.ecommerce.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for Website_NoiThat Admin Banner Management Page (/banners)
 * Port: 5173
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangAdminBannerPage extends BasePage {

    private final By headingBanner = By.xpath("//h1[contains(.,'Banner quảng cáo')]");
    private final By btnCreateBanner = By.xpath("//button[contains(.,'Thêm banner')]");

    // Modal Form
    private final By modalHeading = By.xpath("//h2[contains(.,'banner') or contains(.,'Banner')]");
    private final By inputTitle = By.xpath("//input[@name='title']");
    private final By inputSubtitle = By.xpath("//input[@name='subtitle']");
    private final By inputLink = By.xpath("//input[@name='link']");
    private final By inputSortOrder = By.xpath("//input[@name='sort_order']");
    private final By btnSave = By.xpath("//button[@type='submit' and contains(.,'Lưu')]");
    private final By btnCancelModal = By.xpath("//button[contains(.,'Hủy')]");

    // Table
    private final By tableRows = By.cssSelector("table tbody tr");
    private final By emptyStateRow = By.xpath("//table//tbody//tr[contains(.,'Chưa có') or contains(.,'chưa có')]");
    private final By btnDeleteFirst = By.xpath("(//table//tbody//tr[not(contains(.,'Chưa có'))]//button[contains(@class,'text-rose') or contains(@class,'text-red') or contains(@title,'Xóa') or descendant::*[local-name()='svg' and contains(@class,'lucide-trash')]])[1]");
    private final By confirmDeleteDialog = By.xpath("//h3[contains(.,'Xác nhận xóa')] | //div[contains(@class,'fixed')]//button[contains(.,'Xóa')]");
    private final By btnCancelDelete = By.xpath("//div[contains(@class,'fixed')]//button[contains(.,'Hủy')]");

    public boolean hasBanners() {
        return !isDisplayed(emptyStateRow) && isDisplayed(btnDeleteFirst);
    }

    public boolean isPageLoaded() {
        return isDisplayed(headingBanner) && isDisplayed(btnCreateBanner);
    }

    public void openCreateModal() {
        click(btnCreateBanner, "Nút Thêm banner");
    }

    public boolean isModalOpen() {
        return isDisplayed(modalHeading) || isDisplayed(inputTitle);
    }

    public void fillBannerForm(String title, String subtitle, String link) {
        type(inputTitle, title, "Tiêu đề banner");
        if (subtitle != null && isDisplayed(inputSubtitle)) {
            type(inputSubtitle, subtitle, "Phụ đề banner");
        }
        if (link != null && isDisplayed(inputLink)) {
            type(inputLink, link, "Liên kết banner");
        }
    }

    public void submitSave() {
        click(btnSave, "Nút Lưu banner");
        WaitUtil.waitForInvisibility(inputTitle, 10);
    }

    public void closeModal() {
        click(btnCancelModal, "Nút Hủy modal banner");
        WaitUtil.waitForInvisibility(inputTitle, 5);
    }

    public int getBannerCount() {
        try {
            List<WebElement> rows = findAll(tableRows);
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void clickDeleteFirstBanner() {
        click(btnDeleteFirst, "Icon Xóa banner đầu tiên");
    }

    public boolean isConfirmDeleteDialogDisplayed() {
        return isDisplayed(confirmDeleteDialog);
    }

    public void cancelDeleteDialog() {
        click(btnCancelDelete, "Hủy dialog xác nhận xóa banner");
    }
}
