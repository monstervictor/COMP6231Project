package FlightCorba;

/** 
 * Helper class for : Airport
 *  
 * @author OpenORB Compiler
 */ 
public class AirportHelper
{
    /**
     * Insert Airport into an any
     * @param a an any
     * @param t Airport value
     */
    public static void insert(org.omg.CORBA.Any a, FlightCorba.Airport t)
    {
        a.insert_Object(t , type());
    }

    /**
     * Extract Airport from an any
     *
     * @param a an any
     * @return the extracted Airport value
     */
    public static FlightCorba.Airport extract( org.omg.CORBA.Any a )
    {
        if ( !a.type().equivalent( type() ) )
        {
            throw new org.omg.CORBA.MARSHAL();
        }
        try
        {
            return FlightCorba.AirportHelper.narrow( a.extract_Object() );
        }
        catch ( final org.omg.CORBA.BAD_PARAM e )
        {
            throw new org.omg.CORBA.MARSHAL(e.getMessage());
        }
    }

    //
    // Internal TypeCode value
    //
    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the Airport TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_interface_tc( id(), "Airport" );
        }
        return _tc;
    }

    /**
     * Return the Airport IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:FlightCorba/Airport:1.0";

    /**
     * Read Airport from a marshalled stream
     * @param istream the input stream
     * @return the readed Airport value
     */
    public static FlightCorba.Airport read(org.omg.CORBA.portable.InputStream istream)
    {
        return(FlightCorba.Airport)istream.read_Object(FlightCorba._AirportStub.class);
    }

    /**
     * Write Airport into a marshalled stream
     * @param ostream the output stream
     * @param value Airport value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, FlightCorba.Airport value)
    {
        ostream.write_Object((org.omg.CORBA.portable.ObjectImpl)value);
    }

    /**
     * Narrow CORBA::Object to Airport
     * @param obj the CORBA Object
     * @return Airport Object
     */
    public static Airport narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof Airport)
            return (Airport)obj;

        if (obj._is_a(id()))
        {
            _AirportStub stub = new _AirportStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
            return stub;
        }

        throw new org.omg.CORBA.BAD_PARAM();
    }

    /**
     * Unchecked Narrow CORBA::Object to Airport
     * @param obj the CORBA Object
     * @return Airport Object
     */
    public static Airport unchecked_narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof Airport)
            return (Airport)obj;

        _AirportStub stub = new _AirportStub();
        stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
        return stub;

    }

}
