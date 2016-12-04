package messages;

public class ACKMessage implements IMessage {

	
	
	private final ACKType _ackType;
	private final long _sequence;
	private final String _sender;
	private final int _port;

	public ACKMessage(ACKType ackType,long sequence, String sender, int port){
		_ackType = ackType;
		_sequence = sequence;
		_sender = sender;
		_port = port;
	}
	
	public ACKType getACKType(){return _ackType;}
	
	@Override
	public long getSequence() {
		// TODO Auto-generated method stub
		return _sequence;
	}

	@Override
	public String getSender() {
		// TODO Auto-generated method stub
		return _sender;
	}

	@Override
	public int getSenderPort() {
		// TODO Auto-generated method stub
		return _port;
	}

	@Override
	public byte[] getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
