package FlightCorba;

/**
 * Interface definition: Airport.
 * 
 * @author OpenORB Compiler
 */
public class _AirportStub extends org.omg.CORBA.portable.ObjectImpl
        implements Airport
{
    static final String[] _ids_list =
    {
        "IDL:FlightCorba/Airport:1.0"
    };

    public String[] _ids()
    {
     return _ids_list;
    }

    private final static Class _opsClass = FlightCorba.AirportOperations.class;

    /**
     * Operation bookFlight
     */
    public String bookFlight(String firstName, String lastName, String address, String phone, String destination, String flightClass, String date)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("bookFlight",true);
                    _output.write_string(firstName);
                    _output.write_string(lastName);
                    _output.write_string(address);
                    _output.write_string(phone);
                    _output.write_string(destination);
                    _output.write_string(flightClass);
                    _output.write_string(date);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("bookFlight",_opsClass);
                if (_so == null)
                   continue;
                FlightCorba.AirportOperations _self = (FlightCorba.AirportOperations) _so.servant;
                try
                {
                    return _self.bookFlight( firstName,  lastName,  address,  phone,  destination,  flightClass,  date);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation createFlightRecord
     */
    public String createFlightRecord(String origin, String destination, String date, String time, short economySeats, short businessSeats, short firstSeats)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("createFlightRecord",true);
                    _output.write_string(origin);
                    _output.write_string(destination);
                    _output.write_string(date);
                    _output.write_string(time);
                    _output.write_short(economySeats);
                    _output.write_short(businessSeats);
                    _output.write_short(firstSeats);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("createFlightRecord",_opsClass);
                if (_so == null)
                   continue;
                FlightCorba.AirportOperations _self = (FlightCorba.AirportOperations) _so.servant;
                try
                {
                    return _self.createFlightRecord( origin,  destination,  date,  time,  economySeats,  businessSeats,  firstSeats);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation editFlightRecord
     */
    public String editFlightRecord(String recordID, String fieldName, String newValue)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("editFlightRecord",true);
                    _output.write_string(recordID);
                    _output.write_string(fieldName);
                    _output.write_string(newValue);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("editFlightRecord",_opsClass);
                if (_so == null)
                   continue;
                FlightCorba.AirportOperations _self = (FlightCorba.AirportOperations) _so.servant;
                try
                {
                    return _self.editFlightRecord( recordID,  fieldName,  newValue);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation getBookedFlightCount
     */
    public String getBookedFlightCount(String recordType)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("getBookedFlightCount",true);
                    _output.write_string(recordType);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getBookedFlightCount",_opsClass);
                if (_so == null)
                   continue;
                FlightCorba.AirportOperations _self = (FlightCorba.AirportOperations) _so.servant;
                try
                {
                    return _self.getBookedFlightCount( recordType);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation transferReservation
     */
    public String transferReservation(String PassengerID, String CurrentCity, String OtherCity)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("transferReservation",true);
                    _output.write_string(PassengerID);
                    _output.write_string(CurrentCity);
                    _output.write_string(OtherCity);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("transferReservation",_opsClass);
                if (_so == null)
                   continue;
                FlightCorba.AirportOperations _self = (FlightCorba.AirportOperations) _so.servant;
                try
                {
                    return _self.transferReservation( PassengerID,  CurrentCity,  OtherCity);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation deleteFlightRecord
     */
    public String deleteFlightRecord(String recordID)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("deleteFlightRecord",true);
                    _output.write_string(recordID);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("deleteFlightRecord",_opsClass);
                if (_so == null)
                   continue;
                FlightCorba.AirportOperations _self = (FlightCorba.AirportOperations) _so.servant;
                try
                {
                    return _self.deleteFlightRecord( recordID);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation editPassengerRecord
     */
    public String editPassengerRecord(String recordID, String lastName, String fieldName, String newValue)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("editPassengerRecord",true);
                    _output.write_string(recordID);
                    _output.write_string(lastName);
                    _output.write_string(fieldName);
                    _output.write_string(newValue);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("editPassengerRecord",_opsClass);
                if (_so == null)
                   continue;
                FlightCorba.AirportOperations _self = (FlightCorba.AirportOperations) _so.servant;
                try
                {
                    return _self.editPassengerRecord( recordID,  lastName,  fieldName,  newValue);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation logDumpPassengers
     */
    public void logDumpPassengers()
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("logDumpPassengers",true);
                    _input = this._invoke(_output);
                    return;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("logDumpPassengers",_opsClass);
                if (_so == null)
                   continue;
                FlightCorba.AirportOperations _self = (FlightCorba.AirportOperations) _so.servant;
                try
                {
                    _self.logDumpPassengers();
                    return;
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation logDumpFlights
     */
    public void logDumpFlights()
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("logDumpFlights",true);
                    _input = this._invoke(_output);
                    return;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("logDumpFlights",_opsClass);
                if (_so == null)
                   continue;
                FlightCorba.AirportOperations _self = (FlightCorba.AirportOperations) _so.servant;
                try
                {
                    _self.logDumpFlights();
                    return;
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

}
