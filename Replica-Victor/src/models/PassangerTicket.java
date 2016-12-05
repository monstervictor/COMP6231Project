package models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import Clients.CityEnum;
import Clients.SeatClassEnum;

public class PassangerTicket {
	private static final SimpleDateFormat _dateFormat = new SimpleDateFormat("yyyyMMdd");

	private int uniqueID;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String address;
	private CityEnum destination;
	private Date dateTime;
	private SeatClassEnum seatClass;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public CityEnum getDestination() {
		return destination;
	}

	public void setDestination(CityEnum destination) {
		this.destination = destination;
	}

	public Date getDateTime() {
		return dateTime;
	}
	public String getDateTimeString(){
		return _dateFormat.format(dateTime);
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public SeatClassEnum getSeatClass() {
		return seatClass;
	}

	public void setSeatClass(SeatClassEnum seatClass) {
		this.seatClass = seatClass;
	}

	public int getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(int uniqueID) {
		this.uniqueID = uniqueID;
	}

	public boolean TryParseDate(String input) {
		Boolean ret = true; 
		Date date;
		try {
			date = _dateFormat.parse(input);
			setDateTime(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret = false;
		}
		return ret;
	}

}
