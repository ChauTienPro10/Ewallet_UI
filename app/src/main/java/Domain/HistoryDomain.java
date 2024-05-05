package Domain;

import java.math.BigDecimal;

public class HistoryDomain {
    private String type;
    private  String time;
    private String content;
    private  String receiver;
    private BigDecimal amount;
    private  String picPath;

    public HistoryDomain(String type, String time, String content, String receiver, BigDecimal amount) {
        this.type = type;
        this.time = time;
        this.content = content;
        this.receiver = receiver;
        this.amount = amount;
        this.picPath="bitcoin";
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

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}
