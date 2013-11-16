package no.kodemaker.ps.jdbiapp.repository;

import no.kodemaker.ps.jdbiapp.domain.Person;
import no.kodemaker.ps.jdbiapp.repository.jdbi.JdbiHelper;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import java.util.List;

/**
 * A repository that internally uses a generated JDBI class.
 *
 * @author Per Spilling
 */
public class PersonDaoJdbi implements PersonDao {

    public static String TABLE_NAME = "PERSON";

    public static String createPersonTableSql_postgres =
            "create table PERSON (" +
                    "person_id serial PRIMARY KEY, " +
                    "name varchar(80) NOT NULL, " +
                    "email varchar(80), " +
                    "phone varchar(20))";

    private JdbiHelper jdbiHelper = new JdbiHelper();
    private final DBI dbi = jdbiHelper.getDBI();

     // Let DBI manage the connection
    private PersonDao personDao = dbi.onDemand(PersonDao.class);

    public PersonDaoJdbi() {
    }

    @Override
    public Person save(Person person) {
        long pk;
        if (person.getId() == null) {
            pk = personDao.insert(person);
        } else {
            pk = person.getId();
            personDao.update(person);
        }
        return personDao.get(pk);
    }

    @Override
    public Person get(long id) {
        return personDao.get(id);
    }

    @Override
    public List<Person> findByName(String name) {
        return personDao.findByName(name);
    }

    @Override
    public List<Person> findByEmail(String email) {
        return personDao.findByEmail(email);
    }

    @Override
    public List<Person> getAll() {
        return personDao.getAll();
    }

    @Override
    public void deleteById(long id) {
        personDao.deleteById(id);
    }

    // generated JDBI class used internally

    @RegisterMapper(PersonMapper.class)
    private interface PersonDao extends Transactional<PersonDao> {

        @SqlUpdate("insert into PERSON (name, email, phone) values (:p.name, :p.emailVal, :p.phone)")
        @GetGeneratedKeys
        long insert(@BindBean("p") Person person);

        @SqlUpdate("update PERSON set name = :p.name, email = :p.emailVal, phone = :p.phone where person_id = :p.id")
        void update(@BindBean("p") Person person);

        @SqlQuery("select * from PERSON where person_id = :id")
        Person get(@Bind("id") long id);

        @SqlQuery("select * from PERSON where name like :name")
        List<Person> findByName(@Bind("name") String name);

        @SqlQuery("select * from PERSON where email like :email")
        List<Person> findByEmail(@Bind("email") String email);

        @SqlQuery("select * from PERSON")
        List<Person> getAll();

        @SqlUpdate("delete from PERSON where person_id = :id")
        void deleteById(@Bind("id") long id);
    }
}
