package no.kodemaker.ps.jdbiapp.repository;

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
public class PersonRepository2Test {

    private static PersonRepository2 repository;

    @BeforeClass
    public static void init() {
        JdbiHelper jdbiHelper = new JdbiHelper();
        jdbiHelper.resetTable(PersonRepository2.TABLE_NAME, PersonRepository2.createTableSql);
        repository = jdbiHelper.getDBI().onDemand(PersonRepository2.class);
        new DbSeeder().initPersonTable(repository);
    }

    @Test
    public void shouldNotFindNonExistingPerson() {
        Person person2 = repository.findById(100L);
        assertNull(person2);
    }

    @Test
    public void idShouldHaveBeenSetByDB() {

    }

    @Test
    public void retrieveAll() {
        List<Person> persons = repository.listAll();
        assertThat(persons.size(), equalTo(5));
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
        List<Person> persons = repository.listAll();
        assertThat(persons.size(), equalTo(5));
        Person firstPerson = persons.get(0);

        repository.deleteById(firstPerson.getId());
        persons = repository.listAll();
        assertThat(persons.size(), equalTo(4));

        repository.insert(firstPerson);
        persons = repository.listAll();
        assertThat(persons.size(), equalTo(5));
    }

    @Test
    public void testUpdate() {
        List<Person> persons = repository.listAll();
        Person person = persons.get(0);
        person.setPhone("12345678");
        repository.update(person);
        Person updatedPerson = repository.findById(person.getId());
        assertThat(person, equalTo(updatedPerson));
    }
}
