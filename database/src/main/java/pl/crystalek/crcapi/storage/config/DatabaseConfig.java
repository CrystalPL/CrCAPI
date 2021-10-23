package pl.crystalek.crcapi.storage.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.crystalek.crcapi.storage.StorageType;

import java.io.File;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
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
