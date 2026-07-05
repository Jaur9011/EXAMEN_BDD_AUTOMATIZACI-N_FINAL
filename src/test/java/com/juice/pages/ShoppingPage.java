package com.juice.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.juice.utils.ConfigReader;

/** Page de la cesta. */
public class ShoppingPage extends BasePage {

    @FindBy(id = "checkoutButton")
    private WebElement checkoutButton;

    public ShoppingPage(WebDriver driver) {
        super(driver);
    }

    public ShoppingPage open() {
        driver.get(ConfigReader.getAppUrl() + "/#/basket");
        wait.until(ExpectedConditions.urlContains("/basket"));
        return this;
    }

    public int getItemCount() {
        return driver.findElements(By.cssSelector("mat-table mat-row, .mat-row")).size();
    }

    public void goToCheckout() {
        waitVisible(checkoutButton);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("mat-spinner, .mat-spinner, .mat-mdc-progress-spinner")));
        try {
            wait.until(d -> checkoutButton.isDisplayed() && checkoutButton.isEnabled());
        } catch (TimeoutException firstTry) {
            log.warn("Checkout seguia deshabilitado en cesta; recargando pagina y reintentando");
            driver.navigate().refresh();
            waitVisible(checkoutButton);
            wait.until(d -> checkoutButton.isDisplayed() && checkoutButton.isEnabled());
        }
        click(checkoutButton);
        log.info("Checkout iniciado desde la cesta");
    }
}

