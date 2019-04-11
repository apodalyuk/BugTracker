package BugTracker;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Utils
{
    /**
     * Получает Date из LocalDate, используя системную таймзону
     * @param dateToConvert дата LocalDate
     * @return преобразованная дата
     */
    public static Date convertToDate(LocalDate dateToConvert)
    {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                                                .atZone(ZoneId.systemDefault())
                                                .toInstant());
    }
}
