package com.ecommerce.pages.noithat;

import com.ecommerce.pages.base.BasePage;
import com.ecommerce.utils.LogUtil;
import com.ecommerce.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for Admin Product Management (/admin/san-pham)
 */
public class AdminProductPage extends BasePage {

    // Header & Actions
    private final By pageTitle = By.xpath("//h1[contains(text(),'Quản Lý Sản Phẩm')]");
    private final By btnOpenAddModal = By.id("btn-open-add-modal");

    // Filter & Search Controls
    private final By inputSearch = By.cssSelector("input[data-adm-search]");
    private final By selectCategory = By.cssSelector("select[data-adm-category]");
    private final By selectStatus = By.cssSelector("select[data-adm-status]");
    private final By btnResetFilter = By.cssSelector("button[data-adm-reset]");
    private final By lblProductCount = By.cssSelector("p[data-adm-count]");
    private final By emptyMessage = By.cssSelector("p[data-adm-empty]");

    // Table & Rows
    private final By tableRows = By.cssSelector("tbody[data-adm-body] tr[data-row]");
    private final By btnToggleStatus = By.cssSelector("button[data-btn-toggle-status]");
    private final By btnEdit = By.cssSelector("button[data-btn-edit]");
    private final By btnDelete = By.cssSelector("button[data-btn-delete]");

    // Add / Edit Modal
    private final By modalProduct = By.id("product-modal");
    private final By modalTitle = By.id("modal-title");
    private final By inputProdName = By.id("prod-name");
    private final By inputProdSlug = By.id("prod-slug");
    private final By inputProdPrice = By.id("prod-price");
    private final By selectProdCategory = By.id("prod-category");
    private final By selectProdSpace = By.id("prod-space");
    private final By inputProdMaterial = By.id("prod-material");
    private final By selectProdStatus = By.id("prod-status");
    private final By inputProdDimensions = By.id("prod-dimensions");
    private final By inputProdWarranty = By.id("prod-warranty");
    private final By txtProdDesc = By.id("prod-desc");
    private final By btnSubmitProduct = By.cssSelector("#product-form button[type='submit']");
    private final By btnCloseModal = By.id("btn-close-modal");
    private final By btnCancelModal = By.id("btn-cancel-modal");

    // Delete Modal
    private final By modalDelete = By.id("delete-modal");
    private final By btnConfirmDelete = By.id("btn-confirm-delete");
    private final By btnCancelDelete = By.id("btn-cancel-delete");

    // Toast
    private final By toastContainer = By.id("admin-toast");
    private final By toastMessage = By.id("toast-message");

    public boolean isPageLoaded() {
        return isDisplayed(pageTitle);
    }

    public void openAddProductModal() {
        click(btnOpenAddModal, "Button Thêm sản phẩm");
        WaitUtil.waitForVisibility(modalProduct, 5);
    }

    public boolean isAddProductModalDisplayed() {
        return isDisplayed(modalProduct);
    }

    public void closeAddProductModal() {
        if (isDisplayed(btnCloseModal)) {
            click(btnCloseModal, "Close modal button");
        } else if (isDisplayed(btnCancelModal)) {
            click(btnCancelModal, "Cancel modal button");
        }
    }

    public void fillProductForm(String name, String slug, String price, String categoryValue,
                                String spaceValue, String material, String statusValue, String desc) {
        type(inputProdName, name, "Tên sản phẩm");
        if (slug != null && !slug.isEmpty()) {
            type(inputProdSlug, slug, "Slug sản phẩm");
        }
        type(inputProdPrice, price, "Đơn giá");
        if (categoryValue != null && !categoryValue.isEmpty()) {
            selectDropdownByValue(selectProdCategory, categoryValue, "Danh mục");
        }
        if (spaceValue != null && !spaceValue.isEmpty()) {
            selectDropdownByValue(selectProdSpace, spaceValue, "Không gian");
        }
        if (material != null && !material.isEmpty()) {
            type(inputProdMaterial, material, "Chất liệu");
        }
        if (statusValue != null && !statusValue.isEmpty()) {
            selectDropdownByValue(selectProdStatus, statusValue, "Trạng thái kho");
        }
        if (desc != null && !desc.isEmpty()) {
            type(txtProdDesc, desc, "Mô tả sản phẩm");
        }
    }

    public void submitProductForm() {
        click(btnSubmitProduct, "Button Lưu sản phẩm");
    }

    public void searchProduct(String keyword) {
        type(inputSearch, keyword, "Ô tìm kiếm sản phẩm");
    }

    public void clearSearch() {
        find(inputSearch).clear();
    }

    public void filterByCategory(String categoryValue) {
        selectDropdownByValue(selectCategory, categoryValue, "Lọc danh mục");
    }

    public void filterByStatus(String statusValue) {
        selectDropdownByValue(selectStatus, statusValue, "Lọc trạng thái");
    }

    public void resetFilters() {
        click(btnResetFilter, "Nút Xóa lọc");
    }

    public int getVisibleProductRowCount() {
        try {
            List<WebElement> rows = getDriver().findElements(tableRows);
            int visible = 0;
            for (WebElement row : rows) {
                if (row.isDisplayed()) {
                    visible++;
                }
            }
            return visible;
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean hasProductInTable(String productName) {
        try {
            List<WebElement> rows = getDriver().findElements(tableRows);
            for (WebElement row : rows) {
                if (row.isDisplayed() && row.getText().toLowerCase().contains(productName.toLowerCase())) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void toggleFirstProductStatus() {
        click(btnToggleStatus, "Nút chuyển trạng thái sản phẩm đầu tiên");
    }

    public void clickFirstProductDelete() {
        click(btnDelete, "Nút xóa sản phẩm đầu tiên");
        WaitUtil.waitForVisibility(modalDelete, 5);
    }

    public boolean isDeleteConfirmModalDisplayed() {
        return isDisplayed(modalDelete);
    }

    public void cancelDelete() {
        click(btnCancelDelete, "Hủy xóa sản phẩm");
    }

    public boolean isToastMessageDisplayed() {
        return isDisplayed(toastContainer);
    }

    public String getToastText() {
        return getText(toastMessage);
    }

    /**
     * Chờ toast xuất hiện rồi trả về nội dung (dùng để assert thông báo sau khi thêm/sửa/xoá).
     */
    public String waitForToastText() {
        WaitUtil.waitForVisibility(toastContainer, 5);
        String text = getToastText();
        WaitUtil.waitForInvisibility(toastContainer, 5);
        return text;
    }

    // ==================================================================
    // BỔ SUNG PHỤC VỤ PHASE 1 (assert chặt hơn cho thêm/sửa/xoá/đổi trạng thái)
    // ==================================================================

    /**
     * Dòng sản phẩm đầu tiên đang hiển thị trong bảng.
     */
    private WebElement firstVisibleRow() {
        for (WebElement row : getDriver().findElements(tableRows)) {
            if (row.isDisplayed()) {
                return row;
            }
        }
        throw new org.openqa.selenium.NoSuchElementException("Bảng sản phẩm không có dòng nào hiển thị.");
    }

    /**
     * Dòng sản phẩm theo tên (không phân biệt hoa thường).
     */
    private WebElement rowByName(String productName) {
        for (WebElement row : getDriver().findElements(tableRows)) {
            if (row.isDisplayed() && row.getText().toLowerCase().contains(productName.toLowerCase())) {
                return row;
            }
        }
        throw new org.openqa.selenium.NoSuchElementException("Không tìm thấy dòng sản phẩm: " + productName);
    }

    public String getFirstProductName() {
        return firstVisibleRow().getAttribute("data-name");
    }

    /**
     * Giá trị trạng thái thật của dòng đầu tiên ({@code data-status}: con-hang / het-hang / nhap).
     */
    public String getFirstProductStatusValue() {
        return firstVisibleRow().getAttribute("data-status");
    }

    public String getFirstProductStatusLabel() {
        return firstVisibleRow().findElement(By.cssSelector("[data-btn-toggle-status]")).getText().trim();
    }

    public String getProductStatusValue(String productName) {
        return rowByName(productName).getAttribute("data-status");
    }

    public String getProductStatusLabel(String productName) {
        return rowByName(productName).findElement(By.cssSelector("[data-btn-toggle-status]")).getText().trim();
    }

    /**
     * Trạng thái kế tiếp theo vòng lặp của UI: con-hang -> het-hang -> nhap -> con-hang.
     */
    public static String nextStatusInCycle(String currentStatus) {
        return switch (currentStatus == null ? "" : currentStatus) {
            case "con-hang" -> "het-hang";
            case "het-hang" -> "nhap";
            default -> "con-hang";
        };
    }

    public String getProductCountLabelText() {
        return getText(lblProductCount);
    }

    public boolean isEmptyMessageDisplayed() {
        return isDisplayed(emptyMessage);
    }

    public void confirmDelete() {
        click(btnConfirmDelete, "Nút xác nhận xóa sản phẩm");
    }

    /**
     * Xoá sản phẩm theo tên: mở modal xác nhận rồi xác nhận xoá.
     */
    public void deleteProductByName(String productName) {
        WebElement row = rowByName(productName);
        row.findElement(By.cssSelector("[data-btn-delete]")).click();
        WaitUtil.waitForVisibility(modalDelete, 5);
        confirmDelete();
    }

    /**
     * Xoá sản phẩm theo tên nhưng KHÔNG xác nhận (dùng để kiểm tra nhánh huỷ).
     */
    public void requestDeleteProductByName(String productName) {
        WebElement row = rowByName(productName);
        row.findElement(By.cssSelector("[data-btn-delete]")).click();
        WaitUtil.waitForVisibility(modalDelete, 5);
    }
}
