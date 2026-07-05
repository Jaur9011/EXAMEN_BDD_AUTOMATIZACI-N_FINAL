package com.juice.steps;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.juice.factory.DriverFactory;
import com.juice.log.LogManager;
import com.juice.pages.HomePage;
import com.juice.pages.RegisterPage;
import com.juice.utils.TestDataGenerator;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;

import java.time.Duration;

/** Steps de registro. */
public class RegisterSteps {

    private static final Logger log = LogManager.getLogger(RegisterSteps.class);

    // Guarda email/password por alias.
    private final Map<String, String[]> registeredUsers = new HashMap<>();

    @Dado("que el usuario esta en la pagina de registro de la aplicacion")
    public void el_usuario_esta_en_la_pagina_de_registro() {
        HomePage homePage = new HomePage(DriverFactory.getDriver());
        homePage.open();
        homePage.goToRegister();
    }

    @Cuando("el usuario completa el formulario de registro con datos validos para {string}")
    public void el_usuario_completa_el_formulario_de_registro(String alias) {
        String email = TestDataGenerator.randomEmail();
        String password = TestDataGenerator.defaultPassword();
        registeredUsers.put(alias, new String[]{email, password});

        RegisterPage registerPage = new RegisterPage(DriverFactory.getDriver());
        registerPage.register(email, password, "Ranty QA " + alias);
        log.info("Registro completado para alias={} email={}", alias, email);
    }

    @Entonces("la cuenta del usuario {string} se crea correctamente")
    public void la_cuenta_del_usuario_se_crea_correctamente(String alias) {
        Assert.assertTrue(registeredUsers.containsKey(alias), "No se genero el usuario " + alias);
    }

    @Entonces("el sistema redirige al usuario a la pagina de inicio de sesion")
    public void el_sistema_redirige_a_login() {
        WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(20));
        boolean redirected = wait.until(ExpectedConditions.urlContains("/login"));
        Assert.assertTrue(redirected, "No se redirigio a la pagina de login tras el registro");
    }
}
