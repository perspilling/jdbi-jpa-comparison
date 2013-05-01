package no.kodemaker.ps.repository.datasource;

import javax.sql.DataSource;

/**
 * @author Per Spilling
 */
public interface DataSourceProvider {
    DataSource getDataSource();
}
