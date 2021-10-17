package pl.crystalek.crcapi.config;

import java.io.IOException;

public final class FileHelper extends ConfigHelper {

    public FileHelper(final String fileName) {
        super(fileName);
    }

    public void save() throws IOException {
        configuration.save(file);
    }
}
