package com.ecommerce.pages.noithatbang;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for Website_NoiThat Admin Categories Page (/categories)
 * Port: 5173
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangAdminCategoryPage extends BasePage {

    private final By headingCategory = By.xpath("//h1[contains(.,'Danh mục sản phẩm')]");
    private final By btnCreateCategory = By.xpath("//button[contains(.,'Thêm danh mục')]");

    // Modal Form
    private final By modalHeading = By.xpath("//h2[contains(.,'danh mục') or contains(.,'Danh mục')]");
    private final By inputName = By.xpath("//input[@name='name']");
    private final By inputSlug = By.xpath("//input[@name='slug']");
    private final By selectParent = By.xpath("//select[@name='parent_id']");
    private final By inputSortOrder = By.xpath("//input[@name='sort_order']");
    private final By btnSave = By.xpath("//button[@type='submit']");
    private final By btnCancelModal = By.xpath("//button[contains(.,'Hủy')]");

    // Table & Actions
    private final By tableRows = By.cssSelector("table tbody tr");
    private final By btnDeleteFirst = By.cssSelector("table tbody tr:first-child button.text-rose-600, table tbody tr:first-child td:last-child button:last-child, table tbody tr:first-child button[title*='Xóa']");

    // Confirm Dialog
    private final By confirmDeleteDialog = By.xpath("//h3[contains(.,'Xác nhận xóa')]");
    private final By btnCancelDelete = By.xpath("//div[contains(@class,'fixed')]//button[contains(.,'Hủy')]");
    private final By btnConfirmDelete = By.xpath("//div[contains(@class,'fixed')]//button[contains(.,'Xóa')]");

    public boolean isPageLoaded() {
        return isDisplayed(headingCategory) && isDisplayed(btnCreateCategory);
    }

    public void openCreateModal() {
        click(btnCreateCategory, "Nút Thêm danh mục");
    }

    public boolean isModalOpen() {
        return isDisplayed(modalHeading) || isDisplayed(inputName);
    }

    public void fillCategoryForm(String name, String slug) {
        type(inputName, name, "Tên danh mục");
        if (slug != null && isDisplayed(inputSlug)) {
            type(inputSlug, slug, "Slug danh mục");
        }
    }

    public void submitSave() {
        click(btnSave, "Nút Tạo danh mục / Lưu");
    }

    public void closeModal() {
        click(btnCancelModal, "Nút Hủy modal danh mục");
    }

    public int getCategoryCount() {
        try {
            List<WebElement> rows = findAll(tableRows);
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void clickDeleteFirstCategory() {
        click(btnDeleteFirst, "Icon Xóa danh mục đầu tiên");
    }

    public boolean isConfirmDeleteDialogDisplayed() {
        return isDisplayed(confirmDeleteDialog);
    }

    public void cancelDeleteDialog() {
        click(btnCancelDelete, "Nút Hủy trong dialog xác nhận xóa");
    }
}
