# SauceDemo Automation - Selenium + Java + JUnit 5 + POM

Automatización de las pruebas funcionales descritas en el documento
`Pruebas_Manuales_SauceDemo.docx`, para el sitio de prueba
https://www.saucedemo.com/

## Tecnologías

- Java 17
- Selenium WebDriver 4.23
- JUnit 5 (Jupiter)
- WebDriverManager (descarga automática del ChromeDriver)
- Maven

## Patrón de diseño: Page Object Model (POM)

```
src/main/java/com/saucedemo/pages/
├── BasePage.java              # Esperas explícitas y utilidades comunes
├── LoginPage.java              # /  (login)
├── InventoryPage.java          # /inventory.html
├── CartPage.java                # /cart.html
├── CheckoutStepOnePage.java    # /checkout-step-one.html
├── CheckoutStepTwoPage.java    # /checkout-step-two.html
└── CheckoutCompletePage.java   # /checkout-complete.html

src/test/java/com/saucedemo/tests/
├── BaseTest.java        # setUp/tearDown del WebDriver (@BeforeEach/@AfterEach)
├── LoginTest.java         # TC-LOG-01 .. TC-LOG-10
├── InventoryTest.java     # TC-INV-01 .. TC-INV-08
└── E2EPurchaseTest.java   # TC-E2E-01 .. TC-E2E-05
```

Cada página expone únicamente métodos de negocio (`loginAs`, `addProductToCart`,
`checkout`, `finish`, etc.) y devuelve la Page Object de la página a la que se
navega, permitiendo encadenar acciones (`cartPage.checkout().fillInfo(...).continueCheckout()`).
Los locators (`By.id`, `By.className`, etc.) están encapsulados dentro de cada
Page Object y nunca se usan directamente en las clases de prueba.

## Requisitos previos

- JDK 17 o superior instalado (`java -version`)
- Maven instalado (`mvn -version`)
- Google Chrome instalado (WebDriverManager descarga el driver correspondiente automáticamente)

## Cómo ejecutar las pruebas

```bash
cd saucedemo-automation
mvn test
```

Para ejecutar una sola clase de prueba:

```bash
mvn test -Dtest=LoginTest
```

Para ejecutar en modo headless (por ejemplo en un pipeline de CI/CD),
descomentar en `BaseTest.java` la línea:

```java
options.addArguments("--headless=new");
```

## Reporte de resultados

Maven Surefire genera automáticamente los reportes en:

```
target/surefire-reports/
```

## Trazabilidad con el documento de pruebas manuales

| Caso manual | Clase / método de automatización |
|---|---|
| TC-LOG-01 | LoginTest.loginExitosoConCredencialesValidas |
| TC-LOG-02 | LoginTest.loginConUsuarioBloqueado |
| TC-LOG-03 | LoginTest.loginConContrasenaIncorrecta |
| TC-LOG-04 | LoginTest.loginConUsuarioInexistente |
| TC-LOG-05/06/07 | LoginTest.loginConCamposVacios (parametrizado) |
| TC-LOG-09 | LoginTest.loginEsSensibleAMayusculas |
| TC-LOG-10 | LoginTest.cerrarMensajeDeError |
| TC-INV-01 | InventoryTest.agregarUnProductoAlCarrito |
| TC-INV-02 | InventoryTest.agregarTodosLosProductosAlCarrito |
| TC-INV-03 | InventoryTest.removerProductoDesdeInventario |
| TC-INV-04 | InventoryTest.ordenarProductosPrecioBajoAAlto |
| TC-INV-05 | InventoryTest.ordenarProductosPrecioAltoABajo |
| TC-INV-07 | InventoryTest.carritoSinBadgeAlIniciarSesion |
| TC-E2E-01 | E2EPurchaseTest.compraExitosaDeUnProducto (incluye validación de TC-E2E-05) |
| TC-E2E-02 | E2EPurchaseTest.compraExitosaDeMultiplesProductos |
| TC-E2E-03 | E2EPurchaseTest.checkoutConCamposVaciosMuestraError (parametrizado, tabla de decisión) |
| TC-E2E-04 | E2EPurchaseTest.cancelarCheckoutDesdeStepOne |

Los casos TC-LOG-08, TC-INV-06 y TC-INV-08 quedan documentados como
exploratorios/manuales en el Word (bugs conocidos de datos de prueba /
navegación de detalle) y pueden automatizarse siguiendo el mismo patrón.

## Prueba no funcional (Performance)

La prueba TC-PERF-01 se ejecuta con **Apache JMeter** (no con Selenium), según
se documenta en la sección 5 del Word. 
