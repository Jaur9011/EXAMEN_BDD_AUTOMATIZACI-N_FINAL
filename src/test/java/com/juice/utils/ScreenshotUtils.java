package com.juice.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.juice.log.LogManager;

import io.qameta.allure.Attachment;

/** Utilidad para tomar screenshots. */
public final class ScreenshotUtils {

    private static final Logger log = LogManager.getLogger(ScreenshotUtils.class);
    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private ScreenshotUtils() {
    }

    /** Toma screenshot y lo guarda. */
    @Attachment(value = "Screenshot on Failure", type = "image/png", fileExtension = ".png")
    public static byte[] takeScreenshot(WebDriver driver, String scenarioName) {
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

        // Guarda copia local.
        try {
            String safeName = scenarioName.replaceAll("[^a-zA-Z0-9_-]", "_");
            String fileName = safeName + "_" + LocalDateTime.now().format(TS) + ".png";
            Path folder = Path.of(ConfigReader.getScreenshotPath());
            Files.createDirectories(folder);
            Path destination = folder.resolve(fileName);
            Files.write(destination, screenshot);
            log.info("Screenshot guardado en {}", destination.toAbsolutePath());
        } catch (IOException e) {
            log.error("No se pudo guardar el screenshot en disco", e);
        }

        return screenshot;
    }
}
