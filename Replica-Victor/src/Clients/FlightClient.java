package Clients;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.omg.CORBA.ORB;

import CorbaServers.ICounterServer;
import CorbaServers.ICounterServerHelper;
import CorbaServers.IFlightServer;
import CorbaServers.IFlightServerHelper;
import infrastructure.Logger;
import models.PassangerTicket;

public class FlightClient {

	public FlightClient() {
		_logger = new Logger("flightClient.txt");
		_logger.SaveLog("FlightClient Initialized");
	}

	private Logger _logger;

	private static ORB _orb;

	private static String montrealRef;
	private static String newDelhiRef;
	private static String washingtonRef;

	Scanner _keyboard = new Scanner(System.in);
	Boolean _resetMenu = false;

	PassangerTicket _issuingTicket;
	private IFlightServer _leServer;
	private static CityEnum _leServerLocation;

	SimpleDateFormat _dateFormat = new SimpleDateFormat("yyyyMMdd");

	private void showReturnToStart() {
		System.out.println("0. Cancel Operation");
	}

	private static CityEnum showServerSelection() {
		System.out.println("Please select a public server to connect:");
		System.out.println("1. Montreal");
		System.out.println("2. Washington");
		System.out.println("3. New Delhi");
		Scanner keyboard = new Scanner(System.in);
		Boolean valid = false;
		int userChoice = -1;
		// Enforces a valid integer input.
		while (!valid) {
			try {
				userChoice = keyboard.nextInt();
				valid = userChoice > 0 && userChoice < 4;
			} catch (Exception e) {
				System.out.println("Invalid Input, please enter an Integer");
				valid = false;
				keyboard.nextLine();
			}
		}
		switch (userChoice) {
		case 1:
			return CityEnum.Montreal;
		case 2:
			return CityEnum.Washington;
		case 3:
			return CityEnum.NewDelhi;
		default:
			return CityEnum.Invalid;
		}
	}

	private void showPersonStatusSelection() {
		System.out.println("Please identify yourself as:");
		System.out.println("1. Manager");
		System.out.println("2. Passanger (and wants to book a ticket)");
	}

	private void askPassangerName() {
		System.out.println("Please enter your First name");
	}

	private void askPassangerLastName() {
		System.out.println("Please enter your Last name");
	}

	private void askPassangerAddress() {
		System.out.println("Please enter your Address");
	}

	private void askPassangerPhone() {
		System.out.println("Please enter your phone number");
	}

	private void askPassangerDestination() {
		System.out.println("Please select your destination");
		System.out.println("1. Montreal");
		System.out.println("2. Washington");
		System.out.println("3. New Delhi");
	}

	private void askPassangerDay() {
		System.out.println("What day would you like to travel? (2-digit)");
	}

	private void askPassangerMonth() {
		System.out.println("What month would you like to travel? (2-digit)");
	}

	private void askPassangeryear() {
		System.out.println("What year would you like to travel? (4-digit)");
	}

	private SeatClassEnum askPassangerClass() {
		System.out.println("Please select class");
		SeatClassEnum seatEnum = SeatClassEnum.Unavailable;
		while (seatEnum == SeatClassEnum.Unavailable && !_resetMenu) {
			System.out.println("1. First Class");
			System.out.println("2. Business Class");
			System.out.println("3. Economy Class");
			int result = WaitForInt();
			if (result == 0)
				_resetMenu = true;
			else if (SeatClassEnum.IsIntParsable(result)) {
				seatEnum = SeatClassEnum.ParseInt(result);
				if (seatEnum == SeatClassEnum.Unavailable)
					System.out.println("Please select a valid class");
			}
		}
		return seatEnum;
	}

	private void askManagerID() {
		System.out.println("Please enter your Manager ID");
	}

	private void askManagerOptions() {
		System.out.println("Please select within the following options:");
		System.out.println("1. Retrieve total number of passanger tickets for a class");
		System.out.println("2. Create a new flight");
		System.out.println("3. Delete a flight");
		System.out.println("4. Edit an existing flight");
		System.out.println("5. Transfer a passanger ticket");
	}

	private int WaitForInt() {
		// Taken from Scrambler Tutorial Sample
		Boolean valid = false;
		int userChoice = -1;
		// Enforces a valid integer input.
		while (!valid) {
			try {
				userChoice = _keyboard.nextInt();
				valid = true;
			} catch (Exception e) {
				System.out.println("Invalid Input, please enter an Integer");
				valid = false;
				_keyboard.nextLine();
			}
		}
		return userChoice;
	}

	private String WaitForString() {
		String ret = _keyboard.next();
		_logger.SaveLog("User selected: " + ret);
		return ret;
	}

	public static void main(String args[]) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader("servers.txt"));
		List<String> servers = new ArrayList<>();
		String currentLine;
		while ((currentLine = br.readLine()) != null) {
			servers.add(currentLine);
		}
		br.close();
		montrealRef = servers.get(1);
		newDelhiRef = servers.get(2);
		washingtonRef = servers.get(3);

		_leServerLocation = showServerSelection();

		FlightClient leClient = new FlightClient();

		_orb = ORB.init(args, null);

		IFlightServer server = requestFlightServer(_leServerLocation);

		leClient.setServer(server);
		leClient.startMenuLoop();

	}

	private void setServer(IFlightServer server) {
		// TODO Auto-generated method stub
		_leServer = server;

	}

	private void startMenuLoop() {
		// TODO Auto-generated method stub
		_issuingTicket = new PassangerTicket();
		_logger.SaveLog("Starting main menu loop");
		while (true) {
			String input;
			Boolean valid = true;
			int personStatus;
			do {
				showPersonStatusSelection();
				personStatus = WaitForInt(); // 1: Manager, 2: Passanger

				if (personStatus == 2) {
					_logger.SaveLog("User selected Passanger menu");
					do {
						askPassangeryear();
						input = WaitForString();
						askPassangerMonth();
						input += WaitForString();
						askPassangerDay();
						input += WaitForString();
						_logger.SaveLog("User tried to input date " + input);
					} while (!_issuingTicket.TryParseDate(input));
					try {
						_issuingTicket.setDateTime(_dateFormat.parse(input));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					askPassangerDestination();
					_logger.SaveLog("Asking user destination");
					showReturnToStart();
					int destination = WaitForInt();
					if (destination == 1) {
						_issuingTicket.setDestination(CityEnum.Montreal);
					} else if (destination == 2) {
						_issuingTicket.setDestination(CityEnum.Washington);
					} else if (destination == 3) {
						_issuingTicket.setDestination(CityEnum.NewDelhi);
					} else {
						_logger.SaveLog("User selected to go back to the start");
						break;
					}
					_logger.SaveLog("User selected " + _issuingTicket.getDestination().ToString());
					_issuingTicket.setSeatClass(askPassangerClass());
					_logger.SaveLog("User selected " + _issuingTicket.getSeatClass().toString());
					askPassangerName();
					input = WaitForString();
					_logger.SaveLog("User first name is: " + input);
					_issuingTicket.setFirstName(input);
					askPassangerLastName();
					input = WaitForString();
					_logger.SaveLog("User last name is: " + input);
					_issuingTicket.setLastName(input);
					askPassangerPhone();
					input = WaitForString();
					_logger.SaveLog("User phone number is: " + input);
					_issuingTicket.setPhoneNumber(input);
					askPassangerAddress();
					input = WaitForString();
					_logger.SaveLog("User address is: " + input);
					_issuingTicket.setAddress(input);
					System.out.println("Issuing ticket . . .");
					// Ticket to issue

					_logger.SaveLog("Calling CORBA bookticket");
					int ret = _leServer.bookFlight(_issuingTicket.getFirstName(), _issuingTicket.getLastName(),
							_issuingTicket.getAddress(), _issuingTicket.getPhoneNumber(),
							_issuingTicket.getDestination().ToString(),
							_dateFormat.format(_issuingTicket.getDateTime()), _issuingTicket.getSeatClass().ToInt());

					if (ret == -1 || ret == 0) {
						System.out.println("An error was encountered, make sure to use the correct date");
						_logger.SaveLog("Ticket not created");
					} else {
						System.out.println("Ticket Created! ID: " + ret);
						_logger.SaveLog("Ticket created, id: " + ret);
					}
				} else {
					// Manager
					_logger.SaveLog("User identified himself as  Manager");
					askManagerID();
					input = WaitForString();
					Boolean validIdentification = false;
					if (input.length() == 7) {
						String city = input.substring(0, 3);
						String number = input.substring(3, 7);
						CityEnum cityEnum = CityEnum.parseShortString(city);
						validIdentification = cityEnum != CityEnum.Invalid;
						_logger.SaveLog("Manager identified as " + input);
						_logger.SaveLog("Manager is located in " + cityEnum.ToString());
						if (validIdentification) {
							IFlightServer operationServer = _leServer;
							if (cityEnum != _leServerLocation) {
								_logger.SaveLog("Connecting to the proper server");
								operationServer = requestFlightServer(cityEnum);

							}
							askManagerOptions();
							showReturnToStart();
							int optionSelected = WaitForInt();
							if (optionSelected == 0) {
								_logger.SaveLog("Manager decided to go back to the main menu");
								break;
							}
							switch (optionSelected) {
							case 1:
								// Total number in class
								_logger.SaveLog("Manager requested number of seats for a certain class");
								SeatClassEnum seatClass = askPassangerClass();
								_logger.SaveLog("Manager requested " + seatClass.toString());

								System.out.println("Processing. . .");
								String output = operationServer.getBookFlightCount(seatClass.ToInt());
								_logger.SaveLog("the result is " + output);
								System.out.println(output);
								break;
							case 2:
								// Create new flight
								_logger.SaveLog("Manager asked to create a flight");
								do {
									input = "";
									askPassangeryear();
									input += WaitForString();
									askPassangerMonth();
									input += WaitForString();
									askPassangerDay();
									input += WaitForString();
									_logger.SaveLog("Manager asked for date " + input);
								} while (!TryParseDate(input));

								askPassangerDestination();
								int dest = 0;
								do {
									dest = WaitForInt();
									_logger.SaveLog("Manager selected destination " + dest);
								} while (dest < 1 || dest > 3);
								input += " " + CityEnum.parseFromInt(dest).ToShortString();
								_logger.SaveLog("Manager selected desitination" + CityEnum.parseFromInt(dest));

								System.out.println("Processing. . .");
								_logger.SaveLog("Calling server to create flight for the requested date");
								operationServer.editFlightRecord(0, "datetime", "insert " + input);
								_logger.SaveLog("Request processed by the server");
								System.out.println("Transaction completed");
								break;
							case 3:
								// Delete a flight
								_logger.SaveLog("Manager request to delete a flight");
								askUniqueID();
								int toDeleteID = WaitForInt();
								System.out.println("Processing. . .");
								_logger.SaveLog("Calling server to delete flight for the requested date");
								operationServer.editFlightRecord(toDeleteID, "datetime", "delete");
								_logger.SaveLog("Request processed by the server");
								System.out.println("Transaction completed");
								break;
							case 4:
								// Edit a flight
								_logger.SaveLog("Manager wants to edit a flight");
								input = "";
								askUniqueID();
								int uniID = WaitForInt();
								_logger.SaveLog("Manager entered id: " + uniID);
								seatClass = askManagerClassToEdit();
								_logger.SaveLog("Manager entered seatClass " + seatClass.toString());
								askManagerNewNumber();
								int newNumber = WaitForInt();
								_logger.SaveLog("Manager netered seat number " + newNumber + ". Processing request");
								operationServer.editFlightRecord(uniID, seatClass.ToCodeString(), Integer.toString(newNumber));
								_logger.SaveLog("Transaction Completed");
								break;
							case 5:
								// Transfer a ticket
								_logger.SaveLog("Manager wants to transfer passanger ticket");
								input = "";
								askUniqueID();
								int uniqueID = WaitForInt();
								_logger.SaveLog("unique ID of the ticket is: " + uniqueID);
								askPassangerDestination();
								int destination = 0;
								do {
									destination = WaitForInt();
									_logger.SaveLog("Manager selected destination " + destination);
								} while (destination < 1 || destination > 3);
								_logger.SaveLog("source: " + cityEnum.ToString() + " moving to: "
										+ CityEnum.parseFromInt(destination));
								boolean ret = operationServer.transferReservation(uniqueID, cityEnum.ToInt(), destination);
								if (ret) {
									System.out.println("Transfer successful");
									_logger.SaveLog("Transfer was successful");
								} else {
									System.out.println("Error detected and transfer could not be done");
									_logger.SaveLog("Transfer was unsuccessful");
								}
								break;
							default:
								System.out.println("Command unrecognized");
								_logger.SaveLog("Command unrecognized: " + input);
								break;
							}

						}

					} else {
						_logger.SaveLog("Manager not identified. " + input + " is invalid");
					}
					if (!validIdentification) {
						System.out.println("Invalid ID Detected");
						_logger.SaveLog("Manager not identified. " + input + " is invalid");
						break;
					}

				}

			} while (personStatus == 1 || personStatus == 2);
		}

	}

	private void askManagerNewNumber() {
		System.out.println("Please enter the new number");
		
	}

	private SeatClassEnum askManagerClassToEdit() {
		System.out.println("Overriding class");
		return askPassangerClass();
	}

	private void askUniqueID() {
		System.out.println("Please enter the ID");
	}

	public static IFlightServer requestFlightServer(CityEnum city) {
		org.omg.CORBA.Object objRef = null;
		switch (city) {
		case Montreal:
			objRef = _orb.string_to_object(montrealRef);
			break;
		case NewDelhi:
			objRef = _orb.string_to_object(newDelhiRef);
			break;
		case Washington:
			objRef = _orb.string_to_object(washingtonRef);
			break;
		default:
			break;
		}
		IFlightServer server = IFlightServerHelper.narrow(objRef);
		return server;
	}

	public boolean TryParseDate(String input) {
		Boolean ret = true;
		Date date;
		try {
			date = _dateFormat.parse(input);
			// if(_issuingTicket != null) _issuingTicket.setDateTime(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret = false;
		}
		return ret;
	}

}
