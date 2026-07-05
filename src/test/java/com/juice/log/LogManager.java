package com.juice.log;

import org.apache.logging.log4j.Logger;

/** Helper minimo para obtener loggers. */
public final class LogManager {

    private LogManager() {
    }

    public static Logger getLogger(Class<?> clazz) {
        return org.apache.logging.log4j.LogManager.getLogger(clazz);
    }
}
