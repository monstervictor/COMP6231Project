package UDPCommunication;

import java.util.*;
import java.util.stream.Stream;

import communication.IReliableUDPReceiver;
import data.*;
import communication.*;
import messages.*;
import util.*;

public class SequencerSender {
	IReliableUDPReceiver feReceiver;
	IReliableUDPSender[] _senders;

	// history of messages
	Map<Long, IMessage> history;
	// SeqID -> number of replicas that received the message
	Map<Long, Integer> _seqToReplicaAnswerCount;

	// Monitor for history
	RWMonitor _monitor;

	// Monitor for _seqToReplicaAnswerCount
	RWMonitor _seqToReplicaMonitor;

	public SequencerSender(IReliableUDPReceiver receiver, IReliableUDPSender[] senders) {
		_monitor = new RWMonitor();
		_senders = senders;
		feReceiver = receiver;
		history = new HashMap<>();
		_seqToReplicaAnswerCount = new HashMap<>();
		_seqToReplicaMonitor = new RWMonitor();
	}

	public void start() {

		while (true) {
			IMessage forRM = feReceiver.receive();
			// TODO: Check duplicate
			if (forRM instanceof Sequencer_RMMessage) {

				_seqToReplicaMonitor.requestWriteAccess();
				_seqToReplicaAnswerCount.put(forRM.getSequence(), 3);
				_seqToReplicaMonitor.finishedWriteAccess();

				List<Thread> threads = new ArrayList<Thread>();
				List<SequencerSendingThread> runnables = new ArrayList<SequencerSendingThread>();

				// Send to all replicas at the same time
				for (IReliableUDPSender sender : _senders) {
					SequencerSendingThread r = new SequencerSendingThread(sender, forRM);
					Thread leThread = new Thread(r);
					runnables.add(r);
					leThread.start();
					threads.add(leThread);
				}

				// wait for everyone to receive the info
				boolean threadsRunning = true;
				while (threadsRunning) {
					threadsRunning = false;
					for (Thread thread : threads) {
						if (thread.isAlive()) {
							threadsRunning = true;
							break;
						}
					}
					Thread.yield();
				}

				// Check errors
				if (runnables.stream().anyMatch(c -> c.getErrorDetected())) {

					// Add msg to history
					_monitor.requestWriteAccess();
					history.put(forRM.getSequence(), forRM);
					_monitor.finishedWriteAccess();

					//select errors
					Stream<SequencerSendingThread> errors = runnables.stream()
							.filter(c -> c.getAnswer() instanceof ErrorMessage);

					//update value of _seqToReplica ("errors" because those are the ones missing the msg)
					_seqToReplicaMonitor.requestWriteAccess();
					int numberOfReplicasSent = _seqToReplicaAnswerCount.get(forRM.getSequence());
					_seqToReplicaAnswerCount.put(forRM.getSequence(), (int) errors.count());
					_seqToReplicaMonitor.finishedWriteAccess();

					//create troublshooterThreads and start
					errors.forEach(c -> {
						ErrorTroubleshooterThread runnable = new ErrorTroubleshooterThread(this, c.getUDPSender(),
								c.getAnswer());
						Thread troubleShooterThread = new Thread(runnable);
						troubleShooterThread.start();
					});
				} else {
					_seqToReplicaMonitor.requestWriteAccess();
					_seqToReplicaAnswerCount.remove(forRM.getSequence());
					_seqToReplicaMonitor.finishedWriteAccess();
				}

			}
		}

	}

	public Map<Long, IMessage> getHistory() {
		return history;
	}

	public RWMonitor getMonitor() {
		return _monitor;
	}

	public Map<Long, Integer> getSeqToReplicaAnswerCount() {
		return _seqToReplicaAnswerCount;
	}

	public RWMonitor getSeqToReplicaMonitor() {
		return _seqToReplicaMonitor;
	}
}
