package Client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;
public class Log {

    String id;
    private PrintStream out;
    private File file;

    /**
     * Instantiate a Log for a given name;
     *
     * @param id name of log
     */
    public Log(String id) 
	    {
	    this.id = id;
	    try 
	    	{
	    	file = new File("log\\" + id + ".txt");
	        if (file.createNewFile()){System.out.println("Server log for " + id + " was created!");}
	        out = new PrintStream(new FileOutputStream("log\\"+id+".txt"));
	    	} 
	    catch (Exception e) 
	    	{
	        throw new RuntimeException(e);
	    	}
	    }

    /**
     * Log an entry with the name and date
     *
     * @param content content to log
     */
    public void log(String content) 
    	{
        Date date = new Date();
        out.println("[" + date.toString() + " " + id + "] " + content + "\n");
        //System.out.println("[" + date.toString() + " " + id + "] " + content);
    	}

}
