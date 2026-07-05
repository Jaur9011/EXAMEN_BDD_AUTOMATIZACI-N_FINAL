package com.juice.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.juice.utils.ConfigReader;

/** Pagina principal del catalogo. */
public class HomePage extends BasePage {

    // Boton de buscar.
    @FindBy(xpath = "//*[@id='navbarSearch' or (self::button and (contains(@aria-label,'earch') or contains(@aria-label,'squeda')))]")
    private WebElement searchIcon;

    @FindBy(css = "#searchQuery input")
    private WebElement searchInput;

    @FindBy(css = "button[aria-label='Mostrar/ocultar menú de cuenta'], button#navbarAccount")
    private WebElement accountMenuButton;

    @FindBy(css = ".mat-mdc-menu-panel a[routerlink='/basket'], a[href='#/basket']")
    private WebElement basketLink;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage open() {
        driver.get(ConfigReader.getAppUrl());
        dismissPopups();
        return this;
    }

    /** Cierra popups si salen. */
    public void dismissPopups() {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(4));
        try {
            WebElement welcomeClose = shortWait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button.close-dialog, button[aria-label='Close Welcome Banner']")));
            welcomeClose.click();
            log.info("Dialogo de bienvenida cerrado");
        } catch (Exception e) {
            log.debug("No aparecio el dialogo de bienvenida");
        }
        try {
            List<WebElement> cookieBtn = driver.findElements(
                    By.cssSelector("button[aria-label='descartar mensaje de cookies'], .cc-dismiss, #cookieconsent button"));
            if (!cookieBtn.isEmpty()) {
                cookieBtn.get(0).click();
                log.info("Banner de cookies cerrado");
            }
        } catch (Exception e) {
            log.debug("No aparecio el banner de cookies");
        }
    }

    public void searchProduct(String term) {
        // Abre el buscador si todavia no esta visible.
        By searchInputLocator = By.cssSelector("#searchQuery input");

        boolean searchAlreadyOpen = driver.findElements(searchInputLocator)
                .stream().anyMatch(WebElement::isDisplayed);

        if (!searchAlreadyOpen) {
            click(searchIcon);
            // Espera corta para no escribir antes de tiempo.
            wait.until(ExpectedConditions.visibilityOfElementLocated(searchInputLocator));
        }

        type(searchInputLocator,
                org.openqa.selenium.Keys.chord(org.openqa.selenium.Keys.CONTROL, "a"),
                org.openqa.selenium.Keys.DELETE,
                term,
                org.openqa.selenium.Keys.ENTER);
        log.info("Busqueda de producto: {}", term);
    }

    public void goToRegister() {
        driver.get(ConfigReader.getAppUrl() + "/#/register");
        dismissPopups();
    }

    public void goToLogin() {
        driver.get(ConfigReader.getAppUrl() + "/#/login");
        dismissPopups();
    }

    public void goToBasket() {
        driver.get(ConfigReader.getAppUrl() + "/#/basket");
        dismissPopups();
    }
}
