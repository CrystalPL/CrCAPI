package pl.crystalek.crcapi.messagei18n.storage.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang.LocaleUtils;
import org.bson.Document;
import pl.crystalek.crcapi.messagei18n.storage.Provider;
import pl.crystalek.crcapi.storage.config.DatabaseConfig;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public final class MongoProvider extends Provider {
    final ReplaceOptions replaceOptions = new ReplaceOptions().upsert(true);
    final MongoDatabase mongoDatabase;
    final DatabaseConfig databaseConfig;
    MongoCollection<Document> userLocaleCollection;

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
