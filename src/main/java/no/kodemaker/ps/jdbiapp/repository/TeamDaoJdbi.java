package no.kodemaker.ps.jdbiapp.repository;

import no.kodemaker.ps.jdbiapp.domain.Person;
import no.kodemaker.ps.jdbiapp.domain.Team;
import no.kodemaker.ps.jdbiapp.repository.jdbi.JdbiHelper;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.TransactionCallback;
import org.skife.jdbi.v2.TransactionStatus;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import java.util.List;

/**
 * Repository for {@link no.kodemaker.ps.jdbiapp.domain.Team} objects. Internally it uses the {@link no.kodemaker.ps.jdbiapp.repository.TeamDaoJdbi.TeamDao} SQL
 * Object interface. The {@link no.kodemaker.ps.jdbiapp.domain.Person} collection association is mapped via
 * a mapping table (TEAM_PERSON).
 *
 * @author Per Spilling
 */
public class TeamDaoJdbi implements TeamDao {
    private JdbiHelper jdbiHelper = new JdbiHelper();
    private final DBI dbi = jdbiHelper.getDBI();

    // Let DBI manage the db connections
    private TeamDao teamDao = dbi.onDemand(TeamDao.class);
    private TeamPersonDao teamPersonDao = dbi.onDemand(TeamPersonDao.class); // team->person mapping table
    private PersonDao personDaoJdbi = new PersonDaoJdbi();

    public TeamDaoJdbi() {
    }

    @Override
    public void createTable() {
        jdbiHelper.createTableIfNotPresent(TeamDao.TEAM_TABLE_NAME, TeamDao.createTeamTableSql_postgres);
        jdbiHelper.createTableIfNotPresent(TeamPersonDao.TEAM_PERSON_TABLE_NAME, TeamPersonDao.createTeamPersonMappingTableSql);
    }

    @Override
    public void dropTable() {
        jdbiHelper.dropTableIfNotPresent(TeamDao.TEAM_TABLE_NAME);
        jdbiHelper.dropTableIfNotPresent(TeamPersonDao.TEAM_PERSON_TABLE_NAME);
    }

    @Override
    public Team save(final Team team) {
        if (team.getId() == null) {
            long id = insert(team);
            return get(id);
        }

        /**
         * Must update 2 tables here, so will do this in a transaction
         */
        return dbi.inTransaction(new TransactionCallback<Team>() {
            @Override
            public Team inTransaction(Handle conn, TransactionStatus status) throws Exception {
                if (team.getPointOfContact() != null) {
                    teamDao.updateWithPoC(team);
                } else {
                    teamDao.updateWithoutPoC(team);
                }
                for (Person p : team.getMembers()) {
                    // update the team->person mapping table
                    TeamPerson tp = new TeamPerson(team.getId(), p.getId());
                    if (!teamPersonDao.findByTeamId(team.getId()).contains(tp)) {
                        teamPersonDao.insert(tp);
                    }
                }
                return get(team.getId());
            }
        });
    }

    private long insert(final Team team) {
        return dbi.inTransaction(new TransactionCallback<Long>() {
            @Override
            public Long inTransaction(Handle conn, TransactionStatus status) throws Exception {
                long teamId;
                if (team.getPointOfContact() != null) {
                    teamId = teamDao.insertWithPoC(team);
                } else {
                    teamId = teamDao.insertWithoutPoC(team);
                }
                for (Person p : team.getMembers()) {
                    // update the team->person mapping table
                    teamPersonDao.insert(new TeamPerson(teamId, p.getId()));
                }
                return teamId;
            }
        });
    }

    @Override
    public Team get(Long pk) {
        Team team = teamDao.get(pk);
        getTeamMembers(team, teamPersonDao, personDaoJdbi);
        return team;
    }

    private void getTeamMembers(Team team, TeamPersonDao teamPersonDao, PersonDao personDao) {
        List<TeamPerson> teamPersonList = teamPersonDao.findByTeamId(team.getId());
        for (TeamPerson tp : teamPersonList) {
            team.addMember(personDao.get(tp.getPersonId()));
        }
    }

    @Override
    public List<Team> getAll() {
        List<Team> teams = teamDao.getAll();
        for (Team t : teams) {
            getTeamMembers(t, teamPersonDao, personDaoJdbi);
        }
        return teams;
    }

    @Override
    public boolean exists(Long id) {
        return get(id) != null;
    }

    @Override
    public void delete(Long id) {
        // delete associated entries from TEAM and TEAM_PERSON tables, but not from the PERSON table
        Team team = get(id);
        if (team == null) return;

        for (Person p : team.getMembers()) {
            teamPersonDao.delete(new TeamPerson(team.getId(), p.getId()));
        }
        teamDao.delete(team.getId());
    }

    /**
     * Internal Team -> Person association dao
     */
    @RegisterMapper(TeamPersonMapper.class)
    interface TeamPersonDao extends Transactional<TeamPersonDao> {

        final static String TEAM_PERSON_TABLE_NAME = "TEAM_PERSON";

        final static String createTeamPersonMappingTableSql =
                "create table TEAM_PERSON (" +
                        "teamId integer REFERENCES TEAM, " +
                        "personId integer REFERENCES PERSON, " +
                        "PRIMARY KEY (teamId, personId) );";


        @SqlUpdate("insert into TEAM_PERSON (teamId, personId) values (:tp.teamId, :tp.personId)")
        @GetGeneratedKeys
        long insert(@BindBean("tp") TeamPerson teamPerson);

        @SqlQuery("select * from TEAM_PERSON where teamId = :teamId")
        List<TeamPerson> findByTeamId(@Bind("teamId") Long teamId);

        @SqlUpdate("delete from TEAM where teamId = :tp.teamId and personId = :tp.personId")
        void delete(@BindBean("tp") TeamPerson teamPerson);
    }

    /**
     * Generated JDBI DAO for the {@link Team} class.
     */
    @RegisterMapper(TeamMapper.class)
    interface TeamDao extends Transactional<TeamDao> {
        String TEAM_TABLE_NAME = "TEAM";

        String createTeamTableSql_postgres =
                "create table TEAM (" +
                        "teamId serial PRIMARY KEY, " +
                        "name varchar(80) NOT NULL, " +
                        "pocPersonId integer REFERENCES PERSON (personId), " +
                        "unique(name))";

        @SqlUpdate("insert into TEAM (teamId, name, pocPersonId) values (default, :t.name, :t.pointOfContactId)")
        @GetGeneratedKeys
        long insertWithPoC(@BindBean("t") Team team);

        @SqlUpdate("insert into TEAM (teamId, name, pocPersonId) values (default, :t.name)")
        @GetGeneratedKeys
        long insertWithoutPoC(@BindBean("t") Team team);

        @SqlUpdate("update TEAM set name = :t.name,  pocPersonId = :t.pointOfContactId where teamId = :t.id")
        void updateWithPoC(@BindBean("t") Team team);

        @SqlUpdate("update TEAM set name = :t.name where teamId = :t.id")
        void updateWithoutPoC(@BindBean("t") Team team);

        @SqlQuery("select * from TEAM where teamId = :id")
        Team get(@Bind("id") long id);

        @SqlQuery("select * from TEAM where name like :name")
        List<Team> findByName(@Bind("name") String name);

        @SqlQuery("select * from TEAM")
        List<Team> getAll();

        @SqlUpdate("delete from TEAM where teamId = :id")
        void delete(@Bind("id") long id);
    }
}
