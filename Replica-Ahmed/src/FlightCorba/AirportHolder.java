package FlightCorba;

/**
 * Holder class for : Airport
 * 
 * @author OpenORB Compiler
 */
final public class AirportHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal Airport value
     */
    public FlightCorba.Airport value;

    /**
     * Default constructor
     */
    public AirportHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public AirportHolder(FlightCorba.Airport initial)
    {
        value = initial;
    }

    /**
     * Read Airport from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = AirportHelper.read(istream);
    }

    /**
     * Write Airport into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        AirportHelper.write(ostream,value);
    }

    /**
     * Return the Airport TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return AirportHelper.type();
    }

}
