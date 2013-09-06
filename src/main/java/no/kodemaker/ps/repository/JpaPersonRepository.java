package no.kodemaker.ps.repository;

import no.kodemaker.ps.domain.JpaPerson;

import java.util.List;

/**
 * @author Per Spilling
 */
public interface JpaPersonRepository {

    JpaPerson getPerson(int id);

    List<JpaPerson> findByName(String name);

    void add(JpaPerson person);

    List listAll();
}
