package com.juice.listeners;

import org.apache.logging.log4j.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.juice.log.LogManager;

/** Listener simple para logs de TestNG. */
public class ScreenshotListener implements ITestListener {

    private static final Logger log = LogManager.getLogger(ScreenshotListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        log.info(">>> Iniciando: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("<<< EXITO: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("<<< FALLO: {} - {}", result.getMethod().getMethodName(), result.getThrowable());
        // El screenshot real se toma en Hooks.
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("<<< OMITIDO: {}", result.getMethod().getMethodName());
    }
}
