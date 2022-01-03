package pl.crystalek.crcapi.message.impl.storage.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang.LocaleUtils;
import org.bson.Document;
import pl.crystalek.crcapi.database.config.DatabaseConfig;
import pl.crystalek.crcapi.database.provider.mongo.BaseMongoProvider;
import pl.crystalek.crcapi.message.impl.storage.Provider;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class MongoProvider extends BaseMongoProvider implements Provider {
    final ReplaceOptions replaceOptions = new ReplaceOptions().upsert(true);
    MongoCollection<Document> userLocaleCollection;

    public MongoProvider(final DatabaseConfig databaseConfig, final MongoDatabase mongoDatabase) {
        super(databaseConfig, mongoDatabase);
    }

    @Override
    public void createTable() {
        this.userLocaleCollection = mongoDatabase.getCollection(String.format("%slocaleMap", databaseConfig.getPrefix()));
    }

    @Override
    public Optional<Locale> getPlayerLocale(final UUID playerUUID) {
        final Document document = userLocaleCollection.find(new Document("_id", playerUUID.toString())).first();
        if (document == null) {
            return Optional.empty();
        }

        final String languageTag = document.get("language_tag", String.class);
        final Locale locale = LocaleUtils.toLocale(languageTag);

        return Optional.of(locale);
    }

    @Override
    public void setPlayerLocale(final UUID playerUUID, final Locale locale) {
        final Document document = new Document("_id", playerUUID.toString())
                .append("language_tag", locale.toString());

        userLocaleCollection.replaceOne(new Document("_id", playerUUID.toString()), document, replaceOptions);
    }
}
