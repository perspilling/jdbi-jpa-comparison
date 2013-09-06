package no.kodemaker.ps.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Per Spilling
 */
public class JdbiPersonTest {

    @Test
    public void canConstructAPersonWithAName() {
        JdbiPerson person = new JdbiPerson("Larry Wall", "larry@mail.com");
        assertEquals("Larry Wall", person.getName());
    }
}
