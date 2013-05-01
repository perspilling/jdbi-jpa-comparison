package no.kodemaker.ps.repository;

import no.kodemaker.ps.domain.Person;

import java.util.List;

/**
 * @author Per Spilling
 */
public interface PersonRepository {

    void initializeDB();

    Person getPerson(int id);

    List<Person> findByName(String name);

    void add(Person person);

    List<Person> listAll();
}
