package com.calculator.assignment;

import java.util.Scanner;

public class CalculatorClient {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while (!(input = scanner.nextLine()).equals("end")) {
            System.out.println("Output: " + Calculator.evaluateExpression(input));
        }

        scanner.close();
    }

}
