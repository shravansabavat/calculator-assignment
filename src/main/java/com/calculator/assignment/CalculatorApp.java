package com.calculator.assignment;

import java.util.Scanner;

public class CalculatorApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while (!(input = scanner.nextLine()).equals("end")) {
            System.out.print("Given expression> " + input);
        }

        scanner.close();
    }
}
