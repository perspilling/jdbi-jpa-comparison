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
public class PersonDaoFluentStyleTest {

    private static PersonDaoFluentStyle repository;

    @BeforeClass
    public static void initDb() {
        JdbiHelper jdbiHelper = new JdbiHelper();
        repository = new PersonDaoFluentStyle(jdbiHelper.getDBI());
        jdbiHelper.resetTable(PersonDao.TABLE_NAME, PersonDao.createTableSql);
        DbSeeder.initPersonTable(repository);
    }

    @Test
    public void shouldNotFindNonExistingPerson() {
        Person person2 = repository.getPerson(100);
        assertNull(person2);
    }

    @Test
    public void idShouldHaveBeenSetByDB() {
        repository.insert(new Person("John Doe", new Email("john.doe@nomail.com")));
        Person p = repository.findByName("John Doe").get(0);
        assertTrue(p.getId() != null);
    }

    @Test
    public void retrieveAll() {
        List<Person> persons = repository.getAll();
        assertTrue(persons.size() > 4);
    }

    @Test
    public void findPer() {
        List<Person> persons = repository.findByName("Per%");
        assertThat(persons.size(), equalTo(2));

        persons = repository.findByName("Per Spilling");
        assertThat(persons.size(), equalTo(1));
    }
}
