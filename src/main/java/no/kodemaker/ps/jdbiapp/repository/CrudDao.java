package no.kodemaker.ps.jdbiapp.repository;

import java.util.List;

/**
 * @author Per Spilling
 */
public interface CrudDao<T, PK> {
        List<T> getAll();

        T get(PK id);

        boolean exists(PK id);

        T save(T instance);

        void delete(PK id);
}
