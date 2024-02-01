package eu.alehem.longestlines.model.trafiklab;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum ZoneShortName {
  A,
  B,
  C,
  @JsonEnumDefaultValue
  UNDEFINED
}
