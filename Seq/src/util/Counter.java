package util;

public class Counter {

	private static long _counter = 1;
	
	public synchronized long getNext(){
		long ret = _counter;
		_counter++;
		return ret;
	}
}
