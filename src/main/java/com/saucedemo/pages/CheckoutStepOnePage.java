package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object de "Checkout: Your Information" (/checkout-step-one.html).
 * Cubre: TC-E2E-01, TC-E2E-02, TC-E2E-03 (tabla de decision), TC-E2E-04
 */
public class CheckoutStepOnePage extends BasePage {

    private final By firstNameInput = By.id("first-name");
    private final By lastNameInput = By.id("last-name");
    private final By postalCodeInput = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By cancelButton = By.id("cancel");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");

    public CheckoutStepOnePage(WebDriver driver) {
        super(driver);
    }

    public boolean isAt() {
        return isVisible(firstNameInput);
    }

    public CheckoutStepOnePage fillInfo(String firstName, String lastName, String postalCode) {
        if (firstName != null) type(firstNameInput, firstName);
        if (lastName != null) type(lastNameInput, lastName);
        if (postalCode != null) type(postalCodeInput, postalCode);
        return this;
    }

    public CheckoutStepTwoPage continueCheckout() {
        click(continueButton);
        return new CheckoutStepTwoPage(driver);
    }

    /** Usado cuando se espera un error de validacion y no se navega a step two. */
    public CheckoutStepOnePage continueExpectingError() {
        click(continueButton);
        return this;
    }

    public boolean isErrorDisplayed() {
        return isVisible(errorMessage);
    }

    public String getErrorText() {
        return getText(errorMessage);
    }

    public CartPage cancel() {
        click(cancelButton);
        return new CartPage(driver);
    }
}
