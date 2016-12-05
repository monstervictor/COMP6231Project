package ReplicaManager;

import java.io.IOException;

import Shared.Util;

public class InitRMs
{
    public static void main(String[] args)
    {   
    	RMservice service= new RMservice();
		Util.loadSettings();
        String implName = Util.getMachineName();
        RMserver server = new RMserver();
        try 
        {
            server.startCitiesServers(implName);
            //server.startRMtoFE();
            server.initializeCityServers();
        } 
        catch (IOException e) {e.printStackTrace();}
        System.out.println("[RMserver] RM Servers started");
    }
}
