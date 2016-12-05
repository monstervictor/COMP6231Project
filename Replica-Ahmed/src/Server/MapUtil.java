package Server;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {


    public static Map<String, String> parse(String in) 
    {

        String[] tokens = in.trim().split(";");
        HashMap<String, String> map = new HashMap<String, String>();
        for (String t : tokens) 
        {
            int colon = t.indexOf(":");
            String key = t.substring(0, colon).trim();
            String value = t.substring(colon + 1).trim();
            map.put(key, value);
        }

        return map;
    }


    public static String stringify(Map<String, String> in) 
    {
        String buffer = "";
        boolean val = false;
        for (String value : in.keySet()) 
        {
            if (val) {buffer += ";";}
            buffer += value + ":" + in.get(value);
            val = true;
        }
        return buffer;
    }
}