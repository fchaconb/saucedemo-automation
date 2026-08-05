package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object de "Checkout: Complete!" (/checkout-complete.html).
 * Cubre: TC-E2E-01, TC-E2E-02
 */
public class CheckoutCompletePage extends BasePage {

    private final By completeHeader = By.className("complete-header");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public boolean isAt() {
        return isVisible(completeHeader);
    }

    public String getConfirmationMessage() {
        return getText(completeHeader);
    }
}
