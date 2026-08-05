package com.saucedemo.tests;

import com.saucedemo.pages.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Automatizacion del flujo E2E de compra completa.
 * Corresponde a los casos TC-E2E-01 .. TC-E2E-05 del documento de pruebas manuales.
 */
class E2EPurchaseTest extends BaseTest {

    private InventoryPage inventoryPage;

    @BeforeEach
    void login() {
        inventoryPage = new LoginPage(driver).open().loginAs("standard_user", "secret_sauce");
    }

    @Test
    @DisplayName("TC-E2E-01 - Compra exitosa de un solo producto (flujo completo)")
    void compraExitosaDeUnProducto() {
        inventoryPage.addProductToCart("sauce-labs-backpack");

        CartPage cartPage = inventoryPage.goToCart();
        assertTrue(cartPage.getProductNamesInCart().contains("Sauce Labs Backpack"));

        CheckoutStepOnePage stepOne = cartPage.checkout();
        stepOne.fillInfo("Mauro", "Vargas", "10101");
        CheckoutStepTwoPage stepTwo = stepOne.continueCheckout();

        assertTrue(stepTwo.isAt());
        double itemTotal = stepTwo.getItemTotal();
        double tax = stepTwo.getTax();
        double total = stepTwo.getTotal();
        assertEquals(Math.round((itemTotal + tax) * 100.0) / 100.0, total, 0.01,
                "El total debe ser igual a Item total + Tax (TC-E2E-05)");

        CheckoutCompletePage completePage = stepTwo.finish();

        assertTrue(completePage.isAt());
        assertTrue(completePage.getConfirmationMessage().contains("Thank you for your order"));
    }

    @Test
    @DisplayName("TC-E2E-02 - Compra exitosa de multiples productos")
    void compraExitosaDeMultiplesProductos() {
        inventoryPage.addProductToCart("sauce-labs-backpack");
        inventoryPage.addProductToCart("sauce-labs-bike-light");
        inventoryPage.addProductToCart("sauce-labs-bolt-t-shirt");

        CartPage cartPage = inventoryPage.goToCart();
        List<String> productos = cartPage.getProductNamesInCart();
        assertEquals(3, productos.size());

        CheckoutStepOnePage stepOne = cartPage.checkout();
        CheckoutStepTwoPage stepTwo = stepOne.fillInfo("Mauro", "Vargas", "10101").continueCheckout();

        assertTrue(stepTwo.isAt());
        CheckoutCompletePage completePage = stepTwo.finish();

        assertTrue(completePage.isAt());
        assertTrue(completePage.getConfirmationMessage().contains("Thank you for your order"));
    }

    @ParameterizedTest(name = "TC-E2E-03 - firstName=[{0}] lastName=[{1}] zip=[{2}] -> error contiene ''{3}''")
    @DisplayName("Checkout con campos obligatorios vacios (tabla de decision)")
    @CsvSource({
            "'', Vargas, 10101, First Name is required",
            "Mauro, '', 10101, Last Name is required",
            "Mauro, Vargas, '', Postal Code is required",
            "'', '', '', First Name is required"
    })
    void checkoutConCamposVaciosMuestraError(String firstName, String lastName, String zip, String expectedFragment) {
        inventoryPage.addProductToCart("sauce-labs-backpack");
        CartPage cartPage = inventoryPage.goToCart();
        CheckoutStepOnePage stepOne = cartPage.checkout();

        stepOne.fillInfo(firstName, lastName, zip).continueExpectingError();

        assertTrue(stepOne.isErrorDisplayed());
        assertTrue(stepOne.getErrorText().contains(expectedFragment),
                "Se esperaba que el error contuviera: " + expectedFragment);
    }

    @Test
    @DisplayName("TC-E2E-04 - Cancelar el checkout desde el paso de informacion")
    void cancelarCheckoutDesdeStepOne() {
        inventoryPage.addProductToCart("sauce-labs-backpack");
        CartPage cartPage = inventoryPage.goToCart();
        CheckoutStepOnePage stepOne = cartPage.checkout();

        CartPage cartPageDespuesDeCancelar = stepOne.cancel();

        assertTrue(cartPageDespuesDeCancelar.isAt());
        assertTrue(cartPageDespuesDeCancelar.getProductNamesInCart().contains("Sauce Labs Backpack"),
                "El producto agregado previamente debe seguir en el carrito tras cancelar");
    }
}
