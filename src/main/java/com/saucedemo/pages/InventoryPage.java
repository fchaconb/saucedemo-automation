package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object de la pagina de inventario / inicio (/inventory.html).
 * Cubre: TC-INV-01 a TC-INV-08
 */
public class InventoryPage extends BasePage {

    private final By inventoryContainer = By.id("inventory_container");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By cartLink = By.className("shopping_cart_link");
    private final By sortDropdown = By.className("product_sort_container");
    private final By inventoryItemName = By.className("inventory_item_name");
    private final By inventoryItemPrice = By.className("inventory_item_price");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isAt() {
        return isVisible(inventoryContainer);
    }

    private By addToCartButtonFor(String productSlug) {
        return By.id("add-to-cart-" + productSlug);
    }

    private By removeButtonFor(String productSlug) {
        return By.id("remove-" + productSlug);
    }

    public InventoryPage addProductToCart(String productSlug) {
        click(addToCartButtonFor(productSlug));
        return this;
    }

    public InventoryPage removeProductFromCart(String productSlug) {
        click(removeButtonFor(productSlug));
        return this;
    }

    public boolean isProductInCart(String productSlug) {
        return isVisible(removeButtonFor(productSlug));
    }

    public int getCartCount() {
        if (!isVisible(cartBadge)) {
            return 0;
        }
        return Integer.parseInt(getText(cartBadge));
    }

    public CartPage goToCart() {
        click(cartLink);
        return new CartPage(driver);
    }

    public InventoryPage sortBy(String visibleOptionText) {
        Select select = new Select(waitVisible(sortDropdown));
        select.selectByVisibleText(visibleOptionText);
        return this;
    }

    public List<String> getProductNamesInOrder() {
        return driver.findElements(inventoryItemName)
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public List<Double> getProductPricesInOrder() {
        return driver.findElements(inventoryItemPrice)
                .stream()
                .map(el -> Double.parseDouble(el.getText().replace("$", "")))
                .collect(Collectors.toList());
    }

    public void openProductDetail(String productName) {
        click(By.linkText(productName));
    }
}
