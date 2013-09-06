package no.kodemaker.ps.repository.jpa;

import no.kodemaker.ps.domain.JdbiPerson;
import no.kodemaker.ps.domain.JpaPerson;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Per Spilling
 */
public class JpaPersonRepositoryTest {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-ds");

    private static PersonRepositoryJPA repo;

    @BeforeClass
    public static void init() {
        repo = new PersonRepositoryJPA(emf.createEntityManager());
        populateDB();
    }

    private static void populateDB() {
        repo.add(new JpaPerson("Per Spilling", "per@kodemaker.no"));
        repo.add(new JpaPerson("Per Spellman", "pspellman@nomail.com"));
        repo.add(new JpaPerson("Larry", "larry@nomail.com"));
        repo.add(new JpaPerson("James", "james@nomail.com"));
    }

    @Test
    public void retrieveAll() {
        PersonRepositoryJPA repo = new PersonRepositoryJPA(emf.createEntityManager());
        List<JpaPerson> persons = repo.listAll();
        assertThat(persons.size(), equalTo(4));
    }
}
