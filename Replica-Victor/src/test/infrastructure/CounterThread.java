package test.infrastructure;

import CorbaServers.ICounterServer;
import Servers.CounterServer;

public class CounterThread extends Thread {

	Integer[] results = new Integer[10];

	public Integer[] getResults() {
		return results;
	}

	CounterServer _counterServ;

	public CounterThread(CounterServer leCounter) {
		_counterServ = leCounter;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 10; i++) {
			results[i] = _counterServ.getNext();
		}
	}

}
