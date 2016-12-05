package communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import data.*;
import data.ServerInfo;
import messages.ErrorMessage;
import messages.IMessage;
import util.*;

public class ReliableUDPSender implements IReliableUDPSender {

	private static final int INITIAL_BACKOFF = 1000;
	private boolean _isOpen = false;
	private ServerInfo _servInfo;
	private DatagramSocket socket;

	public ReliableUDPSender(ServerInfo servInfo) throws IOException {
		_servInfo = servInfo;
	}

	@Override
	public boolean isOpen() {
		return _isOpen;
	}

	@Override
	public void open() {
		try {
			socket = new DatagramSocket(_servInfo.getPort(), _servInfo.getIpAddress());
			_isOpen = true;
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void close() {
		socket.close();
		_isOpen = false;
	}

	//Returns error if timeout
	@Override
	public synchronized IMessage sendAndReceive(IMessage message) {
		IMessage responseMsg = ErrorMessage.getInstance();
		if (_isOpen) {

			try {
				byte[] serializedMsg = Util.serialize(message);
				byte[] response = new byte[2048];

				DatagramPacket packet = new DatagramPacket(serializedMsg, 0, serializedMsg.length,
						_servInfo.getIpAddress(), _servInfo.getPort());

				// now, wait for the sequencer answer, otherwise resend packet 3
				// times
				DatagramPacket responsePacket = new DatagramPacket(response, response.length);
				if (sendAndReceive(packet, responsePacket)) {
					responseMsg = (IMessage) Util.deserialize(responsePacket.getData());
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return responseMsg;
	}

	private boolean sendAndReceive(DatagramPacket packet, DatagramPacket responsePacket) {
		int backoffTime = INITIAL_BACKOFF;
		int retries = 3;
		while (retries > 0) {
			try {
				this.socket.send(packet);
				// now, wait for the sequacer answer, otherwise resend packet
				this.socket.setSoTimeout(backoffTime);
				this.socket.receive(responsePacket);
				return true;
			} catch (IOException e) {
				backoffTime *= 2;
			}
			retries--;
		}
		return false;
	}

}
