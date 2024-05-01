package Entities;

public class Member {
    private int member_id;
    private String fname;
    private String lname;
    private String email    ;
    private String country;
    private String phone;
    private String username;
    private String password;
    int account_status;

    String avatar;



    public Member(){}

    public Member( String fname, String lname, String email, String country, String phone, String username, String password, int account_status) {

        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.country = country;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.account_status = account_status;

    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getMember_id() {
        return member_id;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getEmail() {
        return email;
    }

    public String getCountry() {
        return country;
    }

    public String getPhone() {
        return phone;
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

    public String getAvatar() {
        return avatar;
    }
}
