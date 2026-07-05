package com.juice.steps;

import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import com.juice.factory.DriverFactory;
import com.juice.log.LogManager;
import com.juice.pages.PaymentPage;
import com.juice.utils.TestDataGenerator;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;

/** Steps de metodos de pago. */
public class PaymentSteps {

    private static final Logger log = LogManager.getLogger(PaymentSteps.class);

    @Cuando("el usuario agrega una tarjeta de pago valida")
    public void el_usuario_agrega_una_tarjeta_de_pago_valida() {
        agregarTarjeta();
    }

    @Cuando("el usuario agrega una segunda tarjeta de pago valida")
    public void el_usuario_agrega_una_segunda_tarjeta_de_pago_valida() {
        agregarTarjeta();
    }

    @Entonces("el usuario cuenta con 2 metodos de pago guardados en su cuenta")
    public void el_usuario_cuenta_con_2_metodos_de_pago() {
        PaymentPage paymentPage = new PaymentPage(DriverFactory.getDriver()).open();
        int count = paymentPage.getSavedCardCount();
        Assert.assertEquals(count, 2, "Se esperaban 2 tarjetas guardadas");
    }

    /** Helper para agregar una tarjeta. */
    void agregarTarjeta() {
        PaymentPage paymentPage = new PaymentPage(DriverFactory.getDriver()).open();
        paymentPage.addCard(
                TestDataGenerator.randomCardHolder(),
                TestDataGenerator.randomCardNumber(),
                "12",
                "2080");
        log.info("Tarjeta agregada correctamente");
    }
}
