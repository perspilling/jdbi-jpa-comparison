package no.kodemaker.ps.jdbiapp.repository;

import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import java.util.List;

/**
 * @author Per Spilling
 */
@RegisterMapper(TeamPersonMapper.class)
interface TeamPersonDao extends Transactional<TeamPersonDao> {

    final static String TEAM_PERSON_TABLE_NAME = "TEAM_PERSON";

    final static String createTeamPersonMappingTableSql = "create table TEAM_PERSON (" +
            "team_id integer REFERENCES TEAM (id), person_id integer REFERENCES PERSON (id), PRIMARY KEY (team_id, person_id) );";


    @SqlUpdate("insert into TEAM_PERSON (team_id, person_id) values (:tp.teamId, :tp.personId)")
    @GetGeneratedKeys
    long insert(@BindBean("tp") TeamPerson teamPerson);

    @SqlQuery("select * from TEAM_PERSON where team_id = :teamId")
    List<TeamPerson> findByTeamId(@Bind("teamId") Long teamId);

    @SqlUpdate("delete from TEAM where team_id = :tp.teamId and person_id = :tp.personId")
    void delete(@BindBean("tp") TeamPerson teamPerson);
}
