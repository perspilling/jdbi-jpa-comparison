package no.kodemaker.ps.jdbiapp.repository;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
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
            "team_id integer REFERENCES TEAM, person_id integer REFERENCES PERSON, PRIMARY KEY (team_id, person_id) );";


    @SqlUpdate("insert into TEAM_PERSON (team_id, person_id) values (:tp.teamId, :tp.personId)")
    void insert(@BindBean("tp") TeamPerson teamPerson);

    @SqlQuery("select * from TEAM_PERSON where team_id = :teamId")
    List<TeamPerson> findByTeamId(@Bind("teamId") Long teamId);

    @SqlUpdate("delete from TEAM where team_id = :tp.teamId and person_id = :tp.personId")
    void delete(@BindBean("tp") TeamPerson teamPerson);
}