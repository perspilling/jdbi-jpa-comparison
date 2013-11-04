package no.kodemaker.ps.jdbiapp.repository;

import no.kodemaker.ps.jdbiapp.domain.Email;
import no.kodemaker.ps.jdbiapp.domain.Person;

/**
 * @author Per Spilling
 */
public class DbSeeder {
    public void initPersonTable(PersonRepository2 repository) {
        repository.insert(new Person("Per Spilling", new Email("per@kodemaker.no")));
        repository.insert(new Person("Per Spelling", new Email("pspelling@nomail.com")));
        repository.insert(new Person("Neil Armstrong", new Email("armstrong@nomail.com")));
        repository.insert(new Person("Edwin Aldrin", new Email("aldrin@nomail.com")));
        repository.insert(new Person("Michael Collins", new Email("collins@nomail.com")));
    }
}
