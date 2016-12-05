package Servers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Random;

import Clients.CityEnum;
import Clients.SeatClassEnum;
import infrastructure.CountOutputString;

public class UDPSeatRequest implements Runnable {
	private CountOutputString _outputString;
	private CityEnum _cityEnumToRequestTo;
	private DatagramSocket _udpSocket;
	private SeatClassEnum _seatToRequest;
	private FlightServer _leServ;

	public UDPSeatRequest(FlightServer serv, CountOutputString outputString, CityEnum toRequestTo,
			SeatClassEnum seatToRequest) {
		_leServ = serv;
		_outputString = outputString;
		_cityEnumToRequestTo = toRequestTo;
		_seatToRequest = seatToRequest;
	}

	@Override
	public void run() {
		try {
			_leServ.getLogger().SaveLog("Creating packet to send to " + _cityEnumToRequestTo.ToString() + " " + _cityEnumToRequestTo.toSeatRequestUDPServer());
			_udpSocket = new DatagramSocket();
			byte[] data = _seatToRequest.ToCodeString().getBytes();
			InetAddress aHost = InetAddress.getByName("localhost");
			int serverPort = _cityEnumToRequestTo.toSeatRequestUDPServer();
			DatagramPacket request = new DatagramPacket(data, data.length, aHost, serverPort);
			_leServ.getLogger().SaveLog("Packet created to send to " + _cityEnumToRequestTo.ToString());

			int timeoutInt = (int) Math.random() * 500 + 200;
			_udpSocket.setSoTimeout(timeoutInt);
			_leServ.getLogger().SaveLog("Timeout for UDP set as " + "ms to " + _cityEnumToRequestTo.ToString() + " " + _cityEnumToRequestTo.toSeatRequestUDPServer());

			DatagramPacket reply;
			byte[] buffer = new byte[100];
			Boolean timeout;
			do {
				try {
					_leServ.getLogger().SaveLog("Sending packet to " + _cityEnumToRequestTo.ToString() + " " + _cityEnumToRequestTo.toSeatRequestUDPServer());
					_udpSocket.send(request);
					reply = new DatagramPacket(buffer, buffer.length);
					_leServ.getLogger().SaveLog("Waiting for answer from " + _cityEnumToRequestTo.ToString());
					_udpSocket.receive(reply);
					_leServ.getLogger().SaveLog("Reply received from " + _cityEnumToRequestTo.ToString() + " " + _cityEnumToRequestTo.toSeatRequestUDPServer());
					timeout = false;
				} catch (SocketTimeoutException e) {
					_leServ.getLogger().SaveLog("Timeout number reached, trying again");
					e.printStackTrace();
					timeout = true;
				}
			} while (timeout);
			String leAnswer = new String(buffer);
			_leServ.getLogger()
					.SaveLog("Stamping answer from " + leAnswer + " to output stirng");
			_outputString.addString(leAnswer);

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

}
