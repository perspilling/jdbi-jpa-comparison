package no.kodemaker.ps.jdbiapp.repository;

import no.kodemaker.ps.jdbiapp.domain.Team;
import no.kodemaker.ps.jdbiapp.repository.jdbi.JdbiHelper;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author Per Spilling
 */
public class TeamRepositoryTest {
    private static TeamRepository teamRepository;
    private static PersonRepository2 personRepository;

    @BeforeClass
    public static void initDb() {
        JdbiHelper jdbiHelper = new JdbiHelper();

        personRepository = jdbiHelper.getDBI().onDemand(PersonRepository2.class);
        jdbiHelper.resetTable(PersonRepository2.TABLE_NAME, PersonRepository2.createTableSql);
        new DbSeeder().initPersonTable(personRepository);

        teamRepository = jdbiHelper.getDBI().onDemand(TeamRepository.class);
        teamRepository.resetTable();
    }

    @Test
    public void testIt() {
        Team team = createApollo11Team();
        Integer teamPk = teamRepository.insert(team);
        Team savedTeam = teamRepository.get(teamPk);
        assertTrue(team.getMembers().size() == savedTeam.getMembers().size());

        List<Team> teamList = teamRepository.getAll();
        assertTrue(teamList.size() == 1);
        Team t = teamList.get(0);
        assertTrue(t.getName().equals("apollo11"));
        assertTrue(t.getMembers().size() == 3);
        assertTrue(t.getId() != null);
    }

    private Team createApollo11Team() {
        Team team = new Team("apollo11");
        team.addMember(personRepository.findByName("Neil Armstrong").get(0));
        team.addMember(personRepository.findByName("Edwin Aldrin").get(0));
        team.addMember(personRepository.findByName("Michael Collins").get(0));
        return team;
    }
}
