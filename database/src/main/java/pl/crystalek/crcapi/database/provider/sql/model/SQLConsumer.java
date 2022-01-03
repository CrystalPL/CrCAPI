package pl.crystalek.crcapi.database.provider.sql.model;

import java.sql.SQLException;

public interface SQLConsumer<T> {
    void accept(T connection) throws SQLException;
}
