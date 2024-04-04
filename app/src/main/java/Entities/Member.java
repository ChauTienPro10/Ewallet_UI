package Entities;

public class Member {
    private int member_id;
    private String first_name;
    private String last_name;
    private String email_address;
    private String country;
    private String contact_number;
    private String username;
    private String password;
    int account_status;

    public int getMember_id() {
        return member_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail_address() {
        return email_address;
    }

    public String getCountry() {
        return country;
    }

    public String getContact_number() {
        return contact_number;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getAccount_status() {
        return account_status;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccount_status(int account_status) {
        this.account_status = account_status;
    }

    public Member(String first_name, String last_name, String email_address, String country, String contact_number, String username, String password, int account_status) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email_address = email_address;
        this.country = country;
        this.contact_number = contact_number;
        this.username = username;
        this.password = password;
        this.account_status = account_status;
    }

    public Member(){}
}
