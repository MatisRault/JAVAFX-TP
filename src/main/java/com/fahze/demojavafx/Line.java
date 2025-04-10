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

    public void convertToUsd() {
        if (inEuro) {
            logger.debug("Converting line {} from EUR to USD", period);
            float oldTotal = this.total;
            this.total = (float) CurrencyService.convertEurToUsd(this.total);
            this.housing = (float) CurrencyService.convertEurToUsd(this.housing);
            this.food = (float) CurrencyService.convertEurToUsd(this.food);
            this.outing = (float) CurrencyService.convertEurToUsd(this.outing);
            this.transport = (float) CurrencyService.convertEurToUsd(this.transport);
            this.travel = (float) CurrencyService.convertEurToUsd(this.travel);
            this.taxes = (float) CurrencyService.convertEurToUsd(this.taxes);
            this.other = (float) CurrencyService.convertEurToUsd(this.other);
            logger.debug("Converted total from €{} to ${}", oldTotal, this.total);
            inEuro = false;
        }
    }

    public void convertToEuro() {
        if (!inEuro) {
            logger.debug("Converting line {} from USD to EUR", period);
            float oldTotal = this.total;
            this.total = (float) CurrencyService.convertUsdToEur(this.total);
            this.housing = (float) CurrencyService.convertUsdToEur(this.housing);
            this.food = (float) CurrencyService.convertUsdToEur(this.food);
            this.outing = (float) CurrencyService.convertUsdToEur(this.outing);
            this.transport = (float) CurrencyService.convertUsdToEur(this.transport);
            this.travel = (float) CurrencyService.convertUsdToEur(this.travel);
            this.taxes = (float) CurrencyService.convertUsdToEur(this.taxes);
            this.other = (float) CurrencyService.convertUsdToEur(this.other);
            logger.debug("Converted total from ${} to €{}", oldTotal, this.total);
            inEuro = true;
        }
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