package pl.crystalek.crcapi.core.time;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TimeUtil {

    public String getDateInString(long timeMillis, final String delimiter, final boolean shortForm) {
        if (timeMillis < 1000) {
            return "0";
        }

        final StringBuilder result = new StringBuilder();

        for (final Time time : Time.values()) {
            final long divisionTime = timeMillis / time.getMillis();

            final String form = time.getForm(divisionTime, shortForm);
            if (!form.isEmpty()) {
                result.append(delimiter).append(divisionTime).append(" ").append(form);
                timeMillis -= divisionTime * time.getMillis();
            }
        }

        return result.substring(delimiter.length());
    }
}
