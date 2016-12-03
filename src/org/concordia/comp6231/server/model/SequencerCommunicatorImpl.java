package org.concordia.comp6231.server.model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.concordia.comp6231.util.Util;

public class SequencerCommunicatorImpl implements SequencerCommunicator {
	public static final int INITIAL_BACKOFF = 1000;
	public static final int TOTAL_NUM_REPLICAS = 3;
	private long nextRequestId;
	
	private boolean isOpen;
	
	private DatagramSocket socket;
	
	private String host;
	private int port;
	private InetAddress address;
	
	public SequencerCommunicatorImpl(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public void open() throws UnknownHostException, SocketException {
		this.socket = new DatagramSocket();
		this.address = InetAddress.getByName(this.host);
		this.isOpen = true;
	}
	
	public void close() {
		if(this.isOpen) {
			this.isOpen = false;
			this.socket.close();
		}
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	
	private boolean sendAndReceive(DatagramPacket packet, DatagramPacket responsePacket) {
		int backoffTime = INITIAL_BACKOFF;
		int retries = 3;
		while(retries > 0) {
			try {
				this.socket.send(packet);
				//now, wait for the sequacer answer, otherwise resend packet
				this.socket.setSoTimeout(backoffTime);
				this.socket.receive(responsePacket);
				return true;
			}catch(IOException e ) {
				backoffTime*=2;
			}
			retries--;
		}
		return false;
	}

	@Override
	public long feSend(byte [] data) {
		if(this.isOpen) {
			FE_SequencerMessage message = new FE_SequencerMessage(data, nextRequestId++);
			
			try {
				byte [] serializedMsg = Util.serialize(message);
				byte [] response = new byte[2048];
				
				DatagramPacket packet = new DatagramPacket(serializedMsg, 0, serializedMsg.length, this.address, this.port);
				
				//now, wait for the sequencer answer, otherwise resend packet 3 times
				DatagramPacket responsePacket = new DatagramPacket(response, response.length);
				if(sendAndReceive(packet, responsePacket)) {
					Util.serialize(responsePacket);
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}
	
	private DatagramPacket getNewDatagram() {
		byte [] response = new byte[2048];
		
		return new DatagramPacket(response, response.length);
	}
	
	private byte [] waitForReplicaResponse(long responseId) {
		DatagramPacket packet = this.getNewDatagram();
		byte [] replicaManagerRawresponse = null;
		int backoffTime = INITIAL_BACKOFF;
		int retries = 3;
		while(retries > 0){
			try {
				this.socket.setSoTimeout(backoffTime);
				this.socket.receive(packet);
				replicaManagerRawresponse = packet.getData();
				return replicaManagerRawresponse;
			} catch (SocketTimeoutException e) {
				e.printStackTrace();
				//we must mark this replica manager as bad
			} catch(IOException e) {
				e.printStackTrace();
			}
			retries--;
		}
		return null;
	}
	
	@Override
	public void waitForResponse(long responseId) {
		Map<String, byte []> response = new HashMap<>();
		Thread [] replicas = new Thread[TOTAL_NUM_REPLICAS];
		if(this.isOpen) {	
			for(int i=0; i< TOTAL_NUM_REPLICAS; i++) {
				replicas[i] = new Thread(new ReplicaManagerListener(response, responseId, this.socket));
				replicas[i].start();
			}
			
			for(int i=0; i< TOTAL_NUM_REPLICAS; i++) {
				try {
					replicas[i].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			/*Thread r1 = new Thread(new ReplicaManagerListener(response, responseId, this.socket));
			waitForReplicaResponse(responseId);
			waitForReplicaResponse(responseId);
			waitForReplicaResponse(responseId);
			*/
			
			
		}
	}
	
	//private Map<>

}

class ReplicaManagerListener implements Runnable {
	private Map<String, byte []> response;
	private long responseId;
	private DatagramSocket socket;
	
	public ReplicaManagerListener(Map<String, byte []> response, long responseId, DatagramSocket socket) {
		this.response = response;
		this.socket = socket;
		this.responseId = responseId;
	}
	
	private DatagramPacket getNewDatagram() {
		byte [] response = new byte[2048];
		
		return new DatagramPacket(response, response.length);
	}

	@Override
	public void run() {
		DatagramPacket packet = this.getNewDatagram();
		byte [] replicaManagerRawresponse = null;
		int backoffTime = SequencerCommunicatorImpl.INITIAL_BACKOFF;
		int retries = 3;
		while(retries > 0){
			try {
				this.socket.setSoTimeout(backoffTime);
				this.socket.receive(packet);
				replicaManagerRawresponse = packet.getData();
				response.put(""+responseId, replicaManagerRawresponse);
				break;
			} catch (SocketTimeoutException e) {
				e.printStackTrace();
				//we must mark this replica manager as bad
			} catch(IOException e) {
				e.printStackTrace();
			}
			retries--;
		}		
	}
	
}
