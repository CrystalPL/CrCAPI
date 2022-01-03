package pl.crystalek.crcapi.database.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.crystalek.crcapi.database.storage.type.StorageType;

import java.io.File;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class DatabaseConfig {
    StorageType storageType;
    String hostname;
    String port;
    String database;
    String username;
    String password;
    boolean useSSL;
    int poolSize;
    int connectionTimeout;
    String prefix;
    File sqliteDatabaseLocation;
}
