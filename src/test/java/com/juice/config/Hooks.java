package com.juice.config;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.juice.factory.DriverFactory;
import com.juice.log.LogManager;
import com.juice.utils.ScreenshotUtils;
import com.juice.utils.TestContext;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

/** Hooks de setup y teardown por escenario. */
public class Hooks {

    private static final Logger log = LogManager.getLogger(Hooks.class);

    @Before
    public void setUp(Scenario scenario) {
        log.info("==================== INICIO: {} ====================", scenario.getName());
        TestContext.clear();
        DriverFactory.createDriver();
    }

    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver;
        try {
            driver = DriverFactory.getDriver();
        } catch (IllegalStateException e) {
            // Si no hay driver, solo limpia contexto.
            log.error("El escenario '{}' termino sin WebDriver activo: {}", scenario.getName(), e.getMessage());
            TestContext.clear();
            return;
        }
        try {
            if (scenario.isFailed()) {
                log.error("Escenario FALLIDO: {}", scenario.getName());
                try {
                    byte[] screenshot = ScreenshotUtils.takeScreenshot(driver, scenario.getName());
                    scenario.attach(screenshot, "image/png", scenario.getName());
                } catch (Exception e) {
                    log.error("No se pudo capturar el screenshot: {}", e.getMessage());
                }
            } else {
                log.info("Escenario EXITOSO: {}", scenario.getName());
            }
        } finally {
            DriverFactory.quitDriver();
            TestContext.clear();
            log.info("==================== FIN: {} ====================", scenario.getName());
        }
    }
}
