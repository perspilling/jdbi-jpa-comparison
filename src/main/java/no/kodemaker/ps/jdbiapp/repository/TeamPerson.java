package no.kodemaker.ps.jdbiapp.repository;

/**
 * @author Per Spilling
 */
public class TeamPerson {
    public Long teamId;
    public Long personId;

    public TeamPerson(Long teamId, Long personId) {
        this.teamId = teamId;
        this.personId = personId;
    }
}
