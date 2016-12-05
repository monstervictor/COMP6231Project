package FlightCorba;

/**
 * Interface definition: Airport.
 * 
 * @author OpenORB Compiler
 */
public abstract class AirportPOA extends org.omg.PortableServer.Servant
        implements AirportOperations, org.omg.CORBA.portable.InvokeHandler
{
    public Airport _this()
    {
        return AirportHelper.narrow(_this_object());
    }

    public Airport _this(org.omg.CORBA.ORB orb)
    {
        return AirportHelper.narrow(_this_object(orb));
    }

    private static String [] _ids_list =
    {
        "IDL:FlightCorba/Airport:1.0"
    };

    public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte [] objectId)
    {
        return _ids_list;
    }

    private static final java.util.Map operationMap = new java.util.HashMap();

    static {
            operationMap.put("bookFlight",
                    new Operation_bookFlight());
            operationMap.put("createFlightRecord",
                    new Operation_createFlightRecord());
            operationMap.put("deleteFlightRecord",
                    new Operation_deleteFlightRecord());
            operationMap.put("editFlightRecord",
                    new Operation_editFlightRecord());
            operationMap.put("editPassengerRecord",
                    new Operation_editPassengerRecord());
            operationMap.put("getBookedFlightCount",
                    new Operation_getBookedFlightCount());
            operationMap.put("logDumpFlights",
                    new Operation_logDumpFlights());
            operationMap.put("logDumpPassengers",
                    new Operation_logDumpPassengers());
            operationMap.put("transferReservation",
                    new Operation_transferReservation());
    }

    public final org.omg.CORBA.portable.OutputStream _invoke(final String opName,
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler)
    {

        final AbstractOperation operation = (AbstractOperation)operationMap.get(opName);

        if (null == operation) {
            throw new org.omg.CORBA.BAD_OPERATION(opName);
        }

        return operation.invoke(this, _is, handler);
    }

    // helper methods
    private org.omg.CORBA.portable.OutputStream _invoke_bookFlight(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = _is.read_string();
        String arg1_in = _is.read_string();
        String arg2_in = _is.read_string();
        String arg3_in = _is.read_string();
        String arg4_in = _is.read_string();
        String arg5_in = _is.read_string();
        String arg6_in = _is.read_string();

        String _arg_result = bookFlight(arg0_in, arg1_in, arg2_in, arg3_in, arg4_in, arg5_in, arg6_in);

        _output = handler.createReply();
        _output.write_string(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_createFlightRecord(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = _is.read_string();
        String arg1_in = _is.read_string();
        String arg2_in = _is.read_string();
        String arg3_in = _is.read_string();
        short arg4_in = _is.read_short();
        short arg5_in = _is.read_short();
        short arg6_in = _is.read_short();

        String _arg_result = createFlightRecord(arg0_in, arg1_in, arg2_in, arg3_in, arg4_in, arg5_in, arg6_in);

        _output = handler.createReply();
        _output.write_string(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_editFlightRecord(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = _is.read_string();
        String arg1_in = _is.read_string();
        String arg2_in = _is.read_string();

        String _arg_result = editFlightRecord(arg0_in, arg1_in, arg2_in);

        _output = handler.createReply();
        _output.write_string(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_getBookedFlightCount(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = _is.read_string();

        String _arg_result = getBookedFlightCount(arg0_in);

        _output = handler.createReply();
        _output.write_string(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_transferReservation(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = _is.read_string();
        String arg1_in = _is.read_string();
        String arg2_in = _is.read_string();

        String _arg_result = transferReservation(arg0_in, arg1_in, arg2_in);

        _output = handler.createReply();
        _output.write_string(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_deleteFlightRecord(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = _is.read_string();

        String _arg_result = deleteFlightRecord(arg0_in);

        _output = handler.createReply();
        _output.write_string(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_editPassengerRecord(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = _is.read_string();
        String arg1_in = _is.read_string();
        String arg2_in = _is.read_string();
        String arg3_in = _is.read_string();

        String _arg_result = editPassengerRecord(arg0_in, arg1_in, arg2_in, arg3_in);

        _output = handler.createReply();
        _output.write_string(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_logDumpPassengers(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;

        logDumpPassengers();

        _output = handler.createReply();

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_logDumpFlights(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;

        logDumpFlights();

        _output = handler.createReply();

        return _output;
    }

    // operation classes
    private abstract static class AbstractOperation {
        protected abstract org.omg.CORBA.portable.OutputStream invoke(
                AirportPOA target,
                org.omg.CORBA.portable.InputStream _is,
                org.omg.CORBA.portable.ResponseHandler handler);
    }

    private static final class Operation_bookFlight extends AbstractOperation
    {
        protected org.omg.CORBA.portable.OutputStream invoke(
                final AirportPOA target,
                final org.omg.CORBA.portable.InputStream _is,
                final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_bookFlight(_is, handler);
        }
    }

    private static final class Operation_createFlightRecord extends AbstractOperation
    {
        protected org.omg.CORBA.portable.OutputStream invoke(
                final AirportPOA target,
                final org.omg.CORBA.portable.InputStream _is,
                final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_createFlightRecord(_is, handler);
        }
    }

    private static final class Operation_editFlightRecord extends AbstractOperation
    {
        protected org.omg.CORBA.portable.OutputStream invoke(
                final AirportPOA target,
                final org.omg.CORBA.portable.InputStream _is,
                final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_editFlightRecord(_is, handler);
        }
    }

    private static final class Operation_getBookedFlightCount extends AbstractOperation
    {
        protected org.omg.CORBA.portable.OutputStream invoke(
                final AirportPOA target,
                final org.omg.CORBA.portable.InputStream _is,
                final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_getBookedFlightCount(_is, handler);
        }
    }

    private static final class Operation_transferReservation extends AbstractOperation
    {
        protected org.omg.CORBA.portable.OutputStream invoke(
                final AirportPOA target,
                final org.omg.CORBA.portable.InputStream _is,
                final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_transferReservation(_is, handler);
        }
    }

    private static final class Operation_deleteFlightRecord extends AbstractOperation
    {
        protected org.omg.CORBA.portable.OutputStream invoke(
                final AirportPOA target,
                final org.omg.CORBA.portable.InputStream _is,
                final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_deleteFlightRecord(_is, handler);
        }
    }

    private static final class Operation_editPassengerRecord extends AbstractOperation
    {
        protected org.omg.CORBA.portable.OutputStream invoke(
                final AirportPOA target,
                final org.omg.CORBA.portable.InputStream _is,
                final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_editPassengerRecord(_is, handler);
        }
    }

    private static final class Operation_logDumpPassengers extends AbstractOperation
    {
        protected org.omg.CORBA.portable.OutputStream invoke(
                final AirportPOA target,
                final org.omg.CORBA.portable.InputStream _is,
                final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_logDumpPassengers(_is, handler);
        }
    }

    private static final class Operation_logDumpFlights extends AbstractOperation
    {
        protected org.omg.CORBA.portable.OutputStream invoke(
                final AirportPOA target,
                final org.omg.CORBA.portable.InputStream _is,
                final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_logDumpFlights(_is, handler);
        }
    }

}
