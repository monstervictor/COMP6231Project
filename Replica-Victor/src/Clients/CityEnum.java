package Clients;

public enum CityEnum {
	Invalid, Montreal, Washington, NewDelhi,;

	public String ToString() {
		switch (this) {
		case Montreal:
			return "Montreal";
		case NewDelhi:
			return "New Delhi";
		case Washington:
			return "Washington";
		default:
			break;
		}
		return null;
	}

	public String ToShortString() {
		switch (this) {
		case Montreal:
			return "MTL";
		case NewDelhi:
			return "NDL";
		case Washington:
			return "WGN";
		default:
			break;
		}
		return null;
	}

	public int toCORBAPort() {
		switch (this) {
		case Montreal:
			return 2020;
		case NewDelhi:
			return 2021;
		case Washington:
			return 2022;
		default:
			break;
		}
		return 0;
	}

	public int toSeatRequestUDPServer() {
		switch (this) {
		case Montreal:
			return 6005;
		case NewDelhi:
			return 6007;
		case Washington:
			return 6006;
		default:
			break;
		}
		return 0;
	}
	public int toPassangerTransferUDPServer(){
		switch (this) {
		case Montreal:
			return 6008;
		case NewDelhi:
			return 6009;
		case Washington:
			return 6010;
		default:
			break;
		}
		return 0;
	}

	public static CityEnum parseShortString(String city) {
		if (city.equals("MTL"))
			return Montreal;
		if (city.equals("WGN"))
			return Washington;
		if (city.equals("NDL"))
			return NewDelhi;
		return Invalid;
	}

	public int ToInt() {
		switch (this) {
		case Invalid:
			return 0;
		case Montreal:
			return 1;
		case Washington:
			return 2;
		case NewDelhi:
			return 3;
		default:
			return -1;
		}
	}

	public static CityEnum parseFromInt(int toParse) {
		switch (toParse) {
		case 0:
			return Invalid;
		case 1:
			return Montreal;
		case 2:
			return Washington;
		case 3:
			return NewDelhi;
		default:
			return null;
		}
	}
}
