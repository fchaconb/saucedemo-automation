package com.saucedemo.tests;

import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Automatizacion del modulo de Login.
 * Corresponde a los casos TC-LOG-01 .. TC-LOG-10 del documento de pruebas manuales.
 */
class LoginTest extends BaseTest {

    private static final String VALID_PASSWORD = "secret_sauce";

    @Test
    @DisplayName("TC-LOG-01 - Login exitoso con credenciales validas")
    void loginExitosoConCredencialesValidas() {
        LoginPage loginPage = new LoginPage(driver).open();
        InventoryPage inventoryPage = loginPage.loginAs("standard_user", VALID_PASSWORD);

        assertTrue(inventoryPage.isAt(), "Se esperaba navegar a la pagina de inventario tras un login valido");
        assertTrue(driver.getCurrentUrl().contains("inventory.html"));
    }

    @Test
    @DisplayName("TC-LOG-02 - Login con usuario bloqueado (locked_out_user)")
    void loginConUsuarioBloqueado() {
        LoginPage loginPage = new LoginPage(driver).open();
        loginPage.attemptLogin("locked_out_user", VALID_PASSWORD);

        assertTrue(loginPage.isErrorDisplayed(), "Se esperaba un mensaje de error para usuario bloqueado");
        assertTrue(loginPage.getErrorText().contains("locked out"));
    }

    @Test
    @DisplayName("TC-LOG-03 - Login con contrasena incorrecta")
    void loginConContrasenaIncorrecta() {
        LoginPage loginPage = new LoginPage(driver).open();
        loginPage.attemptLogin("standard_user", "password123");

        assertTrue(loginPage.isErrorDisplayed());
        assertTrue(loginPage.getErrorText().contains("do not match"));
    }

    @Test
    @DisplayName("TC-LOG-04 - Login con usuario inexistente")
    void loginConUsuarioInexistente() {
        LoginPage loginPage = new LoginPage(driver).open();
        loginPage.attemptLogin("usuario_random", VALID_PASSWORD);

        assertTrue(loginPage.isErrorDisplayed());
        assertTrue(loginPage.getErrorText().contains("do not match"));
    }

    @ParameterizedTest(name = "TC-LOG-05/06/07 - usuario=[{0}] password=[{1}] -> error contiene ''{2}''")
    @DisplayName("Login con campos obligatorios vacios")
    @CsvSource({
            "'', '', Username is required",
            "'', secret_sauce, Username is required",
            "standard_user, '', Password is required"
    })
    void loginConCamposVacios(String username, String password, String expectedErrorFragment) {
        LoginPage loginPage = new LoginPage(driver).open();
        loginPage.attemptLogin(username, password);

        assertTrue(loginPage.isErrorDisplayed());
        assertTrue(loginPage.getErrorText().contains(expectedErrorFragment),
                "Se esperaba que el error contuviera: " + expectedErrorFragment);
    }

    @Test
    @DisplayName("TC-LOG-09 - Login sensible a mayusculas/minusculas en el usuario")
    void loginEsSensibleAMayusculas() {
        LoginPage loginPage = new LoginPage(driver).open();
        loginPage.attemptLogin("STANDARD_USER", VALID_PASSWORD);

        assertTrue(loginPage.isErrorDisplayed(),
                "El login deberia fallar porque el usuario es sensible a mayusculas/minusculas");
    }

    @Test
    @DisplayName("TC-LOG-10 - Cerrar el mensaje de error con el boton X")
    void cerrarMensajeDeError() {
        LoginPage loginPage = new LoginPage(driver).open();
        loginPage.attemptLogin("standard_user", "clave_incorrecta");
        assertTrue(loginPage.isErrorDisplayed());

        loginPage.closeError();

        assertFalse(loginPage.isErrorDisplayed(), "El mensaje de error deberia desaparecer al hacer clic en X");
    }
}
