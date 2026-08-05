package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object de la pantalla de Login (https://www.saucedemo.com/).
 * Cubre: TC-LOG-01 a TC-LOG-10
 */
public class LoginPage extends BasePage {

    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");
    private final By errorCloseButton = By.className("error-button");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        driver.get(BASE_URL);
        return this;
    }

    public InventoryPage loginAs(String username, String password) {
        type(usernameInput, username);
        type(passwordInput, password);
        click(loginButton);
        return new InventoryPage(driver);
    }

    /** Usado cuando se espera que el login falle y el usuario permanezca en esta pagina. */
    public LoginPage attemptLogin(String username, String password) {
        type(usernameInput, username);
        type(passwordInput, password);
        click(loginButton);
        return this;
    }

    public boolean isErrorDisplayed() {
        return isVisible(errorMessage);
    }

    public String getErrorText() {
        return getText(errorMessage);
    }

    public void closeError() {
        click(errorCloseButton);
    }

    public boolean isAt() {
        return driver.getCurrentUrl().equals(BASE_URL) || driver.getCurrentUrl().endsWith("saucedemo.com/");
    }
}
