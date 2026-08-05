package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object de "Checkout: Overview" (/checkout-step-two.html).
 * Cubre: TC-E2E-01, TC-E2E-02, TC-E2E-05 (validacion de calculo de total)
 */
public class CheckoutStepTwoPage extends BasePage {

    private final By summaryInfo = By.className("summary_info");
    private final By subtotalLabel = By.className("summary_subtotal_label");
    private final By taxLabel = By.className("summary_tax_label");
    private final By totalLabel = By.className("summary_total_label");
    private final By finishButton = By.id("finish");
    private final By cancelButton = By.id("cancel");

    public CheckoutStepTwoPage(WebDriver driver) {
        super(driver);
    }

    public boolean isAt() {
        return isVisible(summaryInfo);
    }

    private double extractAmount(String rawText) {
        // Ej: "Item total: $29.99" -> 29.99
        return Double.parseDouble(rawText.replaceAll("[^0-9.]", ""));
    }

    public double getItemTotal() {
        return extractAmount(getText(subtotalLabel));
    }

    public double getTax() {
        return extractAmount(getText(taxLabel));
    }

    public double getTotal() {
        return extractAmount(getText(totalLabel));
    }

    public CheckoutCompletePage finish() {
        click(finishButton);
        return new CheckoutCompletePage(driver);
    }

    public CartPage cancel() {
        click(cancelButton);
        return new CartPage(driver);
    }
}
