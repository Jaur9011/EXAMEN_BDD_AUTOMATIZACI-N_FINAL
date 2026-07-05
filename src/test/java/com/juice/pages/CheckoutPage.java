package com.juice.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Orquesta todo el flujo posterior a la cesta:
 * seleccionar direccion (#/address/select) -&gt; velocidad de entrega
 * (#/delivery-method) -&gt; seleccionar metodo de pago (#/payment/shop) -&gt;
 * resumen del pedido (#/order-summary) -&gt; confirmacion (#/order-completion/*).
 *
 * Cada pantalla del wizard comparte el mismo boton "Continuar", por lo que
 * se reutiliza un unico metodo clickContinue().
 */
public class CheckoutPage extends BasePage {

    // Sin [not(@disabled)]: elementToBeClickable espera a que se habilite si esta temporalmente deshabilitado
    private static final By CONTINUE_BUTTON = By.xpath(
            "//button[contains(., 'Continuar') or contains(., 'Continue') or contains(., 'Siguiente') or contains(., 'Next')]");
    private static final By ADDRESS_ROWS = By.cssSelector(".address-table mat-row, mat-radio-button");
    private static final By STANDARD_DELIVERY_ROW = By.xpath(
            "//*[contains(text(),'Entrega estándar') or contains(text(),'Standard Delivery')]");
    // mat-radio-button cubre el caso en que el checkout muestra las tarjetas como radios en lugar de mat-rows
    private static final By CARD_ROWS = By.cssSelector("mat-table mat-row, mat-radio-button");
    private static final By PLACE_ORDER_BUTTON = By.xpath(
            "//button[contains(., 'Realice su pedido y pague') or contains(., 'Place your order and pay')]");
    private static final By CONFIRMATION_TITLE = By.xpath(
            "//*[contains(text(),'Gracias por su compra') or contains(text(),'Thank you for your purchase')]");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    /** Selecciona la direccion de envio por posicion (1 = primera, 2 = segunda...). */
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

    /** Selecciona el metodo de pago guardado por posicion (1 = primero, 2 = segundo...).
     *  Juice Shop renderiza las tarjetas como mat-radio-button; hay que hacer click en el radio
     *  (no en el mat-row) para que Angular actualice el estado del formulario y habilite Continue.
     */
    public void selectPaymentMethod(int position) {
        // Esperar a que el paso de pago cargue completamente
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(CARD_ROWS));
        // Intentar click en mat-radio-button primero; si no hay radios, caer en la fila de la tabla
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
        // La url de confirmacion tiene el formato #/order-completion/{orderId}
        String url = driver.getCurrentUrl();
        return url.substring(url.lastIndexOf('/') + 1);
    }

    private void clickContinue() {
        WebElement continueBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(CONTINUE_BUTTON));
        click(continueBtn);
    }
}
