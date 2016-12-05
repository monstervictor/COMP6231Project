package infrastructure;

public class CountOutputString {

	String _leString = "";
	RWMonitor _monitor;
	public CountOutputString(){
		_monitor = new RWMonitor();
	}
	public void addString(String toAdd){
		_monitor.requestWriteAccess();
		if(_leString != "") _leString += ", ";
		_leString += toAdd;
		_monitor.finishedWriteAccess();
	}
	public String getString(){
		_monitor.requestReadAccess();
		String ret = _leString;
		_monitor.finishedReadAccess();
		return ret;
	}
}
