package Server;

import java.io.Serializable;

public class PassengerRecord implements Serializable {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String origin;
    private String destination;
    private String FlightClass;
    private String date;
    private String RecordID;
    private String flightID;
    private static final long serialVersionUID = 1L;
    
    public PassengerRecord(String RecordID,
    					   String firstName,
    					   String lastName,
	    				   String address,
	    				   String phone,
	    				   String origin,
	    				   String destination,
	    				   String FlightClass,
	    				   String date,
	    				   String flightID) 
    {
        this.RecordID = RecordID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone	 = phone;
        this.origin = origin;
        this.destination = destination;
        this.FlightClass = FlightClass;
        this.date = date;
        this.setFlightID(flightID);
    }
    

	public String getFirstName() {return firstName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public String getLastName() {return lastName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
	public String getFlightClass(){return FlightClass;}
	public void setFlightClass(String class1){FlightClass = class1;}
	public String getDate(){return date;}
	public void setDate(String date){this.date = date;}
    public String getId() {return  RecordID;}
	public String getAddress()	{return address;}
	public void setAddress(String address){this.address = address;}
	public String getPhone(){return phone;}
	public void setPhone(String phone){this.phone = phone;}
	public String getDestination(){return destination;}
	public void setDestination(String destination){this.destination = destination;}
	public String getOrigin(){return origin;}
	public void setOrigin(String origin){this.origin = origin;}
	public String getFlightID(){return flightID;}
	public void setFlightID(String flightID){this.flightID = flightID;}


}