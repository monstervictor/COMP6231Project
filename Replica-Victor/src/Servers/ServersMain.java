package Servers;

import java.io.PrintWriter;

import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import org.omg.PortableServer.*;

import Clients.CityEnum;
import CorbaServers.ICounterServer;
import CorbaServers.ICounterServerHelper;
import infrastructure.RWMonitor;

public class ServersMain {

	public static void main(String args[]) {
		try {
			PrintWriter file = new PrintWriter("servers.txt");

			RWMonitor serverListMonitor = new RWMonitor();

			// create and initialize the ORB
			ORB orb = ORB.init(args, null);

			// get reference to rootpoa & activate the POAManager
			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));

			// CounterServer
			CounterServer cs = new CounterServer();
			byte[] id = rootpoa.activate_object(cs);
			org.omg.CORBA.Object ref = rootpoa.id_to_reference(id);
			file.println(orb.object_to_string(ref));
			System.out.println("CounterServer Instantiated");

			// Montreal
			FlightServer montreal = new FlightServer();
			byte[] mID = rootpoa.activate_object(montreal);
			org.omg.CORBA.Object mRef = rootpoa.id_to_reference(mID);
			file.println(orb.object_to_string(mRef));
			System.out.println("MontrealServer Instantiated");

			// NewDelhi
			FlightServer newDelhi = new FlightServer();
			byte[] ndID = rootpoa.activate_object(newDelhi);
			org.omg.CORBA.Object ndRef = rootpoa.id_to_reference(ndID);
			file.println(orb.object_to_string(ndRef));
			System.out.println("NewDelhiServer Instantiated");

			// Washington
			FlightServer washington = new FlightServer();
			byte[] wID = rootpoa.activate_object(washington);
			org.omg.CORBA.Object wRef = rootpoa.id_to_reference(wID);
			file.println(orb.object_to_string(wRef));
			System.out.println("WashingtonServer Instantiated");

			file.close();

			//Setting server locations
			montreal.setCityServerLocation(CityEnum.Montreal, serverListMonitor);
			newDelhi.setCityServerLocation(CityEnum.NewDelhi, serverListMonitor);
			washington.setCityServerLocation(CityEnum.Washington, serverListMonitor);

			rootpoa.the_POAManager().activate();

			/*
			 * // create servant and register it with the ORB CounterServer cs =
			 * new CounterServer(); cs.setORB(orb);
			 * 
			 * // get object reference from the servant org.omg.CORBA.Object ref
			 * = rootpoa.servant_to_reference(cs); ICounterServer csref =
			 * ICounterServerHelper.narrow(ref);
			 * 
			 * // get the root naming context // NameService invokes the name
			 * service org.omg.CORBA.Object objRef =
			 * orb.resolve_initial_references("NameService"); // Use
			 * NamingContextExt which is part of the Interoperable // Naming
			 * Service (INS) specification. NamingContextExt ncRef =
			 * NamingContextExtHelper.narrow(objRef);
			 * 
			 * // bind the Object Reference in Naming String name =
			 * "CounterServer"; NameComponent path[] = ncRef.to_name( name );
			 * ncRef.rebind(path, csref);
			 */

			System.out.println("Servants ready and waiting ...");

			// wait for invocations from clients
			orb.run();
		}

		catch (Exception e) {
			System.err.println("ERROR: " + e);
			e.printStackTrace(System.out);
		}

		System.out.println("HelloServer Exiting ...");

	}

}
