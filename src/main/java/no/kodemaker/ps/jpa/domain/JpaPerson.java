package no.kodemaker.ps.jpa.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * A standard POJO entity class representing persons, to be used with JPA.
 *
 * @author Per Spilling
 */
@Entity
public class JpaPerson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // required fields
    private String name;
    private String email;

    // optional fields
    private String phone;

    public JpaPerson() {
    }

    public JpaPerson(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public JpaPerson(Integer id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpaPerson person = (JpaPerson) o;

        if (!email.equals(person.email)) return false;
        if (id != null ? !id.equals(person.id) : person.id != null) return false;
        if (!name.equals(person.name)) return false;
        if (phone != null ? !phone.equals(person.phone) : person.phone != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + email.hashCode();
        return result;
    }
}
