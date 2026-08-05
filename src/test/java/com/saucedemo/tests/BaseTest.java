package com.saucedemo.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Clase base para todas las clases de prueba.
 * Se encarga de inicializar y cerrar el WebDriver antes/despues de cada @Test,
 * evitando duplicar el setup en cada clase.
 */
public abstract class BaseTest {

    protected WebDriver driver;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        // Descomentar la siguiente linea para correr en modo headless (ej. en CI/CD)
        // options.addArguments("--headless=new");
        options.addArguments("--window-size=1400,900");
        driver = new ChromeDriver(options);
        driver.manage().deleteAllCookies();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
