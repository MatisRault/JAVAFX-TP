package com.fahze.demojavafx.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;

public class CurrencyService {
    private static final Logger logger = LogManager.getLogger(CurrencyService.class);
    private static double eurToUsdRate = 1.09; // Default exchange rate (more realistic than 1.0)

    /**
     * Simulates getting the EUR to USD exchange rate from an external service
     * In a real application, this would make an API call to a currency exchange rate service
     * @return CompletableFuture containing the exchange rate
     */
    public static CompletableFuture<Double> getExchangeRate() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Simulate network delay
                Thread.sleep(200);

                // In a real app, we would fetch the rate from an API
                // For now, we return a realistic EUR to USD rate
                double rate = eurToUsdRate;
                logger.info("Retrieved EUR to USD rate: {}", rate);
                return rate;
            } catch (InterruptedException e) {
                logger.error("Error while retrieving exchange rate", e);
                Thread.currentThread().interrupt();
                return 1.09; // Fallback value
            }
        });
    }

    /**
     * Set the EUR to USD exchange rate
     * @param rate the exchange rate
     */
    public static void setEurToUsdRate(double rate) {
        eurToUsdRate = rate;
        logger.info("EUR to USD rate set to: {}", rate);
    }

    /**
     * Convert EUR amount to USD
     * @param eurAmount amount in EUR
     * @return amount in USD
     */
    public static double convertEurToUsd(double eurAmount) {
        return eurAmount * eurToUsdRate;
    }

    /**
     * Convert USD amount to EUR
     * @param usdAmount amount in USD
     * @return amount in EUR
     */
    public static double convertUsdToEur(double usdAmount) {
        return usdAmount / eurToUsdRate;
    }
}