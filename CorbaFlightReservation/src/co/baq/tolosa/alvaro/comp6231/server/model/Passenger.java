package co.baq.tolosa.alvaro.comp6231.server.model;

import java.io.Serializable;
import java.util.Date;

public class Passenger implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4069822458702834744L;
	private static int passengerIdCounter = 1;
	private short type; //economic, business or firt class. This data is in the Flight class
	private String name;
	private String lastName;
	private String address;
	private String phoneNumber;
	private String destinationCity; //TODO: possible redundant. Flight has this info
	private Date departureDate; //TODO: possible redundant. Flight has this info
	private String departureCity;
	
	private String flightNumber; //flight where the passenger is.
	private int passengerId;
	
	public String getDepartureCity() {
		return departureCity;
	}

	public void setDepartureCity(String departureCity) {
		this.departureCity = departureCity;
	}

	@Override
	public String toString() {
		return String.format("(%d, %s, %s, %s, %s, %s, %s, %s, %s)", this.passengerId, name, lastName, flightNumber==null?"NO-FLIGHT":flightNumber,address, phoneNumber, departureCity, destinationCity, departureDate);
	}
	
	public Passenger(String firstName, String lastName, String address, String phone, String destination,
			Date date, short type){
		this(firstName, lastName, address, phone, "", destination, date, type);
	}
	
	public Passenger(String firstName, String lastName, String address, String phone, String departureCity, String destinationCity,
			Date date, short type){
		this.name = firstName;
		this.lastName = lastName;
		this.address = address;
		this.phoneNumber = phone;
		this.destinationCity = destinationCity;
		this.departureCity = departureCity;
		this.departureDate = date;
		this.type = type;
		this.passengerId = Passenger.passengerIdCounter++;
	}

	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getDestination() {
		return destinationCity;
	}

	public void setDestination(String destination) {
		this.destinationCity = destination;
	}

	public Date getDeparture() {
		return departureDate;
	}

	public void setDeparture(Date departure) {
		this.departureDate = departure;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public int getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(int passengerId) {
		this.passengerId = passengerId;
	}
}
