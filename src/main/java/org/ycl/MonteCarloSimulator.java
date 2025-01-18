package org.ycl;

import java.util.ArrayList;
import java.util.List;

public class MonteCarloSimulator {
    public static void main(String[] args) {
        double strike = 100.0;
        double spot = 100.0;
        double volatility = 0.2;
        double timeToExpiry = 1.0;
        double riskFreeRate = 0.05;
        int numberOfPaths = 100_000;
        int timeSteps = 252; // Daily steps for 1 year
        double confidenceLevel = 95.0; // Confidence interval level
        double barrier = 120.0; // Example barrier level

        // Create the Options instance
        Options option = new Options(strike, spot, volatility, timeToExpiry, riskFreeRate, numberOfPaths);

        // Lists to store chart data
        List<String> categories = new ArrayList<>();
        List<Double> prices = new ArrayList<>();
        List<Double> lowerCI = new ArrayList<>();
        List<Double> upperCI = new ArrayList<>();

        System.out.println("Option Pricing Results:");
        System.out.println("=============================================================");
        System.out.printf("%-25s %-20s %-10s %-10s%n", "Option Type", "Price", "Lower CI", "Upper CI");
        System.out.println("-------------------------------------------------------------");

        // Add results for each option type
        addOptionResults(option, "European Call", OptionType.CALL, 0, 0, false, confidenceLevel, categories, prices, lowerCI, upperCI);
        addOptionResults(option, "European Put", OptionType.PUT, 0, 0, false, confidenceLevel, categories, prices, lowerCI, upperCI);
        addOptionResults(option, "Knock-In Call", OptionType.KNOCK_IN_CALL, barrier, 0, false, confidenceLevel, categories, prices, lowerCI, upperCI);
        addOptionResults(option, "Knock-Out Call", OptionType.KNOCK_OUT_CALL, barrier, 0, false, confidenceLevel, categories, prices, lowerCI, upperCI);
        addOptionResults(option, "Asian Call", OptionType.CALL, 0, timeSteps, true, confidenceLevel, categories, prices, lowerCI, upperCI);
        addOptionResults(option, "Asian Put", OptionType.PUT, 0, timeSteps, true, confidenceLevel, categories, prices, lowerCI, upperCI);

        System.out.println("=============================================================");

        // Generate charts
        System.out.println("Generating charts...");
        ChartUtil.createChart("Option Prices", "Option Type", "Price", categories, prices, "option_prices.png");
        ChartUtil.createChart("Lower CI", "Option Type", "Lower CI", categories, lowerCI, "lower_ci.png");
        ChartUtil.createChart("Upper CI", "Option Type", "Upper CI", categories, upperCI, "upper_ci.png");
        System.out.println("Charts generated successfully!");
    }

    private static void addOptionResults(Options option, String label, OptionType optionType, double barrier, int timeSteps, boolean isAsian, double confidenceLevel,
                                         List<String> categories, List<Double> prices, List<Double> lowerCI, List<Double> upperCI) {
        double[] result = option.calculateMonteCarloWithCI(optionType, barrier, timeSteps, isAsian, confidenceLevel);
        System.out.printf("%-25s %-20.6f %-10.6f %-10.6f%n", label, result[0], result[1], result[2]);
        categories.add(label);
        prices.add(result[0]);
        lowerCI.add(result[1]);
        upperCI.add(result[2]);
    }
}
