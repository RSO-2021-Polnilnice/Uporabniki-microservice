package si.fri.rso.uporabniki.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "customer")
@NamedQueries(value =
        {
                @NamedQuery(name = "CustomerEntity.getAll",
                        query = "SELECT c FROM CustomerEntity c"),

                @NamedQuery(name = "CustomerEntity.getById",
                        query = "SELECT c FROM CustomerEntity c WHERE c.id = :id"),

                @NamedQuery(name = "CustomerEntity.getSubscribedEmails",
                        query = "SELECT c FROM CustomerEntity c"),

                @NamedQuery(name = "CustomerEntity.getByUsername",
                        query = "SELECT c FROM CustomerEntity c WHERE c.username = :username"),
        })
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "yearBorn")
    private Integer yearBorn;

    @Column(name = "funds")
    private Float funds;

    @Column(name = "charging")
    private Boolean charging;

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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}