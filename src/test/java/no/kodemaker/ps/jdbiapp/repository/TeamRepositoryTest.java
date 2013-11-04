package no.kodemaker.ps.jdbiapp.repository;

import no.kodemaker.ps.jdbiapp.domain.Person;
import no.kodemaker.ps.jdbiapp.domain.Team;
import no.kodemaker.ps.jdbiapp.repository.jdbi.JdbiHelper;
import org.junit.BeforeClass;
import org.junit.Test;
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
        teamRepository = jdbiHelper.getDBI().onDemand(TeamRepository.class);
        teamRepository.resetTable();
        personRepository = jdbiHelper.getDBI().onDemand(PersonRepository2.class);
        jdbiHelper.resetTable(PersonRepository2.TABLE_NAME, PersonRepository2.createTableSql);
        new DbSeeder().initPersonTable(personRepository);
    }

    @Test
    public void testInsert() {
        Team team = new Team("apollo11");
        Person p = personRepository.findByName("Neil Armstrong").get(0);
        team.addMember(p);
        p = personRepository.findByName("Edwin Aldrin").get(0);
        team.addMember(p);
        p = personRepository.findByName("Tom Collins").get(0);
        team.addMember(p);
        teamRepository.insert(team);
        assertTrue(true);
    }
}
