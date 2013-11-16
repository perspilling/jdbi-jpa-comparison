package no.kodemaker.ps.jdbiapp.repository;

import no.kodemaker.ps.jdbiapp.domain.Team;
import no.kodemaker.ps.jdbiapp.repository.jdbi.TableCreator;

import java.util.List;

/**
 * @author Per Spilling
 */
public interface TeamDao extends TableCreator {
    long save(final Team team);
    Team get(Long pk);
    List<Team> getAll();
}
