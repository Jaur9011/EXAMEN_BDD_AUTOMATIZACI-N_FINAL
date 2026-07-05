package com.juice.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/** Runner de cesta e historial. */
@CucumberOptions(
    features = "src/test/resources/features",
    glue = "com.juice",
    plugin = {
        "pretty", "summary",
        "html:target/cucumber-reports/cesta-historial.html",
        "json:target/cucumber-reports/cesta-historial.json",
        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
    },
    monochrome = true,
    publish = false,
    tags = "@cesta or @historial"
)
public class CestaHistorialRunner extends AbstractTestNGCucumberTests {
}
