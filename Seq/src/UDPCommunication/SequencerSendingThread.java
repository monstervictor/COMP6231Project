package UDPCommunication;

import communication.IReliableUDPSender;
import messages.IMessage;

public class SequencerSendingThread implements Runnable{

	private IMessage _leAnswer;
	private IReliableUDPSender _sender;
	private IMessage _toSend;
	public SequencerSendingThread(IReliableUDPSender sender, IMessage toSend) {
		// TODO Auto-generated constructor stub
		_sender = sender;
		_toSend = toSend;
	}
	
	@Override
	public void run() {
		_leAnswer = _sender.sendAndReceive(_toSend);
	}

}
