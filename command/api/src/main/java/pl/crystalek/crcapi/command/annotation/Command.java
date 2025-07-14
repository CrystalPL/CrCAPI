package pl.crystalek.crcapi.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to define command metadata
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    /**
     * The name of the command
     */
    String name();

    /**
     * Command aliases
     */
    String[] aliases() default {};

    /**
     * The permission required to use the command
     */
    String permission() default "";

    /**
     * Whether the command can be used by the console
     */
    boolean useConsole() default true;

    /**
     * The minimum required number of arguments
     */
    int minArgs() default 0;

    /**
     * The maximum allowed number of arguments
     */
    int maxArgs() default Integer.MAX_VALUE;

    /**
     * Path to the command usage message in the configuration file
     */
    String commandUsagePath() default "";
}
