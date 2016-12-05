package Servers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

import Clients.SeatClassEnum;

public class UDPSeatsServer implements Runnable {

	private final FlightServer _leServer;
	private DatagramSocket _udpSocket;

	public UDPSeatsServer(FlightServer leServer) {
		_leServer = leServer;
	}

	@Override
	public void run() {
		try {
			_leServer.getLogger().SaveLog("UDPSeatServer: Starting");
			_udpSocket = new DatagramSocket(_leServer.getCityServerLocation().toSeatRequestUDPServer());

			byte[] buffer = new byte[1000];
			while (true) {
				DatagramPacket lePacket = new DatagramPacket(buffer, buffer.length);
				_leServer.getLogger().SaveLog("UDPSeatServer: Listening to port "
						+ _leServer.getCityServerLocation().toSeatRequestUDPServer());
				_udpSocket.receive(lePacket);
				_leServer.getLogger().SaveLog("UDPSeatServer:  received request");
				if (lePacket.getLength() > 0) {
					String requestString = new String(lePacket.getData());
					_leServer.getLogger()
							.SaveLog("UDPSeatServer parsed the data and is requesting read access of the data");
					_leServer.getFlightsAvailableMonitor().requestReadAccess();
					_leServer.getLogger().SaveLog("UDPSeatServer: Access granted to read data");
					int localRequestedCount = 0;
					SeatClassEnum toSwitch = SeatClassEnum.parseShortString(requestString);
					switch (toSwitch) {
					case BusinessClass:
						_leServer.getLogger().SaveLog("UDPSeatServer: Reading count of business seats");
						localRequestedCount = (int) _leServer.getPassangerTickets().stream()
								.filter(c -> c.getSeatClass() == SeatClassEnum.BusinessClass).count();
						break;
					case EconomyClass:
						_leServer.getLogger().SaveLog("UDPSeatServer: Reading count of economy seats");
						localRequestedCount = (int) _leServer.getPassangerTickets().stream()
								.filter(c -> c.getSeatClass() == SeatClassEnum.EconomyClass).count();
						break;
					case FirstClass:
						_leServer.getLogger().SaveLog("UDPSeatServer: Reading count of first class seats");
						localRequestedCount = (int) _leServer.getPassangerTickets().stream()
								.filter(c -> c.getSeatClass() == SeatClassEnum.FirstClass).count();
						break;
					}
					_leServer.getFlightsAvailableMonitor().finishedReadAccess();
					_leServer.getLogger().SaveLog("UDPSeatServer: Notified finished read access and shaping a reply");
					String replyString = _leServer.getCityServerLocation().ToShortString() + " "+ localRequestedCount;
					byte[] toSend = replyString.getBytes();
					DatagramPacket leReply = new DatagramPacket(toSend, toSend.length,
							lePacket.getAddress(), lePacket.getPort());
					_leServer.getLogger().SaveLog("UDPSeatServer: reply sent: " + replyString);
					_udpSocket.send(leReply);
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public byte[] toByteArray(int value) {
		return ByteBuffer.allocate(4).putInt(value).array();
	}

}
