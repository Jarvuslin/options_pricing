package org.ycl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Options {
    private final double strike;
    private final double spot;
    private final double volatility;
    private final double timeToExpiry;
    private final double riskFreeRate;
    private final int numberOfPaths;

    public Options(double strike, double spot, double volatility, double timeToExpiry, double riskFreeRate, int numberOfPaths) {
        this.strike = strike;
        this.spot = spot;
        this.volatility = volatility;
        this.timeToExpiry = timeToExpiry;
        this.riskFreeRate = riskFreeRate;
        this.numberOfPaths = numberOfPaths;
    }

    public double[] calculateMonteCarloWithCI(OptionType optionType, double barrier, int timeSteps, boolean isAsian, double confidenceLevel) {
        List<Double> payoffs = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numberOfPaths; i++) {
            double payoff;
            if (isAsian) {
                List<Double> spotPrices = new ArrayList<>();
                double currentSpot = spot;

                for (int t = 0; t < timeSteps; t++) {
                    double z = random.nextGaussian();
                    currentSpot *= Math.exp((riskFreeRate - 0.5 * volatility * volatility) * (timeToExpiry / timeSteps)
                            + volatility * Math.sqrt(timeToExpiry / timeSteps) * z);
                    spotPrices.add(currentSpot);
                }
                payoff = calculateAsianPayoff(optionType, spotPrices);
            } else {
                double z = random.nextGaussian();
                double simulatedSpot = spot * Math.exp((riskFreeRate - 0.5 * volatility * volatility) * timeToExpiry
                        + volatility * Math.sqrt(timeToExpiry) * z);
                payoff = (barrier > 0) ? calculateBarrierPayoff(optionType, simulatedSpot, barrier) : calculatePayoff(optionType, simulatedSpot);
            }
            payoffs.add(payoff);
        }

        double meanPrice = Math.exp(-riskFreeRate * timeToExpiry) * payoffs.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double[] confidenceInterval = calculateConfidenceInterval(payoffs, confidenceLevel);

        return new double[]{meanPrice, confidenceInterval[0], confidenceInterval[1]};
    }

    public double calculatePayoff(OptionType optionType, double spotPrice) {
        switch (optionType) {
            case CALL:
                return Math.max(spotPrice - strike, 0.0);
            case PUT:
                return Math.max(strike - spotPrice, 0.0);
            default:
                throw new IllegalArgumentException("Unsupported option type specified.");
        }
    }

    public double calculateBarrierPayoff(OptionType optionType, double spotPrice, double barrier) {
        switch (optionType) {
            case KNOCK_IN_CALL:
                return (spotPrice >= barrier) ? Math.max(spotPrice - strike, 0.0) : 0.0;
            case KNOCK_OUT_CALL:
                return (spotPrice < barrier) ? Math.max(spotPrice - strike, 0.0) : 0.0;
            case KNOCK_IN_PUT:
                return (spotPrice <= barrier) ? Math.max(strike - spotPrice, 0.0) : 0.0;
            case KNOCK_OUT_PUT:
                return (spotPrice > barrier) ? Math.max(strike - spotPrice, 0.0) : 0.0;
            default:
                throw new IllegalArgumentException("Unsupported option type specified.");
        }
    }

    public double calculateAsianPayoff(OptionType optionType, List<Double> spotPrices) {
        double averageSpot = spotPrices.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        switch (optionType) {
            case CALL:
                return Math.max(averageSpot - strike, 0.0);
            case PUT:
                return Math.max(strike - averageSpot, 0.0);
            default:
                throw new IllegalArgumentException("Unsupported option type specified.");
        }
    }

    public double[] calculateConfidenceInterval(List<Double> payoffs, double confidenceLevel) {
        double mean = payoffs.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double variance = payoffs.stream().mapToDouble(payoff -> Math.pow(payoff - mean, 2)).sum() / (payoffs.size() - 1);
        double standardDeviation = Math.sqrt(variance);

        double z = (confidenceLevel == 99) ? 2.576 : 1.96;
        double marginOfError = z * standardDeviation / Math.sqrt(payoffs.size());

        return new double[]{mean - marginOfError, mean + marginOfError};
    }
}
