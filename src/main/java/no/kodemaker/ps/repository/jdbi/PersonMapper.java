package no.kodemaker.ps.repository.jdbi;

import no.kodemaker.ps.domain.Person;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A JDBI mapper class for {@link Person} instances.
 *
 * @author Per Spilling
 */
public class PersonMapper implements ResultSetMapper<Person> {
    public static PersonMapper INSTANCE = new PersonMapper();

    public Person map(int index, ResultSet rs, StatementContext ctx) throws SQLException {
        return new Person(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("phone"));
    }
}
