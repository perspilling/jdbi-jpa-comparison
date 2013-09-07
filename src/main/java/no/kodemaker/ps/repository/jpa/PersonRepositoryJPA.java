package no.kodemaker.ps.repository.jpa;

import no.kodemaker.ps.domain.JpaPerson;
import no.kodemaker.ps.repository.JpaPersonRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Per Spilling
 */
public class PersonRepositoryJPA implements JpaPersonRepository {

    @PersistenceContext(unitName = "jpa-ds")
    private EntityManager em;

    // for testing
    public PersonRepositoryJPA(EntityManager entityManager) {
        em = entityManager;
    }

    @Override
    public JpaPerson getPerson(int id) {
        return em.find(JpaPerson.class, id);
    }

    @Override
    public List<JpaPerson> findByName(String name) {
        return em.createQuery("select p from JpaPerson p where p.name like :name", JpaPerson.class)
                .setParameter("name", name)
                .getResultList();
    }

    @Override
    public void add(JpaPerson person) {
        em.merge(person);
    }

    @Override
    public List<JpaPerson> listAll() {
        return em.createQuery("select entry from JpaPerson entry", JpaPerson.class).getResultList();
    }
}
