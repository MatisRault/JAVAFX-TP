package com.fahze.demojavafx;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Income {
    private static final Logger logger = LogManager.getLogger(Income.class);

    private String period;
    private Float total;
    private Float salary;
    private Float benefits;
    private Float selfEmployment;
    private Float passive;
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

    public Float getSalary() {
        return salary;
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }

    public Float getBenefits() {
        return benefits;
    }

    public void setBenefits(Float benefits) {
        this.benefits = benefits;
    }

    public Float getSelfEmployment() {
        return selfEmployment;
    }

    public void setSelfEmployment(Float selfEmployment) {
        this.selfEmployment = selfEmployment;
    }

    public Float getPassive() {
        return passive;
    }

    public void setPassive(Float passive) {
        this.passive = passive;
    }

    public Float getOther() {
        return other;
    }

    public void setOther(Float other) {
        this.other = other;
    }

    public boolean isInEuro() {
        return inEuro;
    }

    public void setExchangeRate(Float rate) {
        this.exchangeRate = rate != null && rate > 0 ? rate : 1.0f;
    }

    public void convertToUsd() {
        if (inEuro && needsConversion()) {
            logger.debug("Converting income {} from EUR to USD with rate {}", period, exchangeRate);
            this.total *= exchangeRate;
            this.salary *= exchangeRate;
            this.benefits *= exchangeRate;
            this.selfEmployment *= exchangeRate;
            this.passive *= exchangeRate;
            this.other *= exchangeRate;
            inEuro = false;
        }
    }

    public void convertToEuro() {
        if (!inEuro && needsConversion()) {
            logger.debug("Converting income {} from USD to EUR with rate {}", period, exchangeRate);
            this.total /= exchangeRate;
            this.salary /= exchangeRate;
            this.benefits /= exchangeRate;
            this.selfEmployment /= exchangeRate;
            this.passive /= exchangeRate;
            this.other /= exchangeRate;
            inEuro = true;
        }
    }

    private boolean needsConversion() {
        return Math.abs(exchangeRate - 1.0f) > 0.001f;
    }

    public Income() {}

    public Income(String period, Float total, Float salary, Float benefits, Float selfEmployment, Float passive, Float other) {
        this.period = period;
        this.total = total;
        this.salary = salary;
        this.benefits = benefits;
        this.selfEmployment = selfEmployment;
        this.passive = passive;
        this.other = other;
    }
}