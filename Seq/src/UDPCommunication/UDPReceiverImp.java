package UDPCommunication;

import java.net.InetAddress;
import java.util.*;

import communication.IReliableUDPReceiver;
import messages.DuplicatedMessage;
import messages.FE_SequencerMessage;
import messages.IMessage;
import messages.Sequencer_RMMessage;
import util.Counter;
import util.Tuple;

public class UDPReceiverImp implements IReliableUDPReceiver {
	private IReliableUDPReceiver _receiver;
	private Counter _counter;

	List<Tuple<String, Map<Long, Long>>> _messageHistory;

	public UDPReceiverImp(IReliableUDPReceiver receiver) {
		_receiver = receiver;
		_counter = new Counter();
		_messageHistory = new ArrayList<Tuple<String, Map<Long, Long>>>();
	}

	@Override
	public IMessage receive() {
		IMessage ret = _receiver.receive();
		FE_SequencerMessage leMessage = (FE_SequencerMessage) ret;
		final String netAddress = ret.getSender().getHostAddress();
		// see if this is the first message from address
		Tuple<String, Map<Long, Long>> lookForSender = _messageHistory.stream().filter(c -> c.x.equals(netAddress))
				.findFirst().orElse(null);
		if (lookForSender != null) {
			// See if we received the message already
			if (lookForSender.y.containsKey(ret.getSequence())) {
				// duplicate detected notify sender and return duplicate message
				createNotifySenderThread(lookForSender.y.get(ret.getSequence()));
				return DuplicatedMessage.getInstance();
			} else {
				// addKey and wrap message
				long seq = addKey(lookForSender, ret.getSequence());
				ret = new Sequencer_RMMessage(leMessage, seq, ret.getSender().getHostAddress(), ret.getSenderPort());
			}
		} else {
			// Add server, key and wrap message
			Tuple<String, Map<Long, Long>> toAdd = new Tuple<String, Map<Long, Long>>(ret.getSender().getHostAddress(),
					new LinkedHashMap<Long, Long>());
			_messageHistory.add(toAdd);
			long seq = addKey(toAdd, ret.getSequence());
			ret = new Sequencer_RMMessage(leMessage, seq, ret.getSender().getHostAddress(), ret.getSenderPort());
		}
		return ret;
	}

	private long addKey(Tuple<String, Map<Long, Long>> map, long feKey) {
		long newKey = _counter.getNext();
		map.y.put(feKey, newKey);
		return newKey;
	}

	private void createNotifySenderThread(Long seqID) {
		// TODO Auto-generated method stub
		Thread notifyThread = new Thread() {
			@Override
			public void run() {
				notifySender(seqID);
			}
		};
		notifyThread.start();
	}

	private Object _notifySenderLock = new Object();

	@Override
	public void notifySender(long seqID) {
		synchronized (_notifySenderLock) {
			_receiver.notifySender(seqID);
		}
	}

}
