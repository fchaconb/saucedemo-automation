package com.saucedemo.tests;

import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Automatizacion del modulo Pagina de inicio / Inventario.
 * Corresponde a los casos TC-INV-01 .. TC-INV-08 del documento de pruebas manuales.
 */
class InventoryTest extends BaseTest {

    private InventoryPage inventoryPage;

    @BeforeEach
    void login() {
        inventoryPage = new LoginPage(driver).open().loginAs("standard_user", "secret_sauce");
        assertTrue(inventoryPage.isAt(), "Precondicion: el login debe ser exitoso antes de cada prueba");
    }

    @Test
    @DisplayName("TC-INV-01 - Agregar un producto al carrito de compras")
    void agregarUnProductoAlCarrito() {
        inventoryPage.addProductToCart("sauce-labs-backpack");

        assertEquals(1, inventoryPage.getCartCount());
        assertTrue(inventoryPage.isProductInCart("sauce-labs-backpack"));
    }

    @Test
    @DisplayName("TC-INV-02 - Agregar todos los productos disponibles al carrito")
    void agregarTodosLosProductosAlCarrito() {
        String[] productos = {
                "sauce-labs-backpack", "sauce-labs-bike-light", "sauce-labs-bolt-t-shirt",
                "sauce-labs-fleece-jacket", "sauce-labs-onesie", "test.allthethings()-t-shirt-(red)"
        };
        for (String slug : productos) {
            inventoryPage.addProductToCart(slug);
        }

        assertEquals(6, inventoryPage.getCartCount());
    }

    @Test
    @DisplayName("TC-INV-03 - Remover un producto desde la pagina de inventario")
    void removerProductoDesdeInventario() {
        inventoryPage.addProductToCart("sauce-labs-backpack");
        assertEquals(1, inventoryPage.getCartCount());

        inventoryPage.removeProductFromCart("sauce-labs-backpack");

        assertEquals(0, inventoryPage.getCartCount());
        assertFalse(inventoryPage.isProductInCart("sauce-labs-backpack"));
    }

    @Test
    @DisplayName("TC-INV-04 - Ordenar productos de precio bajo a alto")
    void ordenarProductosPrecioBajoAAlto() {
        inventoryPage.sortBy("Price (low to high)");
        List<Double> precios = inventoryPage.getProductPricesInOrder();

        List<Double> preciosOrdenados = precios.stream().sorted().toList();
        assertEquals(preciosOrdenados, precios, "La lista deberia estar ordenada ascendentemente por precio");
    }

    @Test
    @DisplayName("TC-INV-05 - Ordenar productos de precio alto a bajo")
    void ordenarProductosPrecioAltoABajo() {
        inventoryPage.sortBy("Price (high to low)");
        List<Double> precios = inventoryPage.getProductPricesInOrder();

        List<Double> preciosOrdenadosDesc = precios.stream()
                .sorted((a, b) -> Double.compare(b, a))
                .toList();
        assertEquals(preciosOrdenadosDesc, precios, "La lista deberia estar ordenada descendentemente por precio");
    }

    @Test
    @DisplayName("TC-INV-07 - El icono del carrito no muestra badge sin productos agregados")
    void carritoSinBadgeAlIniciarSesion() {
        assertEquals(0, inventoryPage.getCartCount(), "No deberia existir un badge/contador al iniciar sin productos");
    }
}
