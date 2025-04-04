package com.fahze.demojavafx;

public class Line {

    private String period;
    private Float total;
    private Float housing;
    private Float food;
    private Float outing;
    private Float transport;
    private Float travel;
    private Float taxes;
    private Float other;

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
