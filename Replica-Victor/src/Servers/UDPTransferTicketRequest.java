package Servers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import Clients.CityEnum;
import Clients.SeatClassEnum;
import infrastructure.CountOutputString;
import models.PassangerTicket;

public class UDPTransferTicketRequest implements Runnable {
	private boolean _wasRequestSuccessful = false;
	private CityEnum _cityEnumToRequestTo;
	private DatagramSocket _udpSocket;
	private PassangerTicket _toTransfer;
	private int newUniqueID;
	private FlightServer _leServ;

	public UDPTransferTicketRequest(FlightServer serv, CityEnum toRequestTo, PassangerTicket toTransfer) {
		_cityEnumToRequestTo = toRequestTo;
		_toTransfer = toTransfer;
		_leServ = serv;
	}

	@Override
	public void run() {
		int tries = 3;
		int currentTries = 0;
		try {
			_leServ.getLogger().SaveLog("Starting thread to send request to " + _cityEnumToRequestTo.ToString());
			_udpSocket = new DatagramSocket();
			// (String firstName, String lastName, String address, String phone,
			// String destination, String d, int sClass)
			String toSend = _toTransfer.getFirstName() + "|" + _toTransfer.getLastName() + "|" + _toTransfer.getAddress() + "|"
					+ _toTransfer.getPhoneNumber() + "|" + _toTransfer.getDestination().ToShortString() + "|"
					+ _toTransfer.getDateTimeString() + "|" + _toTransfer.getSeatClass().ToInt() + "|"
					+ _toTransfer.getUniqueID();

			byte[] data = toSend.getBytes();
			_leServ.getLogger().SaveLog("Shaped packet to send request to " + _cityEnumToRequestTo.ToString() + " " + _cityEnumToRequestTo.toPassangerTransferUDPServer());
			InetAddress aHost = InetAddress.getByName("localhost");
			int serverPort = _cityEnumToRequestTo.toPassangerTransferUDPServer();
			DatagramPacket request = new DatagramPacket(data, data.length, aHost, serverPort);

			int timeoutInt =(int)(Math.random() * 2500) + 2000;
			_leServ.getLogger().SaveLog("Timeout set to " + timeoutInt + " to send to " + _cityEnumToRequestTo.ToString() + " " + _cityEnumToRequestTo.toPassangerTransferUDPServer());
			_udpSocket.setSoTimeout(timeoutInt);

			DatagramPacket reply;
			byte[] buffer = new byte[100];
			Boolean timeout;
			do {
				try {
					_leServ.getLogger().SaveLog("Sending request packet to" + _cityEnumToRequestTo.ToString() + " " + _cityEnumToRequestTo.toPassangerTransferUDPServer());
					_udpSocket.send(request);
					reply = new DatagramPacket(buffer, buffer.length);
					_udpSocket.receive(reply);
					_leServ.getLogger().SaveLog("Received answer from " + _cityEnumToRequestTo.ToString() + " " + _cityEnumToRequestTo.toPassangerTransferUDPServer());
					timeout = false;
					_wasRequestSuccessful = true;
					newUniqueID = fromByteArray(buffer);

				} catch (SocketTimeoutException e) {
					_leServ.getLogger().SaveLog("current try failed: " + currentTries);
					e.printStackTrace();
					timeout = true;
					currentTries++;
				}
			} while (timeout && currentTries < tries);
			_leServ.getLogger().SaveLog("Finishing thread to send transfer request to " + _cityEnumToRequestTo.ToString() + " " + _cityEnumToRequestTo.toPassangerTransferUDPServer());

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public int fromByteArray(byte[] bytes) {
		return ByteBuffer.wrap(bytes).getInt();
	}

	public int getNewUniqueID() {
		return newUniqueID;
	}

	public boolean getWasRequestSuccessful() {
		return _wasRequestSuccessful;
	}
}
