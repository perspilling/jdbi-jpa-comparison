package no.kodemaker.ps.jdbiapp.repository;

import no.kodemaker.ps.jdbiapp.domain.Person;
import no.kodemaker.ps.jdbiapp.repository.jdbi.TableCreator;

import java.util.List;

/**
 * @author Per Spilling
 */
public interface PersonDao extends CrudDao<Person, Long>, TableCreator {
    List<Person> findByName(String name);
    List<Person> findByEmail(String email);
}
