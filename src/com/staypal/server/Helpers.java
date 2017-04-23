package com.staypal.server;

import com.staypal.db.Staypaldb;
import fi.iki.elonen.NanoHTTPD;
import org.jooq.DSLContext;
import org.jooq.Result;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Pack200;

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

    public static HashMap<String, String> parseParams(NanoHTTPD.IHTTPSession sessionIn) throws NanoHTTPD.ResponseException, IOException
    {
        Map<String, String> files = new HashMap<String, String>();
        sessionIn.parseBody(files);
        sessionIn.getQueryParameterString();
        //parse session.getParms().toString());
        HashMap<String, String> parsedParams = (HashMap<String, String> ) sessionIn.getParms();
        return parsedParams;
    }
    public static boolean isAdmin( NanoHTTPD.IHTTPSession sessionIn) throws Exception {
        HashMap<String, String> parsedParams = parseParams(sessionIn);
        if (parsedParams.containsKey("email"))
        {
            if(parsedParams.get("email").equals("admin") && isAuthed(sessionIn))
            {
                return true;
            }
            else
            {
                return false;
            }

        }
        else
        {
            return false;
        }
    }
    public static boolean isAuthed( NanoHTTPD.IHTTPSession sessionIn) throws Exception
    {
        HashMap<String, String> parsedParams = parseParams(sessionIn);
        if(parsedParams.containsKey("email") && parsedParams.containsKey("a_tkn"))
        {
            long maxTime = new Date().getTime() - 1000 * 3600; //one hour
            Timestamp stamp = new Timestamp(maxTime);

            DSLContext db = DatabaseConnector.startConnect();
            Result res = db.selectFrom(Staypaldb.STAYPALDB.AUTH_TOKENS).
                    where(Staypaldb.STAYPALDB.AUTH_TOKENS.ACCESS_TOKEN.equal(parsedParams.get("a_tkn")).
                            and(Staypaldb.STAYPALDB.AUTH_TOKENS.EMAIL.equal(parsedParams.get("email")))
                            .and(Staypaldb.STAYPALDB.AUTH_TOKENS.TIME_ISSUED.greaterOrEqual(stamp))
                    ).fetch();
            db.close();

            if(res.size() > 0)
            {
                //user is authenticated
                return true;
            }
            else
            {
                return false;
            }


        }
        else
        {
            return false;
        }


    }

    public static String getLanguage( NanoHTTPD.IHTTPSession sessionIn)  throws Exception
    {
        HashMap<String, String> parsedParams = parseParams(sessionIn);
        return parsedParams.get("language");
    }

    public static NanoHTTPD.Response loadPageLangauge(NanoHTTPD.IHTTPSession sessionIn, String urlIn) throws Exception
    {
        String language = getLanguage(sessionIn);
        if(language.equals("arabic") || language.equals("spanish")  || language.equals("french") )
        {
            return loadPage("./pages-" + language + "/" + urlIn + "-" + language +".html");
        }
        else
        {
            return loadPage("./pages/" + urlIn + ".html");
        }
    }
}
