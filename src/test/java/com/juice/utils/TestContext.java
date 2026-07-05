package com.juice.utils;

import java.util.ArrayList;
import java.util.List;

/** Guarda datos del escenario actual usando ThreadLocal. */
public final class TestContext {

    private static final ThreadLocal<String> email = new ThreadLocal<>();
    private static final ThreadLocal<String> password = new ThreadLocal<>();
    private static final ThreadLocal<List<String>> orderIds = ThreadLocal.withInitial(ArrayList::new);

    private TestContext() {
    }

    public static void setCredentials(String userEmail, String userPassword) {
        email.set(userEmail);
        password.set(userPassword);
    }

    public static String getEmail() {
        return email.get();
    }

    public static String getPassword() {
        return password.get();
    }

    public static void addOrderId(String orderId) {
        orderIds.get().add(orderId);
    }

    public static List<String> getOrderIds() {
        return new ArrayList<>(orderIds.get());
    }

    public static void clear() {
        email.remove();
        password.remove();
        orderIds.remove();
    }
}
