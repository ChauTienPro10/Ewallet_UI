package Entities;

import java.util.List;

public class LoginResponse {
    private String jwt;
    private List<Object> Role;
    private String username;
    private long id;





    public LoginResponse(String jwt, List<Object> role, String username, long id) {
        super();
        this.jwt = jwt;
        Role = role;
        this.username = username;
        this.id = id;
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }




    public List<Object> getRole() {
        return Role;
    }


    public void setRole(List<Object> role) {
        Role = role;
    }





    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
