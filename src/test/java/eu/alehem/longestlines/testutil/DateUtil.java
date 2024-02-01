package eu.alehem.longestlines.testutil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

  public static Date dateFromString(String dateString) {
    return dateFromString(dateString, "yyyy-MM-dd HH:mm:ss.SSS");
  }

  public static Date dateFromString(String dateString, String formatStr) {
    try {
      DateFormat format = new SimpleDateFormat(formatStr);
      format.setTimeZone(TimeZone.getTimeZone("UTC"));
      return format.parse(dateString);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
