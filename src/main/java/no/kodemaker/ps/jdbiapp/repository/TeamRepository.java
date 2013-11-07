package no.kodemaker.ps.jdbiapp.repository;

import no.kodemaker.ps.jdbiapp.domain.Person;
import no.kodemaker.ps.jdbiapp.domain.Team;
import no.kodemaker.ps.jdbiapp.repository.jdbi.JdbiHelper;
import no.kodemaker.ps.jdbiapp.repository.jdbi.TableCreator;
import org.skife.jdbi.v2.*;

import java.util.List;

/**
 * Repository for {@link no.kodemaker.ps.jdbiapp.domain.Team} objects. Internally it uses the {@link TeamDao} SQL
 * Object interface. The {@link no.kodemaker.ps.jdbiapp.domain.Person} collection association is mapped via
 * a mapping table (TEAM_PERSON).
 *
 * @author Per Spilling
 */
public class TeamRepository implements TableCreator {
    private JdbiHelper jdbiHelper = new JdbiHelper();
    private final DBI dbi = jdbiHelper.getDBI();
    /**
     * Using dbi.onDemand(repo.class) means that the repo instance and it's db connection will be managed
     * by DBI.
     */
    private TeamDao teamDao = dbi.onDemand(TeamDao.class);
    private TeamPersonDao teamPersonDao = dbi.onDemand(TeamPersonDao.class); // team->person mapping table
    private PersonDao personDao = dbi.onDemand(PersonDao.class);

    public TeamRepository() {
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

    public long insert(final Team team) {
        /**
         * Must update 2 tables here, so will do this in a TransactionCallback.
         */
        return dbi.inTransaction(new TransactionCallback<Long>() {
            @Override
            public Long inTransaction(Handle conn, TransactionStatus status) throws Exception {
                long teamId = teamDao.insert(team);
                for (Person p : team.getMembers()) {
                    // update the team->person mapping table
                    teamPersonDao.insert(new TeamPerson(teamId, p.getId()));
                }
                return teamId;
            }
        });
    }

    public long insertOrUpdate(final Team team) {
        if (team.getId() == null) return insert(team);

        return dbi.inTransaction(new TransactionCallback<Long>() {
            @Override
            public Long inTransaction(Handle conn, TransactionStatus status) throws Exception {
                teamDao.update(team);
                for (Person p : team.getMembers()) {
                    // update the team->person mapping table
                    TeamPerson tp = new TeamPerson(team.getId(), p.getId());
                    if (!teamPersonDao.findByTeamId(team.getId()).contains(tp)) {
                        teamPersonDao.insert(tp);
                    }
                }
                return team.getId();
            }
        });
    }

    public Team get(Long pk) {
        Team team = teamDao.get(pk);
        getTeamMembers(team, teamPersonDao, personDao);
        return team;
    }

    private void getTeamMembers(Team team, TeamPersonDao teamPersonDao, PersonDao personDao) {
        List<TeamPerson> teamPersonList = teamPersonDao.findByTeamId(team.getId());
        for (TeamPerson tp : teamPersonList) {
            team.addMember(personDao.get(tp.getPersonId()));
        }
    }

    public List<Team> getAll() {
        List<Team> teams = teamDao.getAll();
        for (Team t : teams) {
            getTeamMembers(t, teamPersonDao, personDao);
        }
        return teams;
    }
}
