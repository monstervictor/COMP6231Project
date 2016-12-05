package co.baq.tolosa.alvaro.comp6231.server.model;

import java.io.Serializable;

public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5394453223759635428L;
	
	public static final int TYPE_REQUEST_NUMBER_OF_FREE_SEATS_BY_CLASS = 1;
	public static final int TYPE_RESPONSE_NUMBER_OF_FREE_SEATS_BY_CLASS = 2;
	public static final int TYPE_RESPONSE_TRANSFER_RESERVATION = 4;
	
	public static final int RESPONSE_UNUSED = 3;
	
	public static final int RESPONSE_ERROR_FLIGHT_NOT_FOUND = 5;
	
	private String cityOrigin;
	private int type;
	private int classType;
	private int response;
	
	public Message(String city, int type, int classType, int response) {
		this.cityOrigin = city;
		this.type = type;
		this.classType = classType;
		this.response = response;
	}
	
	public Message(String city, int classType) {
		this(city, TYPE_REQUEST_NUMBER_OF_FREE_SEATS_BY_CLASS, classType, RESPONSE_UNUSED);
	}
	
	public Message(String city, int classType, int response) {
		this(city, TYPE_RESPONSE_NUMBER_OF_FREE_SEATS_BY_CLASS, classType, response);
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getClassType() {
		return classType;
	}
	public void setClassType(int clasType) {
		this.classType = clasType;
	}

	public String getCityOrigin() {
		return cityOrigin;
	}

	public int getResponse() {
		return response;
	}
	
	@Override
	public String toString() {
		return String.format("%s %3d %3d %3d", this.cityOrigin, this.type, this.classType, this.response);
	}

}
