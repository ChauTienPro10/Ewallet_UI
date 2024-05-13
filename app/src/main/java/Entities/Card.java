package Entities;

import java.math.BigDecimal;

public class Card {

    private String pin;
    private BigDecimal  balance;
    private String card_number;
    private int member_id;

    public String getPin() {
        return pin;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getCard_number() {
        return card_number;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public Card(String pin, BigDecimal balance, String card_number, int member_id) {
        this.pin = pin;
        this.balance = balance;
        this.card_number = card_number;
        this.member_id = member_id;
    }

    public Card() {

    }
}
