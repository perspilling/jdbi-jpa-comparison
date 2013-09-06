package no.kodemaker.ps.repository.jdbi;

import no.kodemaker.ps.domain.JdbiPerson;
import no.kodemaker.ps.repository.JdbiPersonRepository;
import no.kodemaker.ps.repository.datasource.H2DataSourceProvider;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * @author Per Spilling
 */
public class JdbiPersonRepositoryTest {

    private static JdbiPersonRepository repository;

    @BeforeClass
    public static void init() {
        repository = new PersonRepositoryJDBI(new H2DataSourceProvider());
        repository.initializeDB();
        populateDB();
    }

    private static void populateDB() {
        repository.add(new JdbiPerson("Per Spilling", "per@kodemaker.no"));
        repository.add(new JdbiPerson("Per Spellman", "pspellman@nomail.com"));
        repository.add(new JdbiPerson("Larry", "larry@nomail.com"));
        repository.add(new JdbiPerson("James", "james@nomail.com"));
    }

    @Test
    public void shouldNotFindNonExistingPerson() {
        JdbiPerson person2 = repository.getPerson(100);
        assertNull(person2);
    }

    @Test
    public void idShouldHaveBeenSetByDB() {

    }

    @Test
    public void retrieveAll() {
        List<JdbiPerson> persons = repository.listAll();
        assertThat(persons.size(), equalTo(4));
    }

    @Test
    public void findPer() {
        List<JdbiPerson> persons = repository.findByName("Per%");
        assertThat(persons.size(), equalTo(2));

        persons = repository.findByName("Per Spilling");
        assertThat(persons.size(), equalTo(1));
    }
}
