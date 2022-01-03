package pl.crystalek.crcapi.database.provider.mongo;

import com.mongodb.client.MongoDatabase;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.crystalek.crcapi.database.config.DatabaseConfig;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
public abstract class BaseMongoProvider {
    DatabaseConfig databaseConfig;
    MongoDatabase mongoDatabase;
}
