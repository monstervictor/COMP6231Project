package UDPCommunication;

import communication.IReliableUDPSender;
import messages.ACKMessage;
import messages.ACKType;
import messages.IMessage;

public class ErrorTroubleshooterThread implements Runnable {

	private IReliableUDPSender _udp;
	private SequencerSender _sequencer;
	private IMessage _errorMsg;

	public ErrorTroubleshooterThread(SequencerSender sequencerSender, IReliableUDPSender udp, IMessage errorMsg) {
		// TODO Auto-generated constructor stub
		_udp = udp;
		_sequencer = sequencerSender;
		_errorMsg = errorMsg;
	}

	@Override
	public void run() {
		ACKMessage errorMsg = (ACKMessage) _errorMsg;
		if (errorMsg.getACKType() == ACKType.RequestSequence) {

			_sequencer.getMonitor().requestReadAccess();
			IMessage toSend = _sequencer.getHistory().get(errorMsg.getSequence());
			_sequencer.getMonitor().finishedReadAccess();

			IMessage answer = _udp.sendAndReceive(toSend);
			if (answer instanceof ACKMessage) {
				ACKMessage ackAnswer = (ACKMessage) answer;
				if (ackAnswer.getACKType() == ACKType.NoError) {

					boolean removeFromHistory = false;
					// Update seqToReplicaAnswerCount
					_sequencer.getSeqToReplicaMonitor().requestWriteAccess();
					int count = _sequencer.getSeqToReplicaAnswerCount().get(errorMsg.getSequence());
					_sequencer.getSeqToReplicaAnswerCount().put(errorMsg.getSequence(), count--);
					if (_sequencer.getSeqToReplicaAnswerCount().get(errorMsg.getSequence()) <= 0) {
						_sequencer.getSeqToReplicaAnswerCount().remove(errorMsg.getSequence());
						removeFromHistory = true;
					}
					_sequencer.getSeqToReplicaMonitor().finishedWriteAccess();

					//Update history
					if (removeFromHistory) {
						_sequencer.getMonitor().requestWriteAccess();
						_sequencer.getHistory().remove(errorMsg.getSequence());
						_sequencer.getMonitor().finishedWriteAccess();
					}

				}
			}
		}

	}

}
