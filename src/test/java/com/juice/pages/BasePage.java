package com.juice.pages;

import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.juice.log.LogManager;
import com.juice.utils.ConfigReader;

/** Base para todas las pages. */
public abstract class BasePage {

    private static final By TRANSIENT_OVERLAYS = By.cssSelector(
            ".mat-mdc-snack-bar-container, .mat-snack-bar-container, .mdc-snackbar, .cdk-overlay-backdrop.cdk-overlay-backdrop-showing");

    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final Logger log = LogManager.getLogger(getClass());

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitTimeout()));
        PageFactory.initElements(driver, this);
    }

    protected WebElement waitVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected WebElement waitVisible(By locator) {
        return wait.until(driver -> driver.findElements(locator).stream()
                .filter(this::isDisplayed)
                .findFirst()
                .orElse(null));
    }

    protected WebElement waitClickable(WebElement element) {
        return wait.until(driver -> {
            try {
                WebElement candidate = ExpectedConditions.elementToBeClickable(element).apply(driver);
                return candidate.isEnabled() ? candidate : null;
            } catch (StaleElementReferenceException e) {
                return null;
            }
        });
    }

    protected WebElement waitClickable(By locator) {
        return wait.until(driver -> driver.findElements(locator).stream()
                .filter(this::isDisplayed)
                .filter(WebElement::isEnabled)
                .findFirst()
                .orElse(null));
    }

    protected void click(WebElement element) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                waitTransientOverlays();
                WebElement el = waitClickable(element);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
                el.click();
                return;
            } catch (StaleElementReferenceException | ElementClickInterceptedException e) {
                attempts++;
                log.debug("Reintentando click (intento {} de 3): {}", attempts + 1, e.getClass().getSimpleName());
            }
        }

        // Ultimo intento por JS.
        waitTransientOverlays();
        WebElement el = waitClickable(element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    private void waitTransientOverlays() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(d -> d.findElements(TRANSIENT_OVERLAYS).stream().noneMatch(WebElement::isDisplayed));
        } catch (TimeoutException e) {
            log.debug("Overlay transitorio aun visible, se continua con reintento de click");
        }
    }

    protected void type(WebElement element, String text) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                WebElement el = waitClickable(element);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'}); arguments[0].focus();", el);
                el.clear();
                el.sendKeys(text);
                return;
            } catch (StaleElementReferenceException | InvalidElementStateException e) {
                attempts++;
                log.debug("Reintentando escritura (intento {} de 3): {}", attempts + 1, e.getClass().getSimpleName());
            }
        }

        WebElement el = waitClickable(element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'}); arguments[0].focus();", el);
        el.clear();
        el.sendKeys(text);
    }

    protected void type(By locator, CharSequence... keys) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                WebElement el = waitClickable(locator);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'}); arguments[0].focus();", el);
                el.clear();
                el.sendKeys(keys);
                return;
            } catch (StaleElementReferenceException | InvalidElementStateException e) {
                attempts++;
                log.debug("Reintentando escritura por locator (intento {} de 3): {}", attempts + 1, e.getClass().getSimpleName());
            }
        }

        WebElement el = waitClickable(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'}); arguments[0].focus();", el);
        el.clear();
        el.sendKeys(keys);
    }

    private boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }
}
