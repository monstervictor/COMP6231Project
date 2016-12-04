package UDPCommunication;

import communication.IReliableUDPSender;
import messages.ACKMessage;
import messages.ACKType;
import messages.ErrorMessage;
import messages.IMessage;

public class SequencerSendingThread implements Runnable {

	private IMessage _leAnswer;
	private IReliableUDPSender _sender;
	private IMessage _toSend;
	private boolean _error;

	public SequencerSendingThread(IReliableUDPSender sender, IMessage toSend) {
		// TODO Auto-generated constructor stub
		_sender = sender;
		_toSend = toSend;
	}

	@Override
	public void run() {
		_leAnswer = _sender.sendAndReceive(_toSend);
		_error = _leAnswer instanceof ErrorMessage;
		/*ACKMessage msg = (ACKMessage) _leAnswer;
		if(msg.getACKType() == ACKType.RequestSequence){
			
		}*/
	}

	public IMessage getAnswer() {
		return _leAnswer;
	}

	public boolean getErrorDetected() {
		return _error;
	}

	public IReliableUDPSender getUDPSender() {
		// TODO Auto-generated method stub
		return _sender;
	}

}
