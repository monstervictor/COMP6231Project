package Client;


import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;

import FlightCorba.*;

import org.omg.CORBA.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.util.*;

public class ManagerClient
{
	
	static String managerID;
	static String city;
    static Log log;
	static InputStreamReader r = new InputStreamReader(System.in);
	static BufferedReader br = new BufferedReader(r);
	static Airport server;
	static String naming = "";
	static Scanner in;
	
    public ManagerClient(String managerID) 
    {
        if (!managerID.matches("[A-Z]+\\d\\d\\d\\d")) 
        {
            throw new RuntimeException("Manager ID has wrong format");
        }
        this.log = new Log(managerID);
        this.managerID = managerID;
        this.city = managerID.substring(0, managerID.length() - 4);
        this.log.log("Started!");
    }

    protected static String input(String str) 
    {
        System.out.print(str);
        return in.nextLine();
    }
    
	private static void mainMenu()
	{
		System.out.println("\n****Welcome to " + naming + " Server****\n");
		System.out.println("Please select an option (1-7)");
		System.out.println("1. Create Flight Record.");
		System.out.println("2. Book a Flight");
		System.out.println("3. Get Total Number of Records");
		System.out.println("4. Edit Flight Record");
		System.out.println("5. Transfer Record");
		System.out.println("6. Dump Flights to log");
		System.out.println("7. Dump Passengers to log");
	}
    
	public static void main(String[] args) {	
				String userInput = "";	
				in = new Scanner(System.in);
				int choice = 0;
				int udpServerPort = 0;
				
				try{
					Properties props = new Properties();
					props.put("org.omg.CORBA.ORBInitialPort", "1050");
					props.put("org.omg.CORBA.ORBInitialHost", "localhost");
			        ORB orb = ORB.init(args, props);
			        org.omg.CORBA.Object objRef = 
			            orb.resolve_initial_references("NameService");
			        NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			        login();
			        server = AirportHelper.narrow(ncRef.resolve_str(naming));

			        System.out.println("Obtained a handle on server object: " + naming);
					
			    log = new Log(managerID);
			    mainMenu();		
				while(true)
				{
					boolean valid = false;
					while(!valid)
					{
						try{
							choice = Integer.parseInt(br.readLine());
							valid=true;
						} catch(Exception e){
							System.out.println("Invalid Input, please enter an Integer");
							valid=false;
							br.readLine();
						}
					}
					
					// Manage user selection.
					switch(choice)
					{
					case 1: 
						createFlightRecord();		
						mainMenu();				
						break;
						
					case 2:
						bookFlight();
						mainMenu();
						break;
						
					case 3:
						getBookedFlightCount();
						mainMenu();
						break;
					case 4:
						editFlightRecord();
						mainMenu();
						break;
					case 5:
						TransferRecord();
						mainMenu();
						break;
					case 6:
						DUMPflights();
						mainMenu();
						break;
					case 7:
						DUMPpassengers();
						mainMenu();
						break;
					default:
						System.out.println("Invalid Input, please try again.");
					}
				
				}
				
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
	}


	private static void DUMPpassengers()
	{
		server.logDumpPassengers();
	}

	private static void DUMPflights()
	{
		server.logDumpFlights();
	}

	private static void TransferRecord()
	{
		String PassengerID, CurrentCity, OtherCity;
		
		PassengerID = input("Passenger Record ID: ");
		
		CurrentCity = input("Current City: ");
		while (!verifyLocation(CurrentCity))
		{
		System.out.println("Invalid Location, please input \"MTL\" \"NDL\" or \"WST\"");
		CurrentCity =input("Current City: ");
		}
		
		OtherCity = input("Other City: ");
		while (!verifyLocation(OtherCity))
		{
		System.out.println("Invalid Location, please input \"MTL\" \"NDL\" or \"WST\"");
		OtherCity =input("Other City: ");
		}
		log.log("[CLIENT] Trying to transfer passenger record " + PassengerID + " FROM " + CurrentCity + " TO " + OtherCity);
		String result  = server.transferReservation(PassengerID, CurrentCity, OtherCity);
		if(result.equals("FAIL") || result.equals("NONE"))
		{
			System.out.println("Operation failed see log for more details");
			
		}
		else {System.out.println("Operation Sucessfull: new id of the record: " + result);}
	}

	private static void getBookedFlightCount()
	{
		String FlightClass;
		FlightClass = input("Flight Class: ");
		System.out.println(server.getBookedFlightCount(FlightClass));
	}

	private static void editFlightRecord()
	{
        String recordID, fieldName, newValue;
        
        recordID = input("Record ID: ");
        fieldName = input("Field name: ");
		while (!verifyField(fieldName))
		{
		System.out.println("Invalid field name, please input \"origin\" "
				            + "\"destination\" \"date\" \"time\"  \"economySeats\" \"businessSeats\" \"firstSeats\"");
		fieldName =input("Field name: ");
		}
		newValue = input("New value: ");
		
		log.log("[CLIENT] Trying set the " + fieldName.toUpperCase() +  " of record " + recordID + " to " + newValue);
        String res = server.editFlightRecord(recordID, fieldName.toLowerCase(), newValue);
    	if(res != null)
    	{
    		System.out.println("[CLIENT] New " + fieldName.toUpperCase() + " for flight " + recordID +" is now: "+ newValue);
            log.log("[CLIENT] New " + fieldName.toUpperCase() + " for flight " + recordID +" is now: "+ newValue);
    	}
    	else
    	{
    		System.out.println("[CLIENT] Editing Flight failed (Flight <"+ recordID + ">)");
            log.log("[CLIENT] Editing Flight failed (Flight <"+ recordID + ">)");
    	}
		
	}


	private static void bookFlight()
	{
		String firstName, lastName, address, phone, destination,  flightClass, date;
		
		firstName = input("First Name: ");
		lastName = input("Last Name: ");
		
		address = input("Address: ");
		while (!verifyLocation(address))
		{
		System.out.println("Invalid Location, please input \"MTL\" \"NDL\" or \"WST\"");
		address =input("Origin: ");
		}
		
		phone = input("Phone: ");
		
		destination = input("Destination: ");
		while (!verifyLocation(destination))
		{
		System.out.println("Invalid Location, please input \"MTL\" \"NDL\" or \"WST\"");
		destination =input("Origin: ");
		}
		
		flightClass = input("Flight Class: ");
		date = input("Date: ");
			
		
		log.log("[CLIENT]Trying to book a flight from " + address + " to " + destination + " on the " + date);
    	String res = server.bookFlight(firstName, lastName, address, phone, destination, flightClass, date);
    	if(res != null)
    	{
    		if(res.equals("double"))
    		{
    			System.out.println("[CLIENT]Booking Operation for client: "+ firstName + " " + lastName +" Failed because it already exists");
            	log.log("[CLIENT]Booking Operation for client: "+ firstName + " " + lastName +" Failed because it already exists");
    		}
    		else if(res.equals("NULL"))
    		{
    			System.out.println("[CLIENT]Booking Operation for client: "+ firstName + " " + lastName +" Failed because there is no suitable flight");
            	log.log("[CLIENT]Booking Operation for client: "+ firstName + " " + lastName +" Failed because there is no suitable flight");
    		}
    		else if(res.equals("full"))
    		{
    			System.out.println("[CLIENT]Booking Operation for client: "+ firstName + " " + lastName +" Failed because flight is full");
            	log.log("[CLIENT]Booking Operation for client: "+ firstName + " " + lastName +" Failed because flight is full");
    		}
    		else
    		{
    			System.out.println("[CLIENT]Booking Operation Successful: Flight <"+ res + "> was booked for client: "+firstName + " " + lastName);
            	log.log("[CLIENT]Booking Operation Successful: Flight might be booked for client: "+firstName + " " + lastName);
    		}
    		
    	}
    	else 
    	{
    		System.out.println("[CLIENT]Booking Operation for client: "+ firstName + " " + lastName +" Failed");
        	log.log("[CLIENT]Booking Operation for client: "+ firstName + " " + lastName +" Failed");
    	}
		
	}
	
	private static void createFlightRecord()
	{
		String origin, destination, date, time; 
		short economySeats, businessSeats, firstSeats;
		
		System.out.println("---Create Flight Record---");
		
		origin = input("Origin: ");
		while (!verifyLocation(origin))
			{
			System.out.println("Invalid Origin, please input \"MTL\" \"NDL\" or \"WST\"");
			origin =input("Origin: ");
			}	
		
		destination = input("Destination: ");
		while (!verifyLocation(origin))
			{
			System.out.println("Invalid Destination, please input \"MTL\" \"NDL\" or \"WST\"");
			origin = input("Destination: ");
			}
		
		date = input("Date: ");
		time = input("Time: ");
		
		economySeats = (short) Integer.parseInt(input("Economy Seats: "));
		businessSeats = (short) Integer.parseInt(input("Business Seats: "));
		firstSeats = (short) Integer.parseInt(input("First Seats: "));

		//CORBA call
    	log.log("[CLIENT]Creating flight from " + origin + " to " + destination + " on the " + date + " at " + time);
    	String id = server.createFlightRecord(origin, destination, date, time, economySeats, businessSeats, firstSeats);
    	System.out.println("[CLIENT]Success: Flight <"+ id +"> created");
    	log.log("[CLIENT]Success: Flight <"+ id + "> created");
		
	}

    private static boolean verifyLocation(String City)
	{
	if (City.equals("MTL") || City.equals("NDL") || City.equals("WST")){return true;}						
		
		else {return false;}
	}
    
    private static boolean verifyField(String Field)
	{
	if (Field.toLowerCase().equals("origin") ||
	    Field.toLowerCase().equals("destination") ||
	    Field.toLowerCase().equals("date") ||
	    Field.toLowerCase().equals("time") ||
	    Field.toLowerCase().equals("economyseats") ||
	    Field.toLowerCase().equals("businessseats") ||
	    Field.toLowerCase().equals("firstseats"))
	    {
		return true;
		}						
		
		else {return false;}
	}

	private static void login()
	{
		managerID = input("Please input your manager ID to start:");
		while (!verifyManagerID(managerID))
		{
			System.out.println("Invalid Manager ID");
			System.out.println("Manager ID must be 8 characters and start with \"MTL\", \"LVL\" or \"DDO\":");
			managerID = input("Manager ID:");
		}
		
		if (managerID.substring(0,3).equals("MTL")){
			naming = "MTL";
		}else if (managerID.substring(0,3).equals("NDL")){
			naming = "NDL";
		}else if (managerID.substring(0,3).equals("WST")){
			naming = "WST";
		}
		
	}
	
	public static boolean verifyManagerID(String managerID)
	{
        if (!managerID.matches("[A-Z]+\\d\\d\\d\\d")) 
        {
            return false;
        }
		return true;
	}
	
	
}
