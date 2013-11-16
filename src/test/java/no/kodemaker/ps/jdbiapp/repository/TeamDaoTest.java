package no.kodemaker.ps.jdbiapp.repository;

import no.kodemaker.ps.jdbiapp.domain.Email;
import no.kodemaker.ps.jdbiapp.domain.Person;
import no.kodemaker.ps.jdbiapp.domain.Team;
import no.kodemaker.ps.jdbiapp.repository.jdbi.JdbiHelper;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 * @author Per Spilling
 */
public class TeamDaoTest {
    private static TeamDao teamDao;
    private static PersonDao personDao;

    @BeforeClass
    public static void initDb() {
        JdbiHelper jdbiHelper = new JdbiHelper();

        personDao = new PersonDaoJdbi();
        jdbiHelper.resetTable(PersonDaoJdbi.TABLE_NAME, PersonDaoJdbi.createPersonTableSql_postgres);
        DbSeeder.initPersonTable(personDao);

        teamDao = jdbiHelper.getDBI().onDemand(TeamDaoJdbi.class);
        teamDao.resetTable();
    }

    @Test
    public void testInsertAndGet() {
        Team team = createApollo11Team();
        Long teamPk = teamDao.save(team);
        Team savedTeam = teamDao.get(teamPk);
        assertTrue(team.getMembers().size() == savedTeam.getMembers().size());
        assertTrue(savedTeam.getName().equals("apollo11"));
        assertTrue(savedTeam.getMembers().size() == 3);
        assertTrue(savedTeam.getId() != null);
    }

    private Team createApollo11Team() {
        Person commander = personDao.findByName("Neil Armstrong").get(0);
        Team team = new Team("apollo11", commander);
        team.addMember(commander);
        team.addMember(personDao.findByName("Edwin Aldrin").get(0));
        team.addMember(personDao.findByName("Michael Collins").get(0));
        return team;
    }

    @Test
    public void testUpdate() {
        Person edsger = new Person("Edsger Dijktstra", new Email("dijkstra@mail.com"));
        edsger = personDao.save(edsger);

        Team team = new Team("dining philosophers", edsger);
        team.addMember(edsger);
        Long teamPk = teamDao.save(team);
        team = teamDao.get(teamPk);
        assertThat(team.getMembers().size(), equalTo(1));

        Person donald = personDao.save(new Person("Donald Knuth", new Email("knuth@nomail.com")));
        team.addMember(donald);
        teamDao.save(team);
        team = teamDao.get(teamPk);
        assertThat(team.getMembers().size(), equalTo(2));
    }

    @Test
    public void deletingTeamShouldNotDeletePersons() {
        ensureTeamApolloPresent();
    }

    private void ensureTeamApolloPresent() {
    }


}
