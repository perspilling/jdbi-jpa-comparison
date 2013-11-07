package no.kodemaker.ps.jdbiapp.repository;

import no.kodemaker.ps.jdbiapp.domain.Email;
import no.kodemaker.ps.jdbiapp.domain.Person;
import no.kodemaker.ps.jdbiapp.repository.jdbi.JdbiHelper;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Per Spilling
 */
public class PersonDaoTest {

    private static PersonDao repository;

    @BeforeClass
    public static void init() {
        JdbiHelper jdbiHelper = new JdbiHelper();
        jdbiHelper.resetTable(PersonDao.TABLE_NAME, PersonDao.createTableSql);
        repository = jdbiHelper.getDBI().onDemand(PersonDao.class);
        DbSeeder.initPersonTable(repository);
    }

    @Test
    public void shouldNotFindNonExistingPerson() {
        Person person2 = repository.get(100);
        assertNull(person2);
    }

    @Test
    public void insertShouldReturnPk() {
        long pk = repository.insert(new Person("Albert Einstein", new Email("albert@nomail.com")));
        Person p = repository.get(pk);
        assertThat(p.getName(), equalTo("Albert Einstein"));
    }

    @Test
    public void retrieveAll() {
        List<Person> persons = repository.getAll();
        assertTrue(persons.size() >= 5);
    }

    @Test
    public void testFinders() {
        List<Person> persons = repository.findByName("Per%");
        assertThat(persons.size(), equalTo(2));

        persons = repository.findByName("Per Spilling");
        assertThat(persons.size(), equalTo(1));

        persons = repository.findByEmail("per@kodemaker.no");
        assertThat(persons.size(), equalTo(1));
    }

    @Test
    public void testDelete() {
        List<Person> persons = repository.getAll();
        int size = persons.size();
        Person firstPerson = persons.get(0);

        repository.deleteById(firstPerson.getId());
        persons = repository.getAll();
        assertThat(persons.size(), equalTo(size - 1));

        repository.insert(firstPerson);
        persons = repository.getAll();
        assertThat(persons.size(), equalTo(size));
    }

    @Test
    public void testUpdate() {
        List<Person> persons = repository.getAll();
        Person person = persons.get(0);
        person.setPhone("12345678");
        repository.update(person);
        Person updatedPerson = repository.get(person.getId());
        assertThat(person, equalTo(updatedPerson));
    }
}
