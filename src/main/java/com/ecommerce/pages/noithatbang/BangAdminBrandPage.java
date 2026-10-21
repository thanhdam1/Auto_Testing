package com.ecommerce.pages.noithatbang;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for Website_NoiThat Admin Brands Page (/brands)
 * Port: 5173
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangAdminBrandPage extends BasePage {

    private final By headingBrand = By.xpath("//h1[contains(.,'Thương hiệu')]");
    private final By btnCreateBrand = By.xpath("//button[contains(.,'Thêm thương hiệu')]");

    // Modal Form
    private final By modalHeading = By.xpath("//h2[contains(.,'thương hiệu') or contains(.,'Thương hiệu')]");
    private final By inputName = By.xpath("//input[@name='name']");
    private final By inputSlug = By.xpath("//input[@name='slug']");
    private final By btnSave = By.xpath("//button[@type='submit']");
    private final By btnCancelModal = By.xpath("//button[contains(.,'Hủy')]");

    // Table
    private final By tableRows = By.cssSelector("table tbody tr");
    private final By btnDeleteFirst = By.cssSelector("table tbody tr:first-child button.text-rose-600, table tbody tr:first-child td:last-child button:last-child, table tbody tr:first-child button[title*='Xóa']");
    private final By confirmDeleteDialog = By.xpath("//h3[contains(.,'Xác nhận xóa')]");
    private final By btnCancelDelete = By.xpath("//div[contains(@class,'fixed')]//button[contains(.,'Hủy')]");

    public boolean isPageLoaded() {
        return isDisplayed(headingBrand) && isDisplayed(btnCreateBrand);
    }

    public void openCreateModal() {
        click(btnCreateBrand, "Nút Thêm thương hiệu");
    }

    public boolean isModalOpen() {
        return isDisplayed(modalHeading) || isDisplayed(inputName);
    }

    public void fillBrandForm(String name, String slug) {
        type(inputName, name, "Tên thương hiệu");
        if (slug != null && isDisplayed(inputSlug)) {
            type(inputSlug, slug, "Slug thương hiệu");
        }
    }

    public void submitSave() {
        click(btnSave, "Nút Lưu thương hiệu");
    }

    public void closeModal() {
        click(btnCancelModal, "Nút Hủy modal thương hiệu");
    }

    public boolean hasBrands() {
        return isDisplayed(btnDeleteFirst);
    }

    public int getBrandCount() {
        try {
            List<WebElement> rows = findAll(tableRows);
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void clickDeleteFirstBrand() {
        click(btnDeleteFirst, "Icon Xóa thương hiệu đầu tiên");
    }

    public boolean isConfirmDeleteDialogDisplayed() {
        return isDisplayed(confirmDeleteDialog);
    }

    public void cancelDeleteDialog() {
        click(btnCancelDelete, "Hủy dialog xác nhận xóa thương hiệu");
    }
}
