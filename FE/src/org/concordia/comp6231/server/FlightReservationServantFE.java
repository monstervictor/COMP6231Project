package org.concordia.comp6231.server;

import java.io.IOException;

import org.concordia.comp6231.server.model.Passenger;
import org.concordia.comp6231.server.model.SequencerCommunicator;
import org.concordia.comp6231.server.model.SequencerCommunicatorImpl;
import org.concordia.comp6231.util.Util;

import flightreservationfrontend.FlightReservationFrontEndPOA;

public class FlightReservationServantFE extends FlightReservationFrontEndPOA {
	
	private SequencerCommunicator sequencerCommunicator;
	private String city;
	
	public FlightReservationServantFE(String city) {
		this(city, "localhost", 1234);
	}
	
	public FlightReservationServantFE(String city, String host, int port) {
		this.sequencerCommunicator = new SequencerCommunicatorImpl(host, port);
		this.city = city;
	}

	@Override
	public String editFlightRecord(String adminUser, String flightInfo, short operationType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBookedFlightCount(short type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String transferReservation(short passengerId, String currentCity, String destinationCity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String bookFlight(String firstName, String lastName, String address, String phone, String destination,
			int date, short type) {
		Passenger passenger = new Passenger(firstName, lastName, address, phone, this.city, destination, date, type);
		byte[] data;
		try {
			data = Util.serialize(passenger);
			long sequencerId = sequencerCommunicator.feSend(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		return null;
	}

}
