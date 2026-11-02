package com.ecommerce.pages.noithatbang;

import com.ecommerce.pages.base.BasePage;
import com.ecommerce.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * Page Object for Website_NoiThat Admin Coupon Management Page (/coupons)
 * Port: 5173
 * Project: NguyenBang01/Website_NoiThat
 */
public class BangAdminCouponPage extends BasePage {

    private final By headingCoupon = By.xpath("//h1[contains(.,'Mã Giảm Giá') or contains(.,'Mã giảm giá') or contains(.,'Voucher')]");
    private final By btnCreateCoupon = By.xpath("//button[contains(.,'Thêm mã giảm giá') or contains(.,'Thêm mã') or contains(.,'Tạo mã mới')]");

    // Modal Form
    private final By modalHeading = By.xpath("//h2[contains(.,'Mã giảm giá') or contains(.,'mã') or contains(.,'Mã')] | //div[@role='dialog']//*[contains(.,'Mã giảm giá')]");
    private final By inputCode = By.xpath("//input[@name='code']");
    private final By selectDiscountType = By.xpath("//select[@name='discount_type']");
    private final By inputDiscountValue = By.xpath("//input[@name='discount_value']");
    private final By inputMinOrderAmount = By.xpath("//input[@name='min_order_amount']");
    private final By btnSave = By.xpath("//button[@type='submit' and (contains(.,'Lưu') or contains(.,'Tạo'))]");
    private final By btnCancelModal = By.xpath("//button[contains(.,'Hủy')]");

    // Table
    private final By tableRows = By.cssSelector("table tbody tr");
    private final By emptyStateRow = By.xpath("//table//tbody//tr[contains(.,'Chưa có') or contains(.,'chưa có')]");
    private final By btnDeleteFirst = By.xpath("(//table//tbody//tr[not(contains(.,'Chưa có'))]//button[contains(@class,'text-rose') or contains(@class,'text-red') or contains(@title,'Xóa') or descendant::*[local-name()='svg' and contains(@class,'lucide-trash')]])[1]");
    private final By confirmDeleteDialog = By.xpath("//h3[contains(.,'Xác nhận xóa')] | //div[contains(@class,'fixed')]//*[contains(.,'Xác nhận xóa')]");
    private final By btnCancelDelete = By.xpath("//div[contains(@class,'fixed')]//button[contains(.,'Hủy')]");

    public boolean isPageLoaded() {
        return isDisplayed(headingCoupon) && isDisplayed(btnCreateCoupon);
    }

    public void openCreateModal() {
        click(btnCreateCoupon, "Nút Tạo mã mới");
    }

    public boolean isModalOpen() {
        return isDisplayed(modalHeading) || isDisplayed(inputCode);
    }

    public void fillCouponForm(String code, String discountType, String discountValue, String minOrder) {
        type(inputCode, code, "Mã giảm giá");
        if (discountType != null && isDisplayed(selectDiscountType)) {
            Select select = new Select(getDriver().findElement(selectDiscountType));
            select.selectByValue(discountType);
        }
        if (discountValue != null && isDisplayed(inputDiscountValue)) {
            type(inputDiscountValue, discountValue, "Giá trị giảm");
        }
        if (minOrder != null && isDisplayed(inputMinOrderAmount)) {
            type(inputMinOrderAmount, minOrder, "Đơn tối thiểu");
        }
    }

    public void submitSave() {
        click(btnSave, "Nút Lưu mã giảm giá");
        WaitUtil.waitForInvisibility(inputCode, 10);
    }

    public void closeModal() {
        click(btnCancelModal, "Nút Hủy modal mã giảm giá");
        WaitUtil.waitForInvisibility(inputCode, 5);
    }

    public boolean hasCoupons() {
        return !isDisplayed(emptyStateRow) && isDisplayed(btnDeleteFirst);
    }

    public int getCouponCount() {
        try {
            List<WebElement> rows = findAll(tableRows);
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void clickDeleteFirstCoupon() {
        click(btnDeleteFirst, "Icon Xóa mã giảm giá đầu tiên");
    }

    public boolean isConfirmDeleteDialogDisplayed() {
        return isDisplayed(confirmDeleteDialog);
    }

    public void cancelDeleteDialog() {
        click(btnCancelDelete, "Hủy dialog xác nhận xóa mã giảm giá");
    }
}
