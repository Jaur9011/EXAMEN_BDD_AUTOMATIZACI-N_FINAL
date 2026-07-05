package com.juice.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/** Runner de registro y login. */
@CucumberOptions(
    features = "src/test/resources/features",
    glue = "com.juice",
    plugin = {
        "pretty", "summary",
        "html:target/cucumber-reports/registro-login.html",
        "json:target/cucumber-reports/registro-login.json",
        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
    },
    monochrome = true,
    publish = false,
    tags = "@registro or @login"
)
public class RegistroLoginRunner extends AbstractTestNGCucumberTests {
}
