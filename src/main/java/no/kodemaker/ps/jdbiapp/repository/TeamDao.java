package no.kodemaker.ps.jdbiapp.repository;

import no.kodemaker.ps.jdbiapp.DbProperties;
import no.kodemaker.ps.jdbiapp.domain.Team;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
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

    String createTeamTableSql =
            "create table TEAM (id " + DbProperties.SQL_AUTO_INCREMENT.val() + " PRIMARY KEY, name varchar(80))";

    /**
     * @param team
     * @return the auto generated pk
     */
    @SqlUpdate("insert into TEAM (id, name) values (default, :t.name)")
    Integer insert(@BindBean("t") Team team);

    @SqlUpdate("update TEAM set name = :t.name where id = :t.id")
    void update(@BindBean("t") Team team);

    @SqlQuery("select * from TEAM where id = :id")
    Team get(@Bind("id") Integer id);

    @SqlQuery("select * from TEAM where name like :name")
    List<Team> findByName(@Bind("name") String name);

    @SqlQuery("select * from TEAM")
    List<Team> getAll();

    @SqlUpdate("delete from TEAM where id = :id")
    void deleteById(@Bind("id") Integer id);
}
