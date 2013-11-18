package no.kodemaker.ps.jdbiapp.repository;

import no.kodemaker.ps.jdbiapp.domain.Address;
import no.kodemaker.ps.jdbiapp.domain.Person;
import no.kodemaker.ps.jdbiapp.repository.jdbi.JdbiHelper;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import java.util.List;

/**
 * Person DAO implemented with a cascading person -> address association for the homeAddress field.
 *
 * @author Per Spilling
 */
public class PersonDaoJdbi implements PersonDao {
    private PersonDao personDao;
    private PersonAddressDao personAddressDao;
    private AddressDao addressDao;
    private JdbiHelper jdbiHelper;

    public PersonDaoJdbi() {
        jdbiHelper = new JdbiHelper();
        personDao = jdbiHelper.getDBI().onDemand(PersonDao.class);
        personAddressDao = jdbiHelper.getDBI().onDemand(PersonAddressDao.class);
        addressDao = new AddressDaoJdbi();
    }

    @Override
    public void createTable() {
        jdbiHelper.createTableIfNotPresent(PersonDao.TABLE_NAME, PersonDao.createTableSql_postgres);
        jdbiHelper.createTableIfNotPresent(PersonAddressDao.TABLE_NAME, PersonAddressDao.createTableSql_postgres);
    }

    @Override
    public void dropTable() {
        jdbiHelper.dropTableIfNotPresent(PersonDao.TABLE_NAME);
        jdbiHelper.dropTableIfNotPresent(PersonAddressDao.TABLE_NAME);
    }

    /**
     * Insert or update the person table and its associated address table if the person has a homeAddress.
     * @param person
     * @return the saved person instance
     */
    @Override
    public Person save(Person person) {
        if (person.getId() == null) {
            long personId = personDao.insert(person);
            insertHomeAddressIfExist(personId, person);
            return get(personId);
        } else {
            personDao.update(person);
            updateHomeAddressIfExist(person);
            return get(person.getId());
        }
    }

    private void insertHomeAddressIfExist(long personId, Person person) {
        if (person.getHomeAddress() != null) {
            Address a = addressDao.save(person.getHomeAddress());
            personAddressDao.insert(new PersonAddressAssoc(personId, a.getId()));
        }
    }

    private void updateHomeAddressIfExist(Person person) {
        Address homeAddress = person.getHomeAddress();
        if (homeAddress != null) {
            if (homeAddress.getId() == null) {
                Address a = addressDao.save(homeAddress);
                personAddressDao.insert(new PersonAddressAssoc(person.getId(), a.getId()));
                person.setHomeAddress(a);
            } else {
                addressDao.save(homeAddress);
            }
        }
    }

    @Override
    public Person get(Long id) {
        return getHomeAddressIfExist(personDao.get(id));
    }

    private Person getHomeAddressIfExist(Person person) {
        if (person == null) return null;
        List<PersonAddressAssoc> personAddressAssocList = personAddressDao.findByPersonId(person.getId());
        if (personAddressAssocList.size() == 1) {
            person.setHomeAddress(addressDao.get(personAddressAssocList.get(0).getAddressId()));
        }
        return person;
    }

    private List<Person> getHomeAddressIfExist(List<Person> persons) {
        if (persons == null) return null;
        for (Person p : persons) {
            getHomeAddressIfExist(p);
        }
        return persons;
    }

    @Override
    public List<Person> findByName(String name) {
        return getHomeAddressIfExist(personDao.findByName(name));
    }

    @Override
    public List<Person> findByEmail(String email) {
        return getHomeAddressIfExist(personDao.findByEmail(email));
    }

    @Override
    public List<Person> getAll() {
        return getHomeAddressIfExist(personDao.getAll());
    }

    @Override
    public boolean exists(Long id) {
        return personDao.get(id) != null;
    }

    @Override
    public void delete(Long id) {
        personDao.deleteById(id);
    }

    /**
     * Internal Person JDBI dao.
     */
    @RegisterMapper(PersonMapper.class)
    private interface PersonDao extends Transactional<PersonDao> {

        final static String TABLE_NAME = "PERSON";

        final static String createTableSql_postgres =
                "create table PERSON (" +
                        "personId serial PRIMARY KEY, " +
                        "name varchar(80) NOT NULL, " +
                        "email varchar(80), " +
                        "phone varchar(20))";

        @SqlUpdate("insert into PERSON (name, email, phone) values (:p.name, :p.emailVal, :p.phone)")
        @GetGeneratedKeys
        long insert(@BindBean("p") Person person);

        @SqlUpdate("update PERSON set name = :p.name, email = :p.emailVal, phone = :p.phone where personId = :p.id")
        void update(@BindBean("p") Person person);

        @SqlQuery("select * from PERSON where personId = :id")
        Person get(@Bind("id") long id);

        @SqlQuery("select * from PERSON where name like :name")
        List<Person> findByName(@Bind("name") String name);

        @SqlQuery("select * from PERSON where email like :email")
        List<Person> findByEmail(@Bind("email") String email);

        @SqlQuery("select * from PERSON")
        List<Person> getAll();

        @SqlUpdate("delete from PERSON where personId = :id")
        void deleteById(@Bind("id") long id);
    }

    /**
     * Internal Team -> Person association dao
     */
    @RegisterMapper(PersonAddressMapper.class)
    interface PersonAddressDao extends Transactional<PersonAddressDao> {

        final static String TABLE_NAME = "PERSON_ADDRESS";

        final static String createTableSql_postgres =
                "create table PERSON_ADDRESS (" +
                        "personId integer REFERENCES PERSON, " +
                        "addressId integer REFERENCES ADDRESS, " +
                        "PRIMARY KEY (personId, addressId) )";

        @SqlUpdate("insert into PERSON_ADDRESS (personId, addressId) values (:pa.personId, :pa.addressId)")
        @GetGeneratedKeys
        long insert(@BindBean("pa") PersonAddressAssoc personAddressAssoc);

        @SqlQuery("select * from PERSON_ADDRESS where personId = :personId")
        List<PersonAddressAssoc> findByPersonId(@Bind("personId") Long personId);

        @SqlUpdate("delete from TEAM where personId = :pa.personId and addressId = :pa.addressId")
        void delete(@BindBean("pa") PersonAddressAssoc personAddressAssoc);
    }
}
