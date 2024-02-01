package eu.alehem.longestlines.testutil;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtil {

  public static ObjectMapper createObjectMapper() {
    return new ObjectMapper()
        .findAndRegisterModules()
        .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);
  }
}
