package com.juice.runner;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
/** Runner para historial. */
@CucumberOptions(
    features = "src/test/resources/features",
    glue = "com.juice",
    plugin = { "pretty", "summary", "html:target/cucumber-reports/historial.html", "json:target/cucumber-reports/historial.json", "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm" },
    monochrome = true,
    publish = false,
    tags = "@historial"
)
public class HistorialRunner extends AbstractTestNGCucumberTests {
}
