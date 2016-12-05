package Servers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

import Clients.SeatClassEnum;

public class UDPTransferTicketServer implements Runnable {

	private final FlightServer _leServer;
	private DatagramSocket _udpSocket;

	public UDPTransferTicketServer(FlightServer leServer) {
		_leServer = leServer;
	}

	@Override
	public void run() {
		try {
			_leServer.getLogger().SaveLog("UDPTransferTicketServer: Initializing");
			_udpSocket = new DatagramSocket(_leServer.getCityServerLocation().toPassangerTransferUDPServer());

			byte[] buffer = new byte[1000];
			while (true) {
				DatagramPacket lePacket = new DatagramPacket(buffer, buffer.length);
				_leServer.getLogger().SaveLog("UDPTransferTicketServer: Listening to port "
						+ _leServer.getCityServerLocation().toPassangerTransferUDPServer());
				_udpSocket.receive(lePacket);
				_leServer.getLogger().SaveLog("UDPTransferTicketServer: Received information");
				if (lePacket.getLength() > 0) {
					String requestString = new String(lePacket.getData());
					String[] requestSplit = requestString.split("\\|");

					_leServer.getLogger().SaveLog(
							"UDPTransferTicketServer: Parsed data and attempting to process in the flight server");
					int newID = -1;
					try {
						int classSeat = Integer.parseInt(requestSplit[6]);
						int uniqueID = Integer.parseInt(requestSplit[7].trim());
						newID = _leServer.bookFlight(requestSplit[0], requestSplit[1], requestSplit[2], requestSplit[3],
								requestSplit[4], requestSplit[5], classSeat, uniqueID);

						_leServer.getLogger().SaveLog(
								"UDPTransferTicketServer: Data Processed and sending a reply to the requester");
						DatagramPacket leReply = new DatagramPacket(toByteArray(newID), 4, lePacket.getAddress(),
								lePacket.getPort());
						_udpSocket.send(leReply);
						_leServer.getLogger().SaveLog("Reply sent back");
					} catch (Exception ex) {
						System.out.println(ex.getStackTrace());
					}
				} else {
					_leServer.getLogger().SaveLog("Length = 0 in packet");
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
