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
public class TeamRepositoryTest {
    private static TeamRepository teamRepository;
    private static PersonDao personRepository;

    @BeforeClass
    public static void initDb() {
        JdbiHelper jdbiHelper = new JdbiHelper();

        personRepository = jdbiHelper.getDBI().onDemand(PersonDao.class);
        jdbiHelper.resetTable(PersonDao.TABLE_NAME, PersonDao.createTableSql);
        DbSeeder.initPersonTable(personRepository);

        teamRepository = jdbiHelper.getDBI().onDemand(TeamRepository.class);
        teamRepository.resetTable();
    }

    @Test
    public void testInsertAndGet() {
        Team team = createApollo11Team();
        Long teamPk = teamRepository.insert(team);
        Team savedTeam = teamRepository.get(teamPk);
        assertTrue(team.getMembers().size() == savedTeam.getMembers().size());
        assertTrue(savedTeam.getName().equals("apollo11"));
        assertTrue(savedTeam.getMembers().size() == 3);
        assertTrue(savedTeam.getId() != null);
    }

    private Team createApollo11Team() {
        Team team = new Team("apollo11");
        team.addMember(personRepository.findByName("Neil Armstrong").get(0));
        team.addMember(personRepository.findByName("Edwin Aldrin").get(0));
        team.addMember(personRepository.findByName("Michael Collins").get(0));
        return team;
    }

    @Test
    public void testUpdate() {
        Team team = new Team("dining philosophers");
        long personId = personRepository.insert(new Person("Edsger Dijktstra", new Email("dijkstra@mail.com")));
        team.addMember(personRepository.get(personId));
        Long teamPk = teamRepository.insert(team);
        team = teamRepository.get(teamPk);
        assertThat(team.getMembers().size(), equalTo(1));

        personId = personRepository.insert(new Person("Donald Knuth", new Email("knuth@nomail.com")));
        team.addMember(personRepository.get(personId));
        teamRepository.insertOrUpdate(team);
        team = teamRepository.get(teamPk);
        assertThat(team.getMembers().size(), equalTo(2));
    }

    @Test
    public void deletingTeamShouldNotDeletePersons() {
        ensureTeamApolloPresent();
    }

    private void ensureTeamApolloPresent() {
    }


}
