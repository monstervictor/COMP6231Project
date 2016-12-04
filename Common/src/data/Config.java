package data;

public class Config {
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

}
