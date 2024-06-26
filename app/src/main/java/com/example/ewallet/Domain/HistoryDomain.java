package com.example.ewallet.Domain;

import com.example.ewallet.R;

import java.math.BigDecimal;

public class HistoryDomain {
    private String type;
    private  String time;
    private String content;
    private  String receiver;
    private BigDecimal amount;
    private  int picPath;

    public HistoryDomain(String type, String time, String content, String receiver, BigDecimal amount, int picPath) {
        this.type = type;
        this.time = time;
        this.content = content;
        this.receiver = receiver;
        this.amount = amount;
        switch (type){
            case "withdraw":
                this.picPath= R.drawable.btn_2;
                break;
            case "deposit":
                this.picPath= R.drawable.btn_1;
                break;
            case "transfer":
                this.picPath=R.drawable.money_bill_transfer_solid;
                break;
            case "recharge telephone":
                this.picPath=R.drawable.btn_2;
                break;

            default: break;

        }

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getPicPath() {
        return picPath;
    }

    public void setPicPath(int picPath) {
        this.picPath = picPath;
    }
}
