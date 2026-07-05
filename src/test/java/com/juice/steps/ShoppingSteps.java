package com.juice.steps;

import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import com.juice.factory.DriverFactory;
import com.juice.log.LogManager;
import com.juice.pages.ShoppingPage;
import com.juice.pages.CheckoutPage;
import com.juice.pages.HomePage;
import com.juice.pages.SearchResultsPage;
import com.juice.utils.TestContext;

import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;

/** Steps de cesta. */
public class ShoppingSteps {

    private static final Logger log = LogManager.getLogger(ShoppingSteps.class);

    /** Mapa simple de terminos de busqueda. */
    private static final Map<String, String> TERMINO_BUSQUEDA = Map.of(
            "manzana", "apple",
            "banana", "banana",
            "camiseta", "shirt"
    );

    @Dado("que un usuario registrado ha iniciado sesion y cuenta con 2 direcciones y 2 metodos de pago guardados")
    public void usuario_con_direcciones_y_pagos_registrados() {
        prepararUsuarioConDireccionesYPagos();
    }

    @Cuando("el usuario busca y agrega a la cesta el producto {string}")
    public void el_usuario_busca_y_agrega_a_la_cesta_el_producto(String productoEsp) {
        String terminoBusqueda = TERMINO_BUSQUEDA.getOrDefault(productoEsp, productoEsp);
        HomePage homePage = new HomePage(DriverFactory.getDriver());
        homePage.searchProduct(terminoBusqueda);

        SearchResultsPage resultsPage = new SearchResultsPage(DriverFactory.getDriver());
        resultsPage.addFirstResultToCart();
        log.info("Producto '{}' (busqueda: '{}') agregado a la cesta", productoEsp, terminoBusqueda);
    }

    @Cuando("el usuario agrega 2 productos aleatorios del catalogo a la cesta")
    public void el_usuario_agrega_2_productos_aleatorios_del_catalogo() {
        HomePage homePage = new HomePage(DriverFactory.getDriver());
        homePage.open();
        SearchResultsPage catalogPage = new SearchResultsPage(DriverFactory.getDriver());
        catalogPage.addRandomProductToCart();
        catalogPage.addRandomProductToCart();
    }

    @Cuando("el usuario completa el pedido usando la segunda direccion y el primer metodo de pago")
    public void el_usuario_completa_el_pedido() {
        completarPedidoConSegundaDireccionYPrimerPago();
    }

    @Entonces("el pedido se confirma exitosamente")
    public void el_pedido_se_confirma_exitosamente() {
        CheckoutPage checkoutPage = new CheckoutPage(DriverFactory.getDriver());
        Assert.assertTrue(checkoutPage.isOrderConfirmed(), "El pedido no llego a la pantalla de confirmacion");
        log.info("Pedido confirmado con id: {}", checkoutPage.getOrderId());
    }

    // Helpers usados tambien por historial.

    void prepararUsuarioConDireccionesYPagos() {
        LoginSteps loginSteps = new LoginSteps();
        loginSteps.registrarNuevoUsuario();
        loginSteps.iniciarSesionConCredencialesCorrectas();

        AddressSteps addressSteps = new AddressSteps();
        addressSteps.agregarDireccion("Direccion Uno");
        addressSteps.agregarDireccion("Direccion Dos");

        PaymentSteps paymentSteps = new PaymentSteps();
        paymentSteps.agregarTarjeta();
        paymentSteps.agregarTarjeta();
    }

    void completarPedidoConSegundaDireccionYPrimerPago() {
        ShoppingPage shoppingPage = new ShoppingPage(DriverFactory.getDriver()).open();
        shoppingPage.goToCheckout();

        CheckoutPage checkoutPage = new CheckoutPage(DriverFactory.getDriver());
        checkoutPage.selectAddress(2);
        checkoutPage.chooseStandardDelivery();
        checkoutPage.selectPaymentMethod(1);
        checkoutPage.confirmOrderSummary();
        Assert.assertTrue(checkoutPage.isOrderConfirmed(), "El pedido no llego a la pantalla de confirmacion durante la preparacion del escenario");

        String orderId = checkoutPage.getOrderId();
        TestContext.addOrderId(orderId);
        log.info("Pedido confirmado con id: {}", orderId);
    }
}

