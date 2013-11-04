package no.kodemaker.ps.jdbiapp.repository;

import no.kodemaker.ps.jdbiapp.DbProperties;
import no.kodemaker.ps.jdbiapp.domain.Person;

import java.util.List;

/**
 * @author Per Spilling
 */
public interface PersonRepository {

    String TABLE_NAME = "PERSON";

    String createTableSql = "create table PERSON (id " + DbProperties.SQL_AUTO_INCREMENT.val() +
            ", name varchar(80), email varchar(80), phone varchar(20))";

    Person getPerson(int id);

    List<Person> findByName(String name);

    void add(Person person);

    List<Person> listAll();
}
