package CorbaServers;


/**
* CorbaServers/IFlightServerPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CorbaServers.idl
* Tuesday, November 8, 2016 5:52:34 PM EST
*/

public abstract class IFlightServerPOA extends org.omg.PortableServer.Servant
 implements CorbaServers.IFlightServerOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("getBookFlightCount", new java.lang.Integer (0));
    _methods.put ("editFlightRecord", new java.lang.Integer (1));
    _methods.put ("bookFlight", new java.lang.Integer (2));
    _methods.put ("transferReservation", new java.lang.Integer (3));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // CorbaServers/IFlightServer/getBookFlightCount
       {
         int recordType = in.read_long ();
         String $result = null;
         $result = this.getBookFlightCount (recordType);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 1:  // CorbaServers/IFlightServer/editFlightRecord
       {
         int recordID = in.read_long ();
         String fieldName = in.read_string ();
         String newValue = in.read_string ();
         this.editFlightRecord (recordID, fieldName, newValue);
         out = $rh.createReply();
         break;
       }

       case 2:  // CorbaServers/IFlightServer/bookFlight
       {
         String firstName = in.read_string ();
         String lastName = in.read_string ();
         String address = in.read_string ();
         String phone = in.read_string ();
         String destination = in.read_string ();
         String date = in.read_string ();
         int seatClass = in.read_long ();
         int $result = (int)0;
         $result = this.bookFlight (firstName, lastName, address, phone, destination, date, seatClass);
         out = $rh.createReply();
         out.write_long ($result);
         break;
       }

       case 3:  // CorbaServers/IFlightServer/transferReservation
       {
         int passangerID = in.read_long ();
         int currentCity = in.read_long ();
         int otherCity = in.read_long ();
         boolean $result = false;
         $result = this.transferReservation (passangerID, currentCity, otherCity);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:CorbaServers/IFlightServer:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public IFlightServer _this() 
  {
    return IFlightServerHelper.narrow(
    super._this_object());
  }

  public IFlightServer _this(org.omg.CORBA.ORB orb) 
  {
    return IFlightServerHelper.narrow(
    super._this_object(orb));
  }


} // class IFlightServerPOA
