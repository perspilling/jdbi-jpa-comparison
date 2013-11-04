package no.kodemaker.ps.jpa.repository;

import no.kodemaker.ps.jpa.domain.JpaPerson;

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
