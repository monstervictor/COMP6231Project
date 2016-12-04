package org.concordia.comp6231.server.model;

import java.io.Serializable;

public class Passenger implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private short type;
	private String name;
	private String lastName;
	private String address;
	private String phoneNumber;
	private String destinationCity;
	private int departureDate;
	private String departureCity;
	
	@Override
	public String toString() {
		return String.format("(%s, %s, %s, %s, %s, %s, %s)", name, lastName, address, phoneNumber, departureCity, destinationCity, departureDate);
	}
	
	public Passenger(String firstName, String lastName, String address, String phone, String departureCity, String destinationCity,
			int date, short type){
		this.name = firstName;
		this.lastName = lastName;
		this.address = address;
		this.phoneNumber = phone;
		this.destinationCity = destinationCity;
		this.departureCity = departureCity;
		this.departureDate = date;
		this.type = type;
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

	public int getDeparture() {
		return departureDate;
	}

	public void setDeparture(int departure) {
		this.departureDate = departure;
	}

}
