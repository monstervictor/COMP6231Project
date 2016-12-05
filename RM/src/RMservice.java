package ReplicaManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.HashMap;

import Interfaces.IRMservice;
import Shared.Const;
import Shared.Util;
import Shared.Serializer;
import Shared.ServerInfo;
import Shared.SynchronizedRequest;
import Shared.UDPClient;
import Shared.UDPMessage;

@SuppressWarnings("unused")
public class RMservice implements IRMservice
{
	
    private UDPClient udpClient;
    private HashMap<String, Integer> errorCount;

    public RMservice() 
    {
        this.udpClient = new UDPClient();
        this.errorCount = new HashMap<>();
        errorCount.put("MTL", 0);
        errorCount.put("WST", 0);
        errorCount.put("NDL", 0);

    }
	
    private void restartServer(String city, String implementationName) 
    {
        stopCityServer(city);
        NewProcess(implementationName, city);
        String machineToGetStateFrom = getMachineToGetStateFrom();
        reset(machineToGetStateFrom, implementationName, city);
    }
    
    private String getMachineToGetStateFrom()
	{
        return isCurrent(Const.MASTER_MACHINE_NAME) ? Const.MACHINE_NAME_VICTOR : Const.MASTER_MACHINE_NAME;
	}

    private static String getCommand(String implementationName, String city) 
    {
        RequestBuilder requestBuilder = new RequestBuilder();
        return requestBuilder.setCode(implementationName).setCity(city).getRequest();
    }

	private void stopCityServer(String city) 
    {
        Process p = RMinstance.getInstance().getServerProcess(city);
        if (p != null) {p.destroy();}
        RMinstance.getInstance().removeServer(city);
    }

	@Override
	public void NewProcess(String implementationName, String city)
	{
    String command = getCommand(implementationName, city);
    Process p = null;
    try 
    {
        File f = new File(System.getProperty("user.dir") + "/../Replica_" + city + "_" + implementationName);
        p = Runtime.getRuntime().exec(command, null, f);
    } 
    catch (IOException e) 
    {
        e.printStackTrace();
    }
    RMinstance.getInstance().addServer(city, p);
    RMinstance.getInstance().setCurImpl(implementationName);

    Thread outputServer1 = getProcessOutputThread(p, implementationName, city);
	outputServer1.start();
		
	}

	private Thread getProcessOutputThread(Process process, String implementationName, String city)
	{
		return new Thread(() ->
        {
            String processOutput;
            try {
                try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream())))
                {
                    while ((processOutput = input.readLine()) != null) 
                    {
                     System.out.println(String.format("[%1$s][%2$s] - %3$s",
                                        implementationName,
                                        city,
                                        processOutput));
                    }
                    try {input.close();}
                    catch (IOException e) {e.printStackTrace();}
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    	});
	}


	@Override
	public void reset(String machineToGetStateFrom, String implementationName, String city)
	{
        if (isCurrent(machineToGetStateFrom)) {return;}

        String machineName = Util.getMachineName();
        ServerInfo replicaFaulty = Util.getReplicaSyncDbServerInfo(implementationName, city);
        ServerInfo replicaNonFaulty = Util.getReplicaSyncDbServerInfo(machineToGetStateFrom, city);
        SynchronizedRequest operationMessage = new SynchronizedRequest(
        		machineName, city, replicaFaulty.getIpAddress(), replicaFaulty.getPort());

        UDPMessage message = new UDPMessage(operationMessage);

        try
        {
            byte[] data = Serializer.serialize(message);

            System.err.println(
                    		"Request initial state to: "
                            + machineToGetStateFrom + " "
                            + replicaNonFaulty.getIpAddress() + " "
                            + replicaNonFaulty.getPort() + " "
                            + city
            );
            udpClient.sendMessageAndForget(data, InetAddress.getByName(replicaNonFaulty.getIpAddress()), replicaNonFaulty.getPort());
        } 
        catch (IOException e) {e.printStackTrace();}
		
	}

	private boolean isCurrent(String Name)
	{
		String myself = Util.getMachineName();
		return Name.toLowerCase().equals(myself.toLowerCase());
	}

}
