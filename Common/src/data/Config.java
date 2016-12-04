package data;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Config {
	
	public static void initialize(){
		try {
			frontEnds = new ServerInfo[] { new ServerInfo("localhost", InetAddress.getLocalHost(), 5678) };
			replicas = new ServerInfo[] { new ServerInfo("localhost", InetAddress.getLocalHost(), 5679) };
			sequencer = new ServerInfo("localhost", InetAddress.getLocalHost(), 5679);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static ServerInfo[] frontEnds;
	private static ServerInfo sequencer;
	private static ServerInfo[] replicas;

	public static ServerInfo getSequencer() {
		return sequencer;
	}

	public static void setSequencer(ServerInfo sequencer) {
		Config.sequencer = sequencer;
	}

	public static ServerInfo[] getReplicas() {
		return replicas;
	}

	public static void setReplicas(ServerInfo[] replicas) {
		Config.replicas = replicas;
	}

	public static ServerInfo[] getFrontEnds() {
		return frontEnds;
	}

	public static void setFrontEnds(ServerInfo[] frontEnds) {
		Config.frontEnds = frontEnds;
	}

}
