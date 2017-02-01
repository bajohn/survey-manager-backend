package com.staypal.server;

import fi.iki.elonen.NanoHTTPD;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by bjohn454 on 1/22/2017.
 */
public class Helpers {
    public static NanoHTTPD.Response loadPage(String pageName)
    {
        try
        {
            String output = "";
            BufferedReader html = new BufferedReader( new FileReader(pageName));
            String line = "";
            while ((line = html.readLine())!=null)
            {
                output+=line+"\n";
            }
            return NanoHTTPD.newFixedLengthResponse(output);
        }
        catch(FileNotFoundException e)
        {
            return NanoHTTPD.newFixedLengthResponse("Error reading file");
        }
        catch(Exception e)
        {
            return NanoHTTPD.newFixedLengthResponse("Error reading file");
        }
    }
}
