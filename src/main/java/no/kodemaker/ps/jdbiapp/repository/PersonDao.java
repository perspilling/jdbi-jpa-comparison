package no.kodemaker.ps.jdbiapp.repository;

import no.kodemaker.ps.jdbiapp.domain.Person;

import java.util.List;

/**
 * @author Per Spilling
 */
public interface PersonDao {
    Person save(Person person);  // i.e. insert-or-update
    Person get(long id);
    List<Person> findByName(String name);
    List<Person> findByEmail(String email);
    List<Person> getAll();
    void deleteById(long id);
}
