package pl.crystalek.crcapi.database.provider.sql;


import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.Cleanup;
import lombok.experimental.FieldDefaults;
import pl.crystalek.crcapi.database.config.DatabaseConfig;
import pl.crystalek.crcapi.database.provider.BaseProvider;
import pl.crystalek.crcapi.database.provider.sql.model.SQLConsumer;
import pl.crystalek.crcapi.database.provider.sql.model.SQLFunction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
public abstract class BaseSQLProvider extends BaseProvider {
    HikariDataSource database;

    public BaseSQLProvider(final DatabaseConfig databaseConfig, final HikariDataSource database) {
        super(databaseConfig);

        this.database = database;
    }

    protected void openConnection(final SQLConsumer<Connection> consumer) {
        try (
                final Connection connection = database.getConnection()
        ) {
            consumer.accept(connection);
        } catch (final SQLException exception) {
            exception.printStackTrace();
        }
    }

    protected <R> R openConnection(final SQLFunction<Connection, R> consumer) {
        try (
                final Connection connection = database.getConnection()
        ) {
            return consumer.apply(connection);
        } catch (final SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    protected Boolean executeUpdate(final Connection connection, final String sql, final Object... params) throws SQLException {
        @Cleanup final PreparedStatement statement = connection.prepareStatement(sql);
        completionStatement(statement, params);

        return statement.executeUpdate() == 1;
    }

    protected Boolean executeUpdateAndOpenConnection(final String sql, final Object... params) {
        return openConnection(connection -> {
            return executeUpdate(connection, sql, params);
        });
    }

    protected <R> R executeQuery(final Connection connection, final String sql, SQLFunction<ResultSet, R> consumer, final Object... params) throws SQLException {
        @Cleanup final PreparedStatement statement = connection.prepareStatement(sql);
        completionStatement(statement, params);
        @Cleanup final ResultSet resultSet = statement.executeQuery();

        return consumer.apply(resultSet);
    }

    protected <R> R executeQueryAndOpenConnection(final String sql, SQLFunction<ResultSet, R> consumer, final Object... params) {
        return openConnection(connection -> {
            return executeQuery(connection, sql, consumer, params);
        });
    }

    protected void completionStatement(final PreparedStatement statement, final Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
    }
}
