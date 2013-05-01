package no.kodemaker.ps.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Per Spilling
 */
public class PersonTest {

    @Test
    public void canConstructAPersonWithAName() {
        Person person = new Person("Larry Wall", "larry@mail.com");
        assertEquals("Larry Wall", person.getName());
    }
}
