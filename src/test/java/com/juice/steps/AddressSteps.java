package com.juice.steps;

import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import com.juice.factory.DriverFactory;
import com.juice.log.LogManager;
import com.juice.pages.AddressPage;
import com.juice.utils.TestDataGenerator;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;

/** Steps de direcciones. */
public class AddressSteps {

    private static final Logger log = LogManager.getLogger(AddressSteps.class);

    @Cuando("el usuario agrega la direccion de envio {string}")
    public void el_usuario_agrega_la_direccion_de_envio(String etiqueta) {
        agregarDireccion(etiqueta);
    }

    @Entonces("el usuario cuenta con 2 direcciones guardadas en su cuenta")
    public void el_usuario_cuenta_con_2_direcciones_guardadas() {
        AddressPage addressPage = new AddressPage(DriverFactory.getDriver()).openSavedAddresses();
        int count = addressPage.getSavedAddressCount();
        Assert.assertEquals(count, 2, "Se esperaban 2 direcciones guardadas");
    }

    /** Helper para crear una direccion. */
    void agregarDireccion(String etiqueta) {
        AddressPage addressPage = new AddressPage(DriverFactory.getDriver());
        addressPage.openNewAddressForm();
        addressPage.addAddress(
                TestDataGenerator.randomCountry(),
                "Antonio Ranty - " + etiqueta,
                TestDataGenerator.randomMobileNumber(),
                TestDataGenerator.randomZipCode(),
                TestDataGenerator.randomStreetAddress() + " (" + etiqueta + ")",
                TestDataGenerator.randomCity(),
                TestDataGenerator.randomState());
        log.info("Direccion '{}' agregada correctamente", etiqueta);
    }
}
