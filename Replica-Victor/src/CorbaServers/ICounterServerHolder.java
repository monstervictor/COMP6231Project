package CorbaServers;

/**
* CorbaServers/ICounterServerHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CorbaServers.idl
* Tuesday, November 8, 2016 5:52:34 PM EST
*/

public final class ICounterServerHolder implements org.omg.CORBA.portable.Streamable
{
  public CorbaServers.ICounterServer value = null;

  public ICounterServerHolder ()
  {
  }

  public ICounterServerHolder (CorbaServers.ICounterServer initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = CorbaServers.ICounterServerHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    CorbaServers.ICounterServerHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return CorbaServers.ICounterServerHelper.type ();
  }

}
