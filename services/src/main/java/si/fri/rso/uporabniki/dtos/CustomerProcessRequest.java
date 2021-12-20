package si.fri.rso.uporabniki.dtos;

public class CustomerProcessRequest {

    private String username;

    public CustomerProcessRequest() {
    }

    public CustomerProcessRequest(String username) {
        this.username = username;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
