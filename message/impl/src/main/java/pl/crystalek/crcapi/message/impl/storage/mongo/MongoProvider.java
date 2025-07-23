package pl.crystalek.crcapi.message.impl.storage.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang.LocaleUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.entity.Player;
import pl.crystalek.crcapi.database.config.DatabaseConfig;
import pl.crystalek.crcapi.database.provider.mongo.BaseMongoProvider;
import pl.crystalek.crcapi.message.impl.storage.Provider;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class MongoProvider extends BaseMongoProvider implements Provider {
    MongoCollection<Document> userLocaleCollection;

    public MongoProvider(final DatabaseConfig databaseConfig, final MongoDatabase mongoDatabase) {
        super(databaseConfig, mongoDatabase);
    }

    @Override
    public void createTable() {
        final String collectionName = String.format("%slocaleMap", databaseConfig.getPrefix());

        this.userLocaleCollection = mongoDatabase.getCollection(collectionName);
    }

    @Override
    public void createPlayer(final UUID playerUUID, final Locale locale) {
        final Document document = new Document("_id", playerUUID.toString())
                .append("language_tag", locale.toString());

        userLocaleCollection.insertOne(document);
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
    public boolean setPlayerLocale(final UUID playerUUID, final Locale locale) {
        final Document document = new Document("_id", playerUUID.toString());

        if (userLocaleCollection.find(document).first() == null) {
            return false;
        }

        final Bson update = Updates.set("language_tag", locale.toString());
        userLocaleCollection.updateOne(document, update);
        return true;
    }

    @Override
    public Map<UUID, Locale> getPlayersLocaleMap() {
        final Map<UUID, Locale> userLocaleMap = new HashMap<>();

        for (final Document document : userLocaleCollection.find()) {
            final String languageTag = document.get("language_tag", String.class);
            final Locale locale = LocaleUtils.toLocale(languageTag);
            final UUID uuid = UUID.fromString(document.get("_id", String.class));

            userLocaleMap.put(uuid, locale);
        }

        return userLocaleMap;
    }

    @Override
    public Map<UUID, Locale> getPlayersLocaleMap(final Collection<? extends Player> players) {
        final ConcurrentHashMap<UUID, Locale> userLocaleMap = new ConcurrentHashMap<>();

        final List<String> uuidList = players.stream()
                .map(Player::getUniqueId)
                .map(UUID::toString)
                .collect(Collectors.toList());

        final Document searchUuids = new Document("$in", uuidList);
        for (final Document document : userLocaleCollection.find(new Document("_id", searchUuids))) {
            final String languageTag = document.get("language_tag", String.class);
            final Locale locale = LocaleUtils.toLocale(languageTag);
            final UUID uuid = UUID.fromString(document.get("_id", String.class));

            userLocaleMap.put(uuid, locale);
        }

        return userLocaleMap;
    }
}
