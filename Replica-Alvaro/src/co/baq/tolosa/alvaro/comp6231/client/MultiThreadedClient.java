package co.baq.tolosa.alvaro.comp6231.client;


import java.io.IOException;
import java.io.StringWriter;
import java.util.Random;

import org.json.simple.JSONObject;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import co.baq.tolosa.alvaro.comp6231.server.FlightReservationServant;
import flightreservation.FlightReservationSystem;
import flightreservation.FlightReservationSystemHelper;

public class MultiThreadedClient {
public static final String CITY="wst";
	
	public static final int NUMBER_OF_THREADS = 10;
	public static String getRandomStringByLength(int length) {
		String answer = "";
		Random r = new Random();
		for(int i=0; i<length; i++)
			answer += (char) (r.nextInt(26) + 'a');
		return answer;
	}
	
	@SuppressWarnings("unchecked")
	public static String createFlight(String departureCity, String arrivalCity) throws IOException {
		JSONObject obj = new JSONObject();
		//departureCity|arrivalCity|economicClassTotalseats|businessClassTotalSeats|firstClassTotalSeats|departureDate
		obj.put("departureCity", departureCity);
		obj.put("arrivalCity", arrivalCity);
		obj.put("economicClassTotalseats", new Integer(10));
		obj.put("businessClassTotalSeats", new Integer(15));
		obj.put("firstClassTotalSeats", new Integer(5));
		obj.put("departureDate", new Long(1477892656));//1478568607
		
		StringWriter out = new StringWriter();
	    obj.writeJSONString(out);
	    return out.toString();
	}
	
	
public static void main(String[] args) {
		
		Thread [] threads = new Thread[NUMBER_OF_THREADS];
		try {
				// create and initialize the ORB
				ORB orb = ORB.init(args, null);

				// get the root naming context
				org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
				// Use NamingContextExt instead of NamingContext. This is
				// part of the Interoperable naming Service.
				NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			for(int i=0; i< NUMBER_OF_THREADS; i++){
				Runnable r = new Runnable() {

					@Override
					public void run() {
						try{
							String name = "mtl";
							FlightReservationSystem fr = FlightReservationSystemHelper.narrow(ncRef.resolve_str(name));
							String destination = "wst";
							String flight = createFlight(name, destination);
							String adminUserMtl = "mtr|alvaro432";
							String s = fr.editFlightRecord(adminUserMtl, flight, FlightReservationServant.CREATE_FLIGHT);
							//logWriter.write(admin, s);
							System.out.println(s);
							//System.out.println(s);
						} catch(Exception e) {
							e.printStackTrace();
						}
						
					}
					
				};
				threads[i] = new Thread(r); 
			}
			
			for(int i=0; i<NUMBER_OF_THREADS; i++) {
				threads[i].start();
			}
			
			for(int i=0; i<NUMBER_OF_THREADS; i++) {
				threads[i].join();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			
		}

	}

}
