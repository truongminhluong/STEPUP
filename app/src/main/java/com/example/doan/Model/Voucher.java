package com.example.doan.Model;

public class Voucher {
    private String title;
    private String detail;
    private boolean used;

    public Voucher(String title, String detail, boolean used) {
        this.title = title;
        this.detail = detail;
        this.used = used;
    }

    public String getTitle() { return title; }
    public String getDetail() { return detail; }
    public boolean isUsed() { return used; }
}
