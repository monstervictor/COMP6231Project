package communication;

import data.*;
import messages.*;

public interface IReliableUDPSender {
	public boolean isOpen();
	public void open();
	public void close();
	public IMessage sendAndReceive(IMessage message);
}
