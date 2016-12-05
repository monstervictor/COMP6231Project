package co.baq.tolosa.alvaro.comp6231.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import co.baq.tolosa.alvaro.comp6231.server.model.Status;

public class LogWriter {
	private String city;
	private PrintWriter pw;
		
	public LogWriter(String city) {
		this.city = city;
		try {
			pw =new PrintWriter(new FileWriter(city+"_server.txt", true));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
	
	public void changeFileName(String fileName) {
		if(pw!=null) pw.close();
		
		try {
			//open the file for append
			pw =new PrintWriter(new FileWriter(fileName+"_client.txt", true));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public LogWriter(String city, String fileName) {
		this.city = city;
		try {
			//open the file for append
			pw =new PrintWriter(new FileWriter(fileName+"_client.txt", true));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
	
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	public void write(String status) {
		String line = String.format("[%s] %s -- %s", dateFormat.format(new Date()), this.city, status);
		System.out.println(line);
		pw.println(line);
		pw.flush();
	}
	
	public void write(String admin, String status) {
		String line = String.format("[%s] %s %s -- %s", dateFormat.format(new Date()), admin, this.city, status);
		System.out.println(line);
		pw.println(line);
		pw.flush();
	}
	
	public void write(String admin, Status status) {
		String line = String.format("[%s] %s %s -- %s", dateFormat.format(new Date()), admin, this.city, status.getMessage());
		System.out.println(line);
		pw.println(line);
		pw.flush();
	}
	
	public void write(Status status) {
		String line = String.format("[%s] %s -- %s", dateFormat.format(new Date()), this.city, status.getMessage());
		System.out.println(line);
		pw.println(line);
		pw.flush();
	}

}
