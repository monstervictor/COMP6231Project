package infrastructure;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Logger {

	PrintWriter file;
	
	public Logger(String fileName){
		try {
			file = new PrintWriter(fileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void SaveLog(String message){
		Date d = new Date();
		file.println(d + ": " + message);
		file.flush();
	}
}
