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

/**
 * @author Per Spilling
 */
public class PersonRepositoryTest {

    private static PersonRepository repository;

    @BeforeClass
    public static void init() {
        JdbiHelper jdbiHelper = new JdbiHelper();
        repository = new PersonRepositoryJDBI(jdbiHelper.getDBI());
        jdbiHelper.createTableIfNotPresent(PersonRepository.TABLE_NAME, PersonRepository2.createTableSql);
        repository.add(new Person("Per Spilling", new Email("per@kodemaker.no")));
        repository.add(new Person("Per Spellman", new Email("pspellman@nomail.com")));
        repository.add(new Person("Larry", new Email("larry@nomail.com")));
        repository.add(new Person("James", new Email("james@nomail.com")));
    }

    @Test
    public void shouldNotFindNonExistingPerson() {
        Person person2 = repository.getPerson(100);
        assertNull(person2);
    }

    @Test
    public void idShouldHaveBeenSetByDB() {

    }

    @Test
    public void retrieveAll() {
        List<Person> persons = repository.listAll();
        assertThat(persons.size(), equalTo(4));
    }

    @Test
    public void findPer() {
        List<Person> persons = repository.findByName("Per%");
        assertThat(persons.size(), equalTo(2));

        persons = repository.findByName("Per Spilling");
        assertThat(persons.size(), equalTo(1));
    }
}
