package no.kodemaker.ps.jdbiapp.repository;

import no.kodemaker.ps.jdbiapp.domain.Email;
import no.kodemaker.ps.jdbiapp.domain.Person;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

/**
 * @author Per Spilling
 */
public class PersonDaoJdbiTest {

    private static PersonDaoJdbi dao = new PersonDaoJdbi();

    @BeforeClass
    public static void init() {
        dao.dropTable();
        dao.createTable();
        DbSeeder.initPersonTable(dao);
    }

    @Test
    public void shouldNotFindNonExistingPerson() {
        Person p = dao.get(100L);
        assertNull(p);
    }

    @Test
    public void saveShouldUpdatePrimaryKey() {
        Person p = dao.save(new Person("Albert Einstein", new Email("albert@nomail.com")));
        assertTrue(p.getId() != null);
    }

    @Test
    public void retrieveAll() {
        List<Person> persons = dao.getAll();
        assertTrue(persons.size() >= 5);
    }

    @Test
    public void testDependantHomeAddress() {
        Person person = dao.findByName("Per Spilling").get(0);
        assertThat(person.getHomeAddress(), notNullValue());
        person.getHomeAddress().setPostalPlace("Oslo");
        Person p = dao.save(person);
        assertThat(p.getHomeAddress().getPostalPlace(), equalTo("Oslo"));
    }

    @Test
    public void testFinders() {
        List<Person> persons = dao.findByName("Per%");
        assertThat(persons.size(), equalTo(2));

        persons = dao.findByName("Per Spilling");
        assertThat(persons.size(), equalTo(1));

        persons = dao.findByEmail("per@kodemaker.no");
        assertThat(persons.size(), equalTo(1));
    }

    @Test
    public void testDelete() {
        List<Person> persons = dao.getAll();
        int size = persons.size();

        Person p = dao.save(new Person("John Doe", new Email("john@mail.com")));
        persons = dao.getAll();
        assertThat(persons.size(), equalTo(size + 1));

        dao.delete(p.getId());
        persons = dao.getAll();
        assertThat(persons.size(), equalTo(size));
    }

    @Test
    public void testUpdate() {
        List<Person> persons = dao.getAll();
        Person person = persons.get(0);
        person.setPhone("12345678");
        dao.save(person);
        Person updatedPerson = dao.get(person.getId());
        assertThat(person, equalTo(updatedPerson));
    }
}
