package com.fahze.demojavafx;

import com.fahze.demojavafx.service.CurrencyService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Line {
    private static final Logger logger = LogManager.getLogger(Line.class);

    private String period;
    private Float total;
    private Float housing;
    private Float food;
    private Float outing;
    private Float transport;
    private Float travel;
    private Float taxes;
    private Float other;
    private boolean inEuro = true;
    private Float exchangeRate = 1.0f;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public Float getHousing() {
        return housing;
    }

    public void setHousing(Float housing) {
        this.housing = housing;
    }

    public Float getFood() {
        return food;
    }

    public void setFood(Float food) {
        this.food = food;
    }

    public Float getOuting() {
        return outing;
    }

    public void setOuting(Float outing) {
        this.outing = outing;
    }

    public Float getTransport() {
        return transport;
    }

    public void setTransport(Float transport) {
        this.transport = transport;
    }

    public Float getTravel() {
        return travel;
    }

    public void setTravel(Float travel) {
        this.travel = travel;
    }

    public Float getOther() {
        return other;
    }

    public void setOther(Float other) {
        this.other = other;
    }

    public Float getTaxes() {
        return taxes;
    }

    public void setTaxes(Float taxes) {
        this.taxes = taxes;
    }

    public boolean isInEuro() {
        return inEuro;
    }

    public void setExchangeRate(Float rate) {
        this.exchangeRate = rate != null && rate > 0 ? rate : 1.0f;
    }

    public void convertToUsd() {
        if (inEuro && needsConversion()) {
            logger.debug("Converting line {} from EUR to USD with rate {}", period, exchangeRate);
            this.total *= exchangeRate;
            this.housing *= exchangeRate;
            this.food *= exchangeRate;
            this.outing *= exchangeRate;
            this.transport *= exchangeRate;
            this.travel *= exchangeRate;
            this.taxes *= exchangeRate;
            this.other *= exchangeRate;
            inEuro = false;
        }
    }

    public void convertToEuro() {
        if (!inEuro && needsConversion()) {
            logger.debug("Converting line {} from USD to EUR with rate {}", period, exchangeRate);
            this.total /= exchangeRate;
            this.housing /= exchangeRate;
            this.food /= exchangeRate;
            this.outing /= exchangeRate;
            this.transport /= exchangeRate;
            this.travel /= exchangeRate;
            this.taxes /= exchangeRate;
            this.other /= exchangeRate;
            inEuro = true;
        }
    }

    private boolean needsConversion() {
        return Math.abs(exchangeRate - 1.0f) > 0.001f;
    }

    public Line() {}

    public Line(String period, Float total, Float housing, Float food, Float outing, Float transport, Float travel, Float taxes, Float other) {
        this.period = period;
        this.total = total;
        this.housing = housing;
        this.food = food;
        this.outing = outing;
        this.transport = transport;
        this.travel = travel;
        this.taxes = taxes;
        this.other = other;
    }
}