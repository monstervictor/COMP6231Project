package messages;

public interface ISimpleMessage {
	long getSequence();
	String getSender();
	int getSenderPort();
}
