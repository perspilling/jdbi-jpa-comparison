package no.kodemaker.ps.domain;

/**
 * A standard POJO entity class representing persons, to be used with JDBI.
 *
 * @author Per Spilling
 */
public class JdbiPerson {
    private Integer id;  // PK

    // required fields
    private String name;
    private String email;

    // optional fields
    private String phone;

    public JdbiPerson(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public JdbiPerson(Integer id, String name, String email, String phone) {
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

        JdbiPerson person = (JdbiPerson) o;

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
