package data;

import java.net.InetAddress;

public class ServerInfo {

	private final String _hostName;
	private final InetAddress _ipAddress;
	private final int _port;
	public ServerInfo(String hostName, InetAddress ipAddress, int port){
		_hostName = hostName;
		_ipAddress = ipAddress;
		_port = port;
	}
	public String getHostName() {
		return _hostName;
	}
	public int getPort() {
		return _port;
	}
	public InetAddress getIpAddress() {
		return _ipAddress;
	}
}
