package pl.crystalek.crcapi.storage.impl;

import java.sql.SQLException;

@FunctionalInterface
interface SQLFunction<T, R> {
    R apply(T t) throws SQLException;
}