package Entities;

public class EtherWallet {
    int id;
    int memberid;
    String address;
    String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getId() {
        return id;
    }


    public int getMemberid() {
        return memberid;
    }

    public String getAddress() {
        return address;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMemberid(int memberid) {
        this.memberid = memberid;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public EtherWallet(int id, int memberid, String address, String key) {
        this.id = id;
        this.memberid = memberid;
        this.address = address;
        this.key = key;
    }

    public EtherWallet(){}

}
