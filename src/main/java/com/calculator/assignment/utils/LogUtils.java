package com.calculator.assignment.utils;

import org.apache.log4j.Logger;

public class LogUtils {
    public static void setAppLogLevel(String newLevel) {
        newLevel = newLevel.toLowerCase();
        Logger logger4j = Logger.getRootLogger();
        logger4j.setLevel(org.apache.log4j.Level.toLevel("ERROR"));

        if ("error".equalsIgnoreCase(newLevel)) {
            logger4j.setLevel(org.apache.log4j.Level.toLevel("ERROR"));
        } else if ("debug".equalsIgnoreCase(newLevel)) {
            logger4j.setLevel(org.apache.log4j.Level.toLevel("DEBUG"));
        } else if ("warning".equalsIgnoreCase(newLevel)) {
            logger4j.setLevel(org.apache.log4j.Level.toLevel("WARNING"));
        } else {
            logger4j.setLevel(org.apache.log4j.Level.toLevel("INFO"));
        }

    }

    public static boolean isValidLogLevel(String logLevel) {
        for (LogLevels level : LogLevels.values()) {
            if (level.name().equalsIgnoreCase(logLevel)) {
                return true;
            }
        }
        return false;
    }
}

enum LogLevels {
    INFO, ERROR, WARN, DEBUG;
}
