package com.juice.pages;

import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Pagina de resultados de busqueda / catalogo de productos (#/search).
 *
 * IMPORTANTE: el indice de busqueda de Juice Shop trabaja sobre el nombre
 * interno del producto en INGLES (aunque la interfaz se muestre en
 * espanol). Por eso, para encontrar "manzana", "platano" o "camiseta" hay
 * que buscar por "apple", "banana" y "shirt" respectivamente. Esto fue
 * verificado manualmente contra la instancia local antes de escribir este
 * Page Object.
 */
public class SearchResultsPage extends BasePage {

    private static final By PRODUCT_CARDS = By.cssSelector("mat-card, mat-grid-tile");
    // Los botones "Añadir" en Juice Shop son mat-mini-fab con clase "addToCart".
    // Se incluyen fallbacks por aria-label para distintas versiones e idiomas.
    private static final By ADD_TO_CART_BUTTONS = By.xpath(
            "//button[contains(@class,'addToCart') " +
            "or contains(@aria-label,'Basket') " +
            "or contains(@aria-label,'cesta') " +
            "or contains(@aria-label,'Cart') " +
            "or (contains(.,'Añadir') and not(contains(.,'Agregar')))" +
            "or (contains(.,'Add to'))]");

    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }

    public int getResultsCount() {
        return driver.findElements(PRODUCT_CARDS).size();
    }

    /** Agrega el primer producto de los resultados a la cesta. */
    public void addFirstResultToCart() {
        // Esperar a que los botones "Añadir" aparezcan (implica que los productos ya se renderizaron)
        List<WebElement> buttons = wait.until(d -> {
            List<WebElement> btns = d.findElements(ADD_TO_CART_BUTTONS);
            return btns.isEmpty() ? null : btns;
        });
        WebElement btn = wait.until(ExpectedConditions.visibilityOf(buttons.get(0)));
        click(btn);
        log.info("Producto agregado a la cesta desde resultados de busqueda");
    }

    /** Agrega un producto aleatorio del catalogo (usado en el "Pedido 2"). */
    public void addRandomProductToCart() {
        List<WebElement> buttons = wait.until(d -> {
            List<WebElement> btns = d.findElements(ADD_TO_CART_BUTTONS);
            return btns.isEmpty() ? null : btns;
        });
        int index = new Random().nextInt(buttons.size());
        WebElement btn = buttons.get(index);
        wait.until(ExpectedConditions.visibilityOf(btn));
        click(btn);
        log.info("Producto aleatorio #{} agregado a la cesta", index);
    }
}
