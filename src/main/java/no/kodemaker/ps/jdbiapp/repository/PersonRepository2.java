package no.kodemaker.ps.jdbiapp.repository;

import no.kodemaker.ps.jdbiapp.DbProperties;
import no.kodemaker.ps.jdbiapp.domain.Person;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

/**
 * JDBI repository for the Person class.
 *
 * @author Per Spilling
 */
@RegisterMapper(PersonMapper.class)
public interface PersonRepository2 {
    String TABLE_NAME = "PERSON";

    String createTableSql = "create table PERSON (id " + DbProperties.SQL_AUTO_INCREMENT.val() +
            ", name varchar(80), email varchar(80), phone varchar(20))";

    @SqlUpdate("insert into PERSON (name, email, phone) values (:p.name, :p.emailVal, :p.phone)")
    void insert(@BindBean("p") Person person);

    @SqlUpdate("update PERSON set name = :p.name, email = :p.emailVal, phone = :p.phone where id = :p.id")
    void update(@BindBean("p") Person person);

    @SqlQuery("select * from PERSON where id = :id")
    Person findById(@Bind("id") Long id);

    @SqlQuery("select * from PERSON where name like :name")
    List<Person> findByName(@Bind("name") String name);

    @SqlQuery("select * from PERSON where email like :email")
    List<Person> findByEmail(@Bind("email") String email);

    @SqlQuery("select * from PERSON")
    List<Person> listAll();

    @SqlUpdate("delete from PERSON where id = :id")
    void deleteById(@Bind("id") Long id);
}
