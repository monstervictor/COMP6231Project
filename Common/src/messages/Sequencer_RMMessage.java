package messages;

import java.net.InetAddress;

public class Sequencer_RMMessage implements IMessage {

	private final long _seqSequence;
	private final FE_SequencerMessage _message;
	private final String _frontEndIP;
	private final int _frontEndPort;
	public Sequencer_RMMessage(FE_SequencerMessage message, long seqID, String frontEndIP, int port){
		_message = message;
		_seqSequence = seqID;
		_frontEndIP = frontEndIP;
		_frontEndPort = port;
	}
	
	public byte[] getData(){
		return _message.getData();
	}
	
	public long getSequence(){
		return _seqSequence;
	}

	public String getFrontEndIP() {
		return _frontEndIP;
	}

	public int getFrontEndPort() {
		return _frontEndPort;
	}

	@Override
	public InetAddress getSender() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSenderPort() {
		// TODO Auto-generated method stub
		return 0;
	}
}
