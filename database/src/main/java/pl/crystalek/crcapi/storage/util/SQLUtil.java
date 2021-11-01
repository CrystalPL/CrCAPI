package pl.crystalek.crcapi.storage.util;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public final class SQLUtil {
    HikariDataSource database;

    public void openConnection(final SQLConsumer<Connection> consumer) {
        try (
                final Connection connection = database.getConnection()
        ) {
            consumer.accept(connection);
        } catch (final SQLException exception) {
            exception.printStackTrace();
        }
    }

    public <R> R openConnection(final SQLFunction<Connection, R> consumer) {
        try (
                final Connection connection = database.getConnection()
        ) {
            return consumer.apply(connection);
        } catch (final SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public Boolean executeUpdate(final Connection connection, final String sql, final Object... params) throws SQLException {
        @Cleanup final PreparedStatement statement = connection.prepareStatement(sql);
        completionStatement(statement, params);

        return statement.executeUpdate() == 1;
    }

    public Boolean executeUpdateAndOpenConnection(final String sql, final Object... params) {
        return openConnection(connection -> {
            return executeUpdate(connection, sql, params);
        });
    }

    public <R> R executeQuery(final Connection connection, final String sql, SQLFunction<ResultSet, R> consumer, final Object... params) throws SQLException {
        @Cleanup final PreparedStatement statement = connection.prepareStatement(sql);
        completionStatement(statement, params);
        @Cleanup final ResultSet resultSet = statement.executeQuery();

        return consumer.apply(resultSet);
    }

    public <R> R executeQueryAndOpenConnection(final String sql, SQLFunction<ResultSet, R> consumer, final Object... params) {
        return openConnection(connection -> {
            return executeQuery(connection, sql, consumer, params);
        });
    }

    public void completionStatement(final PreparedStatement statement, final Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
    }
}
