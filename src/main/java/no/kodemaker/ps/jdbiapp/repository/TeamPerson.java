package no.kodemaker.ps.jdbiapp.repository;

/**
 * @author Per Spilling
 */
public class TeamPerson {
    private Integer teamId;
    private Integer personId;

    public TeamPerson(Integer teamId, Integer personId) {
        this.teamId = teamId;
        this.personId = personId;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }
}
