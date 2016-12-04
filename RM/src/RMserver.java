package ReplicaManager;

import java.io.IOException;

import Interfaces.IRMservice;
import Shared.Const;
import Shared.Util;

public class RMserver
{
	private final static String[] cities = {"MTL", "NDL", "WST"};
	private IRMservice RMservice;
	
    public RMserver() 
    {
        this.RMservice = new RMservice();
    }
	
	
    public void startCitiesServers(String codeName) throws IOException 
    {
        for (String city : cities) 
        {
            RMservice.NewProcess(codeName,city);
        }
    }
    
    public void startFrontEndMessageUdpServer() 
    {
        UdpRMtoFEListener udpServerFrontEnd = new UdpRMtoFEListener(RMservice);
        udpServerFrontEnd.start();
    }
    

    public void initializeCityServers() 
    {
        try 
        {
            Thread.sleep(5000);
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
        String principal = Const.MASTER_MACHINE_NAME;
        if (!Util.getMachineName().equals(principal)) 
        {
            for (String city : cities) 
            {
                RMservice.reset(principal, Util.getMachineName(), city);
            }
        }
    }
    
}
