package ReplicaManager;

import java.util.HashMap;
import java.util.Iterator;

import Shared.Util;

public class RMinstance
{
    private static RMinstance curInstance = new RMinstance();
    private static String curImpl;
    private static HashMap<String, Process> processes = new HashMap<String, Process>();
	
    public static RMinstance getInstance(){return curInstance;}
	private RMinstance(){}
    public void removeServer(String serverName){processes.remove(serverName);}
    public Process getServerProcess(String city){return processes.get(city);}
	public void addServer(String city, Process p){processes.put(city, p);}
	public String getCurImpl() {return curImpl;}
	public void setCurImpl(String implementationName){RMinstance.curImpl = implementationName;}
    public String getNextImpl() 
    	{   
        boolean stay = false;
        boolean isFirst = true;
        Iterator<String> it = Util.getListMachineName();
        String current = this.getCurImpl();
        String next = "";
        String first = "";
        while(it.hasNext())
        	{
            next = (String)it.next();
            if(isFirst)
           		{
                first = next;
                isFirst = false;
            	}
            if (stay)
            	{
                this.setCurImpl(next);
                return next;
            	}
            if (next.equals(current))
            	{
                stay = true;
            	}
        	}
        this.setCurImpl(first);
        return first;
    	}
}
