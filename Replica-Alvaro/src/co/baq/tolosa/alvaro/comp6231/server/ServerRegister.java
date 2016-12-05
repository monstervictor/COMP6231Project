package co.baq.tolosa.alvaro.comp6231.server;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import flightreservation.FlightReservationSystem;
import flightreservation.FlightReservationSystemHelper;

public class ServerRegister {

	public static void main(String[] argv) {
		try {
			ORB orb = ORB.init(argv, null);

			// get reference to rootpoa & activate the POAManager
			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));

			rootpoa.the_POAManager().activate();

			// create mtl servant and register it with the ORB
			FlightReservationServant flightReservationMtlImpl = new FlightReservationServant("mtl");

			// create NDL servant and register it with the ORB
			FlightReservationServant flightReservationNdlImpl = new FlightReservationServant("ndl");

			// create NYC servant and register it with the ORB
			FlightReservationServant flightReservationWstImpl = new FlightReservationServant("wst");

			// get object reference from the mtl servant
			org.omg.CORBA.Object refMtl = rootpoa.servant_to_reference(flightReservationMtlImpl);
			FlightReservationSystem hrefMtl = FlightReservationSystemHelper.narrow(refMtl);

			// get object reference from the ndl servant
			org.omg.CORBA.Object refNdl = rootpoa.servant_to_reference(flightReservationNdlImpl);
			FlightReservationSystem hrefNdl = FlightReservationSystemHelper.narrow(refNdl);

			// get object reference from the ndl servant
			org.omg.CORBA.Object refWst = rootpoa.servant_to_reference(flightReservationWstImpl);
			FlightReservationSystem hrefWst = FlightReservationSystemHelper.narrow(refWst);

			// get the root naming context
			// NameService invokes the name service
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			// Use NamingContextExt which is part of the Interoperable
			// Naming Service (INS) specification.
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			// bind the mtl Object Reference in Naming
			String name = "mtl";
			NameComponent path[] = ncRef.to_name(name);
			ncRef.rebind(path, hrefMtl);

			// bind the ndl Object Reference in Naming
			name = "ndl";
			path = ncRef.to_name(name);
			ncRef.rebind(path, hrefNdl);

			// bind the ndl Object Reference in Naming
			name = "wst";
			path = ncRef.to_name(name);
			ncRef.rebind(path, hrefWst);

			System.out.println("AdditionServer ready and waiting ...");

			// wait for invocations from clients
			orb.run();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
