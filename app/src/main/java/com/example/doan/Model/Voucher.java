package com.example.doan.Model;

public class Voucher {
    private String title;
    private String detail;
    private boolean used;
    private String expiryDate;
    private String type;
    private double value;
    private double minimumSpend;

    public Voucher() {}

    public Voucher(String title, String detail, boolean used, String expiryDate,
                   String type, double value, double minimumSpend) {
        this.title = title;
        this.detail = detail;
        this.used = used;
        this.expiryDate = expiryDate;
        this.type = type;
        this.value = value;
        this.minimumSpend = minimumSpend;
    }

    public String getTitle() { return title; }
    public String getDetail() { return detail; }
    public boolean isUsed() { return used; }
    public String getExpiryDate() { return expiryDate; }
    public String getType() { return type; }
    public double getValue() { return value; }
    public double getMinimumSpend() { return minimumSpend; }

    public void setTitle(String title) { this.title = title; }
    public void setDetail(String detail) { this.detail = detail; }
    public void setUsed(boolean used) { this.used = used; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
    public void setType(String type) { this.type = type; }
    public void setValue(double value) { this.value = value; }
    public void setMinimumSpend(double minimumSpend) { this.minimumSpend = minimumSpend; }
}
