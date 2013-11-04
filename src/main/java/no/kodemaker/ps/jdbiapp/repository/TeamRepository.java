package no.kodemaker.ps.jdbiapp.repository;

import no.kodemaker.ps.jdbiapp.domain.Person;
import no.kodemaker.ps.jdbiapp.domain.Team;
import no.kodemaker.ps.jdbiapp.repository.jdbi.JdbiHelper;
import no.kodemaker.ps.jdbiapp.repository.jdbi.TableCreator;
import org.skife.jdbi.v2.*;

/**
 * Repository for {@link no.kodemaker.ps.jdbiapp.domain.Team} objects. Internally it uses the {@link TeamDao} SQL
 * Object interface. The {@link no.kodemaker.ps.jdbiapp.domain.Person} collection association is mapped via
 * a mapping table (TEAM_PERSON).
 *
 * @author Per Spilling
 */
public class TeamRepository implements TableCreator {
    private JdbiHelper jdbiHelper = new JdbiHelper();

    TeamRepository() {
    }

    @Override
    public void createTable() {
        jdbiHelper.createTableIfNotPresent(TeamDao.TEAM_TABLE_NAME, TeamDao.createTeamTableSql);
        jdbiHelper.createTableIfNotPresent(TeamPersonDao.TEAM_PERSON_TABLE_NAME, TeamPersonDao.createTeamPersonMappingTableSql);
    }

    @Override
    public void dropTable() {
        jdbiHelper.dropTableIfNotPresent(TeamDao.TEAM_TABLE_NAME);
        jdbiHelper.dropTableIfNotPresent(TeamPersonDao.TEAM_PERSON_TABLE_NAME);
    }

    @Override
    public void resetTable() {
        dropTable();
        createTable();
    }

    void insert(final Team team) {
        final DBI dbi = jdbiHelper.getDBI();

        /**
         * Must update 2 tables here, so will do this in a TransactionCallback.
         */
        dbi.inTransaction(new VoidTransactionCallback() {
            @Override
            protected void execute(Handle handle, TransactionStatus status) throws Exception {
                TeamDao teamDao = dbi.onDemand(TeamDao.class);
                TeamPersonDao teamPersonDao = dbi.onDemand(TeamPersonDao.class);
                teamDao.insert(team);
                for (Person p : team.getMembers()) {
                    teamPersonDao.insert(new TeamPerson(team.getId(), p.getId()));
                }
            }
        });
    }

}
