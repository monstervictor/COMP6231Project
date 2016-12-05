package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

import org.omg.CORBA.*;
import org.omg.CosNaming.*;

import CorbaServers.*;

public class CounterServerClientTest {
	static ICounterServer csImp;

	public static void main(String args[]) {
		try {

			BufferedReader br = new BufferedReader(new FileReader("servers.txt"));
			List<String> servers = new ArrayList<>();
			String currentLine;
			while ((currentLine = br.readLine()) != null) {
				servers.add(currentLine);
			}
			br.close();

			// create and initialize the ORB
			ORB orb = ORB.init(args, null);

			// get the root naming context
			org.omg.CORBA.Object objRef = orb.string_to_object(servers.get(0));
			ICounterServer csImp = ICounterServerHelper.narrow(objRef);

			for (int i = 0; i < 50; i++) {
				Thread.sleep(1000);
				System.out.println(csImp.getNext());
			}
			// csImp.shutdown();

		} catch (Exception e) {
			System.out.println("ERROR : " + e);
			e.printStackTrace(System.out);
		}
	}

}
