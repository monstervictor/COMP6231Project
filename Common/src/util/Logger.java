package util;

import java.io.*;
import java.util.*;

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
