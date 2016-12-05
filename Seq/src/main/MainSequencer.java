package main;

import java.io.IOException;

import UDPCommunication.*;
import communication.*;
import data.*;

public class MainSequencer {
	
	public static void main(String[] args){
		Config.initialize();
		
		IReliableUDPSender[] senders = new IReliableUDPSender[3];
		for (int i = 0; i < Config.getReplicas().length; i++) {
			try {
				senders[i] = new ReliableUDPSender(Config.getReplicas()[i]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		UDPReceiverImp receiver = new UDPReceiverImp(new ReliableUDPReceiver());
		
		SequencerSender leSender = new SequencerSender(receiver, senders);
		leSender.start();
	}

}
