package messages;

public class ErrorMessage implements IMessage{
	private static final IMessage _errorMessage = new ErrorMessage();
	public static IMessage getInstance(){ return _errorMessage;}
	
	@Override
	public byte[] getData() {
		return null;
	}

	@Override
	public long getSequence() {
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
		return 0;
	}

}
