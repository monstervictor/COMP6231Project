package Clients;

public enum SeatClassEnum {
	Unavailable, FirstClass, BusinessClass, EconomyClass,;
	public String ToCodeString() {
		switch (this) {
		case FirstClass:
			return "fclass";
		case BusinessClass:
			return "bclass";
		case EconomyClass:
			return "eclass";
		default:
			break;
		}
		return null;
	}

	public int ToInt() {
		switch (this) {
		case Unavailable:
			return 0;
		case FirstClass:
			return 1;
		case BusinessClass:
			return 2;
		case EconomyClass:
			return 3;
		default:
			return -1;
		}
	}

	public static SeatClassEnum ParseInt(int toParse) {
		switch (toParse) {
		case 0:
			return Unavailable;
		case 1:
			return FirstClass;
		case 2:
			return BusinessClass;
		case 3:
			return EconomyClass;
		default:
			return null;
		}
	}

	public static boolean IsIntParsable(int result) {
		// TODO Auto-generated method stub
		return result == 0 || result == 1 || result == 2 || result == 3;
	}

	public static SeatClassEnum parseShortString(String requestString) {
		if (requestString.trim().equals("fclass"))
			return FirstClass;
		if (requestString.trim().equals("bclass"))
			return BusinessClass;
		if (requestString.trim().equals("eclass"))
			return EconomyClass;
		return SeatClassEnum.Unavailable;
	}
}
