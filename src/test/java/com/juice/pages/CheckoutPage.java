package com.juice.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/** Page del checkout. */
public class CheckoutPage extends BasePage {

    // Boton continuar del wizard.
    private static final By CONTINUE_BUTTON = By.xpath(
            "//button[contains(., 'Continuar') or contains(., 'Continue') or contains(., 'Siguiente') or contains(., 'Next')]");
    private static final By ADDRESS_ROWS = By.cssSelector(".address-table mat-row, mat-radio-button");
    private static final By STANDARD_DELIVERY_ROW = By.xpath(
            "//*[contains(text(),'Entrega estándar') or contains(text(),'Standard Delivery')]");
    // Algunas versiones muestran radios y otras filas.
    private static final By CARD_ROWS = By.cssSelector("mat-table mat-row, mat-radio-button");
    private static final By PLACE_ORDER_BUTTON = By.xpath(
            "//button[contains(., 'Realice su pedido y pague') or contains(., 'Place your order and pay')]");
    private static final By CONFIRMATION_TITLE = By.xpath(
            "//*[contains(text(),'Gracias por su compra') or contains(text(),'Thank you for your purchase')]");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    /** Selecciona direccion por posicion. */
    public void selectAddress(int position) {
        List<WebElement> rows = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(ADDRESS_ROWS));
        click(rows.get(position - 1));
        log.info("Direccion #{} seleccionada", position);
        clickContinue();
    }

    public void chooseStandardDelivery() {
        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(STANDARD_DELIVERY_ROW));
        click(row);
        clickContinue();
    }

    /** Selecciona metodo de pago por posicion. */
    public void selectPaymentMethod(int position) {
        // Espera a que cargue el paso de pago.
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(CARD_ROWS));
        // Primero intenta radio, si no, usa fila.
        List<WebElement> radios = driver.findElements(By.cssSelector("mat-radio-button"));
        if (!radios.isEmpty()) {
            click(radios.get(position - 1));
        } else {
            List<WebElement> rows = driver.findElements(By.cssSelector("mat-table mat-row"));
            click(rows.get(position - 1));
        }
        log.info("Metodo de pago #{} seleccionado", position);
        clickContinue();
    }

    public void confirmOrderSummary() {
        WebElement placeOrder = wait.until(ExpectedConditions.visibilityOfElementLocated(PLACE_ORDER_BUTTON));
        click(placeOrder);
        log.info("Pedido confirmado");
    }

    public boolean isOrderConfirmed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(CONFIRMATION_TITLE));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getOrderId() {
        // El id va al final de la URL.
        String url = driver.getCurrentUrl();
        return url.substring(url.lastIndexOf('/') + 1);
    }

    private void clickContinue() {
        WebElement continueBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(CONTINUE_BUTTON));
        click(continueBtn);
    }
}
