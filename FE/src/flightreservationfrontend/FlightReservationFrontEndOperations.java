package flightreservationfrontend;


/**
* flightreservationfrontend/FlightReservationFrontEndOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from /Users/alvaro/Documents/Workspaces/eclipse/FT_HA_DistFlightR/src/FlightReservationFrontEnd.idl
* Tuesday, November 15, 2016 7:27:40 PM EST
*/

public interface FlightReservationFrontEndOperations 
{
  String editFlightRecord (String adminUser, String flightInfo, short operationType);
  String getBookedFlightCount (short type);
  String transferReservation (short passengerId, String currentCity, String destinationCity);
  String bookFlight (String firstName, String lastName, String address, String phone, String destination, int date, short type);
} // interface FlightReservationFrontEndOperations