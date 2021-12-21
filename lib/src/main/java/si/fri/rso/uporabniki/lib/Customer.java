package si.fri.rso.uporabniki.lib;

public class Customer {

    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private Integer yearBorn;
    private Float funds;
    private Boolean charging;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getYearBorn() {
        return yearBorn;
    }

    public void setYearBorn(Integer yearBorn) {
        this.yearBorn = yearBorn;
    }

    public Float getFunds() {
        return funds;
    }

    public void setFunds(Float funds) {
        this.funds = funds;
    }

    public void setCharging(Boolean charging) {
        this.charging = charging;
    }

    public Boolean getCharging() {
        return charging;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
