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
    private PersonDao personDaoJdbi = dbi.onDemand(PersonDaoJdbi.class);

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
    public void resetTable() {
        dropTable();
        createTable();
    }

    public long insert(final Team team) {
        /**
         * Must update 2 tables here, so will do this in a transaction
         */
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
    public long save(final Team team) {
        if (team.getId() == null) return insert(team);

        return dbi.inTransaction(new TransactionCallback<Long>() {
            @Override
            public Long inTransaction(Handle conn, TransactionStatus status) throws Exception {
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
                return team.getId();
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

    public List<Team> getAll() {
        List<Team> teams = teamDao.getAll();
        for (Team t : teams) {
            getTeamMembers(t, teamPersonDao, personDaoJdbi);
        }
        return teams;
    }

    /**
     * Internal Team -> Person association dao
     */
    @RegisterMapper(TeamPersonMapper.class)
    interface TeamPersonDao extends Transactional<TeamPersonDao> {

        final static String TEAM_PERSON_TABLE_NAME = "TEAM_PERSON";

        final static String createTeamPersonMappingTableSql =
                "create table TEAM_PERSON (" +
                        "team_id integer REFERENCES TEAM, " +
                        "person_id integer REFERENCES PERSON, " +
                        "PRIMARY KEY (team_id, person_id) );";


        @SqlUpdate("insert into TEAM_PERSON (team_id, person_id) values (:tp.teamId, :tp.personId)")
        @GetGeneratedKeys
        long insert(@BindBean("tp") TeamPerson teamPerson);

        @SqlQuery("select * from TEAM_PERSON where team_id = :teamId")
        List<TeamPerson> findByTeamId(@Bind("teamId") Long teamId);

        @SqlUpdate("delete from TEAM where team_id = :tp.teamId and person_id = :tp.personId")
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
                        "team_id serial PRIMARY KEY, " +
                        "name varchar(80) NOT NULL, " +
                        "poc_person_id integer REFERENCES PERSON (person_id), " +
                        "unique(name))";

        @SqlUpdate("insert into TEAM (team_id, name, poc_person_id) values (default, :t.name, :t.pointOfContactId)")
        @GetGeneratedKeys
        long insertWithPoC(@BindBean("t") Team team);

        @SqlUpdate("insert into TEAM (team_id, name, poc_person_id) values (default, :t.name)")
        @GetGeneratedKeys
        long insertWithoutPoC(@BindBean("t") Team team);

        @SqlUpdate("update TEAM set name = :t.name,  poc_person_id = :t.pointOfContactId where team_id = :t.id")
        void updateWithPoC(@BindBean("t") Team team);

        @SqlUpdate("update TEAM set name = :t.name where team_id = :t.id")
        void updateWithoutPoC(@BindBean("t") Team team);

        @SqlQuery("select * from TEAM where team_id = :id")
        Team get(@Bind("id") long id);

        @SqlQuery("select * from TEAM where name like :name")
        List<Team> findByName(@Bind("name") String name);

        @SqlQuery("select * from TEAM")
        List<Team> getAll();

        @SqlUpdate("delete from TEAM where team_id = :id")
        void deleteById(@Bind("id") long id);
    }
}
