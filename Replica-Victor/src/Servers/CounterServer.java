package Servers;

import java.io.*;

import org.omg.CORBA.ORB;

import CorbaServers.ICounterServerPOA;

public class CounterServer extends ICounterServerPOA {

	private static final String _fileName = "counter.txt";

	private int _currentCount = 0;

	private PrintWriter _leWriter;

	private ORB _orb;

	public ORB getORB() {
		return _orb;
	}

	public void setORB(ORB orb) {
		_orb = orb;
	}

	public CounterServer() {
		// Create file if it does not exist
		File leFile = new File(_fileName);
		try {
			leFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			FileReader leReader = new FileReader(leFile);
			BufferedReader leBuffer = new BufferedReader(leReader);
			String data = leBuffer.readLine();
			if (data != null && !data.isEmpty()) {
				_currentCount = Integer.parseInt(data);
			}
			leBuffer.close();

			// Keep writer open for upcoming operations
			FileWriter leFileWriter = new FileWriter(leFile);
			_leWriter = new PrintWriter(leFileWriter);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public synchronized int getNext() {
		_currentCount++;
		_leWriter.print(_currentCount);
		return _currentCount;
	}

}
