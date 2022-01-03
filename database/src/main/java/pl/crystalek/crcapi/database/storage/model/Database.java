package pl.crystalek.crcapi.database.storage.model;

public interface Database {
    boolean connect();

    boolean init();

    void close();
}
