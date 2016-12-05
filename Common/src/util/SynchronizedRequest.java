package util;

import java.io.Serializable;


@SuppressWarnings("serial")
public class SynchronizedRequest implements Serializable//, IOperationMessage 
{
	private String city;
	private String ipAddress;
	private int port;
	private String machineName;
	private boolean isSynced;

	public SynchronizedRequest() {}
	public SynchronizedRequest(String machineName, String city, String ipAddress, int port)
	{
		this.city = city;
		this.machineName = machineName;
		this.ipAddress = ipAddress;
		this.port = port;
		this.isSynced = false;
	}

	/*
	public OperationType getOperationType(){return OperationType.RequestSync;}
	public String getIpAddress() {return ipAddress;}
	public void setIpAddress(String ipAddress) {this.ipAddress = ipAddress;}
	public String getBank() {return city;}

	@Override
	public String getMachineName() {return this.machineName;}
	public void setCity(String city) {this.city = city;}
	public int getPort() {return port;}
	public void setPort(int port) {this.port = port;}
	public boolean isSyncDone() {return isSynced;}
	public void setSyncDone(boolean syncDone) {this.isSynced = syncDone;}*/
}