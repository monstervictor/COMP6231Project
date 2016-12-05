package Server;

import java.util.HashMap;
import java.util.Map;

public class RecordFactory {

    public static PassengerRecord createRecordFromMap(Map<String, String> map) 
    {

        String id = map.get("id");
        String fname = map.get("fname");
        String lname = map.get("lname");
        String address = map.get("address");
        String phone = map.get("phone");
        String origin = map.get("origin");
        String destination = map.get("destination");
        String date = map.get("date");
        String FlightClass = map.get("FlightClass");
        String FlightID = map.get("FlightID");


        return new PassengerRecord(id,
								   fname,
								   lname,
								   address,
								   phone,
								   origin,
								   destination,
								   FlightClass,
								   date,
								   FlightID);

    }

    public static Map<String, String> createMapFromRecord(PassengerRecord record) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", record.getId());
        map.put("fname", record.getFirstName());
        map.put("lname", record.getLastName());
        map.put("address", record.getAddress());
        map.put("phone", record.getPhone());
        map.put("origin", record.getOrigin());
        map.put("destination", record.getDestination());
        map.put("date", record.getDate());
        map.put("FlightClass", record.getFlightClass());
        map.put("FlightID", record.getFlightID());
        return map;
    }
}