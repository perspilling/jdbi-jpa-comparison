package no.kodemaker.ps.jdbiapp.domain;

import java.io.Serializable;

/**
 * Abstract superclass for entities.
 *
 * @author Per Spilling
 */
public abstract class Entity implements Serializable {
    protected Integer id;

    protected Entity() {
    }

    protected Entity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity)) return false;

        Entity entity = (Entity) o;

        if (id != null ? !id.equals(entity.id) : entity.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
