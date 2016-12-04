package org.concordia.comp6231.server.model;

public class Sequencer_RMMessage extends FE_SequencerMessage {

	public Sequencer_RMMessage(FE_SequencerMessage message, long newID) {
		super(message.getData(), newID);
	}
	
	

}
