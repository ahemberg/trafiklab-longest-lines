package eu.alehem.longestlines.model.trafiklab;

public enum DefaultTransportModeCode {
  BUS,
  METRO,
  TRAM,
  TRAIN,
  SHIP,
  FERRY,
  UNKNOWN;

  static DefaultTransportModeCode fromStopAreaTypeCode(StopAreaTypeCode stopAreaTypeCode) {
    return switch (stopAreaTypeCode) {
      case BUSTERM -> BUS;
      case METROSTN -> METRO;
      case TRAMSTN -> TRAM;
      case RAILWSTN -> TRAIN;
      case SHIPBER -> SHIP;
      case FERRYBER -> FERRY;
      default -> UNKNOWN;
    };
  }
}
