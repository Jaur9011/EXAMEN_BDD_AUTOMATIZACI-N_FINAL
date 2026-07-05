package com.juice.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import io.qameta.allure.Step;

/** Page de login. */
public class LoginPage extends BasePage {

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(id = "loginButton")
    private WebElement loginButton;

    @FindBy(css = ".error, #loginButton + .error, .mat-mdc-snack-bar-label")
    private WebElement errorMessage;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Iniciar sesion con usuario {email}")
    public void login(String email, String password) {
        type(emailInput, email);
        type(passwordInput, password);
        click(loginButton);
        log.info("Login enviado para el usuario: {}", email);
    }

    @Step("Verificar que el login fue exitoso")
    public boolean isLoggedIn() {
        try {
            wait.until(ExpectedConditions.urlContains("/#/search"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        waitVisible(errorMessage);
        return errorMessage.getText();
    }
}
