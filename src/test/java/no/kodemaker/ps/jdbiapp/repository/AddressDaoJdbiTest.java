package no.kodemaker.ps.jdbiapp.repository;

import no.kodemaker.ps.jdbiapp.domain.Address;
import no.kodemaker.ps.jdbiapp.repository.jdbi.JdbiHelper;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 * @author Per Spilling
 */
public class AddressDaoJdbiTest {
    private static AddressDaoJdbi dao = new AddressDaoJdbi();

    @BeforeClass
    public static void init() {
        new JdbiHelper().resetTable(AddressDaoJdbi.TABLE_NAME, AddressDaoJdbi.createAddressTableSql_postgres);
        dao.save(new Address("Storgata 22", "0123", "Oslo"));
        dao.save(new Address("Karl Johans gate 10", "0100", "Oslo"));
    }

    @Test
    public void testSave() {
        Address address = dao.save(new Address("Drammensveien 1", "0123", "Oslo"));
        assertTrue(address.getId() != null);
    }

    @Test
    public void testMisc() {
        assertTrue(dao.exists(1L));
        int size = dao.getAll().size();
        assertTrue(size >= 2);
        Address a = dao.save(new Address("Alexander Kiellands plass 1", "0123", "Oslo"));
        assertThat(dao.getAll().size(), equalTo(size + 1));
        dao.delete(a.getId());
        assertThat(dao.getAll().size(), equalTo(size));
    }

}
