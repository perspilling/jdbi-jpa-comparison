package no.kodemaker.ps.jpa.repository;

import no.kodemaker.ps.jpa.domain.JpaPerson;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Per Spilling
 */
public class PersonRepositoryTest {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-ds");
    private static PersonRepositoryJPA repo;

    @BeforeClass
    public static void init() {
        EntityManager entityManager = emf.createEntityManager();
        repo = new PersonRepositoryJPA(entityManager);
        entityManager.getTransaction().begin();
        populateDB();
        entityManager.getTransaction().commit();
    }

    private static void populateDB() {
        repo.add(new JpaPerson("Per Spilling", "per@kodemaker.no"));
        repo.add(new JpaPerson("Per Spellman", "pspellman@nomail.com"));
        repo.add(new JpaPerson("Larry", "larry@nomail.com"));
        repo.add(new JpaPerson("James", "james@nomail.com"));
    }

    @Test
    public void retrieveAll() {
        List<JpaPerson> persons = repo.listAll();
        assertThat(persons.size(), equalTo(4));
    }

    @Test
    public void findByName() {
        List<JpaPerson> persons = repo.findByName("Larry");
        assertThat(persons.size(), equalTo(1));

        persons = repo.findByName("Pe%");
        assertThat(persons.size(), equalTo(2));
    }
}
