package si.fri.rso.uporabniki.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "customer")
@NamedQueries(value =
        {
                @NamedQuery(name = "CustomerEntity.getAll",
                        query = "SELECT c FROM CustomerEntity c"),

                @NamedQuery(name = "CustomerEntity.getByUsername",
                        query = "SELECT c FROM CustomerEntity c WHERE c.username = :username"),
        })
public class CustomerEntity {

    @Id
    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "yearBorn")
    private Integer yearBorn;

    @Column(name = "funds")
    private Float funds;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setYearBorn(Integer yearBorn) {
        this.yearBorn = yearBorn;
    }

    public Integer getYearBorn() {
        return yearBorn;
    }

    public void setFunds(Float funds) {
        this.funds = funds;
    }

    public Float getFunds() {
        return funds;
    }
}