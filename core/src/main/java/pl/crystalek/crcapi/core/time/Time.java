package pl.crystalek.crcapi.core.time;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
enum Time {

    YEAR("rok", "lata", "lat", "y", 31_536_000_000L),
    MONTH("miesiąc", "miesiące", "miesięcy", "m", 2_592_000_000L),
    WEEK("tydzień", "tygodnie", "tygodni", "w", 604_800_000L),
    DAY("dzień", "dni", "dni", "d", 86_400_000L),
    HOUR("godzinę", "godziny", "godzin", "h", 3_600_000L),
    MINUTE("minutę", "minuty", "minut", "min", 60_000),
    SECOND("sekundę", "sekundy", "sekund", "s", 1_000);

    String text1;
    String text2;
    String text3;
    String shortForm;
    long millis;

    String getForm(final long number, final boolean shortForm) {
        if (number == 0) {
            return "";
        }

        if (shortForm) {
            return this.shortForm;
        }

        if (number == 1) {
            return text1;
        }

        final long onesDigit = number % 10;
        final long tensNumber = number % 100;

        if (onesDigit < 2 || onesDigit > 4 || tensNumber >= 12 && tensNumber <= 14) {
            return text3;
        }

        return text2;
    }

    long getMillis() {
        return millis;
    }
}
