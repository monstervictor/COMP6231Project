package FlightCorba;

/**
 * Interface definition: Airport.
 * 
 * @author OpenORB Compiler
 */
public interface AirportOperations
{
    /**
     * Operation bookFlight
     */
    public String bookFlight(String firstName, String lastName, String address, String phone, String destination, String flightClass, String date);

    /**
     * Operation createFlightRecord
     */
    public String createFlightRecord(String origin, String destination, String date, String time, short economySeats, short businessSeats, short firstSeats);

    /**
     * Operation editFlightRecord
     */
    public String editFlightRecord(String recordID, String fieldName, String newValue);

    /**
     * Operation getBookedFlightCount
     */
    public String getBookedFlightCount(String recordType);

    /**
     * Operation transferReservation
     */
    public String transferReservation(String PassengerID, String CurrentCity, String OtherCity);

    /**
     * Operation deleteFlightRecord
     */
    public String deleteFlightRecord(String recordID);

    /**
     * Operation editPassengerRecord
     */
    public String editPassengerRecord(String recordID, String lastName, String fieldName, String newValue);

    /**
     * Operation logDumpPassengers
     */
    public void logDumpPassengers();

    /**
     * Operation logDumpFlights
     */
    public void logDumpFlights();

}
