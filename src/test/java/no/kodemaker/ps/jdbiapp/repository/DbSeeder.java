package no.kodemaker.ps.jdbiapp.repository;

import no.kodemaker.ps.jdbiapp.domain.Email;
import no.kodemaker.ps.jdbiapp.domain.Person;

/**
 * @author Per Spilling
 */
public class DbSeeder {
    public static void initPersonTable(PersonDao dao) {
        dao.save(new Person("Per Spilling", new Email("per@kodemaker.no")));
        dao.save(new Person("Per Spelling", new Email("pspelling@nomail.com")));
        dao.save(new Person("Neil Armstrong", new Email("armstrong@nomail.com")));
        dao.save(new Person("Edwin Aldrin", new Email("aldrin@nomail.com")));
        dao.save(new Person("Michael Collins", new Email("collins@nomail.com")));
    }
}
