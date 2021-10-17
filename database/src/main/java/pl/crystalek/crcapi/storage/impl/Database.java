package pl.crystalek.crcapi.storage.impl;

public interface Database {
    boolean connect();

    boolean init();

    void close();
}
