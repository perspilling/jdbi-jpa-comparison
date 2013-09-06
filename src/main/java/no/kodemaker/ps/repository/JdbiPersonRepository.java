package no.kodemaker.ps.repository;

import no.kodemaker.ps.domain.JdbiPerson;

import java.util.List;

/**
 * @author Per Spilling
 */
public interface JdbiPersonRepository {

    void initializeDB();

    JdbiPerson getPerson(int id);

    List<JdbiPerson> findByName(String name);

    void add(JdbiPerson person);

    List<JdbiPerson> listAll();
}
