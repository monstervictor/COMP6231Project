package ReplicaManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import Interfaces.IRMservice;
import Shared.Util;
import Shared.ReplicaStatusMessage;
import Shared.Serializer;

public class UdpRMtoFEListener extends Thread
{
    private static final String RM_HOST = "localhost";
    private static final int UDP_PACKET_SIZE = 4096;
    private static IRMservice replicaManagerService;
    private static int RM_PORT = Util.getReplicaManagerServerInfo(Util.getMachineName()).getPort();
	
	
    public UdpRMtoFEListener(IRMservice replicaManagerService)
    {
        this.replicaManagerService = replicaManagerService;
        System.err.println(RM_PORT + " UdpRMtoFEListener");
    }
    
    @Override
    public void run() 
    {

        DatagramSocket serverSocket = null;

        InetSocketAddress localAddr = new InetSocketAddress(RM_HOST, RM_PORT);

        try {

            serverSocket = new DatagramSocket(localAddr);

            while (true) {
                ReplicaStatusMessage message = receiveMessage(serverSocket);
                processMessage(message);
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(serverSocket != null) serverSocket.close();
        }
    }

	private void  processMessage(ReplicaStatusMessage message)
	{
        switch (message.getFailureType())
        {
        case error:
            replicaManagerService.onError(message.getCity(), message.getAddress());
            break;
        case failure:
            replicaManagerService.onFailure(message.getCity(), message.getAddress());
            break;
        }
	}
	

	private ReplicaStatusMessage receiveMessage(DatagramSocket serverSocket) throws IOException
	{
        byte[] receiveData;
        receiveData = new byte[UDP_PACKET_SIZE];
        final DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        try
		{
			serverSocket.receive(receivePacket);
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}

        byte[] data = new byte[receivePacket.getLength()];
        System.arraycopy(receivePacket.getData(), receivePacket.getOffset(), data, 0, receivePacket.getLength());
        ReplicaStatusMessage message = null;
        try {
            message = Serializer.deserialize(data);
        	} 
        catch (ClassNotFoundException e) {e.printStackTrace();}
    	return message;
	}
    
    

}
