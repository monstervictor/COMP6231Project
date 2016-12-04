package messages;

import java.net.InetAddress;

public interface IMessage {
	byte[] getData();
	long getSequence();
	InetAddress getSender();
	int getSenderPort();
}
