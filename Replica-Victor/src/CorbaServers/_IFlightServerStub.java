package CorbaServers;


/**
* CorbaServers/_IFlightServerStub.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CorbaServers.idl
* Tuesday, November 8, 2016 5:52:34 PM EST
*/

public class _IFlightServerStub extends org.omg.CORBA.portable.ObjectImpl implements CorbaServers.IFlightServer
{

  public String getBookFlightCount (int recordType)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("getBookFlightCount", true);
                $out.write_long (recordType);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return getBookFlightCount (recordType        );
            } finally {
                _releaseReply ($in);
            }
  } // getBookFlightCount

  public void editFlightRecord (int recordID, String fieldName, String newValue)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("editFlightRecord", true);
                $out.write_long (recordID);
                $out.write_string (fieldName);
                $out.write_string (newValue);
                $in = _invoke ($out);
                return;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                editFlightRecord (recordID, fieldName, newValue        );
            } finally {
                _releaseReply ($in);
            }
  } // editFlightRecord

  public int bookFlight (String firstName, String lastName, String address, String phone, String destination, String date, int seatClass)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("bookFlight", true);
                $out.write_string (firstName);
                $out.write_string (lastName);
                $out.write_string (address);
                $out.write_string (phone);
                $out.write_string (destination);
                $out.write_string (date);
                $out.write_long (seatClass);
                $in = _invoke ($out);
                int $result = $in.read_long ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return bookFlight (firstName, lastName, address, phone, destination, date, seatClass        );
            } finally {
                _releaseReply ($in);
            }
  } // bookFlight

  public boolean transferReservation (int passangerID, int currentCity, int otherCity)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("transferReservation", true);
                $out.write_long (passangerID);
                $out.write_long (currentCity);
                $out.write_long (otherCity);
                $in = _invoke ($out);
                boolean $result = $in.read_boolean ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return transferReservation (passangerID, currentCity, otherCity        );
            } finally {
                _releaseReply ($in);
            }
  } // transferReservation

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:CorbaServers/IFlightServer:1.0"};

  public String[] _ids ()
  {
    return (String[])__ids.clone ();
  }

  private void readObject (java.io.ObjectInputStream s) throws java.io.IOException
  {
     String str = s.readUTF ();
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     org.omg.CORBA.Object obj = orb.string_to_object (str);
     org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate ();
     _set_delegate (delegate);
   } finally {
     orb.destroy() ;
   }
  }

  private void writeObject (java.io.ObjectOutputStream s) throws java.io.IOException
  {
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     String str = orb.object_to_string (this);
     s.writeUTF (str);
   } finally {
     orb.destroy() ;
   }
  }
} // class _IFlightServerStub
