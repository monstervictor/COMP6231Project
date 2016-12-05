package co.baq.tolosa.alvaro.comp6231.server.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class Flight {
	public static final int FLIGHT_NUMBER_PREFIX_LENGTH = 3;
	public static final int NUMBER_OF_FIELDS_ON_FLIGHT_WITHOUT_NUMBER = 6;
	//public static final int NUMBER_OF_FIELDS_ON_FLIGHT_WITH_NUMBER = 7;
	/*private static final int DEPARTURE_CITY_INDEX = 0;
	private static final int ARRIVAL_CITY_INDEX   = 1;
	private static final int ECONOMIC_CLASS_INDEX = 2;
	private static final int BUSINESS_CLASS_INDEX = 3;
	private static final int FIRST_CLASS_INDEX    = 4;
	private static final int DEPARTURE_DATE_INDEX = 5; */
	//private static final int FLIGHT_NUMBER_INDEX = 6;
	
	public static final short ECONOMIC_CLASS = 1;
	public static final short BUSINESS_CLASS = 2;
	public static final short FIRST_CLASS = 3;
	
	
	private int economicClassTotalSeats;
	private int businessClassTotalSeats;
	private int firstClassTotalSeats;
	
	private String departureCity;
	private String arrivalCity;
	
	private String flightNumber;
	private Date departureDate;
	
	private int economicClassSoldSeats;
	private int businessClassSoldSeats;
	private int firstClassSoldSeats;
	
	private static int counter = 246;
	
	public static List<String>fieldNames = Arrays.asList("departureCity", "arrivalCity", 
			"economicClassTotalseats", "businessClassTotalSeats", 
			"firstClassTotalSeats", "departureDate", "flightCode");
	
	private static void callFunction(String fieldName, Object value, Flight modified){
		switch(fieldName) {
			case "departureCity":
				modified.setDepartureCity(value.toString());
				break;
			case "arrivalCity":
				modified.setArrivalCity(value.toString());
				break;
			case "flightCode":
				modified.setFlightNumber(value.toString());
				break;
			case "economicClassTotalseats":
				modified.setEconomicClassTotalSeats(Integer.parseInt(value.toString()));
				break;
			case "businessClassTotalSeats":
				modified.setBusinessClassTotalSeats(Integer.parseInt(value.toString()));
				break;
			case "firstClassTotalSeats":
				modified.setFirstClassTotalSeats(Integer.parseInt(value.toString()));
				break;
			case "departureDate":
				modified.setDepartureDate(new Date(Long.parseLong(value.toString())*1000));
				break;
		}
	}
	/*
	 * the string must be of this form:
	 * departureCity|arrivalCity|economicClassTotalseats|businessClassTotalSeats|firstClassTotalSeats|departureDate(long)
	 */
	public static Flight fromString(String flight) {
		Flight answer = new Flight();
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(flight);
			if(obj instanceof JSONObject){
				JSONObject obj2 = (JSONObject)obj;
				for(Object key: obj2.keySet()) {
					String keyS = key.toString();
					if (fieldNames.contains(keyS)) {
						callFunction(keyS, obj2.get(key), answer);
					}
				}
			} else {
				System.out.println("Object is not a json");
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
						
		return answer;
	}
	
	//returns the number of available seats after reducing it by the quantity, -1 if there is not enough seats
		public int reduceNumberOfSeatsAvailables(int type, int quantity){
			int answer = -1;
			switch(type) {
			case Flight.BUSINESS_CLASS:
				if (this.getBusinessClassTotalSeats()- this.getBusinessClassSoldSeats() >= quantity) {
					this.businessClassSoldSeats+= quantity;
					answer = this.getBusinessClassTotalSeats()- this.getBusinessClassSoldSeats();
				}
				break;
			case Flight.ECONOMIC_CLASS:
				if (this.getEconomicClassTotalSeats()- this.getEconomicClassSoldSeats() >= quantity) {
					this.economicClassSoldSeats+= quantity;
					answer = this.getEconomicClassTotalSeats()- this.getEconomicClassSoldSeats();
				}
				break;
			case Flight.FIRST_CLASS:
				if (this.getFirstClassTotalSeats()- this.getFirstClassSoldSeats() >= quantity) {
					this.firstClassSoldSeats+= quantity;
					answer = this.getFirstClassTotalSeats()- this.getFirstClassSoldSeats();
				}
				break;
			}
			
			return answer;
		}
	
	public void merge(Flight other){
		if (other.getArrivalCity()!=null && !other.getArrivalCity().isEmpty()) this.setArrivalCity(other.getArrivalCity());
		if (other.getDepartureCity()!=null && !other.getDepartureCity().isEmpty()) this.setDepartureCity(other.getDepartureCity());
		
		if (other.getEconomicClassTotalSeats() >= 0) this.setEconomicClassTotalSeats(other.getEconomicClassTotalSeats());
		if (other.getBusinessClassTotalSeats() >= 0) this.setBusinessClassTotalSeats(other.getBusinessClassTotalSeats());
		if (other.getFirstClassTotalSeats() >= 0) this.setFirstClassTotalSeats(other.getFirstClassTotalSeats());
		
		if(other.getDepartureDate()!=null) this.setDepartureDate(other.getDepartureDate());

	}
	
	@Override
	public String toString() {
		return String.format("%s:%s:%s (%3d, %3d, %3d, %s))", this.getDepartureCity(), this.getArrivalCity(), 
				this.getFlightNumber(), this.getEconomicClassTotalSeats(), 
				this.getBusinessClassTotalSeats(), this.getFirstClassTotalSeats(), 
				this.getDepartureDate());
	}
	
	public void generateFlightNumber() {
		this.flightNumber = this.generateFlightNumber(FLIGHT_NUMBER_PREFIX_LENGTH);
	}
	
	private String generateFlightNumber(int prefix) {
		Random r = new Random();
		char [] aux = new char [prefix]; 
		for(int i=0; i<prefix; i++) {
			aux[i] = (char)('A' + (char)r.nextInt(26));
		}
		return String.format("%s%03d", new String(aux), Flight.getCounter());
	}
	
	synchronized private static int getCounter() {
		return ++counter;
	}

	public int getEconomicClassTotalSeats() {
		return economicClassTotalSeats;
	}

	public void setEconomicClassTotalSeats(int economicClassTotalSeats) {
		this.economicClassTotalSeats = economicClassTotalSeats;
	}

	public int getBusinessClassTotalSeats() {
		return businessClassTotalSeats;
	}

	public void setBusinessClassTotalSeats(int businessClassTotalSeats) {
		this.businessClassTotalSeats = businessClassTotalSeats;
	}

	public int getFirstClassTotalSeats() {
		return firstClassTotalSeats;
	}

	public void setFirstClassTotalSeats(int firstClassTotalSeats) {
		this.firstClassTotalSeats = firstClassTotalSeats;
	}

	public String getDepartureCity() {
		return departureCity;
	}

	public void setDepartureCity(String departureCity) {
		this.departureCity = departureCity;
	}

	public String getArrivalCity() {
		return arrivalCity;
	}

	public void setArrivalCity(String arrivalCity) {
		this.arrivalCity = arrivalCity;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}

	public int getEconomicClassSoldSeats() {
		return economicClassSoldSeats;
	}

	public int getBusinessClassSoldSeats() {
		return businessClassSoldSeats;
	}

	public int getFirstClassSoldSeats() {
		return firstClassSoldSeats;
	}
	
	public int freeSeats(int classType) {
		int answer = 0;
		switch(classType) {
		case Flight.ECONOMIC_CLASS: answer = this.getEconomicClassTotalSeats() - this.getEconomicClassSoldSeats(); break;
		case Flight.BUSINESS_CLASS: answer = this.getBusinessClassTotalSeats() - this.getBusinessClassSoldSeats(); break;
		case Flight.FIRST_CLASS: answer = this.getFirstClassTotalSeats() - this.getFirstClassSoldSeats(); break;
		}
		return answer;
	}

}

/*

String [] parts = flight.split("\\|");

if (parts.length != Flight.NUMBER_OF_FIELDS_ON_FLIGHT_WITHOUT_NUMBER ) return null;

answer = new Flight();
answer.setDepartureCity(parts[DEPARTURE_CITY_INDEX].trim().toLowerCase());
answer.setArrivalCity(parts[ARRIVAL_CITY_INDEX].trim().toLowerCase());
answer.setEconomicClassTotalSeats(Integer.parseInt(parts[ECONOMIC_CLASS_INDEX]));
answer.setBusinessClassTotalSeats(Integer.parseInt(parts[BUSINESS_CLASS_INDEX]));
answer.setFirstClassTotalSeats(Integer.parseInt(parts[FIRST_CLASS_INDEX]));
answer.setDepartureDate(new Date(Long.parseLong(parts[DEPARTURE_DATE_INDEX])*1000));
*/
