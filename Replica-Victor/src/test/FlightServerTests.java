package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.*;

public class FlightServerTests {
	
	List<String> servers;
	@Before
	public void beforeTests() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("servers.txt"));
		servers = new ArrayList<>();
		String currentLine;
		while ((currentLine = br.readLine()) != null) {
			servers.add(currentLine);
		}
		br.close();
	}

	@Test
	public void passingTest(){
		
	}

}
