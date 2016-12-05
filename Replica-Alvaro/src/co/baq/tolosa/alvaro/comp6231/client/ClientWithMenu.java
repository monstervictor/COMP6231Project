package co.baq.tolosa.alvaro.comp6231.client;

import java.io.IOException;
import java.io.StringWriter;

import org.json.simple.JSONObject;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import co.baq.tolosa.alvaro.comp6231.server.FlightReservationServant;
import co.baq.tolosa.alvaro.comp6231.server.model.Flight;
import flightreservation.FlightReservationSystem;
import flightreservation.FlightReservationSystemHelper;

public class ClientWithMenu {
	
	@SuppressWarnings("unchecked")
	public static String createFlight(String departureCity, String arrivalCity) throws IOException {
		JSONObject obj = new JSONObject();
		//departureCity|arrivalCity|economicClassTotalseats|businessClassTotalSeats|firstClassTotalSeats|departureDate
		obj.put("departureCity", departureCity);
		obj.put("arrivalCity", arrivalCity);
		obj.put("economicClassTotalseats", new Integer(10));
		obj.put("businessClassTotalSeats", new Integer(15));
		obj.put("firstClassTotalSeats", new Integer(5));
		obj.put("departureDate", new Long(1478568607));// //1477892656
		
		StringWriter out = new StringWriter();
	    obj.writeJSONString(out);
	    return out.toString();
	}
	
	@SuppressWarnings("unchecked")
	public static String modifyFlight(String flightCode) throws IOException {
		JSONObject obj = new JSONObject();
		//departureCity|arrivalCity|economicClassTotalseats|businessClassTotalSeats|firstClassTotalSeats|departureDate
		obj.put("flightCode", flightCode);
		obj.put("economicClassTotalseats", new Integer(0));
		obj.put("businessClassTotalSeats", new Integer(0));
		obj.put("firstClassTotalSeats", new Integer(0));		
		StringWriter out = new StringWriter();
	    obj.writeJSONString(out);
	    return out.toString();
	}
	
	static FlightReservationSystem flightReservationSystemMtl;
	static FlightReservationSystem flightReservationSystemWst;
	static FlightReservationSystem flightReservationSystemNdl;

	public static void main(String[] args) {

		try {
			// create and initialize the ORB
			ORB orb = ORB.init(args, null);

			// get the root naming context
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			// Use NamingContextExt instead of NamingContext. This is
			// part of the Interoperable naming Service.
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			// resolve the Object Reference in Naming
			String name = "mtl";
			flightReservationSystemMtl = FlightReservationSystemHelper.narrow(ncRef.resolve_str(name));
			
			String adminUserMtl = "mtr|alvaro432";
			String adminUserWst = "wst|tol765";
			String adminUserNdl = "ndl|ash012";
			String answer = "NO ANSWER";

			System.out.println("Obtained a handle on server object: " + flightReservationSystemMtl);
			
			name = "wst";
			flightReservationSystemWst = FlightReservationSystemHelper.narrow(ncRef.resolve_str(name));
			System.out.println("Obtained a handle on server object: " + flightReservationSystemWst);
			
			name = "ndl";
			flightReservationSystemNdl = FlightReservationSystemHelper.narrow(ncRef.resolve_str(name));
			System.out.println("Obtained a handle on server object: " + flightReservationSystemNdl);
			
			//create a flight
			String flightInfo = null;
			
			//flightInfo = createFlight("ndl", "wst");
			//answer = flightReservationSystemNdl.editFlightRecord(adminUserNdl, flightInfo, FlightReservationServant.CREATE_FLIGHT);
			//System.out.println(answer);
			
		    //step 1
			/*
		    flightInfo = createFlight("mtl", "wst");
			answer = flightReservationSystemMtl.editFlightRecord(adminUserMtl, flightInfo, FlightReservationServant.CREATE_FLIGHT);
			System.out.println(answer);
			
			flightInfo = createFlight("ndl", "wst");
			answer = flightReservationSystemNdl.editFlightRecord(adminUserNdl, flightInfo, FlightReservationServant.CREATE_FLIGHT);
			System.out.println(answer);
			
			answer = flightReservationSystemNdl.bookFlight("Alv2", "Tol2", "my address2", "my phone2", "wst", (int)(System.currentTimeMillis()/1000), Flight.BUSINESS_CLASS);
			System.out.println(answer);
			
			//step 2
			answer = flightReservationSystemMtl.getBookedFlightCount(Flight.BUSINESS_CLASS);
			System.out.println(answer);
			
			
			//
			//answer = flightReservationSystemMtl.getBookedFlightCount(Flight.BUSINESS_CLASS);
			//System.out.println(answer);
			//step 3
			answer = flightReservationSystemNdl.transferReservation((short)1, "ndl", "mtl");
			System.out.println(answer);
			
			//step 4
			answer = flightReservationSystemMtl.getBookedFlightCount(Flight.BUSINESS_CLASS);
			System.out.println(answer);
			*/
			//step 5
			
			String flightCode = "LTP247";
			flightInfo = modifyFlight(flightCode);
			answer = flightReservationSystemMtl.editFlightRecord(adminUserMtl, flightInfo, FlightReservationServant.UPDATE_FLIGHT);
			System.out.println(answer);
			//step 6
			answer = flightReservationSystemMtl.getBookedFlightCount(Flight.BUSINESS_CLASS);
			System.out.println(answer);
			
			//step 7
			answer = flightReservationSystemNdl.bookFlight("Alv1", "Tol1", "my address1", "my phone1", "wst", (int)(System.currentTimeMillis()/1000), Flight.BUSINESS_CLASS);
			System.out.println(answer);
			
			answer = flightReservationSystemNdl.transferReservation((short)2, "ndl", "mtl");
			System.out.println(answer);
			
			answer = flightReservationSystemMtl.getBookedFlightCount(Flight.BUSINESS_CLASS);
			System.out.println(answer);
			
			
			
			/*
			flightInfo = createFlight("wst", "ndl");
			answer = flightReservationSystemWst.editFlightRecord(adminUserWst, flightInfo, FlightReservationServant.CREATE_FLIGHT);
			System.out.println(answer);
			
			flightInfo = createFlight("ndl", "mtl");
			answer = flightReservationSystemNdl.editFlightRecord(adminUserNdl, flightInfo, FlightReservationServant.CREATE_FLIGHT);
			System.out.println(answer);
			
			flightInfo = createFlight("mtl", "ndl");
			answer = flightReservationSystemMtl.editFlightRecord(adminUserMtl, flightInfo, FlightReservationServant.CREATE_FLIGHT);
			System.out.println(answer);
			
			*/
			//until here create flight
			
			
			
			//create a passenger
			//answer = flightReservationSystemNdl.bookFlight("Alv2", "Tol2", "my address2", "my phone2", "wst", (int)(System.currentTimeMillis()/1000), Flight.BUSINESS_CLASS);
			//System.out.println(answer);
			
		    //String flightCode = "COE247";
		    //modifying a flight
			
			//flightInfo = modifyFlight(flightCode);
			//answer = flightReservationSystem.editFlightRecord(adminUser, flightInfo, FlightReservationServant.UPDATE_FLIGHT);
			//System.out.println(answer);
			
			//until here modify a flight
			
			//delete a flight
			
			//answer = flightReservationSystem.editFlightRecord(adminUser, flightCode, FlightReservationServant.DELETE_FLIGHT);
			//System.out.println(answer);
			
			//until here delete a flight
			
			//transfer a passenger who flies from wst to ndl right to mtl
			//answer = flightReservationSystemNdl.transferReservation((short)2, "ndl", "mtl");
			//System.out.println(answer);
			
			//answer = flightReservationSystemMtl.getBookedFlightCount(Flight.BUSINESS_CLASS);
			//System.out.println(answer);

		} catch (Exception e) {
			System.out.println("ERROR : " + e);
			e.printStackTrace(System.out);
		}

	}

}

//String flightInfo = "mtr|nyc|10|15|5|1477892656";

