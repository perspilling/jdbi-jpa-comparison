package no.kodemaker.ps.jdbiapp.domain;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author Per Spilling
 */
public class Team extends Entity {
    private String name;
    private List<Person> members = Lists.newArrayList();

    public Team(String name) {
        this.name = name;
    }

    public Team(Long id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
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
