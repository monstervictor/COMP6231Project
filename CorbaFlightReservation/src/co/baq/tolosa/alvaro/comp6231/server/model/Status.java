package co.baq.tolosa.alvaro.comp6231.server.model;

public class Status {
	public static final int NO_ENOUGH_SEATS = -1;
	public static final int NO_ERROR = 0;
	public static final int  NO_FLIGHT_FOUND = -2;
	
	public static final int PASSENGER_NOT_FOUND = -3;
	
	int errorCode;
	String message;
	
	public Status() {
		this.errorCode = NO_ERROR;
		this.message = "";
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return String.format("%3s          %s", errorCodeToString(), message);
	}
	private String errorCodeToString() {
		String answer = "";
		switch(this.errorCode) {
		case NO_ERROR: answer = "no error"; break;
		case NO_FLIGHT_FOUND: answer = "no flight found"; break;
		case NO_ENOUGH_SEATS: answer = "no enough seats"; break;
		}
		
		return answer;
	}

}
