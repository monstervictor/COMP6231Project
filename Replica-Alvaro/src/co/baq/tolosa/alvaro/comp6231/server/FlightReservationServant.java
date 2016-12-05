package co.baq.tolosa.alvaro.comp6231.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import co.baq.tolosa.alvaro.comp6231.server.model.Flight;
import co.baq.tolosa.alvaro.comp6231.server.model.Message;
import co.baq.tolosa.alvaro.comp6231.server.model.Passenger;
import co.baq.tolosa.alvaro.comp6231.server.model.RecordSummary;
import co.baq.tolosa.alvaro.comp6231.server.model.Status;
import co.baq.tolosa.alvaro.comp6231.util.LogWriter;
import flightreservation.FlightReservationSystemPOA;

public class FlightReservationServant extends FlightReservationSystemPOA {
	public static final short CREATE_FLIGHT = 1;
	public static final short DELETE_FLIGHT = 2;
	public static final short UPDATE_FLIGHT = 3;

	private String city; // city where the servant runs

	private Map<Character, List<Passenger>> passengers;
	private Map<Character, ReadWriteLock> passengerLocks;

	private Map<Character, List<Flight>> flights;
	private Map<Character, ReadWriteLock> flightLocks;

	private Map<String, String> cityByFlightNumber;
	
	private Map<Integer, String> passengerByPassengerId;
	
	private LogWriter logWriter;

	public FlightReservationServant(String city) {
		System.out.println(String.format("Server for %s is running", city));
		this.passengers = new HashMap<>();
		this.flights = new HashMap<>();
		this.passengerLocks = new HashMap<>();
		this.flightLocks = new HashMap<>();
		this.cityByFlightNumber = new HashMap<>();
		
		this.passengerByPassengerId = new HashMap<>();
		
		this.city = city;

		for (char index = 'a'; index <= 'z'; index++) {
			this.passengers.put(index, new ArrayList<>());
			this.passengerLocks.put(index, new ReentrantReadWriteLock());

			this.flights.put(index, new ArrayList<>());
			this.flightLocks.put(index, new ReentrantReadWriteLock());
		}
		logWriter = new LogWriter(city);
		//crate the UDP server.
		createUDPServer();
	}

	private char getKeyFromString(String keyData) {
		return Character.toLowerCase(keyData.trim().charAt(0));
	}

	private Status deleteAFlight(String flightNumber, Character destinationKey) {
		Status answer = new Status();
		List<Flight> auxFlights = this.flights.get(destinationKey);
		Optional<Flight> wantedFlight = auxFlights.stream()
				.filter(flight -> flight.getFlightNumber().equals(flightNumber)).findFirst();

		if (wantedFlight.isPresent()) {
			auxFlights.remove(wantedFlight.get());
			answer.setErrorCode(Status.NO_ERROR);
			answer.setMessage(String.format("Flight %s deleted successsfuly", wantedFlight.get()));
		} else {
			answer.setErrorCode(Status.NO_FLIGHT_FOUND);
			answer.setMessage(String.format("No Flight with number %s was found", flightNumber));
		}
		
		return answer;
	}

	@Override
	public String editFlightRecord(String adminUser, String flightInfo, short operationType) {
		Status answer = new Status();
		String destinationCity = null;
		char destinationKey;
		Lock writeLock = null;

		switch (operationType) {
		case CREATE_FLIGHT:
			Flight remoteFlight = Flight.fromString(flightInfo);
			destinationKey = this.getKeyFromString(remoteFlight.getArrivalCity());
			writeLock = this.flightLocks.get(destinationKey).writeLock();
			try {
				writeLock.lock();
				List<Flight> auxFlights = this.flights.get(destinationKey);
				remoteFlight.generateFlightNumber();
				auxFlights.add(remoteFlight);
			} finally {
				writeLock.unlock();
			}
			answer.setErrorCode(Status.NO_ERROR);
			answer.setMessage(String.format("Flight %s from %s was inserted successfuly", remoteFlight, this.city));
			logWriter.write(adminUser, answer);
			//System.out.println(adminUser);
			System.out.println(answer);
			cityByFlightNumber.put(remoteFlight.getFlightNumber(), remoteFlight.getArrivalCity());
			break;
		case DELETE_FLIGHT:
			destinationCity = cityByFlightNumber.get(flightInfo);
			destinationKey = this.getKeyFromString(destinationCity);
			writeLock = this.flightLocks.get(destinationKey).writeLock();
			try {
				writeLock.lock();
				answer = deleteAFlight(flightInfo, destinationKey);
			} finally {
				writeLock.unlock();
			}
			logWriter.write(adminUser, answer);
			//System.out.println(adminUser);
			System.out.println(answer);
			break;
		case UPDATE_FLIGHT:
			remoteFlight = Flight.fromString(flightInfo);
			destinationCity = cityByFlightNumber.get(remoteFlight.getFlightNumber());
			destinationKey = this.getKeyFromString(destinationCity);
			writeLock = this.flightLocks.get(destinationKey).writeLock();
			try {
				writeLock.lock();
				Optional<Flight> optFlight = this.flights.get(destinationKey).stream()
						.filter(predicate -> predicate.getFlightNumber().equals(remoteFlight.getFlightNumber()))
						.findFirst();
				if (optFlight.isPresent()) {
					optFlight.get().merge(remoteFlight);
					answer.setErrorCode(Status.NO_ERROR);
					answer.setMessage(String.format("Flight %s updated successsfuly", optFlight.get()));
				} else {
					answer.setErrorCode(Status.NO_FLIGHT_FOUND);
					answer.setMessage(
							String.format("No Flight with number %s was found", remoteFlight.getFlightNumber()));
				}
			} finally {
				writeLock.unlock();
			}
			logWriter.write(adminUser, answer);
			//System.out.println(adminUser);
			System.out.println(answer);
			break;
		}
		return answer.toString();
	}

	@Override
	public String getBookedFlightCount(short classType) {
		List<RecordSummary> answer = new ArrayList<>();
		
		int remotePort1 = -1;
		int remotePort2 = -1;
		if(this.city.equals("mtl")) {
			remotePort1 = this.getPort("ndl");
			remotePort2 = this.getPort("wst");
		} else if (this.city.equals("ndl")) {
			remotePort1 = this.getPort("mtl");
			remotePort2 = this.getPort("wst");
		} else {
			remotePort1 = this.getPort("mtl");
			remotePort2 = this.getPort("ndl");
		}
		
		Callable<RecordSummary> task1 = createTask(classType, remotePort1);
		Callable<RecordSummary> task2 = createTask(classType, remotePort2);
		ExecutorService service = Executors.newFixedThreadPool(2);
		Future<RecordSummary> f1 = service.submit(task1);
		Future<RecordSummary> f2 = service.submit(task2);
		
		int localResponse = countAllSeatsByClass(classType);
		RecordSummary rs = new RecordSummary();
		rs.setCity(this.city);
		rs.setTotalRecords(localResponse);
		answer.add(rs);
		
		try {
			rs  = f1.get(2000, TimeUnit.MILLISECONDS);
			answer.add(rs);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			rs = f2.get(2000, TimeUnit.MILLISECONDS);
			answer.add(rs);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StringBuilder response = new StringBuilder();
		answer.forEach(action->response.append(action.getCity()+"|"+action.getTotalRecords()));
		return response.toString();
	}

	@Override
	public String transferReservation(short passId, String currentCity, String destinationCity) {
		Integer passengerId = new Integer(passId);
		Status answer = new Status();
		/*
		System.out.println("PRINTING KEYS");
		for(Integer i:this.passengerByPassengerId.keySet()) {
			System.out.println("key="+i+"; value="+this.passengerByPassengerId.get(i));
		}
		*/
		if(!this.passengerByPassengerId.containsKey(passengerId)) {
			answer.setErrorCode(Status.PASSENGER_NOT_FOUND);
			answer.setMessage(String.format("Passenger with id %d was not found", passengerId));
		} else {
			String lastName = this.passengerByPassengerId.get(passengerId);
			char passengerKey = this.getKeyFromString(lastName);
			int remotePort = this.getPort(destinationCity);
			Lock lock = this.passengerLocks.get(passengerKey).writeLock();
			
			boolean remoteInsertSuccess = false;
			Passenger actualP = null;
			
			if (this.passengers.containsKey(passengerKey)) {
				Optional<Passenger> p = this.passengers.get(passengerKey).stream().filter(passenger->passenger.getPassengerId()==passengerId).findFirst();
				if (p.isPresent()) {
					actualP = p.get();
					Callable<Message> callMsg = createTaskTransferReservation(actualP, remotePort);
					try {
						ExecutorService service = Executors.newFixedThreadPool(1);
						Future<Message> f1 = service.submit(callMsg);
						Message msg = f1.get(2, TimeUnit.SECONDS);
						remoteInsertSuccess = msg.getResponse() == Message.RESPONSE_UNUSED;
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
				if(remoteInsertSuccess) {
					try {
						lock.lock();
						List<Passenger> b = this.passengers.get(passengerKey).stream().filter(passenger->passenger.getPassengerId() != passengerId).collect(Collectors.toList());
						if (b ==null) b = new ArrayList<>();
						this.passengers.put(passengerKey, b);
						this.passengerByPassengerId.remove(actualP.getPassengerId());
						
					} finally {
						lock.unlock();
					}
					char flightKey = this.getKeyFromString(actualP.getDestination());
					lock = this.flightLocks.get(flightKey).writeLock();
					try {
						lock.lock();
						final Passenger aux = actualP;
						if(this.flights.containsKey(flightKey)) {
							Optional<Flight> fAux = this.flights.get(flightKey).stream().filter(predicate->predicate.getFlightNumber() == aux.getFlightNumber()).findFirst();
							if(fAux.isPresent()) {
								Flight fReal = fAux.get();
								fReal.reduceNumberOfSeatsAvailables(actualP.getType(), -1);
							}
						}
						actualP.getType();
					} finally {
						lock.unlock();
					}
				}
			}
		}
		logWriter.write("none|none", answer);
		return answer.toString();
	}

	@Override
	public String bookFlight(String firstName, String lastName, String address, String phone, String destination,
			int date, short type) {
		Status answer = new Status();

		Date d = new Date(date*1000);
		d.setHours(0);
		d.setMinutes(0);

		Passenger passenger = new Passenger(firstName, lastName, address, phone, this.city, destination, d, type);
		boolean flightModified = false;

		char destinationKey = this.getKeyFromString(destination);

		Lock writeLock = this.flightLocks.get(destinationKey).writeLock();

		try {
			writeLock.lock();

			answer = bookFlight(passenger, type, d, destinationKey);
			if (answer.getErrorCode() == Status.NO_ERROR)
				flightModified = true;

		} finally {
			writeLock.unlock();
		}

		// now, if we could modify the flight, we need to insert the
		// passeneger to the list
		if (flightModified) {
			Character passengerKey = this.getKeyFromString(passenger.getLastName());
			writeLock = this.passengerLocks.get(passengerKey).writeLock();
			try {
				writeLock.lock();
				this.passengers.get(passengerKey).add(passenger);
				int idP = passenger.getPassengerId();
				passengerByPassengerId.put(idP, passenger.getLastName());
				answer.setErrorCode(Status.NO_ERROR);
				answer.setMessage(String.format("passenger %s successfully added", passenger));
			} finally {
				writeLock.unlock();
			}
		}

		logWriter.write(answer);
		System.out.println(answer);
		return answer.toString();
	}

	private Status bookFlight(Passenger passenger, short classType, Date departureDate, char key) {
		List<Flight> fs = this.flights.get(key);
		Status answer = new Status();

		List<Flight> flightsAfterDate = fs.stream().filter(flight -> flight.getDepartureDate().after(departureDate))
				.collect(Collectors.toList());

		if (flightsAfterDate == null || flightsAfterDate.isEmpty()) {
			answer.setErrorCode(Status.NO_FLIGHT_FOUND);
			answer.setMessage(String.format("No flights for the specify date %s", departureDate));
		} else {
			Optional<Flight> chosen = flightsAfterDate
					.stream()
					.filter(flight->flight.freeSeats(classType)>0)
					.sorted((f1, f2) -> (int) (f1.getDepartureDate().getTime() - f2.getDepartureDate().getTime()))
					.findFirst();

			if (chosen.isPresent()) {
				Flight actual = chosen.get();
				if (actual.reduceNumberOfSeatsAvailables(classType, 1) < 0) {
					answer.setErrorCode(Status.NO_ENOUGH_SEATS);
					answer.setMessage(String.format("Not enought seats available of type %s in the flight %s",
							classType, actual.getFlightNumber()));
				} else {
					passenger.setFlightNumber(chosen.get().getFlightNumber());
				}
			} else {
				answer.setErrorCode(Status.NO_FLIGHT_FOUND);
				answer.setMessage(String.format("No flight has enough seats on the class %s", classType));
			}
		}
		// logWriter.write(answer);
		//System.out.println(answer);

		return answer;
	}

	private void createUDPServer() {
		final int localPort = getPort(this.city);
		
		Runnable r = new Runnable() {

			@Override
			public void run() {
				DatagramSocket socket = null;
				try {
					System.out.println("PORT:"+localPort + "; city:"+city);
					socket = new DatagramSocket(localPort);
					byte[] inputData = new byte[4096];
		            byte[] outputData = new byte[4096];
		            
					while(true) {
						DatagramPacket inputPacket = new DatagramPacket(inputData, inputData.length);
						socket.receive(inputPacket);
						Object o = deserialize(inputPacket.getData());
						
						if (o instanceof Message) {
							Message m = (Message) o;
							if(m.getType() == Message.TYPE_REQUEST_NUMBER_OF_FREE_SEATS_BY_CLASS) {
								int response = countAllSeatsByClass(m.getClassType());
								InetAddress remoteIp = inputPacket.getAddress();
				                int remotePort = inputPacket.getPort();
				                
				                Message responseMsg = new  Message(city, m.getClassType(), response);
				                outputData = serialize(responseMsg);
				                
				                DatagramPacket outputPacket = new DatagramPacket(outputData, outputData.length, remoteIp, remotePort);
				                
				                socket.send(outputPacket);
							}
						} else if(o instanceof Passenger) {
							Passenger p = (Passenger)o;
							p.setDepartureCity(city);
							char destinationKey = getKeyFromString(p.getDestination());
							Status s = bookFlight(p, p.getType(), p.getDeparture(), destinationKey);
							Message responseMsg = null;
							if(s.getErrorCode() == Status.NO_ERROR) responseMsg = new Message(city, Message.TYPE_RESPONSE_TRANSFER_RESERVATION, p.getType(), Message.RESPONSE_UNUSED);
							else responseMsg = new Message(city, Message.TYPE_RESPONSE_TRANSFER_RESERVATION, p.getType(), Message.RESPONSE_ERROR_FLIGHT_NOT_FOUND);
							InetAddress remoteIp = inputPacket.getAddress();
			                int remotePort = inputPacket.getPort();
							
			                outputData = serialize(responseMsg);
			                
			                DatagramPacket outputPacket = new DatagramPacket(outputData, outputData.length, remoteIp, remotePort);
			                logWriter.write(s);
			                System.out.println(s);
			                
							socket.send(outputPacket);
						}
					}
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (socket != null)	socket.close();
				}
			}
			
		};
		
		Thread t = new Thread(r);
		t.setDaemon(true);
		t.start();
	}
	
	private int countAllSeatsByClass(int classType){
		int answer = 0;
		for(Character key:flights.keySet()) {
			try {
				flightLocks.get(key).readLock().lock();

				answer += flights.get(key)
						.stream()
						.mapToInt(f -> {
							int response = 0;
							switch (classType) {
							case Flight.BUSINESS_CLASS:
								response = f.getBusinessClassSoldSeats();
								break;
							case Flight.ECONOMIC_CLASS:
								response = f.getEconomicClassSoldSeats(); //f.getNumberOfBusinessClassSeatsSold();
								break;
							case Flight.FIRST_CLASS:
								response = f.getFirstClassSoldSeats(); //f.getNumberOfBusinessClassSeatsSold();
								break;
							}
							return response;
						})
						.sum();
			} finally {
				flightLocks.get(key).readLock().unlock();
			}
		}
		
		return answer;
	}
	
	private int getPort(String city) {
		int answer = 10000;
		switch(city) {
		case "mtl": answer = 50001; break;
		case "wst": answer = 50002; break;
		case "ndl": answer = 50003; break;
		}
		return answer;
	}
	
	private Callable<Message> createTaskTransferReservation(Passenger passanger, int remotePort) {
		Callable<Message> task = new Callable<Message> () {
			@Override
			public Message call() throws Exception {
				Message msg = null;
				DatagramSocket socket = null;
				byte[] receiveData = new byte[4096];
				try {
					socket = new DatagramSocket();
					InetAddress adress = InetAddress.getLocalHost();
					byte[] data = serialize(passanger);
					DatagramPacket packet = new DatagramPacket(data, data.length, adress, remotePort);
					socket.send(packet);
					
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					socket.receive(receivePacket);
					
					Object o = deserialize(receivePacket.getData());
					if (o instanceof Message) {
						msg = (Message)o;
					}
				} finally {
					if(socket !=null) socket.close();
				}
				return msg;
			}
			
		};
		return task;
	}
	
	private Callable<RecordSummary> createTask(short classType, int remotePort) {
		Callable<RecordSummary> task = new Callable<RecordSummary> () {
			@Override
			public RecordSummary call() throws Exception {
				RecordSummary rs = new RecordSummary();
				DatagramSocket socket = null;
				byte[] receiveData = new byte[4096];
				try {
					socket = new DatagramSocket();
					InetAddress adress = InetAddress.getLocalHost();
					Message msg = new Message(city, classType);
					byte[] data = serialize(msg);
					DatagramPacket packet = new DatagramPacket(data, data.length, adress, remotePort);
					socket.send(packet);
					
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					socket.receive(receivePacket);
					
					Object o = deserialize(receivePacket.getData());
					if (o instanceof Message) {
						Message m = (Message)o;
						m.getCityOrigin();
						rs.setCity(m.getCityOrigin());
						rs.setTotalRecords(m.getResponse());
					}
				} finally {
					if(socket !=null) socket.close();
				}
				return rs;
			}
			
		};
		return task;
	}
	
	private static byte[] serialize(Object obj) throws IOException {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream os = new ObjectOutputStream(out);
	    os.writeObject(obj);
	    return out.toByteArray();
	}
	
	private static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
	    ByteArrayInputStream in = new ByteArrayInputStream(data);
	    ObjectInputStream is = new ObjectInputStream(in);
	    return is.readObject();
	}
}

/*
.filter(flight -> (classType == Flight.ECONOMIC_CLASS
		&& flight.getEconomicClassTotalSeats() - flight.getEconomicClassSoldSeats() > 0)
		|| (classType == Flight.BUSINESS_CLASS
				&& flight.getBusinessClassTotalSeats() - flight.getBusinessClassSoldSeats() > 0)
		|| (classType == Flight.FIRST_CLASS
				&& flight.getFirstClassTotalSeats() - flight.getFirstClassSoldSeats() > 0))
*/
