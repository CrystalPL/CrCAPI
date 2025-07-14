package pl.crystalek.crcapi.command.util;

import com.google.common.reflect.ClassPath;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@UtilityClass
public class ClassUtils {

    public Set<ClassPath.ClassInfo> getAllClasses(final ClassLoader classLoader) {
        try {
            return ClassPath.from(classLoader).getAllClasses();
        } catch (final IOException exception) {
            return new HashSet<>();
        }
    }

    public Optional<Class<?>> getClassByClassInfo(final ClassPath.ClassInfo classInfo, final ClassLoader classLoader) {
        final String className = classInfo.getName();
        try {
            final Class<?> clazz = Class.forName(className, false, classLoader);
            return Optional.of(clazz);
        } catch (final ClassNotFoundException exception) {
            return Optional.empty();
        }
    }
}
