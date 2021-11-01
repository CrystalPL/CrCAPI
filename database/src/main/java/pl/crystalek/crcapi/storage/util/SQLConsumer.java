package pl.crystalek.crcapi.storage.util;

import java.sql.SQLException;

public interface SQLConsumer<T> {
    void accept(T connection) throws SQLException;
}
