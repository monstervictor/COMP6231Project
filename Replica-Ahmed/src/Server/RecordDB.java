package Server;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import Client.Log;

public class RecordDB {


    protected long nextId = 10000;

    protected final Object lock = new Object();
    // A map for each letter in the alphabet
    @SuppressWarnings("unchecked")
	private HashMap<String, PassengerRecord>[] passengerRecords = new HashMap[26];
    private HashMap<String, FlightRecord> flightRecords = new HashMap<String, FlightRecord>();
    final Object recordsLock = new Object();

    static RecordDB getRecordDB(String stationName)
    {
        RecordDB container = new RecordDB();
        container.createRecordContainer();
        return container;
    }


    public String getNextFreeId() 
    {
        synchronized (lock) 
        {
        	nextId++;
        	return (nextId + "");
        }
    }

    public boolean passengerExists(String firstname, String lastName) 
    {
    	synchronized (passengerRecords) 
        {
            for (HashMap<String, PassengerRecord> map : passengerRecords)
            {
                for (String id : map.keySet())
                {
                    if(map.get(id).getFirstName().equals(firstname) && map.get(id).getLastName().equals(lastName))
                    {
                    	return true;
                    }
                }
            }
        }
    	return false;
    }

    public HashMap<String, PassengerRecord>[] getPassengerRecords() 
    {
    	return this.passengerRecords;
    }
    
    public PassengerRecord getPassengerRecord(String id, String lastName) 
    {
    	PassengerRecord record = null;
        int entry = lastName.toLowerCase().codePointAt(0) - "a".codePointAt(0);

        if (entry < 0 || entry >= 26)
        {
            throw new RuntimeException(lastName + " is a bad last name");
        }
        HashMap<String, PassengerRecord> entries = passengerRecords[entry];

        record = entries.get(id);
        return record;
    }
    
    public PassengerRecord getPassengerRecord(String pid) 
    {
    	PassengerRecord record = null;
        synchronized (passengerRecords) 
        {
        	for (HashMap<String, PassengerRecord> map : passengerRecords)
            {
        		for (String id : map.keySet())
                {
        			if(pid.equals(id))
                	{
        				record = map.get(id);
                	}
                }
            }
        }
        return record;
    }
    
    public FlightRecord getFlightRecord(String id) 
    {
    	FlightRecord record = null;
    	HashMap<String, FlightRecord> map = this.flightRecords;
    	record = map.get(id);
        return record;
    }

    public ArrayList<FlightRecord> getFlightRecords(String origin, String destination, String date, String flightClass) 
    {
    	ArrayList<FlightRecord> list = new ArrayList<FlightRecord>();
        for (Entry<String, FlightRecord> entry :  this.flightRecords.entrySet())
        {
        	FlightRecord current  = entry.getValue();
        	if(current.getOrigin().equals(origin)           &&
        	   current.getDestination().equals(destination) &&
        	   current.getDate().equals(date))
        	{
        		int seats = 0;
        		switch (flightClass)
        		{
        		case "economy":       seats = current.getEconomySeats();
                					  break;
        		case "business":      seats = current.getBusinessSeats();
				  					  break;
        		case "first":         seats = current.getFirstSeats();
				  					  break;
        		default: 		      seats = 0;
			      					  break;
        		}
        		if (seats > 0){list.add(current);}
        	}
        }
        return list;
    }

    public void addRecord(PassengerRecord record) 
    {
        String lastName = record.getLastName();
        int firstLetter = lastName.toLowerCase().codePointAt(0) - "a".codePointAt(0);
        if (firstLetter < 0 || firstLetter >= 26) {throw new RuntimeException(lastName + " is a bad last name");}
        HashMap<String, PassengerRecord> passengersMap = passengerRecords[firstLetter];
        synchronized (passengersMap) { passengersMap.put(record.getId(), record);}
    }

    
    public void addRecord(FlightRecord record) 
    {
    	HashMap<String, FlightRecord> flightsMap = this.flightRecords;
        synchronized (flightsMap) {flightsMap.put(record.getRecordID(), record);}
    }

    public void deleteFlightRecord(FlightRecord record) 
    {
    	HashMap<String, FlightRecord> flightsMap = this.flightRecords;
        synchronized (flightsMap) 
        {
        	flightsMap.remove(record.getRecordID());
        }
        synchronized (passengerRecords) 
        {
            for (HashMap<String, PassengerRecord> map : passengerRecords)
            {
                for (String id : map.keySet())
                {
                	PassengerRecord currentPassenger  = map.get(id);
                	if(currentPassenger.getFlightID().equals(record.getRecordID()))
                 	   {
                	   map.remove(id);
                 	   }
                }
            }
        }
    }
    
    public boolean deletePassengerRecord(PassengerRecord record) 
    {
    	
    	for (HashMap<String, PassengerRecord> map : passengerRecords)
        {
            for (String k : map.keySet())
            {
                if(k.equals(record.getId()))
                {
                	map.remove(k);
                	return true;
                }
            }
        }
    	return false;
    }
    
    protected void createRecordContainer() 
    {
        for (int i = 0; i < 26; i++) {passengerRecords[i] = new HashMap<String, PassengerRecord>();}
    }


    public int getRecordCount(String FlightClass) 
    {
        int count = 0;
        synchronized (passengerRecords) 
        {
        	for (HashMap<String, PassengerRecord> map : passengerRecords)
            {
        		for (String id : map.keySet())
                {
        			if(map.get(id).getFlightClass().toLowerCase().equals(FlightClass.toLowerCase()))
                	{
        				count ++;
                	}
                }
            }
        }
        return count;
    }

    void dumpFlights(Log log) 
    	{
        synchronized (flightRecords) 
	        {
	        	
	        for (String id : flightRecords.keySet())
		        {
		            log.log("Flight " + id);
		        }
	            
	        }
    	}
    
    void dumpPassengers(Log log) 
    {
        synchronized (passengerRecords) 
        {
            for (HashMap<String, PassengerRecord> map : passengerRecords)
            {
                for (String id : map.keySet())
                {
                    log.log("Passenger " + id);
                }
            }
        }
    }
}