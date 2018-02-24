package com.calculator.assignment;

import static com.calculator.assignment.utils.LogUtils.isValidLogLevel;

import org.apache.log4j.Logger;

import com.calculator.assignment.utils.LogUtils;

public class CalculatorClient {
    private final static Logger LOGGER = Logger.getRootLogger();

    public static void main(String[] args) throws Exception {
        try {
            if (args.length == 0) {
                String message = "Valid command to use this calculator: java -jar <target/calculator-1.0.0.jar> <expression> <optional log level>";
                LOGGER.error(message);
                throw new Exception(message);
            }

            if (args.length > 1) {
                String logLevel = args[1];
                if (isValidLogLevel(logLevel)) {
                    LogUtils.setAppLogLevel(logLevel);
                    LOGGER.debug("Log level is now set to: " + logLevel);
                } else {
                    LOGGER.warn("No log level set, default set to INFO");
                    LogUtils.setAppLogLevel("info");
                }
            }

            String input = args[0];
            LOGGER.info("======= Evaluting the expression " + input + " =======");
            LOGGER.info("======= Expression is evaluated to " + Calculator.evaluateExpression(input) + " ======= ");
        } catch (Exception exp) {
            LOGGER.error("Error while evaluating the expression", exp);
        }
    }
}
