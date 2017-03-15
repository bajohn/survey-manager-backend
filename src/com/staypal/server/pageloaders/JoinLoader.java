package com.staypal.server.pageloaders;

import com.staypal.db.Staypaldb;
import com.staypal.server.DatabaseConnector;
import com.staypal.server.GeneralHandler;
import com.staypal.server.Helpers;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.jooq.DSLContext;
import org.jooq.Result;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.TemporalField;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.staypal.server.Helpers.isAuthed;
import static com.staypal.server.Helpers.parseParams;

/**
 * Created by bjohn454 on 1/22/2017.
 */
public class JoinLoader extends GeneralHandler {
    public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> params, NanoHTTPD.IHTTPSession session)
    {

        try {

            if(isAuthed(session))
            {
                return Helpers.loadPage("./pages/homepage.html");
            }
            else
            {
                return Helpers.loadPage("./pages/join.html");
            }
        } catch (Exception e  )
        {
            return(NanoHTTPD.newFixedLengthResponse(e.getMessage()));
        }

    }
}
