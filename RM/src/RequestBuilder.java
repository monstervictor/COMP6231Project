package ReplicaManager;

public class RequestBuilder
{

	private String mainArg;
    private String ReplicaPath;
    private String SharedPath = "../../out/production/Shared";
    private String classPath;
    private String codeName;
    private String SeqPath = "../../out/production/Sequencer";
    private String udpLoc = "";
    private String separator;
    
private String cmdCommand;
	public RequestBuilder setCode(String student)
	{
        this.separator = ";";
        this.cmdCommand = "cmd /c start";
        this.codeName = student;
        this.ReplicaPath =  String.format("../../out/production/Replica_%1$s", codeName);

        switch (student.toLowerCase())
        {
            case "ahmed":
                this.classPath = "Server.StartBankServer";
                break;
            case "alvaro":
                this.classPath = "dlms.StartBankServer";
                break;
            case "victor":
                this.classPath = "impl.InitServers";
                this.udpLoc = this.separator + "../../libs/rudp/classes";
                break;
        }
        return this;
	}
	
    public RequestBuilder setCity(String city)
    {
        this.mainArg = city;
        return this;
    }

	public String getRequest()
	{
        return String.format(
                "%7$s java -cp %1$s%2$s%3$s%2$s%4$s%8$s %5$s %6$s",
                ReplicaPath,
                separator,
                SharedPath,
                SeqPath,
                classPath,
                mainArg,
                cmdCommand,
                udpLoc
        );
	}

}
