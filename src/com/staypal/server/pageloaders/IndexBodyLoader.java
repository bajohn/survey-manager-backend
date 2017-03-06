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
import java.util.Map;

import static com.staypal.server.Helpers.parseParams;

/**
 * Created by bjohn454 on 1/22/2017.
 */
public class IndexBodyLoader extends GeneralHandler {
    public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> params, NanoHTTPD.IHTTPSession session)
    {

        try {


            Map<String, String> parsedParams = parseParams(session);
            if(parsedParams.containsKey("email") && parsedParams.containsKey("a_tkn"))
            {
                long maxTime = new Date().getTime() - 1000*3600 ;
                Timestamp stamp = new Timestamp(maxTime);

                DSLContext db = DatabaseConnector.startConnect();
                Result res = db.selectFrom(Staypaldb.STAYPALDB.AUTH_TOKENS).
                        where(Staypaldb.STAYPALDB.AUTH_TOKENS.ACCESS_TOKEN.equal(parsedParams.get("a_tkn")).
                                and(Staypaldb.STAYPALDB.AUTH_TOKENS.EMAIL.equal(parsedParams.get("email"))).
                                and(Staypaldb.STAYPALDB.AUTH_TOKENS.TIME_ISSUED.greaterOrEqual(stamp))
                        ).fetch();

                if(res.size() > 0)
                {
                    //user is authenticated
                    return Helpers.loadPage("./pages/coming-soon.html");
                }
                else
                {
                    return Helpers.loadPage("./pages/index-body.html");
                }
            }
            else
            {
                return Helpers.loadPage("./pages/index-body.html");
            }
        } catch (Exception e  )
        {
            return(NanoHTTPD.newFixedLengthResponse(e.getMessage()));
        }

    }
}
