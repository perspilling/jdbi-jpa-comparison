package no.kodemaker.ps.repository.jdbi;

import no.kodemaker.ps.domain.Person;
import no.kodemaker.ps.repository.PersonRepository;
import no.kodemaker.ps.repository.datasource.DataSourceProvider;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import java.util.List;

/**
 * A JBDI implementation of the {@link PersonRepository} interface.
 *
 * @author Per Spilling
 */
public class PersonRepositoryJDBI implements PersonRepository {

    private DBI dbi;

    public PersonRepositoryJDBI(DataSourceProvider dataSourceProvider) {
        dbi = new DBI(dataSourceProvider.getDataSource());
    }

    public void initializeDB() {
        String sql =
                "create table PERSON(" +
                "id int auto_increment primary key," +
                "name varchar(100)," +
                "email varchar(100)," +
                "phone varchar(10))";

        Handle h = dbi.open();
        h.execute(sql);
        h.close();
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

    public void add(Person person) {
        try (Handle h = dbi.open()) {
            h.createStatement("insert into PERSON (name, email, phone) values (:name, :email, :phone)")
                    .bind("name", person.getName())
                    .bind("email", person.getEmail())
                    .bind("phone", person.getPhone())
                    .execute();
        }
    }

    public List<Person> listAll() {
        List<Person> persons;
        try (Handle h = dbi.open()) {
            persons = h.createQuery("select * from PERSON").map(PersonMapper.INSTANCE).list();
        }
        return persons;
    }
}
