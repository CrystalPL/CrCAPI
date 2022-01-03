package pl.crystalek.crcapi.database.provider.mongo;

import com.mongodb.client.MongoDatabase;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import pl.crystalek.crcapi.database.config.DatabaseConfig;
import pl.crystalek.crcapi.database.provider.BaseProvider;

@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
public abstract class BaseMongoProvider extends BaseProvider {
    MongoDatabase mongoDatabase;

    public BaseMongoProvider(final DatabaseConfig databaseConfig, final MongoDatabase mongoDatabase) {
        super(databaseConfig);

        this.mongoDatabase = mongoDatabase;
    }
}
