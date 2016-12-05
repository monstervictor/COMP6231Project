package models;

import java.util.*;

import Clients.CityEnum;
import Clients.SeatClassEnum;
import infrastructure.RWMonitor;

public class Flight {
	
	private static Flight empty = new Flight();
	public static Flight getEmpty(){return empty;}

	private Long recordID;
	private RWMonitor flightMonitor;
	private int firstClassSeats;
	private int businessClassSeats;
	private int economyClassSeats;
	private Date departureTime;
	private List<PassangerTicket> firstClassTickets;
	private List<PassangerTicket> businessClassTickets;
	private List<PassangerTicket> economyClassTickets;
	private CityEnum destiantion;

	public Flight() {
		flightMonitor = new RWMonitor();
		firstClassTickets = new ArrayList<PassangerTicket>();
		businessClassTickets = new ArrayList<PassangerTicket>();
		economyClassTickets = new ArrayList<PassangerTicket>();
	}

	public int getFirstClassSeats() {
		return firstClassSeats;
	}

	public void setFirstClassSeats(int firstClassSeats) {
		this.firstClassSeats = firstClassSeats;
	}

	public int getEconomyClassSeats() {
		return economyClassSeats;
	}

	public void setEconomyClassSeats(int economyClassSeats) {
		this.economyClassSeats = economyClassSeats;
	}

	public int getBusinessClassSeats() {
		return businessClassSeats;
	}

	public void setBusinessClassSeats(int businessClassSeats) {
		this.businessClassSeats = businessClassSeats;
	}

	public Date getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(Date departureTime) {
		this.departureTime = departureTime;
	}

	public List<PassangerTicket> getFirstClassTickets() {
		return firstClassTickets;
	}

	public void setFirstClassTickets(List<PassangerTicket> firstClassTickets) {
		this.firstClassTickets = firstClassTickets;
	}

	public List<PassangerTicket> getBusinessClassTickets() {
		return businessClassTickets;
	}

	public void setBusinessClassTickets(List<PassangerTicket> businessClassTickets) {
		this.businessClassTickets = businessClassTickets;
	}

	public List<PassangerTicket> getEconomyClassTickets() {
		return economyClassTickets;
	}

	public void setEconomyClassTickets(List<PassangerTicket> economyClassTickets) {
		this.economyClassTickets = economyClassTickets;
	}

	public RWMonitor getFlightMonitor() {
		return flightMonitor;
	}

	public List<PassangerTicket> getTickets(SeatClassEnum classEnum) {
		switch (classEnum) {
		case BusinessClass:
			return getBusinessClassTickets();
		case EconomyClass:
			return getEconomyClassTickets();
		case FirstClass:
			return getFirstClassTickets();
		default:
			return null;
		}
	}

	public int getSeats(SeatClassEnum seatClass) {
		switch (seatClass) {
		case BusinessClass:
			return getBusinessClassSeats();
		case EconomyClass:
			return getEconomyClassSeats();
		case FirstClass:
			return getFirstClassSeats();
		default:
			return -1;
		}
	}

	public Boolean AreSeatsAvailable(SeatClassEnum seatClass) {
		return getSeats(seatClass) > getTickets(seatClass).size();
	}

	public Long getRecordID() {
		return recordID;
	}

	public void setRecordID(Long recordID) {
		this.recordID = recordID;
	}
	
	public List<PassangerTicket> getTickets(){
		List<PassangerTicket> newList = new ArrayList<>(getBusinessClassSeats());
		newList.addAll(getEconomyClassTickets());
		newList.addAll(getFirstClassTickets());
		return newList;
	}

	public int getAvailableSeats(SeatClassEnum valueOf) {
		int bookedSeats = getTickets(valueOf).size();
		int availableSeats = getSeats(valueOf);
		return availableSeats - bookedSeats;
	}

	public CityEnum getDestiantion() {
		return destiantion;
	}

	public void setDestiantion(CityEnum destiantion) {
		this.destiantion = destiantion;
	}

}
