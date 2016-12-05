package Servers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.*;
import java.util.*;

import org.omg.CORBA.ORB;

import Clients.CityEnum;
import Clients.SeatClassEnum;
import CorbaServers.ICounterServer;
import CorbaServers.ICounterServerHelper;
import CorbaServers.IFlightServer;
import CorbaServers.IFlightServerHelper;
import CorbaServers.IFlightServerPOA;
import infrastructure.CountOutputString;
import infrastructure.Logger;
import infrastructure.RWMonitor;
import models.Flight;
import models.PassangerTicket;

public class FlightServer extends IFlightServerPOA {
	private Logger _logger;
	private CityEnum _cityServerLocation = CityEnum.Invalid;
	private ICounterServer _counterServer;
	private RWMonitor _flightsAvailableMonitor;
	HashSet<Flight> _flightsAvailable;
	HashSet<PassangerTicket> _passangerTickets;
	private final SimpleDateFormat _dateFormat = new SimpleDateFormat("yyyyMMdd");

	private ORB _orb;

	private ORB getOrb() {
		if (_orb == null) {
			String[] args = new String[1];
			_orb = ORB.init(args, null);
		}
		return _orb;
	}

	public FlightServer() {
		_flightsAvailable = new HashSet<>();
		_passangerTickets = new HashSet<>();
		_flightsAvailableMonitor = new RWMonitor();
		_logger = new Logger(_cityServerLocation.ToString() + ".txt");

	}

	@Override
	public String getBookFlightCount(int rType) {
		_logger.SaveLog("getBookFlightCount called with " + rType);

		SeatClassEnum recordType = SeatClassEnum.ParseInt(rType);
		List<Thread> runningThreads = new ArrayList<>();
		CountOutputString output = new CountOutputString();
		_logger.SaveLog("Creating own seat calling thread");
		Thread ownCountThread = new Thread() {
			@Override
			public void run() {
				_flightsAvailableMonitor.requestReadAccess();
				int ownCount = (int) _passangerTickets.stream().filter(c -> c.getSeatClass() == recordType).count();
				_flightsAvailableMonitor.finishedReadAccess();
				output.addString(_cityServerLocation.ToShortString() + " " + ownCount);
			};
		};
		runningThreads.add(ownCountThread);

		if (_cityServerLocation != CityEnum.Montreal) {
			_logger.SaveLog("Creating Montreal request thread");
			Thread montrealThread = new Thread(new UDPSeatRequest(this, output, CityEnum.Montreal, recordType));
			runningThreads.add(montrealThread);
		}
		if (_cityServerLocation != CityEnum.NewDelhi) {
			_logger.SaveLog("Creating NewDelhi request thread");
			Thread newDelhiThread = new Thread(new UDPSeatRequest(this, output, CityEnum.NewDelhi, recordType));
			runningThreads.add(newDelhiThread);
		}
		if (_cityServerLocation != CityEnum.Washington) {
			_logger.SaveLog("Creating Washington request thread");
			Thread washingtonThread = new Thread(new UDPSeatRequest(this, output, CityEnum.Washington, recordType));
			runningThreads.add(washingtonThread);
		}
		_logger.SaveLog("Starting Threads");
		for (Thread thread : runningThreads) {
			thread.start();
		}
		Boolean _threadsRunning = true;

		_logger.SaveLog("Waiting for threads to finish");
		while (_threadsRunning) {
			_threadsRunning = false;
			for (Thread thread : runningThreads) {
				_threadsRunning = thread.isAlive();
				if (_threadsRunning)
					break;
			}
		}
		_logger.SaveLog("Threads finished, return message:" + output.toString());
		return output.getString();
	}

	@Override
	public void editFlightRecord(int rID, String fieldName, String newValue) {
		// TODO Auto-generated method stub
		Long recordID = Long.parseLong(String.valueOf(rID));
		Flight potentialRecord = null;
		if (recordID != 0)
			potentialRecord = _flightsAvailable.stream().filter(c -> c.getRecordID() == recordID).findFirst()
					.orElse(null);
		switch (fieldName) {
		case "datetime": {
			String[] newValueBrokenUp = ((String) newValue).split(" ");
			Date date;

			switch (newValueBrokenUp[0]) {
			case "insert":
				try {
					final Date superDate = _dateFormat.parse(newValueBrokenUp[1]);
					potentialRecord = _flightsAvailable.stream().filter(c -> c.getDepartureTime().equals(superDate))
							.findFirst().orElse(null);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (potentialRecord == null) {
					_flightsAvailableMonitor.requestWriteAccess();
					Flight newFlight = new Flight();
					try {
						date = _dateFormat.parse(newValueBrokenUp[1]);
						newFlight.setDepartureTime(date);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					newFlight.setDestiantion(CityEnum.parseShortString(newValueBrokenUp[2]));
					newFlight.setBusinessClassSeats(0);
					newFlight.setEconomyClassSeats(0);
					newFlight.setFirstClassSeats(0);
					newFlight.setRecordID(Integer.toUnsignedLong(_counterServer.getNext()));
					_flightsAvailable.add(newFlight);
					_flightsAvailableMonitor.finishedWriteAccess();
				}
				break;
			case "edit":
				if (potentialRecord != null) {
					_flightsAvailableMonitor.requestWriteAccess();
					try {
						date = _dateFormat.parse(newValueBrokenUp[1]);
						potentialRecord.setDepartureTime(date);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					_flightsAvailableMonitor.finishedWriteAccess();
				}
				break;
			case "delete":
				if (potentialRecord != null) {
					_flightsAvailableMonitor.requestWriteAccess();
					_flightsAvailable.remove(potentialRecord);
					DeleteAllTicketsFor(potentialRecord);
					_flightsAvailableMonitor.finishedWriteAccess();
				}
				break;
			default:
				break;
			}
			break;
		}
		case "bclass": {
			if (potentialRecord == null)
				return;
			_flightsAvailableMonitor.requestWriteAccess();
			Flight leRecord = potentialRecord;
			if (leRecord.getBusinessClassTickets().size() > Integer.parseInt(newValue)) {
				narrowNumberOfTickets(leRecord.getBusinessClassTickets(), Integer.parseInt(newValue));
			}
			leRecord.setBusinessClassSeats(Integer.parseInt(newValue));

			_flightsAvailableMonitor.finishedWriteAccess();
			break;
		}
		case "eclass": {
			if (potentialRecord == null)
				return;
			_flightsAvailableMonitor.requestWriteAccess();
			Flight leRecord = potentialRecord;
			if (leRecord.getEconomyClassTickets().size() > Integer.parseInt(newValue)) {
				narrowNumberOfTickets(leRecord.getEconomyClassTickets(), Integer.parseInt(newValue));
			}
			leRecord.setEconomyClassSeats(Integer.parseInt(newValue));
			_flightsAvailableMonitor.finishedWriteAccess();
			break;
		}
		case "fclass": {
			if (potentialRecord == null)
				return;
			_flightsAvailableMonitor.requestWriteAccess();
			Flight leRecord = potentialRecord;
			if (leRecord.getFirstClassTickets().size() > Integer.parseInt(newValue)) {
				narrowNumberOfTickets(leRecord.getFirstClassTickets(), Integer.parseInt(newValue));
			}
			leRecord.setFirstClassSeats(Integer.parseInt(newValue));
			_flightsAvailableMonitor.finishedWriteAccess();
			break;
		}
		}
	}

	private void narrowNumberOfTickets(List<PassangerTicket> tickets, int newValue) {
		// TODO Auto-generated method stub
		while (newValue < tickets.size()) {
			PassangerTicket toRemove = tickets.get(tickets.size() - 1);
			tickets.remove(toRemove);
			_passangerTickets.remove(toRemove);
		}
	}

	private void DeleteAllTicketsFor(Flight leFlight) {
		for (PassangerTicket ticket : leFlight.getBusinessClassTickets()) {
			_passangerTickets.remove(ticket);
		}
		for (PassangerTicket ticket : leFlight.getEconomyClassTickets()) {
			_passangerTickets.remove(ticket);
		}
		for (PassangerTicket ticket : leFlight.getFirstClassTickets()) {
			_passangerTickets.remove(ticket);
		}

	}

	public int bookFlight(String firstName, String lastName, String address, String phone, String destination, String d,
			int sClass, int uniqueID) {
		_logger.SaveLog("Attempting to insert record");
		PassangerTicket pTicket = new PassangerTicket();
		pTicket.setUniqueID(uniqueID);
		pTicket.setSeatClass(SeatClassEnum.ParseInt(sClass));
		pTicket.setFirstName(firstName);
		pTicket.setLastName(lastName);
		pTicket.setAddress(address);
		pTicket.setPhoneNumber(phone);
		pTicket.setDestination(CityEnum.parseShortString(destination));
		pTicket.TryParseDate(d);
		_flightsAvailableMonitor.requestReadAccess();
		Flight leFlight = _flightsAvailable.stream().filter(c -> c.getDepartureTime().equals(pTicket.getDateTime()))
				.findFirst().orElse(null);
		_flightsAvailableMonitor.finishedReadAccess();

		if (leFlight == null) {
			_logger.SaveLog("No flight found to allocate passanger");
			return 0;
		}
		if (leFlight.getSeats(pTicket.getSeatClass()) == leFlight.getTickets(pTicket.getSeatClass()).size()) {
			_logger.SaveLog(pTicket.getSeatClass().toString() + " are full in the flight to transfer");
			return 0;
		}

		else {
			_logger.SaveLog("Transfer accepted, transfering ticket");
			_flightsAvailableMonitor.requestWriteAccess();
			leFlight.getTickets(pTicket.getSeatClass()).add(pTicket);
			_passangerTickets.add(pTicket);
			_flightsAvailableMonitor.finishedWriteAccess();
			_logger.SaveLog("Transfer succesful");
		}
		return pTicket.getUniqueID();
	}

	@Override
	public int bookFlight(String firstName, String lastName, String address, String phone, String destination, String d,
			int sClass) {
		int ret = -1;
		Date date = ParseDate(d);
		SeatClassEnum seatClass = SeatClassEnum.ParseInt(sClass);
		if (_flightsAvailable.size() > 0) {
			Flight leFlight = _flightsAvailable.stream().filter(c -> c.getDepartureTime().equals(date)).findFirst()
					.orElse(null);
			if (leFlight != null && leFlight.getAvailableSeats(seatClass) > 0) {
				Flight toBookFor = leFlight;
				RWMonitor monitor = toBookFor.getFlightMonitor();
				if (toBookFor.AreSeatsAvailable(seatClass)) {
					monitor.requestWriteAccess();
					if (toBookFor.AreSeatsAvailable(seatClass)) {
						PassangerTicket ticket = new PassangerTicket();
						ticket.setFirstName(firstName);
						ticket.setLastName(lastName);
						ticket.setAddress(address);
						ticket.setPhoneNumber(phone);
						ticket.setDestination(CityEnum.valueOf(destination));
						ticket.setDateTime(date);
						ticket.setSeatClass(seatClass);
						ticket.setUniqueID(_counterServer.getNext());
						ret = ticket.getUniqueID();
						toBookFor.getTickets(seatClass).add(ticket);
						_passangerTickets.add(ticket);
					}
					monitor.finishedWriteAccess();
				}
			}
		}
		return ret;
	}

	private void exportServer(RWMonitor serverListMonitor) {
		// LookForCounterServer
		List<String> servers = new ArrayList<>();
		BufferedReader br;
		serverListMonitor.requestWriteAccess();
		try {
			br = new BufferedReader(new FileReader("servers.txt"));
			String currentLine;
			while ((currentLine = br.readLine()) != null) {
				servers.add(currentLine);
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		serverListMonitor.finishedWriteAccess();
		String counterServString = servers.get(0);
		org.omg.CORBA.Object counterServRef = getOrb().string_to_object(counterServString);
		_counterServer = ICounterServerHelper.narrow(counterServRef);

		Thread UDPServer = new Thread(new UDPSeatsServer(this), _cityServerLocation.ToShortString() + " SeatServer");
		UDPServer.start();
		_logger.SaveLog("Initialized UDPServer Seats");
		Thread UDPServerTransfer = new Thread(new UDPTransferTicketServer(this),
				_cityServerLocation.ToShortString() + "TransferTicketServer");
		UDPServerTransfer.start();
		_logger.SaveLog("Initialized UDPTransferServer");

	}

	public CityEnum getCityServerLocation() {
		return _cityServerLocation;
	}

	public RWMonitor getFlightsAvailableMonitor() {
		return _flightsAvailableMonitor;
	}

	public HashSet<Flight> getFlightsAvailable() {
		return _flightsAvailable;
	}

	public HashSet<PassangerTicket> getPassangerTickets() {
		return _passangerTickets;
	}

	public Date ParseDate(String input) {
		Boolean ret = true;
		Date date;
		try {
			date = _dateFormat.parse(input);
			return date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean transferReservation(int passangerID, int currentCity, int otherCity) {

		// TODO Auto-generated method stub
		boolean ret = false;
		PassangerTicket leTicket = _passangerTickets.stream().filter(c -> c.getUniqueID() == passangerID).findFirst()
				.orElse(null);
		if (leTicket != null) {
			if (!CityEnum.parseFromInt(currentCity).equals(_cityServerLocation)) {
				// ForwardRequest
				BufferedReader br;
				try {
					br = new BufferedReader(new FileReader("servers.txt"));
					List<String> servers = new ArrayList<>();
					String currentLine;
					while ((currentLine = br.readLine()) != null) {
						servers.add(currentLine);
					}
					br.close();
					String montrealRef = servers.get(1);
					String newDelhiRef = servers.get(2);
					String washingtonRef = servers.get(3);

					org.omg.CORBA.Object objRef = null;
					if (CityEnum.Montreal.ToInt() == currentCity)
						objRef = getOrb().string_to_object(montrealRef);
					if (CityEnum.NewDelhi.ToInt() == currentCity)
						objRef = getOrb().string_to_object(newDelhiRef);
					if (CityEnum.Washington.ToInt() == currentCity)
						objRef = getOrb().string_to_object(washingtonRef);
					IFlightServer properServer = IFlightServerHelper.narrow(objRef);
					return properServer.transferReservation(passangerID, currentCity, otherCity);

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				// I have to handle the request
				UDPTransferTicketRequest runnableRequest = new UDPTransferTicketRequest(this,
						CityEnum.parseFromInt(otherCity), leTicket);
				Thread toRun = new Thread(runnableRequest);
				toRun.start();
				while (toRun.isAlive()) {
					Thread.yield();
				}
				if (runnableRequest.getWasRequestSuccessful() && runnableRequest.getNewUniqueID() > 0) {
					_flightsAvailableMonitor.requestWriteAccess();
					_passangerTickets.remove(leTicket);
					Flight toRemoveFrom = _flightsAvailable.stream()
							.filter(c -> c.getTickets(leTicket.getSeatClass()).contains(leTicket)).findFirst()
							.orElse(null);
					toRemoveFrom.getTickets(leTicket.getSeatClass()).remove(leTicket);
					_flightsAvailableMonitor.finishedWriteAccess();
					ret = true;
				}
			}
		}
		return ret;

	}

	public void setCityServerLocation(CityEnum newValue, RWMonitor serverListMonitor) {
		_cityServerLocation = newValue;
		_logger = new Logger(_cityServerLocation.ToString() + ".txt");
		exportServer(serverListMonitor);
	}

	public Logger getLogger() {
		return _logger;
	}
}
