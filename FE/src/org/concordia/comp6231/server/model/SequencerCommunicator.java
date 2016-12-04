package org.concordia.comp6231.server.model;

public interface SequencerCommunicator {
	public long feSend(byte [] data);
	
	public void waitForResponse(long responseId);

}
