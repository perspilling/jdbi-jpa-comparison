package no.kodemaker.ps.jdbiapp.repository;

import no.kodemaker.ps.jdbiapp.domain.Person;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import java.util.List;

/**
 * Example of using the jDBI fluent style API.
 *
 * @author Per Spilling
 */
public class PersonDaoFluentStyle {

    private DBI dbi;

    public PersonDaoFluentStyle(DBI dbi) {
        this.dbi = dbi;
    }

    public Person getPerson(int id) {
        Person person;
        try (Handle h = dbi.open()) {
            person = h.createQuery("select * from PERSON where id = :id").bind("id", id)
                    .map(PersonMapper.INSTANCE)
                    .first();
        }
        return person;
    }

    public List<Person> findByName(String name) {
        List<Person> persons;
        try (Handle h = dbi.open()) {
            persons = h.createQuery("select * from PERSON where name like :name").bind("name", name)
                    .map(PersonMapper.INSTANCE)
                    .list();
        }
        return persons;
    }

    public void insert(Person person) {
        try (Handle h = dbi.open()) {
            h.createStatement("insert into PERSON (name, email, phone) values (:name, :email, :phone)")
                    .bind("name", person.getName())
                    .bind("email", person.getEmail().getVal())
                    .bind("phone", person.getPhone())
                    .execute();
        }
    }

    public List<Person> getAll() {
        List<Person> persons;
        try (Handle h = dbi.open()) {
            persons = h.createQuery("select * from PERSON").map(PersonMapper.INSTANCE).list();
        }
        return persons;
    }
}
