package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import Client.Log;
import FlightCorba.*;


public class AirportServant extends AirportPOA{
	private Log log;
	private int recordCount;
	private RecordDB records;
	private ORB orb;
	private String Name = "";
	private final static String[] cities = {"MTL", "NDL", "WST"};
	
public AirportServant(String Name)
{
	super();
	this.Name = Name;
    this.records = RecordDB.getRecordDB(Name);
    log = new Log(Name);
    System.out.println("[SERVER] Server Created");
    log.log("[SERVER] Server Created");
}


public static void main(String[] args) {    
    Log serverlog = new Log("ServerMain");
    serverlog.log("Started...");

    for (String city : cities) 
    {
    	AirportServant servant = new AirportServant(city);
    	servant.startServerThreads();
    }
}


public void setORB(ORB orb_val){
	orb = orb_val;
}

//TODO
@Override
public String bookFlight(String firstName, String lastName, String address, String phone, String destination,
		String flightClass, String date)
{
    log.log("[SERVER] Trying to create a PassengerRecord for (" + firstName + " " + lastName + ") Destination: " + destination );
    if(records.passengerExists(firstName, lastName))
    {
    	log.log("[SERVER] Passenger is already registered in the databse");
    	return "double";
    }
    boolean notValid = false;
    ArrayList<FlightRecord> FlightsAvailable  = records.getFlightRecords(this.Name, destination, date, flightClass);
    if(FlightsAvailable.isEmpty())
    {
    	log.log("[SERVER] No flights available for " + firstName + " " + lastName);
    	return "NULL";
    }
    
    FlightRecord fRecord = FlightsAvailable.get(0);
    synchronized (fRecord) 
    {
	    switch (flightClass)
		{
		case "economy":       if(fRecord.getEconomySeats() > 1){fRecord.setEconomySeats(fRecord.getEconomySeats() - 1);}
							  else
							  {
								  log.log("[SERVER] Flight class is full");
								  return "full";
							  }
	    					  break;
		
		case "business":      if(fRecord.getBusinessSeats() > 1){fRecord.setBusinessSeats(fRecord.getBusinessSeats() - 1);}
							  else
							  {
								  log.log("[SERVER] Flight class is full");
								  return "full";
							  }
		  					  break;
		
		case "first":         if(fRecord.getFirstSeats() > 1){fRecord.setFirstSeats(fRecord.getFirstSeats() - 1);}
							  else
							  {
								  log.log("[SERVER] Flight class is full");
								  return "full";
							  }
							  break;
							  
		default: 		      notValid = true;
	      					  break;
		}
		if(notValid)
		{
			log.log("[SERVER]  bookFlight failed wrong parameter for flight class");
			return "NULL";
		}
    }
    PassengerRecord pRecord = new PassengerRecord(records.getNextFreeId(),
		    									  firstName,
		    									  lastName,
		    									  address,
		    									  phone,
		    									  this.Name,
		    									  destination,
		    									  flightClass,
		    									  date,
		    									  fRecord.getRecordID());
    String id = pRecord.getId();
    synchronized (pRecord) 
    {
    	records.addRecord(pRecord);
        log.log("[SERVER]  Passenger record created <" + id + "> for " + firstName + " " + lastName);
        log.log("[SERVER]  Passenger <" + id + ">  has been booked on the flight <" + fRecord.getRecordID()+">" );
    }
    
    if(fRecord.getRecordID() != null){return fRecord.getRecordID();}
	return "failure";
}

@Override
public String createFlightRecord(String origin, String destination, String date, String time, short economySeats,
		short businessSeats, short firstSeats)
{
	log.log("[SERVER] Trying to create the flight record on the " + date);

	FlightRecord record = new FlightRecord(records.getNextFreeId(),
										   origin,
										   destination,
										   date,
										   time,
									 	   economySeats,
									 	   businessSeats,
									 	   firstSeats);
    String id = record.getRecordID();
    synchronized (records) 
    {
    	records.addRecord(record);
    }
	log.log("[SERVER]  Flight created <" + id + "> from " + origin + " to " + destination + " on the " + date + " at " + time);
    
    return id;
}

@Override
public String editFlightRecord(String recordID, String fieldName, String newValue)
{
	 log.log("[SERVER] Trying to set the " + fieldName.toUpperCase() +  " of record " + recordID + " to " + newValue);

	 FlightRecord record = records.getFlightRecord(recordID);
	 String id = null;

	 if (record != null)
	 {
	     synchronized (record) 
	     {	
	     	switch (fieldName.toLowerCase()) 
	     	{
	     	case "origin":        record.setOrigin(newValue);
	         		 		      break;
	     	case "destination":   record.setDestination(newValue);
	     						  break;	
	     	case "date":          record.setDate(newValue);
	 		   				      break;
	     	case "time":          record.setTime(newValue);
	 		   				      break;
			case "economyseats":  record.setEconomySeats(Integer.parseInt(newValue));
							      break;
			case "businessseats": record.setBusinessSeats(Integer.parseInt(newValue));
							      break;
			case "firstseats":    record.setFirstSeats(Integer.parseInt(newValue));
			                      break; 
	     	default: 		      id = record.getRecordID();
	         				      break;
	     	}
	        id = record.getRecordID();
	        synchronized (records.getPassengerRecords()) 
	        {
	            for (HashMap<String, PassengerRecord> map : records.getPassengerRecords())
	            {
	                for (String k : map.keySet())
	                {
	                    if(map.get(k).getFlightID().equals(id))
	                    {
	                    	map.remove(k);
	                    	log.log("[SERVER] Flight booking for client:"+ k + " was cancelled since the flight was modified.");
	                    }
	                }
	            }
	        }
	     }
	     log.log("[SERVER] editFlightRecord sucessfully set the " + fieldName.toUpperCase() +  " of record " + recordID + " to " + newValue);
	 } 
	 
	 else 
	 {
	 	log.log("[SERVER] editFlightRecord did not find record " + recordID);
	 	return null;
	 }
	 
	 return id;
}




@Override
public String deleteFlightRecord(String recordID)
{
	log.log("[SERVER] DeleteFlightRecord Trying to delete the flight record " + recordID);

	FlightRecord record = records.getFlightRecord(recordID);
	String id = null;
	
	if (record != null)
	{
	   synchronized (records) 
	   {	
		   records.deleteFlightRecord(record);
	       id = record.getRecordID();
	   }
	   log.log("[SERVER] DeleteFlightRecord sucessfully deleted the record " + recordID);
	} 
	
	else 
	{
		log.log("[SERVER] DeleteFlightRecord did not find record " + recordID);
		return null;
	}

	return id;
}

@Override
public String editPassengerRecord(String recordID, String lastName, String fieldName, String newValue)
{
	log.log("[SERVER]editPassengerRecord Trying to set the" + fieldName +  " of record " + recordID + " to " + newValue);

    PassengerRecord record = records.getPassengerRecord(recordID, lastName);
    String id = null;

    if (record != null)
    {
        synchronized (record) 
        {	
        	switch (fieldName.toLowerCase()) 
        	{
        	case "firstname":    record.setFirstName(newValue);
            		 		     break;
        	case "lastname":     record.setLastName(newValue);
	 		   				     break;
        	case "address":     record.setAddress(newValue);
	 		   				     break;
			case "phone":        record.setPhone(newValue);
							     break;
        	case "destination":  record.setDestination(newValue);
	 		   				     break;
			case "date":         record.setDate(newValue);
							     break;
			case "class":        record.setFlightClass(newValue);
							     break;
        	default: id = record.getId();
            break;
        	}
            
            id = record.getId();
        }
        log.log("[SERVER]  editPassengerRecord sucessfully set the" + fieldName +  " of record " + recordID + " to " + newValue);
    } 
    
    else {log.log("[SERVER]  editPassengerRecord did not find record " + recordID);}
    
    return id;
}

@Override
public void logDumpPassengers()
{
	records.dumpPassengers(log);
}

@Override
public void logDumpFlights()
{
	records.dumpFlights(log);
	
}



public void startServerThreads() {
    new Thread(new Runnable() 
    {
        @Override
        public void run() {UDPproc();}
    }).start();
    new Thread(new Runnable()
    {
        @Override
        public void run() {CorbaProc();}
    }).start();
}


public void CorbaProc() {

    log.log("Attempting to start CORBA services for server " + this.Name);

    try{
		String[] args = null;
		Properties props = new Properties();
		props.put("org.omg.CORBA.ORBInitialPort", "1050");
		props.put("org.omg.CORBA.ORBInitialHost", "localhost");
		ORB orb = ORB.init(args, props);		
		POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
		rootpoa.the_POAManager().activate();
		this.setORB(orb);	
		org.omg.CORBA.Object ref = rootpoa.servant_to_reference(this);
		Airport href = AirportHelper.narrow(ref);
		org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
		NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
		NameComponent path[] = ncRef.to_name(Name);
		ncRef.rebind(path,  href);
		System.out.println(Name + " Server is ready ...");
		orb.run();
	}
    catch(Exception e)
    {
		System.out.println("ERROR: " + e.getMessage());
	}

}

// UDP method to listen for requests
@SuppressWarnings("resource")
protected void UDPproc() {

    log.log("Attempting to publish UDP for " + this.Name);

    int recvPort = UDPPortGen(Name);
    DatagramSocket socket;
    try { socket = new DatagramSocket(recvPort);} 
    catch (SocketException e)
    {
        log.log(e.getMessage());
        log.log("UDP server failed to start");
        return;
    }
    log.log("UDP port for server : " + Name + " is " + UDPPortGen(Name));
   
    byte[] buffer = new byte[1500];
    while (true) {
        DatagramPacket recvPacket = new DatagramPacket(buffer, buffer.length);
        try 
        {
            socket.receive(recvPacket);
            InetAddress sendAddr = recvPacket.getAddress();
            int sendPort = recvPacket.getPort();
            String requestString = new String(buffer).substring(0, recvPacket.getLength());
            Map<String, String> request = MapUtil.parse(requestString);
            Map<String, String> response = handleRequest(request);
            String responseString = MapUtil.stringify(response);
            DatagramPacket sendPacket = new DatagramPacket(responseString.getBytes(), responseString.length(), sendAddr, sendPort);
            socket.send(sendPacket);
        } 
        catch (IOException ex) 
        {
            log.log(ex.toString() + " " + ex.getMessage());
        }
    }
}

// Get booked flight Count
@Override
public String getBookedFlightCount(String recordType)
{

    log.log("[SERVER] trying to get Booked Flight Count");

    String counts = "";

    for (String city : cities) {
        try 
        {
        	String request = "";
        	log.log("[SERVER] Trying to get the record count for class " + recordType);
        	switch(recordType.toLowerCase())
        	{
        	case "economy":		request = "action:recordE";
        						break;
        	case "first":		request = "action:recordF";
        						break;
        	case "business":	request = "action:recordB";
        						break;
        	default : 			request = "";
        						break;
        	}
        	log.log("[SERVER] Sending the request " + request);
            DatagramSocket socket = new DatagramSocket();
            InetAddress iAddress = InetAddress.getByName("localhost");
            int port = this.UDPPortGen(city);
            DatagramPacket packet = new DatagramPacket(request.getBytes(), request.length(), iAddress, port);
            socket.send(packet);
            byte[] buffer = new byte[1500];
            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String response = new String(buffer, 0, packet.getLength());
            if (response.startsWith("recordCount:")) 
            {
                String count = response.substring(response.indexOf(":") + 1);
                counts += city + ": " + count + " ";
            } 
            else 
            {
                counts += city + " failed to respond ";
                log.log("[SERVER] " + city + " failed to respond.");
            }

            socket.close();

        }
        catch (Exception e) 
        {
        	System.out.println(e.getMessage());
        } 
}
    return counts;
}

// Handle UDP request such as RecordCount or Transfer
protected Map<String, String> handleRequest(Map<String, String> request) {
    String action = request.get("action");
    HashMap<String, String> response = new HashMap<String, String>();

    if (action == null) {action = "";}

    if ("recordE".equals(action)) 
    {
    	int count = records.getRecordCount("economy");
        response.put("recordCount", Integer.toString(count));
        return response;
    }
    
    if ("recordB".equals(action)) 
    {
    	int count = records.getRecordCount("business");
        response.put("recordCount", Integer.toString(count));
        return response;
    }
    
    if ("recordF".equals(action)) 
    {
    	int count = records.getRecordCount("first");
        response.put("recordCount", Integer.toString(count));
        return response;
    }

    if ("transfer".equals(action)) {
        request.put("id", records.getNextFreeId());
        PassengerRecord record = RecordFactory.createRecordFromMap(request);
        boolean notValid = false;
        ArrayList<FlightRecord> FlightsAvailable  = records.getFlightRecords(this.Name, record.getDestination(), record.getDate(), record.getFlightClass());
        if(FlightsAvailable.isEmpty())
        {
        	log.log("[SERVER] No flights available for " + record.getFirstName() + " " + record.getLastName());
        	response.put("status", "NO_SEATS");
        	return response;
        }
        
        FlightRecord fRecord = FlightsAvailable.get(0);
        synchronized (fRecord) 
        {
    	    switch (record.getFlightClass())
    		{
    		case "economy":       if(fRecord.getEconomySeats() > 1){fRecord.setEconomySeats(fRecord.getEconomySeats() - 1);}
    							  else
    							  {
    								  log.log("[SERVER] Flight class is full");
    								  response.put("status", "NULL");
    								  return response;
    							  }
    	    					  break;
    		
    		case "business":      if(fRecord.getBusinessSeats() > 1){fRecord.setBusinessSeats(fRecord.getBusinessSeats() - 1);}
    							  else
    							  {
    								  log.log("[SERVER] Flight class is full");
    								  response.put("status", "NULL");
    								  return response;
    							  }
    		  					  break;
    		
    		case "first":         if(fRecord.getFirstSeats() > 1){fRecord.setFirstSeats(fRecord.getFirstSeats() - 1);}
    							  else
    							  {
    								  log.log("[SERVER] Flight class is full");
    								  response.put("status", "NULL");
    								  return response;
    							  }
    							  break;
    							  
    		default: 		      notValid = true;
    	      					  break;
    		}
    		if(notValid)
    		{
    			log.log("[SERVER] Transfer failed wrong parameter for flight class");
    			response.put("status", "NULL");
    			return response;
    		}
        }
        
        records.addRecord(record);
        response.put("status", "success");
        response.put("id", record.getId());
        return response;
    }

    response.put("error", "invalidAction");
    return response;
}

// generate unique UDP port
protected int UDPPortGen(String string)
	{
    int index = 0;
    for (char ch : string.toCharArray()) 
    	{
        index = (index * 94 + ch - 33) % (94 * 94 - 36);
    	}
    return index + 1024;
	}

//Transfer function
@Override
public String transferReservation(String PassengerID, String CurrentCity, String OtherCity)
{
    log.log("[SERVER] Tryinmg to transfer Record (" + PassengerID + " transfers " + CurrentCity + " --> " + OtherCity + ")");

    if (!Arrays.asList(cities).contains(OtherCity)) 
    {
        log.log("[SERVER ]  Transfer operation FAILED, invalid other city.");
        return "FAIL";
    }

    try
    {
        PassengerRecord record = records.getPassengerRecord(PassengerID);

        if (record == null) 
        {
            log.log("[SERVER] Transfer operation FAILED no record was found.");
            return "NONE";
        }

        Map<String, String> request = RecordFactory.createMapFromRecord(record);
        request.put("action", "transfer");
        DatagramSocket socket = new DatagramSocket();
        InetAddress iAddress = InetAddress.getByName("localhost");
        String requestS = MapUtil.stringify(request);
        int port = this.UDPPortGen(OtherCity);
        DatagramPacket packet = new DatagramPacket(requestS.getBytes(), requestS.length(), iAddress, port);
        socket.send(packet);
        byte[] buffer = new byte[1500];
        packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        Map<String, String> response = MapUtil.parse(new String(buffer, 0, packet.getLength()));
        String status = response.get("status");
        if ("success".equals(status) && records.deletePassengerRecord(record)) 
        {   
            log.log("[SERVER] Transfering Record Sucess");
        }
        else {log.log("[SERVER] Transfering Record Failure " + status);}
        socket.close();
        if (response.containsKey("id")) {return response.get("id");}
        return "NONE";
    } 
    catch (Exception e) 
    {
        log.log("  Exception when transfering Record: "+ e.getMessage());
        return "FAIL";
    }
}

}
