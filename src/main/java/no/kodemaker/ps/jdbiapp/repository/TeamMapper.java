package no.kodemaker.ps.jdbiapp.repository;

import no.kodemaker.ps.jdbiapp.domain.Person;
import no.kodemaker.ps.jdbiapp.domain.Team;
import no.kodemaker.ps.jdbiapp.repository.jdbi.JdbiHelper;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A JDBI mapper class for {@link no.kodemaker.ps.jdbiapp.domain.Team} instances.
 *
 * @author Per Spilling
 */
public class TeamMapper implements ResultSetMapper<Team> {

    private JdbiHelper jdbiHelper = new JdbiHelper();
    private final DBI dbi = jdbiHelper.getDBI();
    private PersonDao personDao = dbi.onDemand(PersonDaoJdbi.class);

    public Team map(int index, ResultSet rs, StatementContext ctx) throws SQLException {
        long pocId = rs.getLong("poc_person_id");
        if (pocId != 0L) {
            Person poc = personDao.get(pocId);
            return new Team(rs.getLong("team_id"), rs.getString("name"), poc);
        } else {
            return new Team(rs.getLong("team_id"), rs.getString("name"));
        }
    }
}
