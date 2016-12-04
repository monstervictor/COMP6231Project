package messages;

public class DuplicatedMessage implements IMessage {

	private static final DuplicatedMessage _message = new DuplicatedMessage();
	public static DuplicatedMessage getInstance(){return _message;}
	@Override
	public byte[] getData() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public long getSequence() {
		// TODO Auto-generated method stub
		return -1;
	}
	@Override
	public String getSender() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getSenderPort() {
		// TODO Auto-generated method stub
		return -1;
	}
}
