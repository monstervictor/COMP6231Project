package Server;

import java.io.Serializable;
import java.util.ArrayList;

public class FlightRecord implements Serializable {
    private String origin;
    private String destination;
    private String date;
    private String time;
    private ArrayList<String> passengers;
    private int economySeats;
    private int businessSeats;
    private int firstSeats;
    private String RecordID;
    
    private static final long serialVersionUID = 1L;
    
    public FlightRecord(String RecordID,
    					String origin,
    					String destination,
    					String date,
    					String hour,
    					int economySeats,
    					int businessSeats,
    					int firstSeats) 
    {
        this.setRecordID(RecordID);
        this.origin = origin;
        this.setDestination(destination);
        this.setDate(date);
        this.setTime(hour);
        this.setDestination(destination);
        this.setEconomySeats(economySeats);
        this.setBusinessSeats(businessSeats);
        this.setFirstSeats(firstSeats);
        this.setPassengers(new ArrayList<String>());
    }
    

	public String getOrigin() {return origin;}
	public void setOrigin(String Origin) {this.origin = Origin;}
	public String getDestination(){return destination;}
	public void setDestination(String destination){this.destination = destination;}
	public String getDate()	{return date;}
	public void setDate(String date){this.date = date;}
	public String getTime(){return time;}
	public void setTime(String hour){this.time = hour;}
	public ArrayList<String> getPassengers(){return passengers;}
	public void setPassengers(ArrayList<String> passengers){this.passengers = passengers;}
	public int getEconomySeats(){return economySeats;}
	public void setEconomySeats(int economySeats){this.economySeats = economySeats;}
	public int getBusinessSeats(){return businessSeats;}
	public void setBusinessSeats(int businessSeats){this.businessSeats = businessSeats;}
	public int getFirstSeats(){return firstSeats;}
	public void setFirstSeats(int firstSeats){this.firstSeats = firstSeats;}
	public String getRecordID(){return RecordID;}
	public void setRecordID(String recordID){RecordID = recordID;}
}