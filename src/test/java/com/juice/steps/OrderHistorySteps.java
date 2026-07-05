package com.juice.steps;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.juice.factory.DriverFactory;
import com.juice.log.LogManager;
import com.juice.pages.HomePage;
import com.juice.pages.OrderHistoryPage;
import com.juice.pages.SearchResultsPage;
import com.juice.utils.ScreenshotUtils;
import com.juice.utils.TestContext;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;

/** Steps de historial de pedidos. */
public class OrderHistorySteps {

    private static final Logger log = LogManager.getLogger(OrderHistorySteps.class);

    @Dado("que un usuario registrado completo 2 pedidos usando la segunda direccion y el primer metodo de pago")
    public void usuario_completo_2_pedidos() {
        ShoppingSteps shoppingSteps = new ShoppingSteps();

        // Prepara usuario con datos necesarios.
        shoppingSteps.prepararUsuarioConDireccionesYPagos();

        // Pedido 1.
        HomePage homePage = new HomePage(DriverFactory.getDriver());
        SearchResultsPage resultsPage = new SearchResultsPage(DriverFactory.getDriver());

        homePage.searchProduct("apple");
        resultsPage.addFirstResultToCart();
        homePage.searchProduct("banana");
        resultsPage.addFirstResultToCart();
        homePage.searchProduct("shirt");
        resultsPage.addFirstResultToCart();
        shoppingSteps.completarPedidoConSegundaDireccionYPrimerPago();
        log.info("Pedido 1 completado");

        // Pedido 2.
        homePage.open();
        resultsPage.addRandomProductToCart();
        resultsPage.addRandomProductToCart();
        shoppingSteps.completarPedidoConSegundaDireccionYPrimerPago();
        log.info("Pedido 2 completado");
    }

    @Cuando("el usuario abre su historial de pedidos")
    public void el_usuario_abre_su_historial_de_pedidos() {
        new OrderHistoryPage(DriverFactory.getDriver()).open();
    }

    @Entonces("el historial muestra al menos 2 pedidos completados")
    public void el_historial_muestra_al_menos_2_pedidos() {
        OrderHistoryPage orderHistoryPage = new OrderHistoryPage(DriverFactory.getDriver());
        List<String> orderIds = TestContext.getOrderIds();
        int count = orderIds.isEmpty()
                ? orderHistoryPage.waitForOrders(2)
                : orderHistoryPage.waitForOrderIds(orderIds);
        Assert.assertTrue(count >= 2, "Se esperaban al menos 2 pedidos en el historial, se encontraron " + count);
        log.info("El historial muestra {} pedidos. IDs esperados: {}", count, orderIds);
    }

    @Entonces("se captura evidencia screenshot de cada uno de los 2 pedidos")
    public void se_captura_evidencia_de_cada_pedido() {
        OrderHistoryPage orderHistoryPage = new OrderHistoryPage(DriverFactory.getDriver());
        List<String> orderIds = TestContext.getOrderIds();

        for (int i = 1; i <= 2; i++) {
            WebElement orderBlock = orderIds.size() >= i
                    ? orderHistoryPage.getOrderElementById(orderIds.get(i - 1))
                    : orderHistoryPage.getOrderBlock(i);
            ((org.openqa.selenium.JavascriptExecutor) DriverFactory.getDriver())
                    .executeScript("arguments[0].scrollIntoView({block:'center'});", orderBlock);
            ScreenshotUtils.takeScreenshot(DriverFactory.getDriver(), "historial_pedido_" + i);
        }
    }
}
