package UDPCommunication;

import java.util.*;

import communication.IReliableUDPReceiver;
import data.Config;
import data.ServerInfo;
import communication.*;
import messages.*;
import util.RWMonitor;
import util.Tuple;

public class SequencerSender{
	IReliableUDPReceiver feReceiver;
	IReliableUDPSender[] _senders;
	
	RWMonitor _monitor;

	public SequencerSender(IReliableUDPReceiver receiver, IReliableUDPSender[] senders){
		_monitor = new RWMonitor();
		_senders = senders;
	}
	
	public void start() {
		
		while(true){
			IMessage forRM = feReceiver.receive();
			List<Thread> threads = new ArrayList<Thread>();
			
			//Send to all replicas at the same time
			for (IReliableUDPSender sender : _senders) {
				Thread leThread = new Thread(new SequencerSendingThread(sender, forRM));
				leThread.start();
				threads.add(leThread);
			}
			
			//wait for everyone to receive the info
			boolean threadsRunning = true;
			while(threadsRunning){
				threadsRunning = false;
				for (Thread thread : threads) {
					if(thread.isAlive()){
						threadsRunning = true;
						break;
					}
				}
				Thread.yield();
			}
			
			//TODO: check if there were problems
			
		}
		
	}

	
	
	

}
