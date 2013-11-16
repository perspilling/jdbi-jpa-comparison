package no.kodemaker.ps.jdbiapp.domain;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Entity class for a team. Schema is defined in {@link no.kodemaker.ps.jdbiapp.repository.TeamDao}.
 *
 * @author Per Spilling
 */
public class Team extends Entity {
    // required fields
    private String name;

    // optional fields
    private Long pointOfContactId;
    private List<Person> members = Lists.newArrayList();

    // transient fields
    private Person pointOfContact;

    public Team(Long id, String name, Long pointOfContactId) {
        super(id);
        this.name = name;
        this.pointOfContactId = pointOfContactId;
    }

    public Team(String name, Person pointOfContact) {
        this.name = name;
        this.pointOfContactId = pointOfContact.id;
        this.pointOfContact = pointOfContact;
    }

    public Team(Long id, String name, Person pointOfContact) {
        super(id);
        this.name = name;
        this.pointOfContactId = pointOfContact.id;
        this.pointOfContact = pointOfContact;
    }

    public String getName() {
        return name;
    }

    public Long getPointOfContactId() {
        return pointOfContactId;
    }

    public Person getPointOfContact() {
        return pointOfContact;
    }

    public void setPointOfContact(Person pointOfContact) {
        this.pointOfContactId = pointOfContact.id;
        this.pointOfContact = pointOfContact;
    }

    public List<Person> getMembers() {
        return members;
    }

    public void setMembers(List<Person> members) {
        if (members == null) throw new IllegalArgumentException("members may not be null");
        this.members = members;
    }

    public void addMember(Person p) {
        members.add(p);
    }
}
