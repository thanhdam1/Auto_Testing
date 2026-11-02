package com.ecommerce.pages.noithat;

import com.ecommerce.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for Admin Category Management (/admin/danh-muc).
 */
public class AdminCategoryPage extends BasePage {

    private final By pageHeader = By.xpath("//h1[contains(text(),'Quản Lý Danh Mục')]");
    private final By btnOpenAddModal = By.id("btn-open-add-cat-modal");
    private final By statTotalCats = By.id("stat-total-cats");
    private final By categoryRows = By.cssSelector("#cat-table-body tr[data-cat-row]");

    // Modal elements
    private final By modal = By.id("cat-modal");
    private final By inputName = By.id("cat-name-input");
    private final By inputSlug = By.id("cat-slug-input");
    private final By inputDesc = By.id("cat-desc-input");
    private final By btnCloseModal = By.id("btn-close-cat-modal");
    private final By btnSubmitForm = By.cssSelector("#cat-form button[type='submit']");

    public boolean isPageLoaded() {
        return isDisplayed(pageHeader);
    }

    public int getCategoryRowCount() {
        try {
            List<WebElement> rows = findAll(categoryRows);
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public String getTotalCategoryStatText() {
        return getText(statTotalCats);
    }

    public void openAddCategoryModal() {
        click(btnOpenAddModal, "Nút Thêm Danh Mục");
    }

    public boolean isModalDisplayed() {
        return isDisplayed(modal);
    }

    public void fillCategoryForm(String name, String slug, String desc) {
        if (name != null) type(inputName, name, "Tên danh mục");
        if (slug != null) type(inputSlug, slug, "Slug danh mục");
        if (desc != null) type(inputDesc, desc, "Mô tả danh mục");
    }

    public void closeModal() {
        click(btnCloseModal, "Đóng modal danh mục");
    }

    public boolean hasCategoryName(String catName) {
        try {
            List<WebElement> rows = findAll(categoryRows);
            for (WebElement row : rows) {
                if (row.getText().toLowerCase().contains(catName.toLowerCase())) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
