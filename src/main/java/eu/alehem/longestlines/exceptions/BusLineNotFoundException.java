package eu.alehem.longestlines.exceptions;

public class BusLineNotFoundException extends RuntimeException {

  public BusLineNotFoundException(int line) {
    super("Line: " + line + " not found");
  }
}
