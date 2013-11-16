package no.kodemaker.ps.jdbiapp.repository;

import no.kodemaker.ps.jdbiapp.domain.Team;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import java.util.List;

/**
 * JDBI DAO for the {@link Team} class. <b>Note:</b>This DAO is used internally in the {@link TeamRepository}
 * class.
 *
 * @author Per Spilling
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
    long insert(@BindBean("t") Team team);

    @SqlUpdate("update TEAM set name = :t.name,  poc_person_id = :t.pointOfContactId where team_id = :t.id")
    void update(@BindBean("t") Team team);

    @SqlQuery("select * from TEAM where team_id = :id")
    Team get(@Bind("id") long id);

    @SqlQuery("select * from TEAM where name like :name")
    List<Team> findByName(@Bind("name") String name);

    @SqlQuery("select * from TEAM")
    List<Team> getAll();

    @SqlUpdate("delete from TEAM where team_id = :id")
    void deleteById(@Bind("id") long id);
}
