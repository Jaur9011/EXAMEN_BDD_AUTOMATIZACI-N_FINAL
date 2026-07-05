package com.juice.steps;

import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.juice.factory.DriverFactory;
import com.juice.log.LogManager;
import com.juice.pages.HomePage;
import com.juice.pages.LoginPage;
import com.juice.pages.RegisterPage;
import com.juice.utils.TestContext;
import com.juice.utils.TestDataGenerator;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;

/** Steps de login y registro de apoyo. */
public class LoginSteps {

    private static final Logger log = LogManager.getLogger(LoginSteps.class);

    @Dado("que existe un nuevo usuario registrado con credenciales validas")
    public void existe_un_nuevo_usuario_registrado() {
        registrarNuevoUsuario();
    }

    @Dado("que un usuario registrado ha iniciado sesion en la aplicacion")
    public void un_usuario_registrado_ha_iniciado_sesion() {
        registrarNuevoUsuario();
        iniciarSesionConCredencialesCorrectas();
    }

    @Cuando("el usuario inicia sesion con sus credenciales correctas")
    public void el_usuario_inicia_sesion_con_credenciales_correctas() {
        iniciarSesionConCredencialesCorrectas();
    }

    @Cuando("el usuario inicia sesion con credenciales incorrectas")
    public void el_usuario_inicia_sesion_con_credenciales_incorrectas() {
        new HomePage(DriverFactory.getDriver()).goToLogin();
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
        loginPage.login("usuario.invalido@ranty-test.com", "PasswordIncorrecto#1");
    }

    @Entonces("el usuario accede correctamente al catalogo de productos")
    public void el_usuario_accede_correctamente() {
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
        Assert.assertTrue(loginPage.isLoggedIn(), "El usuario no fue redirigido al catalogo tras el login");
    }

    @Entonces("el sistema muestra un mensaje de error de acceso")
    public void el_sistema_muestra_un_mensaje_de_error() {
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
        String error = loginPage.getErrorMessage();
        Assert.assertFalse(error.isBlank(), "No se mostro ningun mensaje de error de acceso");
        log.info("Mensaje de error mostrado: {}", error);
    }

    // Helpers compartidos entre steps.

    void registrarNuevoUsuario() {
        String email = TestDataGenerator.randomEmail();
        String password = TestDataGenerator.defaultPassword();

        HomePage homePage = new HomePage(DriverFactory.getDriver());
        homePage.open();
        homePage.goToRegister();

        RegisterPage registerPage = new RegisterPage(DriverFactory.getDriver());
        registerPage.register(email, password, "Ranty QA Automation");

        // Espera redireccion a login antes de continuar.
        new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(20))
                .until(ExpectedConditions.urlContains("/login"));

        TestContext.setCredentials(email, password);
        log.info("Nuevo usuario de prueba registrado: {}", email);
    }

    void iniciarSesionConCredencialesCorrectas() {
        // Navega directo a login para evitar estados intermedios.
        new HomePage(DriverFactory.getDriver()).goToLogin();
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
        loginPage.login(TestContext.getEmail(), TestContext.getPassword());
        // Valida que realmente inicio sesion.
        Assert.assertTrue(loginPage.isLoggedIn(), "El login fallo durante la configuracion del escenario");
        log.info("Login confirmado: usuario en catalogo");
    }
}
