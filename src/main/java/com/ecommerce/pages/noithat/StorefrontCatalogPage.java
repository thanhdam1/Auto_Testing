package com.ecommerce.pages.noithat;

import com.ecommerce.pages.base.BasePage;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.LogUtil;
import com.ecommerce.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Page Object for Storefront Product Catalog (/san-pham).
 *
 * <p>Locators được đối chiếu với DOM contract của ứng dụng Nội Thất:
 * {@code [data-product]} (kèm {@code data-name},
 * {@code data-price}, {@code data-category}, {@code data-space}), {@code [data-result-count]},
 * {@code select[data-sort]}, các input {@code [data-filter='category'|'space'|'price']},
 * {@code [data-filter-reset]}, {@code [data-empty]}, nút thêm nhanh {@code [data-quick-add]}.</p>
 *
 * <p>Lưu ý: trang có phân trang (mỗi thời điểm chỉ một số card không bị class {@code hidden})
 * nên mọi truy vấn "card đầu tiên" đều lọc theo {@code isDisplayed()}.</p>
 */
public class StorefrontCatalogPage extends BasePage {

    /** Giá trị sort hợp lệ của trang (xem các option trong san-pham/index.astro). */
    public static final String SORT_DEFAULT = "default";
    public static final String SORT_PRICE_ASC = "price-asc";
    public static final String SORT_PRICE_DESC = "price-desc";
    public static final String SORT_NAME_ASC = "name-asc";

    // Locators
    private final By headingCatalog = By.xpath("//h1[contains(text(),'Tất cả sản phẩm')]");
    private final By productCards = By.cssSelector("[data-product]");
    private final By lblResultCount = By.cssSelector("[data-result-count]");
    private final By selectSort = By.cssSelector("select[data-sort]");
    private final By btnResetFilter = By.cssSelector("[data-filter-reset]");
    private final By emptyFilterMessage = By.cssSelector("[data-empty]");
    private final By cartToast = By.id("cart-toast-notification");

    public boolean isPageLoaded() {
        return isDisplayed(headingCatalog);
    }

    public int getVisibleProductCount() {
        try {
            List<WebElement> cards = getDriver().findElements(productCards);
            int visible = 0;
            for (WebElement card : cards) {
                if (card.isDisplayed()) {
                    visible++;
                }
            }
            return visible;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Chờ sau khi filter/reset vì render() cập nhật class hidden và result count
     * đồng bộ trong cùng event handler, nhưng Selenium có thể đọc DOM trước khi
     * trình duyệt hoàn tất paint.
     */
    public int waitForVisibleProductCount(int expectedCount) {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(5))
                .until(driver -> getVisibleProductCount() == expectedCount
                        ? expectedCount
                        : null);
    }

    public int waitForVisibleProductsInCategory(String categoryKey) {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(5))
                .until(driver -> {
                    int count = getVisibleProductCount();
                    return count > 0
                            && getVisibleProductCategories().stream().allMatch(categoryKey::equals)
                            ? count
                            : null;
                });
    }

    /**
     * Card sản phẩm đầu tiên đang thực sự hiển thị (đã tính cả phân trang và bộ lọc).
     */
    private WebElement firstVisibleCard() {
        for (WebElement card : getDriver().findElements(productCards)) {
            if (card.isDisplayed()) {
                return card;
            }
        }
        throw new NoSuchElementException("Không có card sản phẩm nào hiển thị trên trang danh mục.");
    }

    public String getFirstProductSlug() {
        return firstVisibleCard().getAttribute("data-product");
    }

    public String getFirstProductName() {
        return firstVisibleCard().getAttribute("data-name");
    }

    public long getFirstProductPrice() {
        return CartPage.parseVnd(firstVisibleCard().getAttribute("data-price"));
    }

    public List<String> getVisibleProductSlugs() {
        List<String> slugs = new ArrayList<>();
        for (WebElement card : getDriver().findElements(productCards)) {
            if (card.isDisplayed()) {
                slugs.add(card.getAttribute("data-product"));
            }
        }
        return slugs;
    }

    /**
     * Danh sách giá (VND) của các card đang hiển thị - dùng để kiểm chứng thứ tự sắp xếp.
     */
    public List<Long> getVisibleProductPrices() {
        List<Long> prices = new ArrayList<>();
        for (WebElement card : getDriver().findElements(productCards)) {
            if (card.isDisplayed()) {
                prices.add(CartPage.parseVnd(card.getAttribute("data-price")));
            }
        }
        return prices;
    }

    public List<String> getVisibleProductSpaces() {
        List<String> spaces = new ArrayList<>();
        for (WebElement card : getDriver().findElements(productCards)) {
            if (card.isDisplayed()) {
                spaces.add(card.getAttribute("data-space"));
            }
        }
        return spaces;
    }

    public List<String> getVisibleProductCategories() {
        List<String> categories = new ArrayList<>();
        for (WebElement card : getDriver().findElements(productCards)) {
            if (card.isDisplayed()) {
                categories.add(card.getAttribute("data-category"));
            }
        }
        return categories;
    }

    public String getResultCountText() {
        return getText(lblResultCount);
    }

    /**
     * Tổng số sản phẩm mà bộ lọc tìm được (đọc từ dòng "Hiển thị 1–12 trên 24 sản phẩm").
     *
     * @return tổng số sản phẩm, hoặc -1 nếu không đọc được
     */
    public int getTotalFilteredProductCount() {
        Matcher matcher = Pattern.compile("trên\\s+([0-9]+)\\s+sản phẩm").matcher(getResultCountText());
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : -1;
    }

    public boolean isEmptyResultMessageDisplayed() {
        return isDisplayed(emptyFilterMessage);
    }

    /**
     * Số sản phẩm hiển thị tối đa trên mỗi trang (đọc từ dòng đếm kết quả).
     */
    public int getPageSize() {
        Matcher matcher = Pattern.compile("Hiển thị\\s+([0-9]+)[–-]([0-9]+)").matcher(getResultCountText());
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(2)) - Integer.parseInt(matcher.group(1)) + 1;
        }
        return -1;
    }

    public void filterByCategory(String categoryKey) {
        By checkbox = By.cssSelector("[data-filter='category'][value='" + categoryKey + "']");
        click(checkbox, "Lọc danh mục: " + categoryKey);
    }

    public void filterBySpace(String spaceKey) {
        By checkbox = By.cssSelector("input[data-filter='space'][value='" + spaceKey + "']");
        click(checkbox, "Lọc không gian: " + spaceKey);
    }

    public void filterByPrice(String priceRangeKey) {
        By radio = By.cssSelector("input[data-filter='price'][value='" + priceRangeKey + "']");
        click(radio, "Lọc khoảng giá: " + priceRangeKey);
    }

    public void resetAllFilters() {
        click(btnResetFilter, "Nút Xóa bộ lọc");
    }

    public void selectSortOption(String sortValue) {
        selectDropdownByValue(selectSort, sortValue, "Sắp xếp sản phẩm");
    }

    public boolean isProductWithTitleDisplayed(String title) {
        try {
            List<WebElement> cards = getDriver().findElements(productCards);
            for (WebElement card : cards) {
                if (card.isDisplayed() && card.getText().toLowerCase().contains(title.toLowerCase())) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Thêm nhanh sản phẩm đầu tiên vào giỏ bằng nút hover {@code [data-quick-add]}.
     */
    public void quickAddFirstProduct() {
        WebElement card = firstVisibleCard();
        WebElement quickAdd = card.findElement(By.cssSelector("[data-quick-add]"));
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", quickAdd);
        LogUtil.info("Đã thêm nhanh sản phẩm '{}' vào giỏ hàng.", card.getAttribute("data-product"));
    }

    public String waitForCartToast() {
        WaitUtil.waitForVisibility(cartToast, 5);
        return getText(cartToast);
    }

    public boolean isCartToastDisplayed() {
        return isDisplayed(cartToast);
    }

    /**
     * Mở trang chi tiết của sản phẩm đầu tiên đang hiển thị.
     */
    public ProductDetailPage openFirstProduct() {
        WebElement card = firstVisibleCard();
        WebElement link = card.findElement(By.cssSelector("a[href*='/san-pham/']"));
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView({block:'center'});", link);
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", link);
        return new ProductDetailPage();
    }

    /**
     * Mở trang chi tiết theo slug cụ thể.
     */
    public ProductDetailPage openProductBySlug(String slug) {
        getDriver().get(ConfigReader.get("app.url", "http://localhost:4321") + "/san-pham/" + slug);
        return new ProductDetailPage();
    }
}
