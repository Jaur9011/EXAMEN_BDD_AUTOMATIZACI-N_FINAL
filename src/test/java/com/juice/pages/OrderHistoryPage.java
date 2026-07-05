package com.juice.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.juice.log.LogManager;
import com.juice.utils.ConfigReader;

/** Page del historial de pedidos. */
public class OrderHistoryPage extends BasePage {

    private static final Logger log = LogManager.getLogger(OrderHistoryPage.class);

    private static final By ORDERS_CONTAINER = By.cssSelector(".orders-container");
    private static final By ORDER_BLOCKS = By.cssSelector(
            ".orders-container mat-row, .orders-container .mat-row, .orders-container mat-card, .orders-container .mat-mdc-card, .orders-container .mat-card, .orders-container .border");
    private static final By CONFIRMATION_HEADER = By.cssSelector("h1.confirmation");

    public OrderHistoryPage(WebDriver driver) {
        super(driver);
    }

    public OrderHistoryPage open() {
        driver.get(ConfigReader.getAppUrl() + "/#/order-history");

        // Abre historial y espera su contenedor.
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(ORDERS_CONTAINER));
            log.info("Página de historial de pedidos cargada exitosamente");
        } catch (TimeoutException e) {
            // Si no aparece, valida si quedo en confirmacion.
            log.warn("No se encontró .orders-container, verificando si está en página de confirmación");

            try {
                WebElement confirmationHeader = driver.findElement(CONFIRMATION_HEADER);
                if (confirmationHeader.isDisplayed()) {
                    log.warn("Detectada página de confirmación. Navegando manualmente al historial...");
                    // Espera explícita en la pantalla de confirmación antes de redirigir.
                    wait.until(ExpectedConditions.or(
                            ExpectedConditions.urlContains("/order-completion"),
                            ExpectedConditions.visibilityOfElementLocated(CONFIRMATION_HEADER)));
                    driver.get(ConfigReader.getAppUrl() + "/#/order-history");
                    wait.until(ExpectedConditions.visibilityOfElementLocated(ORDERS_CONTAINER));
                    log.info("Página de historial recargada exitosamente tras detectar confirmación");
                }
            } catch (NoSuchElementException ex) {
                log.error("No se encontró página de confirmación como fallback. Elemento .orders-container tampoco existe.");
                throw new TimeoutException("No se pudo cargar el historial de pedidos: .orders-container no encontrado y página de confirmación no detectada", e);
            }
        }

        return this;
    }

    public int getOrderCount() {
        return driver.findElements(ORDER_BLOCKS).size();
    }

    /** Espera hasta ver al menos N pedidos. */
    public int waitForOrders(int minCount) {
        try {
            return wait.until(d -> {
                int count = d.findElements(ORDER_BLOCKS).size();
                return count >= minCount ? count : null;
            });
        } catch (TimeoutException e) {
            return getOrderCount();
        }
    }

    public int waitForOrderIds(List<String> expectedOrderIds) {
        if (expectedOrderIds == null || expectedOrderIds.isEmpty()) {
            return getOrderCount();
        }

        try {
            return wait.until(d -> {
                int count = countVisibleOrderIds(expectedOrderIds);
                return count >= expectedOrderIds.size() ? count : null;
            });
        } catch (TimeoutException e) {
            return countVisibleOrderIds(expectedOrderIds);
        }
    }

    public int countVisibleOrderIds(List<String> expectedOrderIds) {
        int visibleOrders = 0;
        for (String orderId : expectedOrderIds) {
            if (findVisibleOrderElements(orderId).stream().findFirst().isPresent()) {
                visibleOrders++;
            }
        }
        return visibleOrders;
    }

    public List<WebElement> getOrderBlocks() {
        return driver.findElements(ORDER_BLOCKS);
    }

    public WebElement getOrderElementById(String orderId) {
        List<WebElement> matches = findVisibleOrderElements(orderId);
        if (matches.isEmpty()) {
            throw new NoSuchElementException("No se encontro ningun pedido visible con id " + orderId);
        }
        return matches.get(0);
    }

    /** Devuelve un pedido por posicion. */
    public WebElement getOrderBlock(int position) {
        List<WebElement> blocks = getOrderBlocks();
        if (position < 1 || position > blocks.size()) {
            throw new IllegalArgumentException("No existe el pedido en la posicion " + position);
        }
        return blocks.get(position - 1);
    }

    private List<WebElement> findVisibleOrderElements(String orderId) {
        List<WebElement> displayedMatches = new ArrayList<>();
        for (WebElement candidate : driver.findElements(orderIdLocator(orderId))) {
            try {
                if (candidate.isDisplayed()) {
                    displayedMatches.add(candidate);
                }
            } catch (Exception ignored) {
                // Si cambia el DOM, se ignora y se sigue.
            }
        }
        return displayedMatches;
    }

    private By orderIdLocator(String orderId) {
        return By.xpath("//*[contains(normalize-space(.), \"" + orderId + "\")]");
    }
}
