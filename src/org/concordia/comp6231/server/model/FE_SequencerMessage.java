package org.concordia.comp6231.server.model;

import java.io.Serializable;

public class FE_SequencerMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte [] data;
	private long sequence;
	
	public FE_SequencerMessage(byte [] data, long sequence) {
		this.sequence = sequence;
		this.data = data;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public long getSequence() {
		return sequence;
	}

	public void setSequence(long sequence) {
		this.sequence = sequence;
	}
	
	

}
