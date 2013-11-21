package no.kodemaker.ps.jdbiapp.repository;

import no.kodemaker.ps.jdbiapp.domain.Email;
import no.kodemaker.ps.jdbiapp.domain.Person;
import no.kodemaker.ps.jdbiapp.domain.Team;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Per Spilling
 */
public class TeamDaoTest {
    private static TeamDao teamDao;
    private static PersonDaoJdbi personDao;

    @BeforeClass
    public static void initDb() {
        personDao = new PersonDaoJdbi();
        personDao.dropTable();
        personDao.createTable();
        DbSeeder.initPersonTable(personDao);

        teamDao = new TeamDaoJdbi();
        teamDao.dropTable();
        teamDao.createTable();
    }

    @Test
    public void testInsertAndGet() {
        Team team = createApollo11Team();
        Team savedTeam = teamDao.save(team);
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
        team = teamDao.save(team);
        assertThat(team.getMembers().size(), equalTo(1));

        Person donald = personDao.save(new Person("Donald Knuth", new Email("knuth@nomail.com")));
        team.addMember(donald);
        team = teamDao.save(team);
        assertThat(team.getMembers().size(), equalTo(2));
    }

    @Test
    public void deletingTeamShouldNotDeletePersons() {
        ensureTeamApolloPresent();
    }

    private void ensureTeamApolloPresent() {
    }


}
