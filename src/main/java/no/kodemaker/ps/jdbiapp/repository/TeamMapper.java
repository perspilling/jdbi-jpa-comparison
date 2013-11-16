package no.kodemaker.ps.jdbiapp.repository;

import no.kodemaker.ps.jdbiapp.domain.Team;
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
    public Team map(int index, ResultSet rs, StatementContext ctx) throws SQLException {
        return new Team(rs.getLong("team_id"), rs.getString("name"), rs.getLong("poc_person_id"));
    }
}
