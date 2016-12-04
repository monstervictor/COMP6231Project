package communication;

import messages.IMessage;

public interface IReliableUDPReceiver {
	IMessage receive();
	void notifySender(long seqID);
}
